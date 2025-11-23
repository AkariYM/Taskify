package com.example.taskifya.models

import java.util.*

data class Nota(
    val id: Int,
    val titulo: String,
    val descripcion: String,
    val categoria: String,
    val fecha: Date  // Ahora es un objeto Date, no String
)