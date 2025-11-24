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

class FechasAdapter(
    private val dias: List<Dia>,
    private val onFechaClick: (Int) -> Unit
) : RecyclerView.Adapter<FechasAdapter.FechaViewHolder>() {

    private var selectedPosition = -1

    class FechaViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvDiaSemana: TextView = view.findViewById(R.id.tvDiaSemana)
        val tvDiaMes: TextView = view.findViewById(R.id.tvDiaMes)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FechaViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_fecha, parent, false)
        return FechaViewHolder(view)
    }

    @SuppressLint("RecyclerView")
    override fun onBindViewHolder(holder: FechaViewHolder, position: Int) {
        val dia = dias[position]

        holder.tvDiaSemana.text = dia.nombreDia
        holder.tvDiaMes.text = dia.numeroDia.toString()

        // Aplicar estilo según si está seleccionado o no
        if (dia.isSelected) {
            selectedPosition = position
            holder.tvDiaMes.setBackgroundResource(R.drawable.date_background_selected)
            holder.tvDiaMes.setTextColor(Color.BLACK)
            holder.tvDiaSemana.setTextColor(Color.BLACK)
            holder.tvDiaMes.textSize = 18f
            holder.tvDiaSemana.textSize = 13f
        } else {
            holder.tvDiaMes.setBackgroundResource(R.drawable.date_background)
            holder.tvDiaMes.setTextColor(Color.DKGRAY)
            holder.tvDiaSemana.setTextColor(Color.GRAY)
            holder.tvDiaMes.textSize = 16f
            holder.tvDiaSemana.textSize = 12f
        }

        // Click en una fecha
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

            onFechaClick(currentPosition)
        }
    }

    override fun getItemCount(): Int = dias.size
}