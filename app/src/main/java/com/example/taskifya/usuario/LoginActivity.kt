package com.example.taskifya.usuario

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.taskifya.R

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // REGISTRATE
        val tvRegistrate = findViewById<TextView>(R.id.tvRegistrate)
        tvRegistrate.setOnClickListener {
            startActivity(Intent(this, RegistroActivity::class.java))
        }

        // INICIAR
        val btnIniciar = findViewById<Button>(R.id.btnIniciar)
        btnIniciar.setOnClickListener {
            startActivity(Intent(this, EditarPerfilActivity::class.java))
        }

        // OLVIDE MI CONTRASEÑA
        val btnOlvide = findViewById<Button>(R.id.btnOlvide)
        btnOlvide.setOnClickListener {
            startActivity(Intent(this, RecuperarPasswordActivity::class.java))
        }

        // CAMBIAR CONTRASEÑA
        val tvCambiar = findViewById<TextView>(R.id.tvCambiarContrasena)
        tvCambiar.setOnClickListener {
            startActivity(Intent(this, CambiarPasswordActivity::class.java))
        }
    }
}