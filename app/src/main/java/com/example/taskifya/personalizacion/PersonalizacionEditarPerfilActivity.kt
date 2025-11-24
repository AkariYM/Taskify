package com.example.taskifya.personalizacion

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.taskifya.R


class PersonalizacionEditarPerfilActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_personalizacion_editar_perfil)

        // Botón guardar
        findViewById<Button>(R.id.buttonEditarPerfil).setOnClickListener {
            // Aquí guardas los cambios y cierras la actividad
            finish()

        }
    }
}
