@file:Suppress("DEPRECATION")
package com.example.taskifya.personalizacion

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SwitchCompat
import androidx.core.content.edit
import com.example.taskifya.R

class PersonalizacionTemaActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_personalizacion_tema)

        val switchDark = findViewById<SwitchCompat>(R.id.switchTheme)
        val switchRosa = findViewById<SwitchCompat>(R.id.switchTemaRosa)

        val prefsDark = getSharedPreferences("theme_prefs", MODE_PRIVATE)
        val prefsRosa = getSharedPreferences("temas", MODE_PRIVATE)

        switchDark.isChecked = prefsDark.getBoolean("dark_mode", false)
        switchRosa.isChecked = prefsRosa.getBoolean("temaRosa", false)

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
            reiniciar()
        }

        switchRosa.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                switchDark.isChecked = false
                prefsDark.edit { putBoolean("dark_mode", false) }
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
            prefsRosa.edit { putBoolean("temaRosa", isChecked) }
            reiniciar()
        }
    }

    @SuppressLint("UnsafeIntentLaunch")
    private fun reiniciar() {
        finish()
        overridePendingTransition(0, 0)
        startActivity(intent)
        overridePendingTransition(0, 0)
    }
}