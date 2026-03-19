package com.example.taskifya.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, "taskify_temp.db", null, 1) {

    override fun onCreate(db: SQLiteDatabase) {

        // Tabla temporal solo para tu módulo de usuario
        db.execSQL(
            """
            CREATE TABLE usuarios (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                nombre TEXT NOT NULL,
                correo TEXT NOT NULL UNIQUE,
                password TEXT NOT NULL
            );
        """.trimIndent()
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS usuarios")
        onCreate(db)
    }

    // REGISTRAR USUARIO TEMPORAL
    fun registrarUsuario(nombre: String, correo: String, password: String): Boolean {
        val db = writableDatabase
        val valores = ContentValues()

        valores.put("nombre", nombre)
        valores.put("correo", correo)
        valores.put("password", password)

        return db.insert("usuarios", null, valores) > 0
    }

    // LOGIN TEMPORAL
    fun loginUsuario(correo: String, password: String): Boolean {
        val db = readableDatabase

        val cursor = db.rawQuery(
            "SELECT * FROM usuarios WHERE correo=? AND password=?",
            arrayOf(correo, password)
        )

        val existe = cursor.moveToFirst()
        cursor.close()
        return existe
    }

    // OBTENER USUARIO TEMPORAL
    fun obtenerUsuario(correo: String): UserModel? {
        val db = readableDatabase

        val cursor = db.rawQuery(
            "SELECT * FROM usuarios WHERE correo=?",
            arrayOf(correo)
        )

        return if (cursor.moveToFirst()) {
            val user = UserModel(
                id = cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                nombre = cursor.getString(cursor.getColumnIndexOrThrow("nombre")),
                correo = cursor.getString(cursor.getColumnIndexOrThrow("correo")),
                password = cursor.getString(cursor.getColumnIndexOrThrow("password"))
            )
            cursor.close()
            user
        } else {
            cursor.close()
            null
        }
    }

    // ACTUALIZAR USUARIO TEMPORAL
    fun actualizarUsuario(id: Int, nombre: String, correo: String, password: String): Boolean {
        val db = writableDatabase
        val valores = ContentValues()

        valores.put("nombre", nombre)
        valores.put("correo", correo)
        valores.put("password", password)

        return db.update("usuarios", valores, "id=?", arrayOf(id.toString())) > 0
    }

    // CAMBIAR CONTRASEÑA TEMPORAL
    fun cambiarPassword(id: Int, password: String): Boolean {
        val db = writableDatabase
        val valores = ContentValues()
        valores.put("password", password)

        return db.update("usuarios", valores, "id=?", arrayOf(id.toString())) > 0
    }
}

data class UserModel(
    val id: Int,
    val nombre: String,
    val correo: String,
    val password: String
)