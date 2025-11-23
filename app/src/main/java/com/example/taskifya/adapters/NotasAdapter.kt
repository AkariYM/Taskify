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
    private val onDeleteClick: (Int) -> Unit
) : RecyclerView.Adapter<NotasAdapter.NotaViewHolder>() {

    private var notasFiltradas = mutableListOf<Nota>()
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

        holder.ivEliminar.setOnClickListener {
            val indexEnListaCompleta = notasCompletas.indexOf(nota)
            if (indexEnListaCompleta >= 0) {
                notasCompletas.removeAt(indexEnListaCompleta)
                notasFiltradas.removeAt(position)
                notifyItemRemoved(position)
                notifyItemRangeChanged(position, notasFiltradas.size)
            }
        }
    }

    override fun getItemCount(): Int = notasFiltradas.size

    fun addItem(nota: Nota) {
        notasCompletas.add(nota)
        notasFiltradas.add(nota)
        notifyItemInserted(notasFiltradas.size - 1)
    }

    // Método para filtrar notas por fecha
    fun filtrarPorFecha(fechaSeleccionada: Date) {
        val calendar = Calendar.getInstance()
        calendar.time = fechaSeleccionada
        val diaSeleccionado = calendar.get(Calendar.DAY_OF_YEAR)
        val anioSeleccionado = calendar.get(Calendar.YEAR)

        notasFiltradas.clear()

        for (nota in notasCompletas) {
            calendar.time = nota.fecha
            val diaNota = calendar.get(Calendar.DAY_OF_YEAR)
            val anioNota = calendar.get(Calendar.YEAR)

            if (diaNota == diaSeleccionado && anioNota == anioSeleccionado) {
                notasFiltradas.add(nota)
            }
        }

        notifyDataSetChanged()
    }

    // Método para mostrar todas las notas
    fun mostrarTodas() {
        notasFiltradas.clear()
        notasFiltradas.addAll(notasCompletas)
        notifyDataSetChanged()
    }

    // Obtener cantidad total de notas
    fun getTotalNotas(): Int = notasCompletas.size
}