package com.example.taskifya.personalizacion

import androidx.appcompat.app.AppCompatActivity
import com.example.taskifya.R

open class BaseActivity : AppCompatActivity() {

    override fun getTheme(): android.content.res.Resources.Theme {
        val theme = super.getTheme()
        val prefs = applicationContext.getSharedPreferences("temas", MODE_PRIVATE)
        val rosaActivo = prefs.getBoolean("temaRosa", false)
        if (rosaActivo) {
            theme.applyStyle(R.style.Theme_TaskifyA_Rose, true)
        } else {
            theme.applyStyle(R.style.Theme_TaskifyA, true)
        }
        return theme
    }
}