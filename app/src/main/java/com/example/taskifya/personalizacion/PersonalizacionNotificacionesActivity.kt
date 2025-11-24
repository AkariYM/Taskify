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
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_personalizacion_notificaciones)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val switchNotificaciones = findViewById<Switch>(R.id.switchNotificaciones)

        // Cargar estado guardado
        val prefs = getSharedPreferences("notificaciones_prefs", MODE_PRIVATE)
        switchNotificaciones.isChecked = prefs.getBoolean("notificaciones_activadas", true)

        // Listener para guardar cambios
        switchNotificaciones.setOnCheckedChangeListener { _, isChecked ->
            prefs.edit().putBoolean("notificaciones_activadas", isChecked).apply()
        }
    }
}
