package com.example.taskifya

import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.example.taskifya.R

open class AppBaseActivity : AppCompatActivity() {

    override fun getTheme(): android.content.res.Resources.Theme {
        val theme = super.getTheme()
        val prefsDark = applicationContext.getSharedPreferences("theme_prefs", MODE_PRIVATE)
        val prefsRosa = applicationContext.getSharedPreferences("temas", MODE_PRIVATE)
        val darkActivo = prefsDark.getBoolean("dark_mode", false)
        val rosaActivo = prefsRosa.getBoolean("temaRosa", false)
        when {
            rosaActivo -> theme.applyStyle(R.style.Theme_TaskifyA_Rose, true)
            darkActivo -> theme.applyStyle(R.style.Theme_TaskifyA_Dark, true)
            else -> theme.applyStyle(R.style.Theme_TaskifyA, true)
        }
        return theme
    }
}