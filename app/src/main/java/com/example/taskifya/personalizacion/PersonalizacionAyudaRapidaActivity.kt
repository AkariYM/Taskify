package com.example.taskifya.personalizacion

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.taskifya.R

class PersonalizacionAyudaRapidaActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_personalizacion_ayuda_rapida)
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
