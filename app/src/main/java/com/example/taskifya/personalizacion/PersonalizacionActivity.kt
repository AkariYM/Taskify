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

        // Aplicar tema rosa
        aplicarTema()

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_personalizacion)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(
                systemBars.left,
                systemBars.top,
                systemBars.right,
                systemBars.bottom
            )
            insets
        }

        // BOTONES DE NAVEGACIÓN
        findViewById<Button>(R.id.buttonTemas).setOnClickListener {
            startActivity(Intent(this, PersonalizacionTemaActivity::class.java))
        }

        findViewById<Button>(R.id.buttonNotificaciones).setOnClickListener {
            startActivity(Intent(this, PersonalizacionNotificacionesActivity::class.java))
        }

        findViewById<Button>(R.id.buttonAyuda).setOnClickListener {
            startActivity(Intent(this, PersonalizacionAyudaRapidaActivity::class.java))
        }

        findViewById<Button>(R.id.buttonBackup).setOnClickListener {
            startActivity(Intent(this, PersonalizacionCopiaDeSeguridadActivity::class.java))
        }

        findViewById<Button>(R.id.buttonEditarPerfil).setOnClickListener {
            startActivity(Intent(this, PersonalizacionEditarPerfilActivity::class.java))
        }
    }


    //  FUNCIÓN PARA APLICAR TEMA ROSA A TODA ESTA PANTALLA
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
