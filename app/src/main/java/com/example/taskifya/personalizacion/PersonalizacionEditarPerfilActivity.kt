package com.example.taskifya.personalizacion

import android.content.Intent
import android.os.Bundle
import androidx.cardview.widget.CardView
import com.example.taskifya.R
import com.example.taskifya.usuario.EditarPerfilActivity

class PersonalizacionEditarPerfilActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_personalizacion_editar_perfil)

        // Obtener correo de sesión
        val correo = getSharedPreferences("sesion", MODE_PRIVATE)
            .getString("correo", "") ?: ""

        findViewById<CardView>(R.id.buttonEditarPerfil).setOnClickListener {
            val intent = Intent(this, EditarPerfilActivity::class.java)
            intent.putExtra("correo", correo)
            startActivity(intent)
        }
    }
}