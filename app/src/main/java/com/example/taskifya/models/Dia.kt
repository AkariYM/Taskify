package com.example.taskifya.models

import java.util.*

data class Dia(
    val numeroDia: Int,
    val nombreDia: String,
    val fecha: Date,
    var isSelected: Boolean = false
)