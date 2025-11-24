package com.example.taskifya.personalizacion

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Switch
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.taskifya.R

class PersonalizacionNotificacionesActivity : AppCompatActivity() {

    @SuppressLint("UseSwitchCompatOrMaterialCode", "UseKtx")
    override fun onCreate(savedInstanceState: Bundle?) {

        // APLICAR TEMA ROSA O NORMAL
        aplicarTema()

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_personalizacion_notificaciones)

        // Ajuste de barras del sistema
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Referencia al switch
        val switchNotificaciones = findViewById<Switch>(R.id.switchNotificaciones)

        // Cargar estado guardado
        val prefs = getSharedPreferences("notificaciones_prefs", MODE_PRIVATE)
        val activadas = prefs.getBoolean("notificaciones_activadas", true)
        switchNotificaciones.isChecked = activadas

        // Guardar cambios
        switchNotificaciones.setOnCheckedChangeListener { _, isChecked ->
            prefs.edit().putBoolean("notificaciones_activadas", isChecked).apply()
        }
    }

    //  FUNCIÓN QUE APLICA EL TEMA ROSA
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

