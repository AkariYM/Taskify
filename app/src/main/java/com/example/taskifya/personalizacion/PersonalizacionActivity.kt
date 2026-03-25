package com.example.taskifya.personalizacion

import android.content.Intent
import android.os.Bundle
import androidx.cardview.widget.CardView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.taskifya.R
import com.example.taskifya.usuario.EditarPerfilActivity

class PersonalizacionActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_personalizacion)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val correo = getSharedPreferences("sesion", MODE_PRIVATE)
            .getString("correo", "") ?: ""

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
            val intent = Intent(this, EditarPerfilActivity::class.java)
            intent.putExtra("correo", correo)
            startActivity(intent)
        }
    }
}