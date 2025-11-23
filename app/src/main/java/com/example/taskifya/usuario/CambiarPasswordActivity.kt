package com.example.taskifya.usuario

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.taskifya.R

class CambiarPasswordActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cambiar_password)

        val etActual = findViewById<EditText>(R.id.etContrasenaActual)
        val etNueva = findViewById<EditText>(R.id.etContrasenaNueva)
        val btnGuardar = findViewById<Button>(R.id.btnGuardarPassword)
        val tvVolver = findViewById<TextView>(R.id.tvVolverLoginDesdeCambiar)

        // Regresar al login
        tvVolver.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        btnGuardar.setOnClickListener {

            val actual = etActual.text.toString().trim()
            val nueva = etNueva.text.toString().trim()

            // Validación: campos vacíos
            if (actual.isEmpty()) {
                Toast.makeText(this, "Ingresa tu contraseña actual.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (nueva.isEmpty()) {
                Toast.makeText(this, "Ingresa tu nueva contraseña.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Validación: nueva ≠ actual
            if (actual == nueva) {
                Toast.makeText(
                    this,
                    "Tu nueva contraseña debe ser diferente a la actual.",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            // Validación: (simulada) contraseña correcta
            // Cuando tengamos SQLite, aquí se validará
            if (actual != "123456") {
                Toast.makeText(
                    this,
                    "La contraseña actual no coincide.",
                    Toast.LENGTH_LONG
                ).show()
                return@setOnClickListener
            }

            // Guardado (simulado)
            Toast.makeText(
                this,
                "Contraseña actualizada correctamente.",
                Toast.LENGTH_LONG
            ).show()

            // Regresar al Login
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
}