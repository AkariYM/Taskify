package com.example.taskifya.eventos

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.taskifya.R
import java.util.*
import java.util.concurrent.Executors

class EditarEventoActivity : AppCompatActivity() {

    private lateinit var repo: EventosRepository
    private val executor = Executors.newSingleThreadExecutor()

    // Variables para almacenar fecha y hora seleccionadas
    private var fechaSeleccionada = ""
    private var horaSeleccionada = ""

    // ID del evento que estamos editando
    private var eventoId: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editar_evento)

        repo = EventosRepository(this)

        // Obtener el ID del evento desde el Intent
        eventoId = intent.getLongExtra("EVENTO_ID", 0)

        // Referencias a los elementos de la interfaz
        val etTitulo = findViewById<EditText>(R.id.etTitulo)
        val etDescripcion = findViewById<EditText>(R.id.etDescripcion)
        val btnFecha = findViewById<Button>(R.id.btnFecha)
        val btnHora = findViewById<Button>(R.id.btnHora)
        val spinnerCategoria = findViewById<Spinner>(R.id.spinnerCategoria)
        val btnGuardar = findViewById<Button>(R.id.btnGuardar)
        val btnCancelar = findViewById<Button>(R.id.btnCancelar)

        // Configurar Spinner de categorías
        val categorias = arrayOf("PERSONAL", "ACADEMICO", "TRABAJO")
        spinnerCategoria.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categorias)

        // Cargar los datos del evento existente
        cargarDatosEvento(eventoId, etTitulo, etDescripcion, btnFecha, btnHora, spinnerCategoria, categorias)

        // Configurar selector de fecha
        btnFecha.setOnClickListener {
            val cal = Calendar.getInstance()
            DatePickerDialog(this, { _, year, month, day ->
                fechaSeleccionada = String.format("%04d-%02d-%02d", year, month + 1, day)
                btnFecha.text = String.format("%02d/%02d/%04d", month + 1, day, year)
            }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show()
        }

        // Configurar selector de hora
        btnHora.setOnClickListener {
            val cal = Calendar.getInstance()
            TimePickerDialog(this, { _, hour, minute ->
                horaSeleccionada = String.format("%02d:%02d", hour, minute)
                btnHora.text = horaSeleccionada
            }, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show()
        }

        // Botón Guardar - Actualiza el evento en la BD
        btnGuardar.setOnClickListener {
            val titulo = etTitulo.text.toString()
            val descripcion = etDescripcion.text.toString()
            val categoria = spinnerCategoria.selectedItem.toString()

            // Validar que los campos obligatorios no estén vacíos
            if (titulo.isBlank()) {
                Toast.makeText(this, "El título es obligatorio", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (fechaSeleccionada.isBlank()) {
                Toast.makeText(this, "Selecciona una fecha", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (horaSeleccionada.isBlank()) {
                Toast.makeText(this, "Selecciona una hora", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Crear objeto Evento actualizado
            val eventoActualizado = Evento(
                id = eventoId,
                titulo = titulo,
                descripcion = descripcion,
                fechaIso = fechaSeleccionada,
                hora = horaSeleccionada,
                categoria = categoria,
                isReminder = 0,
                repeticion = "NUNCA"
            )

            // Guardar en segundo plano
            executor.execute {
                repo.actualizar(eventoActualizado)
                runOnUiThread {
                    Toast.makeText(this, "Evento actualizado", Toast.LENGTH_SHORT).show()
                    finish() // Cerrar la actividad y volver a la anterior
                }
            }
        }

        // Botón Cancelar - Volver sin guardar cambios
        btnCancelar.setOnClickListener {
            finish()
        }
    }

    // Función para cargar los datos del evento existente en los campos
    private fun cargarDatosEvento(
        id: Long,
        etTitulo: EditText,
        etDescripcion: EditText,
        btnFecha: Button,
        btnHora: Button,
        spinnerCategoria: Spinner,
        categorias: Array<String>
    ) {
        executor.execute {
            // Obtener el evento por ID
            val evento = repo.obtenerPorId(id)

            runOnUiThread {
                if (evento != null) {
                    // Pre-llenar los campos con los datos existentes
                    etTitulo.setText(evento.titulo)
                    etDescripcion.setText(evento.descripcion ?: "")

                    // Guardar fecha y hora en variables
                    fechaSeleccionada = evento.fechaIso
                    horaSeleccionada = evento.hora

                    // Mostrar fecha en formato legible
                    val partesFecha = evento.fechaIso.split("-")
                    if (partesFecha.size == 3) {
                        btnFecha.text = "${partesFecha[1]}/${partesFecha[2]}/${partesFecha[0]}"
                    }

                    // Mostrar hora
                    btnHora.text = evento.hora

                    // Seleccionar categoría en el Spinner
                    val categoriaIndex = categorias.indexOf(evento.categoria)
                    if (categoriaIndex >= 0) {
                        spinnerCategoria.setSelection(categoriaIndex)
                    }
                } else {
                    Toast.makeText(this, "Error: Evento no encontrado", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        executor.shutdown()
    }
}