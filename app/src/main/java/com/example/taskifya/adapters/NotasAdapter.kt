package com.example.taskifya.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.taskifya.R
import com.example.taskifya.models.Nota
import java.text.SimpleDateFormat
import java.util.*

class NotasAdapter(
    private val notasCompletas: MutableList<Nota>,
    private val onDeleteClick: (Int) -> Unit,
    private val onNotaClick: (Nota) -> Unit  // Nuevo callback para click
) : RecyclerView.Adapter<NotasAdapter.NotaViewHolder>() {

    private var notasFiltradas = mutableListOf<Nota>()
    private var textoFiltro = ""
    private var fechaFiltro: Date? = null
    private val formatoFecha = SimpleDateFormat("d MMM", Locale("es", "ES"))

    init {
        notasFiltradas.addAll(notasCompletas)
    }

    class NotaViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvCategoria: TextView = view.findViewById(R.id.tvCategoria)
        val tvFecha: TextView = view.findViewById(R.id.tvFecha)
        val ivEliminar: ImageView = view.findViewById(R.id.ivEliminar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotaViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_nota, parent, false)
        return NotaViewHolder(view)
    }

    override fun onBindViewHolder(holder: NotaViewHolder, position: Int) {
        val nota = notasFiltradas[position]
        holder.tvCategoria.text = nota.categoria
        holder.tvFecha.text = formatoFecha.format(nota.fecha)

        // Click en la nota completa para ver detalles
        holder.itemView.setOnClickListener {
            onNotaClick(nota)
        }

        holder.ivEliminar.setOnClickListener {
            val indexEnListaCompleta = notasCompletas.indexOf(nota)
            if (indexEnListaCompleta >= 0) {
                notasCompletas.removeAt(indexEnListaCompleta)
                aplicarFiltros()
            }
        }
    }

    override fun getItemCount(): Int = notasFiltradas.size

    fun addItem(nota: Nota) {
        notasCompletas.add(nota)
        aplicarFiltros()
    }

    // Método para filtrar por fecha
    fun filtrarPorFecha(fechaSeleccionada: Date?) {
        fechaFiltro = fechaSeleccionada
        aplicarFiltros()
    }

    // Método para filtrar por texto
    fun filtrarPorTexto(texto: String) {
        textoFiltro = texto.lowercase()
        aplicarFiltros()
    }

    // Aplicar ambos filtros (fecha Y texto)
    private fun aplicarFiltros() {
        notasFiltradas.clear()

        val calendar = Calendar.getInstance()

        for (nota in notasCompletas) {
            var cumpleFecha = true
            var cumpleTexto = true

            // Filtro por fecha
            if (fechaFiltro != null) {
                calendar.time = fechaFiltro!!
                val diaSeleccionado = calendar.get(Calendar.DAY_OF_YEAR)
                val anioSeleccionado = calendar.get(Calendar.YEAR)

                calendar.time = nota.fecha
                val diaNota = calendar.get(Calendar.DAY_OF_YEAR)
                val anioNota = calendar.get(Calendar.YEAR)

                cumpleFecha = (diaNota == diaSeleccionado && anioNota == anioSeleccionado)
            }

            // Filtro por texto
            if (textoFiltro.isNotEmpty()) {
                cumpleTexto = nota.titulo.lowercase().contains(textoFiltro) ||
                        nota.descripcion.lowercase().contains(textoFiltro) ||
                        nota.categoria.lowercase().contains(textoFiltro)
            }

            // Agregar si cumple AMBOS filtros
            if (cumpleFecha && cumpleTexto) {
                notasFiltradas.add(nota)
            }
        }

        notifyDataSetChanged()
    }

    // Método para mostrar todas las notas
    fun mostrarTodas() {
        fechaFiltro = null
        textoFiltro = ""
        notasFiltradas.clear()
        notasFiltradas.addAll(notasCompletas)
        notifyDataSetChanged()
    }

    // Obtener cantidad total de notas
    fun getTotalNotas(): Int = notasCompletas.size
}