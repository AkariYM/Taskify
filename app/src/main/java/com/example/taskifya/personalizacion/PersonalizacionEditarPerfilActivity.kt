package com.example.taskifya.personalizacion

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.taskifya.R

class PersonalizacionEditarPerfilActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {


        aplicarTema()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_personalizacion_editar_perfil)



        val btnEditarPerfil = findViewById<Button>(R.id.buttonEditarPerfil)

        btnEditarPerfil.setOnClickListener {
            // Aquí después harás la navegación a la pantalla final de EDITAR PERFIL
            // val intent = Intent(this, EditarPerfilActivity::class.java)
            // intent.putExtra("correo", correoUsuario)
            // startActivity(intent)
        }
    }


    private fun aplicarTema() {
        val prefs = getSharedPreferences("temas", MODE_PRIVATE)
        val rosaActivo = prefs.getBoolean("temaRosa", false)

        if (rosaActivo) {
            setTheme(R.style.Theme_TaskifyA_Rose)
        } else {
            setTheme(R.style.Theme_TaskifyA)
        }
    }
}
