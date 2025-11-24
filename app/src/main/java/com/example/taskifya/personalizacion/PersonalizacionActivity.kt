package com.example.taskifya.personalizacion

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.taskifya.R

class PersonalizacionActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_personalizacion)

        // Edge-to-edge padding
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Botón de Temas
        findViewById<Button>(R.id.buttonTemas).setOnClickListener {
            startActivity(Intent(this, PersonalizacionTemaActivity::class.java))
        }

        // Botón de Notificaciones
        findViewById<Button>(R.id.buttonNotificaciones).setOnClickListener {
            startActivity(Intent(this, PersonalizacionNotificacionesActivity::class.java))
        }

        // Botón de Ayuda rápida
        findViewById<Button>(R.id.buttonAyuda).setOnClickListener {
            startActivity(Intent(this, PersonalizacionAyudaRapidaActivity::class.java))
        }

        // Botón de Copia de seguridad
        findViewById<Button>(R.id.buttonBackup).setOnClickListener {
            startActivity(Intent(this, PersonalizacionCopiaDeSeguridadActivity::class.java))
        }

        // Botón de Editar perfil (actividad de David)

        findViewById<Button>(R.id.buttonEditarPerfil).setOnClickListener {
            val intent = Intent(this, PersonalizacionEditarPerfilActivity::class.java)
            startActivity(intent)
        }

    }
}










