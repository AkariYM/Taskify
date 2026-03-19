package com.example.taskifya.usuario

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.taskifya.R
import com.example.taskifya.database.DatabaseHelper

class CambiarPasswordActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cambiar_password)

        val etActual = findViewById<EditText>(R.id.etContrasenaActual)
        val etNueva = findViewById<EditText>(R.id.etContrasenaNueva)
        val btnGuardar = findViewById<Button>(R.id.btnGuardarPassword)
        val tvVolver = findViewById<TextView>(R.id.tvVolverLoginDesdeCambiar)

        // ===========================
        // RECIBIR EL CORREO DEL LOGIN
        // ===========================
        val correoActual = intent.getStringExtra("correo")

        if (correoActual == null) {
            Toast.makeText(this, "Error: No se recibió el correo del usuario.", Toast.LENGTH_LONG).show()
            finish()
            return
        }

        val db = DatabaseHelper(this)
        val usuario = db.obtenerUsuario(correoActual)

        if (usuario == null) {
            Toast.makeText(this, "Error: Usuario no encontrado.", Toast.LENGTH_LONG).show()
            finish()
            return
        }

        // ===========================
        // BOTÓN "VOLVER"
        // ===========================
        tvVolver.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        // ===========================
        // BOTÓN GUARDAR (CAMBIAR PASS)
        // ===========================
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
                    "La nueva contraseña debe ser distinta a la actual.",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            // ===========================
            // VALIDACIÓN REAL: actual correcta
            // ===========================
            if (actual != usuario.password) {
                Toast.makeText(
                    this,
                    "La contraseña actual no coincide.",
                    Toast.LENGTH_LONG
                ).show()
                return@setOnClickListener
            }

            // ===========================
            // ACTUALIZAR PASSWORD EN SQLITE
            // ===========================
            val ok = db.cambiarPassword(usuario.id, nueva)

            if (ok) {
                Toast.makeText(
                    this,
                    "Contraseña actualizada correctamente.",
                    Toast.LENGTH_LONG
                ).show()

                // Redirigir a Login para volver a entrar
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            } else {
                Toast.makeText(
                    this,
                    "Error al actualizar la contraseña.",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
}
