package com.example.taskifya.personalizacion

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.taskifya.R


class PersonalizacionEditarPerfilActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_personalizacion_editar_perfil)

// Recibir correo del usuario (enviado desde Login o MainActivity)
        val correoUsuario = intent.getStringExtra("correo")

        val btnEditarPerfil = findViewById<Button>(R.id.buttonEditarPerfil)

        btnEditarPerfil.setOnClickListener {

            // Enviar el correo a TU pantalla de edición
//            val intent = Intent(this, EditarPerfilActivity::class.java)
//            intent.putExtra("correo", correoUsuario)
//            startActivity(intent)
          }
        }
}