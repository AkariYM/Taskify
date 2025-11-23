package com.example.taskifya.usuario

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.taskifya.R

class RegistroActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)

        // Referencias de las vistas
        val etNombre = findViewById<EditText>(R.id.etNombre)
        val etEmail = findViewById<EditText>(R.id.etEmail)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val etConfirmarPassword = findViewById<EditText>(R.id.etConfirmarPassword)
        val btnRegistrar = findViewById<Button>(R.id.btnRegistrar)
        val tvYaTengoCuenta = findViewById<TextView>(R.id.tvYaTengoCuenta)

        // Acción: regresar al login
        tvYaTengoCuenta.setOnClickListener {
            finish()
        }

        // Acción: REGISTRAR con validaciones
        btnRegistrar.setOnClickListener {

            val nombre = etNombre.text.toString().trim()
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()
            val confirmarPassword = etConfirmarPassword.text.toString().trim()

            // Validación de campos vacíos
            if (nombre.isEmpty()) {
                Toast.makeText(this, "Ingresa tu nombre", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (email.isEmpty()) {
                Toast.makeText(this, "Ingresa tu email", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Validación de email correcto
            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(this, "Email no válido", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password.isEmpty()) {
                Toast.makeText(this, "Ingresa una contraseña", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Largo mínimo
            if (password.length < 6) {
                Toast.makeText(
                    this,
                    "La contraseña debe tener al menos 6 caracteres",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            if (confirmarPassword.isEmpty()) {
                Toast.makeText(this, "Confirma tu contraseña", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Coincidencia
            if (password != confirmarPassword) {
                Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // TODO: Guardar en SQLite (lo haremos después)
            Toast.makeText(this, "Validación correcta. Listo para guardar en BD.", Toast.LENGTH_LONG).show()
        }
    }
}

