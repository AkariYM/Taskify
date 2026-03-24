package com.example.taskifya

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModelProvider
import com.example.taskifya.recursos.HabitDatabase
import com.example.taskifya.recursos.HabitRepository
import com.example.taskifya.recursos.HabitScreen
import com.example.taskifya.recursos.HabitViewModel
import com.example.taskifya.recursos.HabitViewModelFactory

class RecursosActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val correo = getSharedPreferences("sesion", MODE_PRIVATE)
            .getString("correo", "default") ?: "default"

        val db = HabitDatabase.getDatabase(applicationContext, correo)
        val repo = HabitRepository(db)
        val factory = HabitViewModelFactory(application, repo)
        val habitViewModel = ViewModelProvider(this, factory)[HabitViewModel::class.java]

        setContent {
            MaterialTheme(
                colorScheme = MaterialTheme.colorScheme.copy(
                    primary = Color(0xFF6200EE),
                    secondary = Color(0xFF3700B3),
                    background = Color(0xFFF5F5F5),
                    surface = Color.White
                )
            ) {
                Surface(modifier = Modifier.fillMaxSize()) {
                    HabitScreen(viewModel = habitViewModel)
                }
            }
        }
    }
}