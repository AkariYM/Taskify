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
        setContentView(R.layout.activity_notas)

        recyclerViewDias = findViewById(R.id.recyclerViewDias)
        recyclerViewNotas = findViewById(R.id.recyclerViewNotas)
        fabAgregarNota = findViewById(R.id.fabAgregarNota)
        etBuscador = findViewById(R.id.etBuscador)

        // Cargar notas guardadas
        cargarNotasGuardadas()

        generarDias()
        diasAdapter = DiasAdapter(listaDias) { position ->
            val diaSeleccionado = listaDias[position]
            fechaSeleccionada = diaSeleccionado.fecha
            notasAdapter.filtrarPorFecha(fechaSeleccionada)
            val formato = SimpleDateFormat("d 'de' MMMM", Locale("es", "ES"))
            Toast.makeText(
                this,
                "Notas del ${formato.format(fechaSeleccionada)}: ${notasAdapter.itemCount}",
                Toast.LENGTH_SHORT
            ).show()
        }

        recyclerViewDias.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerViewDias.adapter = diasAdapter

        val hoyPosition = listaDias.indexOfFirst { it.isSelected }
        if (hoyPosition >= 0) {
            recyclerViewDias.scrollToPosition(hoyPosition)
            fechaSeleccionada = listaDias[hoyPosition].fecha
        }

        notasAdapter = NotasAdapter(
            listaNotas,
            onDeleteClick = { guardarNotasEnPrefs() },
            onNotaClick = { nota -> mostrarDetalleNota(nota) }
        )

        recyclerViewNotas.layoutManager = LinearLayoutManager(this)
        recyclerViewNotas.adapter = notasAdapter
        notasAdapter.filtrarPorFecha(fechaSeleccionada)

        etBuscador.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                val textoBusqueda = s.toString()
                notasAdapter.filtrarPorTexto(textoBusqueda)
                if (textoBusqueda.isEmpty()) {
                    notasAdapter.filtrarPorFecha(fechaSeleccionada)
                }
            }
        })

        fabAgregarNota.setOnClickListener {
            val intent = Intent(this, NuevaNotaActivity::class.java)
            fechaSeleccionada?.let {
                intent.putExtra("fechaSeleccionada", it.time)
            }
            startActivityForResult(intent, REQUEST_CODE_NUEVA_NOTA)
        }
    }

    private fun cargarNotasGuardadas() {
        val prefs = getSharedPreferences("notas_prefs", MODE_PRIVATE)
        val total = prefs.getInt("total_notas", 0)
        listaNotas.clear()
        for (i in 0 until total) {
            val titulo = prefs.getString("nota_titulo_$i", "") ?: ""
            val descripcion = prefs.getString("nota_descripcion_$i", "") ?: ""
            val categoria = prefs.getString("nota_categoria_$i", "") ?: ""
            val fecha = Date(prefs.getLong("nota_fecha_$i", System.currentTimeMillis()))
            listaNotas.add(Nota(i + 1, titulo, descripcion, categoria, fecha))
        }
    }

    private fun guardarNotasEnPrefs() {
        val prefs = getSharedPreferences("notas_prefs", MODE_PRIVATE)
        val editor = prefs.edit()
        editor.putInt("total_notas", listaNotas.size)
        listaNotas.forEachIndexed { i, nota ->
            editor.putString("nota_titulo_$i", nota.titulo)
            editor.putString("nota_descripcion_$i", nota.descripcion)
            editor.putString("nota_categoria_$i", nota.categoria)
            editor.putLong("nota_fecha_$i", nota.fecha.time)
        }
        editor.apply()
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
                guardarNotasEnPrefs()

                Toast.makeText(
                    this,
                    "Nota guardada ✅",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun onPause() {
        super.onPause()
        guardarNotasEnPrefs()
    }

    companion object {
        private const val REQUEST_CODE_NUEVA_NOTA = 100
    }
}