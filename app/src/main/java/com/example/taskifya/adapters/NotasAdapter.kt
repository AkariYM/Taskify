package com.example.taskifya.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.taskifya.R
import com.example.taskifya.models.Nota

class NotasAdapter(
    private val notas: MutableList<Nota>,
    private val onDeleteClick: (Int) -> Unit
) : RecyclerView.Adapter<NotasAdapter.NotaViewHolder>() {

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
        val nota = notas[position]
        holder.tvCategoria.text = nota.categoria
        holder.tvFecha.text = nota.fecha

        holder.ivEliminar.setOnClickListener {
            onDeleteClick(position)
        }
    }

    override fun getItemCount(): Int = notas.size

    fun removeItem(position: Int) {
        notas.removeAt(position)
        notifyItemRemoved(position)
    }

    fun addItem(nota: Nota) {
        notas.add(nota)
        notifyItemInserted(notas.size - 1)
    }
}