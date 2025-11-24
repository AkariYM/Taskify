package com.example.taskifya.usuario

import android.os.Bundle
import android.text.InputType
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.taskifya.R

class EditarPerfilActivity : AppCompatActivity() {

    private var passwordVisible = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editar_perfil)

        val etNombre = findViewById<EditText>(R.id.etNombre)
        val etCorreo = findViewById<EditText>(R.id.etCorreo)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val ivToggle = findViewById<ImageView>(R.id.ivTogglePassword)

        val btnGuardar = findViewById<Button>(R.id.btnGuardarCambios)
        val btnCancelar = findViewById<Button>(R.id.btnCancelar)

        // OJO para mostrar/ocultar contraseña
        ivToggle.setOnClickListener {
            passwordVisible = !passwordVisible

            if (passwordVisible) {
                etPassword.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            } else {
                etPassword.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            }

            etPassword.setSelection(etPassword.text.length)  // Mantener cursor al final
        }

        // Botón cancelar
        btnCancelar.setOnClickListener {
            finish()
        }

        // Botón guardar
        btnGuardar.setOnClickListener {

            val nombre = etNombre.text.toString().trim()
            val correo = etCorreo.text.toString().trim()
            val password = etPassword.text.toString().trim()

            if (nombre.isEmpty()) {
                Toast.makeText(this, "Ingresa tu nombre.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (correo.isEmpty()) {
                Toast.makeText(this, "Ingresa tu correo.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(correo).matches()) {
                Toast.makeText(this, "Correo inválido.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password.isEmpty()) {
                Toast.makeText(this, "Ingresa tu contraseña.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Aquí luego guardaremos todo en SQLite
            Toast.makeText(
                this,
                "Datos actualizados correctamente.",
                Toast.LENGTH_LONG
            ).show()

            finish()
        }
    }
}