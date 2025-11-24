package com.example.taskifya.eventos

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageButton
import android.widget.TextView
import com.example.taskifya.R

class EventosAdapter(
    private val context: Context,
    private var eventos: List<Evento>,
    private val onEditarClick: (Evento) -> Unit,  // Callback para editar
    private val onEliminarClick: (Evento) -> Unit  // Callback para eliminar
) : BaseAdapter() {

    override fun getCount(): Int = eventos.size

    override fun getItem(position: Int): Any = eventos[position]

    override fun getItemId(position: Int): Long = eventos[position].id

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View
        val holder: ViewHolder

        if (convertView == null) {
            // Inflar el layout personalizado para cada item
            view = LayoutInflater.from(context).inflate(R.layout.item_evento_filtrado, parent, false)
            holder = ViewHolder(view)
            view.tag = holder
        } else {
            view = convertView
            holder = view.tag as ViewHolder
        }

        // Obtener el evento actual
        val evento = eventos[position]

        // Mostrar información del evento (formato: "DD/MM HH:MM - Título")
        val partesFecha = evento.fechaIso.split("-")
        val fechaFormateada = if (partesFecha.size == 3) {
            "${partesFecha[2]}/${partesFecha[1]}"
        } else {
            evento.fechaIso
        }
        holder.tvEventoInfo.text = "$fechaFormateada ${evento.hora} - ${evento.titulo}"

        // Configurar botón Editar
        holder.btnEditar.setOnClickListener {
            onEditarClick(evento)
        }

        // Configurar botón Eliminar
        holder.btnEliminar.setOnClickListener {
            onEliminarClick(evento)
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
        val tvEventoInfo: TextView = view.findViewById(R.id.tvEventoInfo)
        val btnEditar: ImageButton = view.findViewById(R.id.btnEditar)
        val btnEliminar: ImageButton = view.findViewById(R.id.btnEliminar)
    }
}