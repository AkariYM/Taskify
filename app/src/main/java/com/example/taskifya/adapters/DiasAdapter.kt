package com.example.taskifya.adapters

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.taskifya.R
import com.example.taskifya.models.Dia

class DiasAdapter(
    private val dias: List<Dia>,
    private val onDiaClick: (Int) -> Unit
) : RecyclerView.Adapter<DiasAdapter.DiaViewHolder>() {

    private var selectedPosition = -1

    class DiaViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvNumeroDia: TextView = view.findViewById(R.id.tvNumeroDia)
        val tvNombreDia: TextView = view.findViewById(R.id.tvNombreDia)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DiaViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_dia, parent, false)
        return DiaViewHolder(view)
    }

    @SuppressLint("RecyclerView")
    override fun onBindViewHolder(holder: DiaViewHolder, position: Int) {
        val dia = dias[position]

        holder.tvNumeroDia.text = dia.numeroDia.toString()
        holder.tvNombreDia.text = dia.nombreDia

        // Aplicar estilo según si está seleccionado o no
        if (dia.isSelected) {
            selectedPosition = position
            holder.tvNumeroDia.setBackgroundResource(R.drawable.circle_background_selected)
            holder.tvNumeroDia.setTextColor(Color.BLACK)
            holder.tvNombreDia.setTextColor(Color.BLACK)
            holder.tvNumeroDia.textSize = 18f
        } else {
            holder.tvNumeroDia.setBackgroundResource(R.drawable.circle_background)
            holder.tvNumeroDia.setTextColor(Color.WHITE)
            holder.tvNombreDia.setTextColor(Color.GRAY)
            holder.tvNumeroDia.textSize = 16f
        }

        // Click en un día
        holder.itemView.setOnClickListener {
            val currentPosition = holder.bindingAdapterPosition
            if (currentPosition == RecyclerView.NO_POSITION) return@setOnClickListener

            // Deseleccionar el anterior
            if (selectedPosition >= 0 && selectedPosition < dias.size) {
                dias[selectedPosition].isSelected = false
                notifyItemChanged(selectedPosition)
            }

            // Seleccionar el nuevo
            selectedPosition = currentPosition
            dias[selectedPosition].isSelected = true
            notifyItemChanged(selectedPosition)

            onDiaClick(currentPosition)
        }
    }

    override fun getItemCount(): Int = dias.size

    fun setSelectedPosition(position: Int) {
        if (selectedPosition >= 0 && selectedPosition < dias.size) {
            dias[selectedPosition].isSelected = false
            notifyItemChanged(selectedPosition)
        }
        if (position >= 0 && position < dias.size) {
            selectedPosition = position
            dias[position].isSelected = true
            notifyItemChanged(position)
        }
    }
}