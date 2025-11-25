package com.example.taskifya.usuario

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.taskifya.MainActivity
import androidx.appcompat.app.AppCompatActivity
import com.example.taskifya.R

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Referencias a los elementos del XML
        val etEmail = findViewById<EditText>(R.id.etEmail)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val btnIniciar = findViewById<Button>(R.id.btnIniciar)
        val tvRegistrate = findViewById<TextView>(R.id.tvRegistrate)
        val tvCambiarPass = findViewById<TextView>(R.id.tvCambiarContrasena)

        // Navegación: Registro
        tvRegistrate.setOnClickListener {
            startActivity(Intent(this, RegistroActivity::class.java))
        }

        // Navegación: Cambiar Contraseña
        tvCambiarPass.setOnClickListener {
            startActivity(Intent(this, CambiarPasswordActivity::class.java))
        }

        // INICIAR SESIÓN (validaciones coherentes para tu app)
        btnIniciar.setOnClickListener {

            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()

            // Validación: Email vacío
            if (email.isEmpty()) {
                Toast.makeText(
                    this,
                    "Ingresa tu correo para continuar.",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            // Validación: Formato del email
            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(
                    this,
                    "Ingresa un correo válido.",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            // Validación: Contraseña vacía
            if (password.isEmpty()) {
                Toast.makeText(
                    this,
                    "Ingresa tu contraseña.",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            // validacion local

            if (email != "usuario@ejemplo.com" || password != "123456") {
                Toast.makeText(
                    this,
                    "Correo o contraseña incorrectos.",
                    Toast.LENGTH_LONG
                ).show()
                return@setOnClickListener
            }

            // Si todo está correcto:
            Toast.makeText(
                this,
                "¡Bienvenido! Vamos a organizar tu día.",
                Toast.LENGTH_LONG
            ).show()

            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}
