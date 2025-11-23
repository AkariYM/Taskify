package com.example.taskifya.eventos

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.taskifya.R
import java.util.*
import java.util.concurrent.Executors

class CrearEventoActivity : AppCompatActivity() {

    private lateinit var repo: EventosRepository
    private val executor = Executors.newSingleThreadExecutor()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crear_evento)

        repo = EventosRepository(this)

        val etTitulo = findViewById<EditText>(R.id.etTitulo)
        val etDescripcion = findViewById<EditText>(R.id.etDescripcion)
        val btnFecha = findViewById<Button>(R.id.btnFecha)
        val btnHora = findViewById<Button>(R.id.btnHora)
        val spinner = findViewById<Spinner>(R.id.spinnerCategoria)
        val btnGuardar = findViewById<Button>(R.id.btnGuardar)
        val btnCancelar = findViewById<Button>(R.id.btnCancelar)

        ArrayAdapter.createFromResource(this, R.array.categorias_array, android.R.layout.simple_spinner_item).also {
            it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = it
        }

        var fechaIso = ""
        var horaStr = ""
        val cal = Calendar.getInstance()

        btnFecha.setOnClickListener {
            val dp = DatePickerDialog(this, { _, y, m, d ->
                fechaIso = String.format("%04d-%02d-%02d", y, m + 1, d)
                btnFecha.text = fechaIso
            }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH))
            dp.show()
        }

        btnHora.setOnClickListener {
            val tp = TimePickerDialog(this, { _, h, min ->
                horaStr = String.format("%02d:%02d", h, min)
                btnHora.text = horaStr
            }, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true)
            tp.show()
        }

        btnGuardar.setOnClickListener {
            val titulo = etTitulo.text.toString().trim()
            if (titulo.isEmpty()) {
                Toast.makeText(this, "Título obligatorio", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val descripcion = etDescripcion.text.toString().trim().ifEmpty { null }
            val categoria = spinner.selectedItem.toString().uppercase(Locale.getDefault())

            val evento = Evento(
                titulo = titulo,
                descripcion = descripcion,
                fechaIso = fechaIso.ifEmpty { "" },
                hora = horaStr,
                categoria = categoria,
                isReminder = 0,
                repeticion = "NUNCA"
            )

            executor.execute {
                repo.insertar(evento)
                runOnUiThread {
                    Toast.makeText(this, "Evento guardado", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
        }

        btnCancelar.setOnClickListener { finish() }
    }

    override fun onDestroy() {
        super.onDestroy()
        executor.shutdown()
    }
}
