package com.example.taskifya.eventos

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.taskifya.R

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class EventosDbHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        const val DATABASE_NAME = "eventos.db"
        const val DATABASE_VERSION = 1

        const val TABLE = "eventos"
        const val COL_ID = "id"
        const val COL_TITULO = "titulo"
        const val COL_DESCRIPCION = "descripcion"
        const val COL_FECHA = "fechaIso"
        const val COL_HORA = "hora"
        const val COL_CATEGORIA = "categoria"
        const val COL_IS_REMINDER = "isReminder"
        const val COL_REPETICION = "repeticion"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val create = """
            CREATE TABLE $TABLE (
              $COL_ID INTEGER PRIMARY KEY AUTOINCREMENT,
              $COL_TITULO TEXT NOT NULL,
              $COL_DESCRIPCION TEXT,
              $COL_FECHA TEXT NOT NULL,
              $COL_HORA TEXT,
              $COL_CATEGORIA TEXT,
              $COL_IS_REMINDER INTEGER DEFAULT 0,
              $COL_REPETICION TEXT
            );
        """.trimIndent()
        db.execSQL(create)

        // seed data
        fun seed(titulo: String, descripcion: String, fecha: String, hora: String, categoria: String) {
            val cv = ContentValues().apply {
                put(COL_TITULO, titulo)
                put(COL_DESCRIPCION, descripcion)
                put(COL_FECHA, fecha)
                put(COL_HORA, hora)
                put(COL_CATEGORIA, categoria)
                put(COL_IS_REMINDER, 0)
                put(COL_REPETICION, "NUNCA")
            }
            db.insert(TABLE, null, cv)
        }
        seed("Comprar despensa", "Leche, pan, huevos", "2025-11-05", "10:00", "PERSONAL")
        seed("Examen Parcial", "Capítulo 3", "2025-11-10", "08:30", "ACADEMICO")
        seed("Pagar agua", "", "2025-11-12", "12:00", "PERSONAL")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE")
        onCreate(db)
    }

    fun insertar(e: Evento): Long {
        val db = writableDatabase
        val cv = ContentValues().apply {
            put(COL_TITULO, e.titulo)
            put(COL_DESCRIPCION, e.descripcion)
            put(COL_FECHA, e.fechaIso)
            put(COL_HORA, e.hora)
            put(COL_CATEGORIA, e.categoria)
            put(COL_IS_REMINDER, e.isReminder)
            put(COL_REPETICION, e.repeticion)
        }
        return db.insert(TABLE, null, cv)
    }

    fun actualizar(e: Evento): Int {
        val db = writableDatabase
        val cv = ContentValues().apply {
            put(COL_TITULO, e.titulo)
            put(COL_DESCRIPCION, e.descripcion)
            put(COL_FECHA, e.fechaIso)
            put(COL_HORA, e.hora)
            put(COL_CATEGORIA, e.categoria)
            put(COL_IS_REMINDER, e.isReminder)
            put(COL_REPETICION, e.repeticion)
        }
        return db.update(TABLE, cv, "$COL_ID = ?", arrayOf(e.id.toString()))
    }

    fun eliminar(id: Long): Int {
        val db = writableDatabase
        return db.delete(TABLE, "$COL_ID = ?", arrayOf(id.toString()))
    }

    fun obtenerTodos(): List<Evento> {
        val db = readableDatabase
        val list = mutableListOf<Evento>()
        val c = db.query(TABLE, null, null, null, null, null, "$COL_FECHA, $COL_HORA")
        c.use {
            if (it.moveToFirst()) {
                do {
                    list.add(cursorToEvento(
                        it.getLong(it.getColumnIndexOrThrow(COL_ID)),
                        it.getString(it.getColumnIndexOrThrow(COL_TITULO)),
                        it.getString(it.getColumnIndexOrThrow(COL_DESCRIPCION)),
                        it.getString(it.getColumnIndexOrThrow(COL_FECHA)),
                        it.getString(it.getColumnIndexOrThrow(COL_HORA)),
                        it.getString(it.getColumnIndexOrThrow(COL_CATEGORIA)),
                        it.getInt(it.getColumnIndexOrThrow(COL_IS_REMINDER)),
                        it.getString(it.getColumnIndexOrThrow(COL_REPETICION))
                    ))
                } while (it.moveToNext())
            }
        }
        return list
    }

    private fun cursorToEvento(
        id: Long, titulo: String, descripcion: String?,
        fecha: String, hora: String?, categoria: String?, isReminder: Int, repeticion: String?
    ): Evento {
        return Evento(
            id = id,
            titulo = titulo,
            descripcion = descripcion,
            fechaIso = fecha,
            hora = hora ?: "",
            categoria = categoria ?: "PERSONAL",
            isReminder = isReminder,
            repeticion = repeticion
        )
    }

    fun obtenerPorFecha(fechaIso: String): List<Evento> {
        val db = readableDatabase
        val list = mutableListOf<Evento>()
        val c = db.query(TABLE, null, "$COL_FECHA = ?", arrayOf(fechaIso), null, null, "$COL_HORA")
        c.use {
            if (it.moveToFirst()) {
                do {
                    list.add(cursorToEvento(
                        it.getLong(it.getColumnIndexOrThrow(COL_ID)),
                        it.getString(it.getColumnIndexOrThrow(COL_TITULO)),
                        it.getString(it.getColumnIndexOrThrow(COL_DESCRIPCION)),
                        it.getString(it.getColumnIndexOrThrow(COL_FECHA)),
                        it.getString(it.getColumnIndexOrThrow(COL_HORA)),
                        it.getString(it.getColumnIndexOrThrow(COL_CATEGORIA)),
                        it.getInt(it.getColumnIndexOrThrow(COL_IS_REMINDER)),
                        it.getString(it.getColumnIndexOrThrow(COL_REPETICION))
                    ))
                } while (it.moveToNext())
            }
        }
        return list
    }

    fun obtenerPorCategoria(cat: String): List<Evento> {
        val db = readableDatabase
        val list = mutableListOf<Evento>()
        val c = db.query(TABLE, null, "$COL_CATEGORIA = ?", arrayOf(cat), null, null, "$COL_FECHA, $COL_HORA")
        c.use {
            if (it.moveToFirst()) {
                do {
                    list.add(cursorToEvento(
                        it.getLong(it.getColumnIndexOrThrow(COL_ID)),
                        it.getString(it.getColumnIndexOrThrow(COL_TITULO)),
                        it.getString(it.getColumnIndexOrThrow(COL_DESCRIPCION)),
                        it.getString(it.getColumnIndexOrThrow(COL_FECHA)),
                        it.getString(it.getColumnIndexOrThrow(COL_HORA)),
                        it.getString(it.getColumnIndexOrThrow(COL_CATEGORIA)),
                        it.getInt(it.getColumnIndexOrThrow(COL_IS_REMINDER)),
                        it.getString(it.getColumnIndexOrThrow(COL_REPETICION))
                    ))
                } while (it.moveToNext())
            }
        }
        return list
    }
}