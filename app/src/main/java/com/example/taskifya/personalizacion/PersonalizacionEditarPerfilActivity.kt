package com.example.taskifya.personalizacion

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.taskifya.R

class PersonalizacionEditarPerfilActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        //  Aplicar tema rosa antes del layout
        aplicarTema()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_personalizacion_editar_perfil)

        // Recibir correo si en algún momento lo envías

        val btnEditarPerfil = findViewById<Button>(R.id.buttonEditarPerfil)

        btnEditarPerfil.setOnClickListener {
            // Aquí después harás la navegación a la pantalla final de EDITAR PERFIL
            // val intent = Intent(this, EditarPerfilActivity::class.java)
            // intent.putExtra("correo", correoUsuario)
            // startActivity(intent)
        }
    }

    //  FUNCIÓN QUE APLICA EL TEMA ROSA GLOBAL
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
