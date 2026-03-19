package com.example.taskifya.usuario

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.taskifya.R
import com.example.taskifya.database.DatabaseHelper

class EditarPerfilActivity : AppCompatActivity() {

    private var passwordVisible = false
    private var usuarioId = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editar_perfil)

        val etNombre = findViewById<EditText>(R.id.etNombre)
        val etCorreo = findViewById<EditText>(R.id.etCorreo)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val ivToggle = findViewById<ImageView>(R.id.ivTogglePassword)
        val btnGuardar = findViewById<Button>(R.id.btnGuardarCambios)
        val btnCancelar = findViewById<Button>(R.id.btnCancelar)

        // ==========================
        // RECIBIR CORREO
        // ==========================
        val correoRecibido = intent.getStringExtra("correo")
        if (correoRecibido == null) {
            Toast.makeText(this, "Error al recibir datos.", Toast.LENGTH_LONG).show()
            finish()
            return
        }

        // ==========================
        // OBTENER USUARIO DE SQLITE
        // ==========================
        val db = DatabaseHelper(this)
        val usuario = db.obtenerUsuario(correoRecibido)

        if (usuario == null) {
            Toast.makeText(this, "Usuario no encontrado.", Toast.LENGTH_LONG).show()
            finish()
            return
        }

        usuarioId = usuario.id

        // Mostrar datos reales
        etNombre.setText(usuario.nombre)
        etCorreo.setText(usuario.correo)
        etPassword.setText(usuario.password)

        // ==========================
        // OJO: Mostrar/Ocultar password
        // ==========================
        ivToggle.setOnClickListener {
            passwordVisible = !passwordVisible
            etPassword.inputType =
                if (passwordVisible)
                    InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                else
                    InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD

            etPassword.setSelection(etPassword.text.length)
        }

        btnCancelar.setOnClickListener { finish() }

        // ==========================
        // GUARDAR CAMBIOS
        // ==========================
        btnGuardar.setOnClickListener {

            val nuevoNombre = etNombre.text.toString().trim()
            val nuevoCorreo = etCorreo.text.toString().trim()
            val nuevaPass = etPassword.text.toString().trim()

            if (nuevoNombre.isEmpty() || nuevoCorreo.isEmpty() || nuevaPass.isEmpty()) {
                Toast.makeText(this, "Completa todos los campos.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(nuevoCorreo).matches()) {
                Toast.makeText(this, "Correo inválido.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val ok = db.actualizarUsuario(usuarioId, nuevoNombre, nuevoCorreo, nuevaPass)

            if (ok) {
                Toast.makeText(this, "Datos actualizados. Inicia sesión de nuevo.", Toast.LENGTH_LONG).show()

                // REGRESAR A LOGIN AUTOMÁTICAMENTE

                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)

                finish()
            } else {
                Toast.makeText(this, "Error al actualizar usuario.", Toast.LENGTH_LONG).show()
            }
        }
    }
}
