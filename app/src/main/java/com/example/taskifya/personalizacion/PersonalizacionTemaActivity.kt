package com.example.taskifya.personalizacion

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SwitchCompat
import androidx.core.content.edit
import com.example.taskifya.R

class PersonalizacionTemaActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        // 1. APLICAR TEMA ROSA O NORMAL ANTES DEL setContentView
        aplicarTema()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_personalizacion_tema)

        // --- SWITCH DE MODO OSCURO ---
        val switchTheme = findViewById<SwitchCompat>(R.id.switchTheme)
        val prefsDark = getSharedPreferences("theme_prefs", MODE_PRIVATE)

        val isDark = prefsDark.getBoolean("dark_mode", false)
        switchTheme.isChecked = isDark

        AppCompatDelegate.setDefaultNightMode(
            if (isDark) AppCompatDelegate.MODE_NIGHT_YES
            else AppCompatDelegate.MODE_NIGHT_NO
        )

        switchTheme.setOnCheckedChangeListener { _, isChecked ->
            prefsDark.edit { putBoolean("dark_mode", isChecked) }

            AppCompatDelegate.setDefaultNightMode(
                if (isChecked) AppCompatDelegate.MODE_NIGHT_YES
                else AppCompatDelegate.MODE_NIGHT_NO
            )
        }


        // --- SWITCH DE TEMA ROSA ---
        val switchTemaRosa = findViewById<SwitchCompat>(R.id.switchTemaRosa)
        val prefsRosa = getSharedPreferences("temas", MODE_PRIVATE)

        val rosaActivo = prefsRosa.getBoolean("temaRosa", false)
        switchTemaRosa.isChecked = rosaActivo

        switchTemaRosa.setOnCheckedChangeListener { _, isChecked ->
            prefsRosa.edit { putBoolean("temaRosa", isChecked) }
            recreate()  // recargar activity aplicando nuevo tema
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
