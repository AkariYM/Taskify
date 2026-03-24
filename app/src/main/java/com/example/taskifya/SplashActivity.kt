package com.example.taskifya

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.taskifya.usuario.LoginActivity

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Verificar sesión activa
        val prefs = getSharedPreferences("sesion", MODE_PRIVATE)
        val correoGuardado = prefs.getString("correo", null)

        if (correoGuardado != null) {
            val intent = Intent(this, DashboardActivity::class.java)
            intent.putExtra("correo", correoGuardado)
            startActivity(intent)
            finish()
            return
        }

        setContentView(R.layout.activity_splash)
        findViewById<Button>(R.id.btnComenzar).setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
}