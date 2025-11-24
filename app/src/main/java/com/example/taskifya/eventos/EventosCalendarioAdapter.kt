package com.example.taskifya.eventos

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.taskifya.R

class EventosCalendarioAdapter(
    private val context: Context,
    private var eventos: List<Evento>
) : BaseAdapter() {

    override fun getCount(): Int = eventos.size

    override fun getItem(position: Int): Any = eventos[position]

    override fun getItemId(position: Int): Long = eventos[position].id

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View
        val holder: ViewHolder

        if (convertView == null) {
            // Inflar el layout personalizado
            view = LayoutInflater.from(context).inflate(R.layout.item_evento_calendario, parent, false)
            holder = ViewHolder(view)
            view.tag = holder
        } else {
            view = convertView
            holder = view.tag as ViewHolder
        }

        // Obtener el evento actual
        val evento = eventos[position]

        // Mostrar hora y título
        holder.tvHoraTitulo.text = "${evento.hora} - ${evento.titulo}"

        // Mostrar descripción solo si existe y no está vacía
        if (!evento.descripcion.isNullOrBlank()) {
            holder.tvDescripcion.visibility = View.VISIBLE
            holder.tvDescripcion.text = evento.descripcion
        } else {
            holder.tvDescripcion.visibility = View.GONE
        }

        return view
    }

    // Función para actualizar la lista de eventos
    fun actualizarEventos(nuevosEventos: List<Evento>) {
        eventos = nuevosEventos
        notifyDataSetChanged()
    }

    // ViewHolder para optimizar el rendimiento
    private class ViewHolder(view: View) {
        val tvHoraTitulo: TextView = view.findViewById(R.id.tvHoraTitulo)
        val tvDescripcion: TextView = view.findViewById(R.id.tvDescripcion)
    }
}