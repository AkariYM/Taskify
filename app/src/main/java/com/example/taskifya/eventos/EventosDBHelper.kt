package com.example.taskifya.eventos

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class EventosDbHelper(context: Context, correoUsuario: String = "default") :
    SQLiteOpenHelper(context, "eventos_${correoUsuario.replace("@","_").replace(".","_")}.db", null, DATABASE_VERSION) {

    companion object {
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
                do { list.add(cursorToEvento(it)) } while (it.moveToNext())
            }
        }
        return list
    }

    fun obtenerPorFecha(fechaIso: String): List<Evento> {
        val db = readableDatabase
        val list = mutableListOf<Evento>()
        val c = db.query(TABLE, null, "$COL_FECHA = ?", arrayOf(fechaIso), null, null, COL_HORA)
        c.use {
            if (it.moveToFirst()) {
                do { list.add(cursorToEvento(it)) } while (it.moveToNext())
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
                do { list.add(cursorToEvento(it)) } while (it.moveToNext())
            }
        }
        return list
    }

    fun obtenerPorId(id: Long): Evento? {
        val db = readableDatabase
        val c = db.query(TABLE, null, "$COL_ID = ?", arrayOf(id.toString()), null, null, null)
        return c.use {
            if (it.moveToFirst()) cursorToEvento(it) else null
        }
    }

    private fun cursorToEvento(c: android.database.Cursor): Evento {
        return Evento(
            id = c.getLong(c.getColumnIndexOrThrow(COL_ID)),
            titulo = c.getString(c.getColumnIndexOrThrow(COL_TITULO)),
            descripcion = c.getString(c.getColumnIndexOrThrow(COL_DESCRIPCION)),
            fechaIso = c.getString(c.getColumnIndexOrThrow(COL_FECHA)),
            hora = c.getString(c.getColumnIndexOrThrow(COL_HORA)) ?: "",
            categoria = c.getString(c.getColumnIndexOrThrow(COL_CATEGORIA)) ?: "PERSONAL",
            isReminder = c.getInt(c.getColumnIndexOrThrow(COL_IS_REMINDER)),
            repeticion = c.getString(c.getColumnIndexOrThrow(COL_REPETICION))
        )
    }
}