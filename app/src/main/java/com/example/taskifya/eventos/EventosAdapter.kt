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
    private val onEditarClick: (Evento) -> Unit,
    private val onEliminarClick: (Evento) -> Unit
) : BaseAdapter() {

    override fun getCount(): Int = eventos.size

    override fun getItem(position: Int): Any = eventos[position]

    override fun getItemId(position: Int): Long = eventos[position].id

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View
        val holder: ViewHolder

        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_evento_filtrado, parent, false)
            holder = ViewHolder(view)
            view.tag = holder
        } else {
            view = convertView
            holder = view.tag as ViewHolder
        }

        val evento = eventos[position]

        // Mostrar fecha, hora y título
        val partesFecha = evento.fechaIso.split("-")
        val fechaFormateada = if (partesFecha.size == 3) {
            "${partesFecha[2]}/${partesFecha[1]}"
        } else {
            evento.fechaIso
        }
        holder.tvEventoInfo.text = "$fechaFormateada ${evento.hora} - ${evento.titulo}"

        // Mostrar descripción solo si existe y no está vacía
        if (!evento.descripcion.isNullOrBlank()) {
            holder.tvDescripcion.visibility = View.VISIBLE
            holder.tvDescripcion.text = evento.descripcion
        } else {
            holder.tvDescripcion.visibility = View.GONE
        }

        // Configurar botones
        holder.btnEditar.setOnClickListener {
            onEditarClick(evento)
        }

        holder.btnEliminar.setOnClickListener {
            onEliminarClick(evento)
        }

        return view
    }

    fun actualizarEventos(nuevosEventos: List<Evento>) {
        eventos = nuevosEventos
        notifyDataSetChanged()
    }

    private class ViewHolder(view: View) {
        val tvEventoInfo: TextView = view.findViewById(R.id.tvEventoInfo)
        val tvDescripcion: TextView = view.findViewById(R.id.tvDescripcion)  // ← Agregado
        val btnEditar: ImageButton = view.findViewById(R.id.btnEditar)
        val btnEliminar: ImageButton = view.findViewById(R.id.btnEliminar)
    }
}
