package com.example.taskifya

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.taskifya.recursos.HabitScreen
import com.example.taskifya.recursos.HabitViewModel
import com.example.taskifya.ui.theme.TaskifyATheme

class RecursosActivity : ComponentActivity() {
    private val habitViewModel: HabitViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TaskifyATheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    HabitScreen(viewModel = habitViewModel)
                }
            }
        }
    }
}