package com.example.taskifya.eventos

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.taskifya.R


data class Evento(
    val id: Long = 0,
    val titulo: String,
    val descripcion: String?,
    val fechaIso: String, // YYYY-MM-DD
    val hora: String,     // HH:mm
    val categoria: String, // PERSONAL | ACADEMICO | TRABAJO
    val isReminder: Int = 0, // 0 or 1
    val repeticion: String? = null // NUNCA | SEMANAL
)