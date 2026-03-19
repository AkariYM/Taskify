package com.example.taskifya.usuario

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.taskifya.R
import com.example.taskifya.database.DatabaseHelper

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val etEmail = findViewById<EditText>(R.id.etEmail)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val btnIniciar = findViewById<Button>(R.id.btnIniciar)
        val tvRegistrate = findViewById<TextView>(R.id.tvRegistrate)
        val tvCambiarPass = findViewById<TextView>(R.id.tvCambiarContrasena)

        val db = DatabaseHelper(this)

        // Ir a Registro
        tvRegistrate.setOnClickListener {
            startActivity(Intent(this, RegistroActivity::class.java))
        }

        // Ir a cambiar contraseña
        tvCambiarPass.setOnClickListener {
            val correo = etEmail.text.toString().trim()
            val intent = Intent(this, CambiarPasswordActivity::class.java)
            intent.putExtra("correo", correo)
            startActivity(intent)
        }

        btnIniciar.setOnClickListener {
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Completa todos los campos.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(this, "Correo inválido.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // VALIDACIÓN REAL
            val usuario = db.obtenerUsuario(email)

            if (usuario == null) {
                Toast.makeText(this, "Este usuario no existe. Regístrate.", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            if (usuario.password != password) {
                Toast.makeText(this, "Contraseña incorrecta.", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            // LOGIN EXITOSO
            Toast.makeText(this, "Bienvenido ${usuario.nombre}", Toast.LENGTH_LONG).show()

            val intent = Intent(this, EditarPerfilActivity::class.java)
            intent.putExtra("correo", usuario.correo)
            startActivity(intent)
            finish()
        }
    }
}



