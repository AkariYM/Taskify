package com.example.taskifya.personalizacion

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SwitchCompat
import androidx.core.content.edit
import com.example.taskifya.R

class PersonalizacionTemaActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_personalizacion_tema)

        // --- SWITCH DE TEMA ---
        val switchTheme = findViewById<SwitchCompat>(R.id.switchTheme)

        // Obtener valor guardado del modo oscuro
        val prefs = getSharedPreferences("theme_prefs", MODE_PRIVATE)
        val isDark = prefs.getBoolean("dark_mode", false)

        // Inicializa el switch según el valor guardado
        switchTheme.isChecked = isDark

        // Aplica el modo oscuro o claro al iniciar
        AppCompatDelegate.setDefaultNightMode(
            if (isDark) AppCompatDelegate.MODE_NIGHT_YES
            else AppCompatDelegate.MODE_NIGHT_NO
        )

        // Listener para cambiar el tema al alternar el switch
        switchTheme.setOnCheckedChangeListener { _, isChecked ->
            // Guardar preferencia
            prefs.edit { putBoolean("dark_mode", isChecked) }

            // Cambiar tema inmediatamente
            AppCompatDelegate.setDefaultNightMode(
                if (isChecked) AppCompatDelegate.MODE_NIGHT_YES
                else AppCompatDelegate.MODE_NIGHT_NO
            )
        }
    }
}

