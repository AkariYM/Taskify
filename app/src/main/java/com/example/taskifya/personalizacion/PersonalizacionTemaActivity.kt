@file:Suppress("DEPRECATION")

package com.example.taskifya.personalizacion

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SwitchCompat
import androidx.core.content.edit
import com.example.taskifya.R

class PersonalizacionTemaActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        aplicarTema()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_personalizacion_tema)

        val switchDark = findViewById<SwitchCompat>(R.id.switchTheme)
        val switchRosa = findViewById<SwitchCompat>(R.id.switchTemaRosa)

        val prefsDark = getSharedPreferences("theme_prefs", MODE_PRIVATE)
        val prefsRosa = getSharedPreferences("temas", MODE_PRIVATE)

        val darkActivo = prefsDark.getBoolean("dark_mode", false)
        val rosaActivo = prefsRosa.getBoolean("temaRosa", false)

        switchDark.isChecked = darkActivo
        switchRosa.isChecked = rosaActivo


        if (darkActivo) switchRosa.isChecked = false


        if (rosaActivo) switchDark.isChecked = false

        switchDark.setOnCheckedChangeListener { _, isChecked ->

            if (isChecked) {

                switchRosa.isChecked = false
                prefsRosa.edit { putBoolean("temaRosa", false) }
            }

            prefsDark.edit { putBoolean("dark_mode", isChecked) }

            AppCompatDelegate.setDefaultNightMode(
                if (isChecked) AppCompatDelegate.MODE_NIGHT_YES
                else AppCompatDelegate.MODE_NIGHT_NO
            )
        }


        switchRosa.setOnCheckedChangeListener { _, isChecked ->

            if (isChecked) {

                switchDark.isChecked = false
                prefsDark.edit { putBoolean("dark_mode", false) }
            }

            prefsRosa.edit { putBoolean("temaRosa", isChecked) }
            reiniciarActividadRapido()
        }
    }

    private fun aplicarTema() {
        val prefsDark = getSharedPreferences("theme_prefs", MODE_PRIVATE)
        val prefsRosa = getSharedPreferences("temas", MODE_PRIVATE)

        val darkActivo = prefsDark.getBoolean("dark_mode", false)
        val rosaActivo = prefsRosa.getBoolean("temaRosa", false)

        when {
            darkActivo -> setTheme(R.style.Theme_TaskifyA) // modo oscuro se activa automáticamente con delegate
            rosaActivo -> setTheme(R.style.Theme_TaskifyA_Rose)
            else -> setTheme(R.style.Theme_TaskifyA)
        }
    }

    @SuppressLint("UnsafeIntentLaunch")
    private fun reiniciarActividadRapido() {
        finish()
        overridePendingTransition(0, 0)
        startActivity(intent)
        overridePendingTransition(0, 0)
    }
}
