package com.example.taskifya.eventos

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.taskifya.R

class CalendarioMenuActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendario_menu)

        findViewById<Button>(R.id.btnCalendario).setOnClickListener {
            startActivity(Intent(this, CalendarioActivity::class.java))
        }
        findViewById<Button>(R.id.btnCrearEvento).setOnClickListener {
            startActivity(Intent(this, CrearEventoActivity::class.java))
        }
        findViewById<Button>(R.id.btnCrearRecordatorio).setOnClickListener {
            startActivity(Intent(this, CrearRecordatorioActivity::class.java))
        }
        findViewById<Button>(R.id.btnFiltrarEventos).setOnClickListener {
            startActivity(Intent(this, FiltrarEventosActivity::class.java))
        }
    }

    private fun getCorreo(): String =
        getSharedPreferences("sesion", MODE_PRIVATE).getString("correo", "default") ?: "default"
}