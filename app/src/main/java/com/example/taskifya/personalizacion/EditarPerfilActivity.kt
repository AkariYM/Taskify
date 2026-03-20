package com.example.taskifya.personalizacion

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.taskifya.R

class EditarPerfilActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {


        aplicarTema()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_placeholder) // O el layout real
    }

    private fun aplicarTema() {
        val prefsDark = getSharedPreferences("theme_prefs", MODE_PRIVATE)
        val prefsRosa = getSharedPreferences("temas", MODE_PRIVATE)

        val darkActivo = prefsDark.getBoolean("dark_mode", false)
        val rosaActivo = prefsRosa.getBoolean("temaRosa", false)

        when {
            darkActivo -> setTheme(R.style.Theme_TaskifyA) // Android aplica modo oscuro solo
            rosaActivo -> setTheme(R.style.Theme_TaskifyA_Rose)
            else -> setTheme(R.style.Theme_TaskifyA)
        }
    }
}
