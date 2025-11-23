package com.example.taskifya.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.taskifya.R

class NuevaNotaActivity : AppCompatActivity() {

    private lateinit var etTitulo: EditText
    private lateinit var etDescripcion: EditText
    private lateinit var btnCrearNota: Button
    private lateinit var btnVolver: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nueva_nota)

        // Inicializar vistas
        etTitulo = findViewById(R.id.etTitulo)
        etDescripcion = findViewById(R.id.etDescripcion)
        btnCrearNota = findViewById(R.id.btnCrearNota)
        btnVolver = findViewById(R.id.btnVolver)

        // Botón volver
        btnVolver.setOnClickListener {
            finish()
        }

        // Botón crear nota
        btnCrearNota.setOnClickListener {
            val titulo = etTitulo.text.toString()
            val descripcion = etDescripcion.text.toString()

            if (titulo.isNotEmpty() && descripcion.isNotEmpty()) {
                val resultIntent = Intent()
                resultIntent.putExtra("titulo", titulo)
                resultIntent.putExtra("descripcion", descripcion)
                resultIntent.putExtra("categoria", titulo) // Usando título como categoría por ahora

                setResult(RESULT_OK, resultIntent)
                Toast.makeText(this, "Nota creada exitosamente", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Por favor completa todos los campos", Toast.LENGTH_SHORT).show()
            }
        }
    }
}