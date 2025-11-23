package com.example.taskifya.activities

import android.content.Intent
import android.os.Bundle
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

    private val listaDias = mutableListOf<Dia>()
    private val listaNotas = mutableListOf<Nota>()
    private var fechaSeleccionada: Date = Date()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Inicializar vistas
        recyclerViewDias = findViewById(R.id.recyclerViewDias)
        recyclerViewNotas = findViewById(R.id.recyclerViewNotas)
        fabAgregarNota = findViewById(R.id.fabAgregarNota)

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

            // Mostrar mensaje (opcional)
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
        notasAdapter = NotasAdapter(listaNotas) { position ->
            // Ya no se usa este callback porque el delete se maneja dentro del adapter
        }

        recyclerViewNotas.layoutManager = LinearLayoutManager(this)
        recyclerViewNotas.adapter = notasAdapter

        // Filtrar notas para mostrar solo las del día actual
        notasAdapter.filtrarPorFecha(fechaSeleccionada)

        // Click en FAB para agregar nueva nota
        fabAgregarNota.setOnClickListener {
            val intent = Intent(this, NuevaNotaActivity::class.java)
            // Pasar la fecha seleccionada a la siguiente pantalla
            intent.putExtra("fechaSeleccionada", fechaSeleccionada.time)
            startActivityForResult(intent, REQUEST_CODE_NUEVA_NOTA)
        }
    }

    private fun generarDias() {
        val calendar = Calendar.getInstance()
        val formatoDia = SimpleDateFormat("EEE", Locale("es", "ES"))

        // Generar 30 días (15 antes y 15 después del día actual)
        calendar.add(Calendar.DAY_OF_YEAR, -15)

        val hoy = Calendar.getInstance()

        for (i in 0..30) {
            val numeroDia = calendar.get(Calendar.DAY_OF_MONTH)
            val nombreDia = formatoDia.format(calendar.time).capitalize()
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
        listaNotas.add(Nota(1, "Reunión de equipo", "Reunión con el equipo de desarrollo", "Trabajo", calendar.time))
        listaNotas.add(Nota(2, "Compras", "Ir al supermercado", "Personal", calendar.time))

        // Nota para AYER
        calendar.add(Calendar.DAY_OF_YEAR, -1)
        listaNotas.add(Nota(3, "Gimnasio", "Ejercicio de 6-7pm", "Salud", calendar.time))

        // Nota para MAÑANA
        calendar.add(Calendar.DAY_OF_YEAR, 2)
        listaNotas.add(Nota(4, "Dentista", "Cita a las 10am", "Salud", calendar.time))

        // Nota para PASADO MAÑANA
        calendar.add(Calendar.DAY_OF_YEAR, 1)
        listaNotas.add(Nota(5, "Presentación", "Presentar proyecto final", "Trabajo", calendar.time))
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE_NUEVA_NOTA && resultCode == RESULT_OK) {
            data?.let {
                val titulo = it.getStringExtra("titulo") ?: ""
                val descripcion = it.getStringExtra("descripcion") ?: ""
                val categoria = it.getStringExtra("categoria") ?: "Sin categoría"

                val nuevaNota = Nota(
                    id = listaNotas.size + 1,
                    titulo = titulo,
                    descripcion = descripcion,
                    categoria = categoria,
                    fecha = fechaSeleccionada // Usar la fecha actualmente seleccionada
                )

                notasAdapter.addItem(nuevaNota)

                Toast.makeText(
                    this,
                    "Nota agregada a ${SimpleDateFormat("d MMM", Locale("es", "ES")).format(fechaSeleccionada)}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    companion object {
        private const val REQUEST_CODE_NUEVA_NOTA = 100
    }
}