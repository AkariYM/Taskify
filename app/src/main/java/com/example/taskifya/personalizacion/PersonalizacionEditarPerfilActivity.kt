package com.example.taskifya.personalizacion

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.taskifya.R

class PersonalizacionEditarPerfilActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_personalizacion_editar_perfil)


        // Botón que abre Editar Perfil
        findViewById<Button>(R.id.buttonEditarPerfil).setOnClickListener {
            val intent = Intent(this, EditarPerfilActivity::class.java)
            startActivity(intent)

        }
    }
}
