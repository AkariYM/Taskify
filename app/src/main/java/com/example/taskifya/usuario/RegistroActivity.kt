package com.example.taskifya.usuario

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.taskifya.R
import com.example.taskifya.database.DatabaseHelper

class RegistroActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)

        val etNombre = findViewById<EditText>(R.id.etNombre)
        val etEmail = findViewById<EditText>(R.id.etEmail)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val etConfirmar = findViewById<EditText>(R.id.etConfirmarPassword)
        val tvYaTengoCuenta = findViewById<TextView>(R.id.tvYaTengoCuenta)
        val btnRegistrar = findViewById<Button>(R.id.btnRegistrar)

        tvYaTengoCuenta.setOnClickListener {
            finish()
        }

        btnRegistrar.setOnClickListener {
            val nombre = etNombre.text.toString().trim()
            val correo = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()
            val confirmar = etConfirmar.text.toString().trim()

            if (nombre.isEmpty()) {
                Toast.makeText(this, "Ingresa tu nombre.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (correo.isEmpty()) {
                Toast.makeText(this, "Ingresa tu correo.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(correo).matches()) {
                Toast.makeText(this, "Ingresa un correo válido.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (password.isEmpty()) {
                Toast.makeText(this, "Ingresa tu contraseña.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (password.length < 6) {
                Toast.makeText(this, "La contraseña debe tener al menos 6 caracteres.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (password != confirmar) {
                Toast.makeText(this, "Las contraseñas no coinciden.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val db = DatabaseHelper(this)
            val ok = db.registrarUsuario(nombre, correo, password)

            if (ok) {
                Toast.makeText(this, "¡Registro exitoso! Ya puedes iniciar sesión.", Toast.LENGTH_LONG).show()
                finish()
            } else {
                Toast.makeText(this, "Este correo ya está registrado.", Toast.LENGTH_LONG).show()
            }
        }
    }
}