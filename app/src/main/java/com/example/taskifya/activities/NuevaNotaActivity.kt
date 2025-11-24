package com.example.taskifya.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.taskifya.R
import com.example.taskifya.adapters.FechasAdapter
import com.example.taskifya.models.Dia
import java.text.SimpleDateFormat
import java.util.*

class NuevaNotaActivity : AppCompatActivity() {

    private lateinit var recyclerViewFechas: RecyclerView
    private lateinit var fechasAdapter: FechasAdapter
    private lateinit var etTitulo: EditText
    private lateinit var etDescripcion: EditText
    private lateinit var btnCrearNota: Button
    private lateinit var btnVolver: ImageButton

    private val listaFechas = mutableListOf<Dia>()
    private var fechaSeleccionada: Date = Date()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nueva_nota)

        // Inicializar vistas
        recyclerViewFechas = findViewById(R.id.recyclerViewFechas)
        etTitulo = findViewById(R.id.etTitulo)
        etDescripcion = findViewById(R.id.etDescripcion)
        btnCrearNota = findViewById(R.id.btnCrearNota)
        btnVolver = findViewById(R.id.btnVolver)

        // Obtener fecha seleccionada desde MainActivity (si viene)
        val fechaRecibida = intent.getLongExtra("fechaSeleccionada", -1L)
        if (fechaRecibida != -1L) {
            fechaSeleccionada = Date(fechaRecibida)
        }

        // Configurar RecyclerView de fechas
        generarFechas()
        fechasAdapter = FechasAdapter(listaFechas) { position ->
            // Callback cuando se selecciona una fecha
            val fechaSelec = listaFechas[position]
            fechaSeleccionada = fechaSelec.fecha

            val formato = SimpleDateFormat("d 'de' MMMM", Locale("es", "ES"))
            Toast.makeText(
                this,
                "Fecha seleccionada: ${formato.format(fechaSeleccionada)}",
                Toast.LENGTH_SHORT
            ).show()
        }

        recyclerViewFechas.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerViewFechas.adapter = fechasAdapter

        // Scroll automático a la fecha seleccionada
        val posicionSeleccionada = listaFechas.indexOfFirst { it.isSelected }
        if (posicionSeleccionada >= 0) {
            recyclerViewFechas.scrollToPosition(posicionSeleccionada)
        }

        // Botón volver
        btnVolver.setOnClickListener {
            finish()
        }

        // Botón crear nota
        btnCrearNota.setOnClickListener {
            val titulo = etTitulo.text.toString()
            val descripcion = etDescripcion.text.toString()

            if (titulo.isNotEmpty() && descripcion.isNotEmpty()) {
                val resultIntent = Intent()
                resultIntent.putExtra("titulo", titulo)
                resultIntent.putExtra("descripcion", descripcion)
                resultIntent.putExtra("categoria", titulo)
                resultIntent.putExtra("fecha", fechaSeleccionada.time) // Enviar fecha seleccionada

                setResult(RESULT_OK, resultIntent)

                val formato = SimpleDateFormat("d MMM", Locale("es", "ES"))
                Toast.makeText(
                    this,
                    "Nota creada para el ${formato.format(fechaSeleccionada)}",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            } else {
                Toast.makeText(this, "Por favor completa todos los campos", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun generarFechas() {
        val calendar = Calendar.getInstance()
        val formatoDia = SimpleDateFormat("EEE", Locale("es", "ES"))

        // Generar 60 días (30 antes y 30 después del día actual)
        calendar.add(Calendar.DAY_OF_YEAR, -30)

        val fechaObjetivo = Calendar.getInstance()
        fechaObjetivo.time = fechaSeleccionada

        for (i in 0..60) {
            val numeroDia = calendar.get(Calendar.DAY_OF_MONTH)
            val nombreDia = formatoDia.format(calendar.time).replaceFirstChar {
                if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString()
            }
            val esFechaSeleccionada = calendar.get(Calendar.DAY_OF_YEAR) == fechaObjetivo.get(Calendar.DAY_OF_YEAR) &&
                    calendar.get(Calendar.YEAR) == fechaObjetivo.get(Calendar.YEAR)

            listaFechas.add(
                Dia(
                    numeroDia = numeroDia,
                    nombreDia = nombreDia,
                    fecha = calendar.time.clone() as Date,
                    isSelected = esFechaSeleccionada
                )
            )

            calendar.add(Calendar.DAY_OF_YEAR, 1)
        }
    }
}