package com.example.taskifya.eventos

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.taskifya.R
import java.util.concurrent.Executors

class FiltrarEventosActivity : AppCompatActivity() {

    private lateinit var repo: EventosRepository
    private val executor = Executors.newSingleThreadExecutor()
    private lateinit var adapter: EventosAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_filtrar_eventos)

        repo = EventosRepository(this)

        val swPersonal = findViewById<Switch>(R.id.switchPersonal)
        val swAcademico = findViewById<Switch>(R.id.switchAcademico)
        val swTrabajo = findViewById<Switch>(R.id.switchTrabajo)
        val btnAplicar = findViewById<Button>(R.id.btnAplicar)
        val btnTodos = findViewById<Button>(R.id.btnTodos)
        val listView = findViewById<ListView>(R.id.listFiltrada)

        // Configurar colores de switches: azul cuando ON, gris cuando OFF
        configurarColoresSwitch(swPersonal)
        configurarColoresSwitch(swAcademico)
        configurarColoresSwitch(swTrabajo)

        // Configurar el adapter con callbacks para editar y eliminar
        adapter = EventosAdapter(
            context = this,
            eventos = emptyList(),
            onEditarClick = { evento -> abrirEditarEvento(evento) },
            onEliminarClick = { evento -> confirmarEliminar(evento) }
        )
        listView.adapter = adapter

        // Función para refrescar la lista según los filtros seleccionados
        fun refresh() {
            executor.execute {
                val combined = mutableListOf<Evento>()
                if (swPersonal.isChecked) combined.addAll(repo.porCategoria("PERSONAL"))
                if (swAcademico.isChecked) combined.addAll(repo.porCategoria("ACADEMICO"))
                if (swTrabajo.isChecked) combined.addAll(repo.porCategoria("TRABAJO"))

                runOnUiThread {
                    adapter.actualizarEventos(combined)
                }
            }
        }

        // Función para mostrar TODOS los eventos
        fun mostrarTodos() {
            executor.execute {
                val todosEventos = repo.todos()
                runOnUiThread {
                    adapter.actualizarEventos(todosEventos)
                }
            }
        }

        // defaults
        swPersonal.isChecked = true
        swAcademico.isChecked = false
        swTrabajo.isChecked = false

        // Configurar botones
        btnAplicar.setOnClickListener { refresh() }
        btnTodos.setOnClickListener { mostrarTodos() }

        // init list
        refresh()
    }

    // Función que configura los colores del switch según su estado (ON=azul, OFF=gris)
    private fun configurarColoresSwitch(switch: Switch) {
        val azul = Color.parseColor("#1976D2")
        val azulClaro = Color.parseColor("#BBDEFB")
        val gris = Color.parseColor("#9E9E9E")
        val grisClaro = Color.parseColor("#E0E0E0")

        val thumbStates = ColorStateList(
            arrayOf(
                intArrayOf(android.R.attr.state_checked),
                intArrayOf()
            ),
            intArrayOf(azul, gris)
        )

        val trackStates = ColorStateList(
            arrayOf(
                intArrayOf(android.R.attr.state_checked),
                intArrayOf()
            ),
            intArrayOf(azulClaro, grisClaro)
        )

        switch.thumbTintList = thumbStates
        switch.trackTintList = trackStates
    }

    // Abrir la pantalla de Editar Evento
    private fun abrirEditarEvento(evento: Evento) {
        val intent = Intent(this, EditarEventoActivity::class.java)
        intent.putExtra("EVENTO_ID", evento.id)
        startActivity(intent)
    }

    // Confirmar antes de eliminar el evento
    private fun confirmarEliminar(evento: Evento) {
        AlertDialog.Builder(this)
            .setTitle("Eliminar evento")
            .setMessage("¿Estás seguro de que deseas eliminar \"${evento.titulo}\"?")
            .setPositiveButton("Eliminar") { _, _ ->
                eliminarEvento(evento)
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    // Eliminar evento de la base de datos
    private fun eliminarEvento(evento: Evento) {
        executor.execute {
            repo.eliminar(evento.id)
            runOnUiThread {
                Toast.makeText(this, "Evento eliminado", Toast.LENGTH_SHORT).show()
                // Recargar la lista después de eliminar
                val swPersonal = findViewById<Switch>(R.id.switchPersonal)
                val swAcademico = findViewById<Switch>(R.id.switchAcademico)
                val swTrabajo = findViewById<Switch>(R.id.switchTrabajo)

                executor.execute {
                    val combined = mutableListOf<Evento>()
                    if (swPersonal.isChecked) combined.addAll(repo.porCategoria("PERSONAL"))
                    if (swAcademico.isChecked) combined.addAll(repo.porCategoria("ACADEMICO"))
                    if (swTrabajo.isChecked) combined.addAll(repo.porCategoria("TRABAJO"))

                    runOnUiThread {
                        adapter.actualizarEventos(combined)
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        // Recargar la lista cuando volvemos de editar un evento
        val swPersonal = findViewById<Switch>(R.id.switchPersonal)
        val swAcademico = findViewById<Switch>(R.id.switchAcademico)
        val swTrabajo = findViewById<Switch>(R.id.switchTrabajo)

        executor.execute {
            val combined = mutableListOf<Evento>()
            if (swPersonal.isChecked) combined.addAll(repo.porCategoria("PERSONAL"))
            if (swAcademico.isChecked) combined.addAll(repo.porCategoria("ACADEMICO"))
            if (swTrabajo.isChecked) combined.addAll(repo.porCategoria("TRABAJO"))

            runOnUiThread {
                adapter.actualizarEventos(combined)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        executor.shutdown()
    }
}