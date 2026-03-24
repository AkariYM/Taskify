package com.example.taskifya

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.taskifya.activities.MainActivity as NotasActivity
import com.example.taskifya.eventos.CalendarioMenuActivity
import com.example.taskifya.personalizacion.PersonalizacionActivity
import com.example.taskifya.usuario.LoginActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.btnNotas).setOnClickListener {
            startActivity(Intent(this, NotasActivity::class.java))
        }
        findViewById<Button>(R.id.btnCalendario).setOnClickListener {
            startActivity(Intent(this, CalendarioMenuActivity::class.java))
        }
        findViewById<Button>(R.id.btnUsuario).setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
        findViewById<Button>(R.id.btnPersonalizacion).setOnClickListener {
            startActivity(Intent(this, PersonalizacionActivity::class.java))
        }
        findViewById<Button>(R.id.btnRecursos).setOnClickListener {
            startActivity(Intent(this, RecursosActivity::class.java))
        }
    }
}