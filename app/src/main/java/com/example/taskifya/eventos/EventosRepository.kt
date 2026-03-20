package com.example.taskifya.eventos

import android.content.Context

class EventosRepository(context: Context) {
    private val db = EventosDbHelper(context.applicationContext)

    fun insertar(e: Evento): Long = db.insertar(e)
    fun actualizar(e: Evento): Int = db.actualizar(e)
    fun eliminar(id: Long): Int = db.eliminar(id)
    fun todos(): List<Evento> = db.obtenerTodos()
    fun porFecha(fecha: String): List<Evento> = db.obtenerPorFecha(fecha)
    fun porCategoria(cat: String): List<Evento> = db.obtenerPorCategoria(cat)
    fun obtenerPorId(id: Long): Evento? = db.obtenerPorId(id)
}