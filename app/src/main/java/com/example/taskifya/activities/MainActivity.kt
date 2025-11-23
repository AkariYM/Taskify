package com.example.taskifya.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.taskifya.R
import com.example.taskifya.adapters.NotasAdapter
import com.example.taskifya.models.Nota
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var notasAdapter: NotasAdapter
    private lateinit var fabAgregarNota: FloatingActionButton
    private val listaNotas = mutableListOf<Nota>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Inicializar vistas
        recyclerView = findViewById(R.id.recyclerViewNotas)
        fabAgregarNota = findViewById(R.id.fabAgregarNota)

        // Configurar RecyclerView
        notasAdapter = NotasAdapter(listaNotas) { position ->
            notasAdapter.removeItem(position)
        }

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = notasAdapter

        // Agregar notas de ejemplo
        agregarNotasEjemplo()

        // Click en FAB para agregar nueva nota
        fabAgregarNota.setOnClickListener {
            val intent = Intent(this, NuevaNotaActivity::class.java)
            startActivityForResult(intent, REQUEST_CODE_NUEVA_NOTA)
        }
    }

    private fun agregarNotasEjemplo() {
        listaNotas.add(Nota(1, "Reunión", "Reunión con el equipo", "Trabajo", "4 Oct"))
        listaNotas.add(Nota(2, "Compras", "Ir al supermercado", "Personal", "4 Oct"))
        listaNotas.add(Nota(3, "Ejercicio", "Gimnasio a las 6pm", "Salud", "4 Oct"))
        notasAdapter.notifyDataSetChanged()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE_NUEVA_NOTA && resultCode == RESULT_OK) {
            data?.let {
                val titulo = it.getStringExtra("titulo") ?: ""
                val descripcion = it.getStringExtra("descripcion") ?: ""
                val categoria = it.getStringExtra("categoria") ?: "Sin categoría"

                val nuevaNota = Nota(
                    id = listaNotas.size + 1,
                    titulo = titulo,
                    descripcion = descripcion,
                    categoria = categoria,
                    fecha = "7 Oct"
                )

                notasAdapter.addItem(nuevaNota)
            }
        }
    }

    companion object {
        private const val REQUEST_CODE_NUEVA_NOTA = 100
    }
}