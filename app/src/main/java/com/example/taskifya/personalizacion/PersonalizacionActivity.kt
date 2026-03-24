package com.example.taskifya.personalizacion

import android.content.Intent
import android.os.Bundle
import androidx.cardview.widget.CardView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.taskifya.R

class PersonalizacionActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_personalizacion)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        findViewById<CardView>(R.id.buttonTemas).setOnClickListener {
            startActivity(Intent(this, PersonalizacionTemaActivity::class.java))
        }
        findViewById<CardView>(R.id.buttonNotificaciones).setOnClickListener {
            startActivity(Intent(this, PersonalizacionNotificacionesActivity::class.java))
        }
        findViewById<CardView>(R.id.buttonAyuda).setOnClickListener {
            startActivity(Intent(this, PersonalizacionAyudaRapidaActivity::class.java))
        }
        findViewById<CardView>(R.id.buttonBackup).setOnClickListener {
            startActivity(Intent(this, PersonalizacionCopiaDeSeguridadActivity::class.java))
        }
        findViewById<CardView>(R.id.buttonEditarPerfil).setOnClickListener {
            startActivity(Intent(this, PersonalizacionEditarPerfilActivity::class.java))
        }
    }
}