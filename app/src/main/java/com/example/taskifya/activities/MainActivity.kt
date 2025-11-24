package com.example.taskifya.activities

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.taskifya.R
import com.example.taskifya.adapters.DiasAdapter
import com.example.taskifya.adapters.NotasAdapter
import com.example.taskifya.models.Dia
import com.example.taskifya.models.Nota
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerViewDias: RecyclerView
    private lateinit var diasAdapter: DiasAdapter
    private lateinit var recyclerViewNotas: RecyclerView
    private lateinit var notasAdapter: NotasAdapter
    private lateinit var fabAgregarNota: FloatingActionButton
    private lateinit var etBuscador: EditText

    private val listaDias = mutableListOf<Dia>()
    private val listaNotas = mutableListOf<Nota>()
    private var fechaSeleccionada: Date? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Inicializar vistas
        recyclerViewDias = findViewById(R.id.recyclerViewDias)
        recyclerViewNotas = findViewById(R.id.recyclerViewNotas)
        fabAgregarNota = findViewById(R.id.fabAgregarNota)
        etBuscador = findViewById(R.id.etBuscador)

        // Agregar notas de ejemplo ANTES de configurar adapters
        agregarNotasEjemplo()

        // Configurar RecyclerView de días
        generarDias()
        diasAdapter = DiasAdapter(listaDias) { position ->
            // Callback cuando se selecciona un día
            val diaSeleccionado = listaDias[position]
            fechaSeleccionada = diaSeleccionado.fecha

            // Filtrar notas por el día seleccionado
            notasAdapter.filtrarPorFecha(fechaSeleccionada)

            // Mostrar mensaje
            val formato = SimpleDateFormat("d 'de' MMMM", Locale("es", "ES"))
            Toast.makeText(
                this,
                "Notas del ${formato.format(fechaSeleccionada)}: ${notasAdapter.itemCount}",
                Toast.LENGTH_SHORT
            ).show()
        }

        recyclerViewDias.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerViewDias.adapter = diasAdapter

        // Scroll automático al día actual y obtener fecha seleccionada
        val hoyPosition = listaDias.indexOfFirst { it.isSelected }
        if (hoyPosition >= 0) {
            recyclerViewDias.scrollToPosition(hoyPosition)
            fechaSeleccionada = listaDias[hoyPosition].fecha
        }

        // Configurar RecyclerView de notas
        notasAdapter = NotasAdapter(
            listaNotas,
            onDeleteClick = { /* Ya se maneja en el adapter */ },
            onNotaClick = { nota -> mostrarDetalleNota(nota) }
        )

        recyclerViewNotas.layoutManager = LinearLayoutManager(this)
        recyclerViewNotas.adapter = notasAdapter

        // Filtrar notas para mostrar solo las del día actual
        notasAdapter.filtrarPorFecha(fechaSeleccionada)

        // Configurar buscador
        etBuscador.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                val textoBusqueda = s.toString()
                notasAdapter.filtrarPorTexto(textoBusqueda)

                if (textoBusqueda.isEmpty()) {
                    // Si borra el texto, volver a filtrar por fecha
                    notasAdapter.filtrarPorFecha(fechaSeleccionada)
                }
            }
        })

        // Click en FAB para agregar nueva nota
        fabAgregarNota.setOnClickListener {
            val intent = Intent(this, NuevaNotaActivity::class.java)
            // Pasar la fecha seleccionada a la siguiente pantalla
            fechaSeleccionada?.let {
                intent.putExtra("fechaSeleccionada", it.time)
            }
            startActivityForResult(intent, REQUEST_CODE_NUEVA_NOTA)
        }
    }

    private fun mostrarDetalleNota(nota: Nota) {
        val formato = SimpleDateFormat("d 'de' MMMM 'de' yyyy", Locale("es", "ES"))

        AlertDialog.Builder(this)
            .setTitle(nota.categoria)
            .setMessage(
                "📅 Fecha: ${formato.format(nota.fecha)}\n\n" +
                        "📝 Título: ${nota.titulo}\n\n" +
                        "📄 Descripción:\n${nota.descripcion}"
            )
            .setPositiveButton("Cerrar") { dialog, _ -> dialog.dismiss() }
            .show()
    }

    private fun generarDias() {
        val calendar = Calendar.getInstance()
        val formatoDia = SimpleDateFormat("EEE", Locale("es", "ES"))

        // Generar 30 días (15 antes y 15 después del día actual)
        calendar.add(Calendar.DAY_OF_YEAR, -15)

        val hoy = Calendar.getInstance()

        for (i in 0..30) {
            val numeroDia = calendar.get(Calendar.DAY_OF_MONTH)
            val nombreDia = formatoDia.format(calendar.time).replaceFirstChar {
                if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString()
            }
            val esHoy = calendar.get(Calendar.DAY_OF_YEAR) == hoy.get(Calendar.DAY_OF_YEAR) &&
                    calendar.get(Calendar.YEAR) == hoy.get(Calendar.YEAR)

            listaDias.add(
                Dia(
                    numeroDia = numeroDia,
                    nombreDia = nombreDia,
                    fecha = calendar.time.clone() as Date,
                    isSelected = esHoy
                )
            )

            calendar.add(Calendar.DAY_OF_YEAR, 1)
        }
    }

    private fun agregarNotasEjemplo() {
        val calendar = Calendar.getInstance()

        // Notas para HOY
        listaNotas.add(Nota(1, "Reunión de equipo", "Reunión con el equipo de desarrollo para revisar el sprint", "Trabajo", calendar.time))
        listaNotas.add(Nota(2, "Compras", "Ir al supermercado y comprar: leche, pan, huevos, frutas", "Personal", calendar.time))

        // Nota para AYER
        calendar.add(Calendar.DAY_OF_YEAR, -1)
        listaNotas.add(Nota(3, "Gimnasio", "Ejercicio de 6-7pm: cardio y pesas", "Salud", calendar.time))

        // Nota para MAÑANA
        calendar.add(Calendar.DAY_OF_YEAR, 2)
        listaNotas.add(Nota(4, "Dentista", "Cita a las 10am con el Dr. Pérez", "Salud", calendar.time))

        // Nota para PASADO MAÑANA
        calendar.add(Calendar.DAY_OF_YEAR, 1)
        listaNotas.add(Nota(5, "Presentación", "Presentar proyecto final a las 3pm en sala de juntas", "Trabajo", calendar.time))
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE_NUEVA_NOTA && resultCode == RESULT_OK) {
            data?.let {
                val titulo = it.getStringExtra("titulo") ?: ""
                val descripcion = it.getStringExtra("descripcion") ?: ""
                val categoria = it.getStringExtra("categoria") ?: "Sin categoría"
                val fechaTimestamp = it.getLongExtra("fecha", System.currentTimeMillis())
                val fechaNota = Date(fechaTimestamp)

                val nuevaNota = Nota(
                    id = listaNotas.size + 1,
                    titulo = titulo,
                    descripcion = descripcion,
                    categoria = categoria,
                    fecha = fechaNota
                )

                notasAdapter.addItem(nuevaNota)

                Toast.makeText(
                    this,
                    "Nota agregada a ${SimpleDateFormat("d MMM", Locale("es", "ES")).format(fechaNota)}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    companion object {
        private const val REQUEST_CODE_NUEVA_NOTA = 100
    }
}