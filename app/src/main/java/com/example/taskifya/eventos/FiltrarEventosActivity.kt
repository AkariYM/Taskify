package com.example.taskifya.eventos

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.taskifya.R
import java.util.concurrent.Executors

class FiltrarEventosActivity : AppCompatActivity() {

    private lateinit var repo: EventosRepository
    private val executor = Executors.newSingleThreadExecutor()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_filtrar_eventos)

        repo = EventosRepository(this)

        val swPersonal = findViewById<Switch>(R.id.switchPersonal)
        val swAcademico = findViewById<Switch>(R.id.switchAcademico)
        val swTrabajo = findViewById<Switch>(R.id.switchTrabajo)
        val btnAplicar = findViewById<Button>(R.id.btnAplicar)
        val listView = findViewById<ListView>(R.id.listFiltrada)

        fun refresh() {
            executor.execute {
                val combined = mutableListOf<Evento>()
                if (swPersonal.isChecked) combined.addAll(repo.porCategoria("PERSONAL"))
                if (swAcademico.isChecked) combined.addAll(repo.porCategoria("ACADEMICO"))
                if (swTrabajo.isChecked) combined.addAll(repo.porCategoria("TRABAJO"))
                runOnUiThread {
                    listView.adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, combined.map { "${it.fechaIso} ${it.hora} - ${it.titulo}" })
                }
            }
        }

        // defaults
        swPersonal.isChecked = true
        swAcademico.isChecked = false
        swTrabajo.isChecked = false
        btnAplicar.setOnClickListener { refresh() }

        // init list
        refresh()
    }

    override fun onDestroy() {
        super.onDestroy()
        executor.shutdown()
    }
}
