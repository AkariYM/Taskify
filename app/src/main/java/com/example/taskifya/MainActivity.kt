package com.example.taskifya

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.taskifya.eventos.CalendarioActivity
import com.example.taskifya.eventos.CrearEventoActivity
import com.example.taskifya.eventos.CrearRecordatorioActivity
import com.example.taskifya.eventos.FiltrarEventosActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Botón para abrir Calendario
        findViewById<Button>(R.id.btnCalendario).setOnClickListener {
            startActivity(Intent(this, CalendarioActivity::class.java))
        }

        // Botón para abrir Crear Evento
        findViewById<Button>(R.id.btnCrearEvento).setOnClickListener {
            startActivity(Intent(this, CrearEventoActivity::class.java))
        }

        // Botón para abrir Crear Recordatorio
        findViewById<Button>(R.id.btnCrearRecordatorio).setOnClickListener {
            startActivity(Intent(this, CrearRecordatorioActivity::class.java))
        }

        // Botón para abrir Filtrar Eventos
        findViewById<Button>(R.id.btnFiltrarEventos).setOnClickListener {
            startActivity(Intent(this, FiltrarEventosActivity::class.java))
        }
    }
}