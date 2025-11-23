package com.example.taskifya.eventos

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.taskifya.R
import java.util.*
import java.util.concurrent.Executors

class CrearRecordatorioActivity : AppCompatActivity() {

    private lateinit var repo: EventosRepository
    private val executor = Executors.newSingleThreadExecutor()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crear_recordatorio)

        repo = EventosRepository(this)

        val etTitulo = findViewById<EditText>(R.id.etTitulo)
        val etDescripcion = findViewById<EditText>(R.id.etDescripcion)
        val btnFecha = findViewById<Button>(R.id.btnFecha)
        val btnHora = findViewById<Button>(R.id.btnHora)
        val switchRep = findViewById<Switch>(R.id.switchRepetir)
        val btnGuardar = findViewById<Button>(R.id.btnGuardar)
        val btnCancelar = findViewById<Button>(R.id.btnCancelar)

        val cal = Calendar.getInstance()
        var fechaIso = ""
        var horaStr = ""

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
                Toast.makeText(this, "Título requerido", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val descripcion = etDescripcion.text.toString().trim().ifEmpty { null }
            val rep = if (switchRep.isChecked) "SEMANAL" else "NUNCA"

            val evento = Evento(
                titulo = titulo,
                descripcion = descripcion,
                fechaIso = fechaIso,
                hora = horaStr,
                categoria = "PERSONAL",
                isReminder = 1,
                repeticion = rep
            )

            executor.execute {
                repo.insertar(evento)
                runOnUiThread {
                    Toast.makeText(this, "Recordatorio guardado", Toast.LENGTH_SHORT).show()
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
