package com.example.taskifya.usuario

import android.os.Bundle
import android.text.InputType
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.taskifya.R
import com.example.taskifya.database.DatabaseHelper

class EditarPerfilActivity : AppCompatActivity() {

    private var passwordVisible = false
    private var usuarioId: Int = -1  // Guardaremos el ID real del usuario

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editar_perfil)

        val etNombre = findViewById<EditText>(R.id.etNombre)
        val etCorreo = findViewById<EditText>(R.id.etCorreo)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val ivToggle = findViewById<ImageView>(R.id.ivTogglePassword)

        val btnGuardar = findViewById<Button>(R.id.btnGuardarCambios)
        val btnCancelar = findViewById<Button>(R.id.btnCancelar)

        // ===========================
        // 1. RECIBIR CORREO DEL USUARIO
        // ===========================
        val correoOriginal = intent.getStringExtra("correo")

        if (correoOriginal == null) {
            Toast.makeText(this, "Error: No se recibió el usuario.", Toast.LENGTH_LONG).show()
            finish()
            return
        }

        // ===========================
        // 2. OBTENER USUARIO DE SQLITE
        // ===========================
        val db = DatabaseHelper(this)
        val usuario = db.obtenerUsuario(correoOriginal)

        if (usuario == null) {
            Toast.makeText(this, "Error: Usuario no encontrado.", Toast.LENGTH_LONG).show()
            finish()
            return
        }

        usuarioId = usuario.id

        // Rellenar datos en pantalla
        etNombre.setText(usuario.nombre)
        etCorreo.setText(usuario.correo)
        etPassword.setText(usuario.password)

        // ===========================
        // OJO: Mostrar/Ocultar Contraseña
        // ===========================
        ivToggle.setOnClickListener {
            passwordVisible = !passwordVisible

            if (passwordVisible) {
                etPassword.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            } else {
                etPassword.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            }

            etPassword.setSelection(etPassword.text.length)
        }

        // ===========================
        // CANCELAR
        // ===========================
        btnCancelar.setOnClickListener {
            finish()
        }

        // ===========================
        // GUARDAR CAMBIOS
        // ===========================
        btnGuardar.setOnClickListener {

            val nuevoNombre = etNombre.text.toString().trim()
            val nuevoCorreo = etCorreo.text.toString().trim()
            val nuevaPassword = etPassword.text.toString().trim()

            // VALIDACIONES BÁSICAS
            if (nuevoNombre.isEmpty()) {
                Toast.makeText(this, "Ingresa tu nombre.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (nuevoCorreo.isEmpty()) {
                Toast.makeText(this, "Ingresa tu correo.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(nuevoCorreo).matches()) {
                Toast.makeText(this, "Correo inválido.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (nuevaPassword.isEmpty()) {
                Toast.makeText(this, "Ingresa tu contraseña.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // ===========================
            // 3. SI EL CORREO CAMBIÓ, VALIDAR QUE NO EXISTA
            // ===========================
            if (nuevoCorreo != correoOriginal) {
                val usuarioExistente = db.obtenerUsuario(nuevoCorreo)
                if (usuarioExistente != null) {
                    Toast.makeText(this, "Este correo ya está registrado.", Toast.LENGTH_LONG).show()
                    return@setOnClickListener
                }
            }

            // ===========================
            // 4. ACTUALIZAR EN SQLITE
            // ===========================
            val ok = db.actualizarUsuario(
                usuarioId,
                nuevoNombre,
                nuevoCorreo,
                nuevaPassword
            )

            if (ok) {
                Toast.makeText(this, "Datos actualizados correctamente.", Toast.LENGTH_LONG).show()

                // Si cambia el correo, forzar login otra vez
                if (nuevoCorreo != correoOriginal) {
                    Toast.makeText(this, "Debes iniciar sesión nuevamente.", Toast.LENGTH_LONG).show()
                    finish()
                } else {
                    finish()
                }
            } else {
                Toast.makeText(this, "Error al guardar los cambios.", Toast.LENGTH_LONG).show()
            }
        }
    }
}
