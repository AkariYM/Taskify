package com.example.taskifya.usuario

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.taskifya.MainActivity
import com.example.taskifya.R
import com.example.taskifya.database.DatabaseHelper

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

        // Ir a Registro
        tvRegistrate.setOnClickListener {
            startActivity(Intent(this, RegistroActivity::class.java))
        }

        // Ir a Cambiar Contraseña (ENVIANDO EL CORREO)
        tvCambiarPass.setOnClickListener {
            val correo = etEmail.text.toString().trim()

            if (correo.isEmpty()) {
                Toast.makeText(this, "Primero ingresa tu correo para continuar.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val intent = Intent(this, CambiarPasswordActivity::class.java)
            intent.putExtra("correo", correo)
            startActivity(intent)
        }

        // INICIAR SESIÓN con SQLite REAL
        btnIniciar.setOnClickListener {

            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()

            // ============================
            // VALIDACIONES
            // ============================

            if (email.isEmpty()) {
                Toast.makeText(this, "Ingresa tu correo.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

           // Validación: Formato del email
            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(this, "Ingresa un correo válido.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password.isEmpty()) {
                Toast.makeText(this, "Ingresa tu contraseña.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // ============================
            // VALIDACIÓN REAL CON SQLITE
            // ============================

            val db = DatabaseHelper(this)
            val loginCorrecto = db.loginUsuario(email, password)

            if (!loginCorrecto) {
                Toast.makeText(this, "Correo o contraseña incorrectos.", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            // ============================
            // LOGIN EXITOSO
            // ============================

            Toast.makeText(this, "¡Bienvenido!", Toast.LENGTH_LONG).show()

            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("correo", email)
            startActivity(intent)

            finish()
        }
    }
}

