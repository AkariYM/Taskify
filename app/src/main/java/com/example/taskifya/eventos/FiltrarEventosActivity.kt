package com.example.taskifya.eventos

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import com.example.taskifya.R
import java.util.concurrent.Executors

class FiltrarEventosActivity : AppCompatActivity() {

    private lateinit var repo: EventosRepository
    private val executor = Executors.newSingleThreadExecutor()
    private lateinit var adapter: EventosAdapter
    private lateinit var swPersonal: SwitchCompat
    private lateinit var swAcademico: SwitchCompat
    private lateinit var swTrabajo: SwitchCompat

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_filtrar_eventos)

        val correo = getSharedPreferences("sesion", MODE_PRIVATE).getString("correo", "default") ?: "default"
        repo = EventosRepository(this, correo)

        swPersonal = findViewById(R.id.switchPersonal)
        swAcademico = findViewById(R.id.switchAcademico)
        swTrabajo = findViewById(R.id.switchTrabajo)
        val btnAplicar = findViewById<Button>(R.id.btnAplicar)
        val btnTodos = findViewById<Button>(R.id.btnTodos)
        val listView = findViewById<ListView>(R.id.listFiltrada)

        adapter = EventosAdapter(
            context = this,
            eventos = emptyList(),
            onEditarClick = { evento -> abrirEditarEvento(evento) },
            onEliminarClick = { evento -> confirmarEliminar(evento) }
        )
        listView.adapter = adapter

        swPersonal.isChecked = true
        swAcademico.isChecked = false
        swTrabajo.isChecked = false

        btnAplicar.setOnClickListener { refresh() }
        btnTodos.setOnClickListener { mostrarTodos() }

        refresh()
    }

    private fun refresh() {
        executor.execute {
            val combined = mutableListOf<Evento>()
            if (swPersonal.isChecked) combined.addAll(repo.porCategoria("PERSONAL"))
            if (swAcademico.isChecked) combined.addAll(repo.porCategoria("ACADEMICO"))
            if (swTrabajo.isChecked) combined.addAll(repo.porCategoria("TRABAJO"))
            runOnUiThread { adapter.actualizarEventos(combined) }
        }
    }

    private fun mostrarTodos() {
        executor.execute {
            val todos = repo.todos()
            runOnUiThread { adapter.actualizarEventos(todos) }
        }
    }

    private fun abrirEditarEvento(evento: Evento) {
        val intent = Intent(this, EditarEventoActivity::class.java)
        intent.putExtra("EVENTO_ID", evento.id)
        startActivity(intent)
    }

    private fun confirmarEliminar(evento: Evento) {
        AlertDialog.Builder(this)
            .setTitle("Eliminar evento")
            .setMessage("¿Eliminar \"${evento.titulo}\"?")
            .setPositiveButton("Eliminar") { _, _ -> eliminarEvento(evento) }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun eliminarEvento(evento: Evento) {
        executor.execute {
            repo.eliminar(evento.id)
            runOnUiThread {
                Toast.makeText(this, "Evento eliminado", Toast.LENGTH_SHORT).show()
                refresh()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        refresh()
    }

    override fun onDestroy() {
        super.onDestroy()
        executor.shutdown()
    }
}