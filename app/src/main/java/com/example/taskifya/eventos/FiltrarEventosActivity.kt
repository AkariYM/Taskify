package com.example.taskifya.eventos

import android.content.res.ColorStateList
import android.graphics.Color
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

        // Configurar colores de switches: azul cuando ON, gris cuando OFF
        configurarColoresSwitch(swPersonal)
        configurarColoresSwitch(swAcademico)
        configurarColoresSwitch(swTrabajo)

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

    // Función que configura los colores del switch según su estado (ON=azul, OFF=gris)
    private fun configurarColoresSwitch(switch: Switch) {
        val azul = Color.parseColor("#1976D2")  // Color azul para cuando está activado
        val azulClaro = Color.parseColor("#BBDEFB")  // Color azul claro para el riel cuando está activado
        val gris = Color.parseColor("#9E9E9E")  // Color gris para cuando está desactivado
        val grisClaro = Color.parseColor("#E0E0E0")  // Color gris claro para el riel cuando está desactivado

        // Configurar color de la bolita (thumb)
        val thumbStates = ColorStateList(
            arrayOf(
                intArrayOf(android.R.attr.state_checked),  // Estado: activado
                intArrayOf()  // Estado: desactivado
            ),
            intArrayOf(azul, gris)  // Colores: azul cuando ON, gris cuando OFF
        )

        // Configurar color del riel (track)
        val trackStates = ColorStateList(
            arrayOf(
                intArrayOf(android.R.attr.state_checked),  // Estado: activado
                intArrayOf()  // Estado: desactivado
            ),
            intArrayOf(azulClaro, grisClaro)  // Colores: azul claro cuando ON, gris claro cuando OFF
        )

        // Aplicar los colores al switch
        switch.thumbTintList = thumbStates
        switch.trackTintList = trackStates
    }

    override fun onDestroy() {
        super.onDestroy()
        executor.shutdown()
    }
}
