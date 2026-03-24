package com.example.taskifya.recursos

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HabitScreen(viewModel: HabitViewModel) {
    var showAddDialog by remember { mutableStateOf(false) }
    var showEditDialog by remember { mutableStateOf<Habit?>(null) }

    val habits by viewModel.allHabits.observeAsState(initial = emptyList())
    val todaysRecords by viewModel.todaysHabitRecords.observeAsState(initial = emptyList())
    val progress by viewModel.dailyProgress.observeAsState(initial = 0f)

    if (showAddDialog) {
        AddHabitDialog(
            onDismiss = { showAddDialog = false },
            onConfirm = { habitName ->
                viewModel.insertHabit(habitName, "General")
                showAddDialog = false
            }
        )
    }

    showEditDialog?.let { habit ->
        EditHabitDialog(
            habit = habit,
            onDismiss = { showEditDialog = null },
            onConfirm = { updatedHabit ->
                viewModel.updateHabit(updatedHabit)
                showEditDialog = null
            }
        )
    }

    Column(modifier = Modifier.fillMaxSize()) {

        // Encabezado morado/azul
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(Color(0xFF6200EE), Color(0xFF1565C0))
                    )
                )
                .padding(horizontal = 20.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Column {
                Text(
                    text = "Tareas",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = "Seguimiento de actividades diarias",
                    fontSize = 14.sp,
                    color = Color(0xFFE0E0FF)
                )
            }
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            // Progreso
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Progreso del día",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF3700B3)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        val animatedProgress by animateFloatAsState(
                            targetValue = progress, label = ""
                        )
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.size(100.dp)
                        ) {
                            CircularProgressIndicator(
                                progress = { animatedProgress },
                                modifier = Modifier.size(100.dp),
                                strokeWidth = 8.dp,
                                color = Color(0xFF6200EE),
                                trackColor = Color(0xFFE0E0E0)
                            )
                            Text(
                                text = "${(animatedProgress * 100).toInt()}%",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF6200EE)
                            )
                        }
                    }
                }
            }

            // Tareas
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "📋 Mis Tareas",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF3700B3)
                            )
                            IconButton(onClick = { showAddDialog = true }) {
                                Icon(
                                    Icons.Default.Add,
                                    contentDescription = "Añadir tarea",
                                    tint = Color(0xFF6200EE)
                                )
                            }
                        }

                        if (habits.isEmpty()) {
                            Text(
                                text = "No tienes tareas registradas",
                                fontSize = 14.sp,
                                color = Color(0xFF666666),
                                modifier = Modifier.padding(vertical = 8.dp)
                            )
                        } else {
                            habits.forEach { habit ->
                                val isChecked = todaysRecords
                                    .find { it.habitId == habit.id }?.isCompleted ?: false
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp)
                                ) {
                                    Checkbox(
                                        checked = isChecked,
                                        onCheckedChange = {
                                            viewModel.toggleHabitForToday(habit.id)
                                        },
                                        colors = CheckboxDefaults.colors(
                                            checkedColor = Color(0xFF6200EE)
                                        )
                                    )
                                    Text(
                                        text = habit.habitName,
                                        modifier = Modifier.weight(1f),
                                        fontSize = 15.sp,
                                        color = if (isChecked) Color(0xFF999999)
                                        else Color(0xFF333333)
                                    )
                                    IconButton(onClick = { showEditDialog = habit }) {
                                        Icon(
                                            Icons.Default.Edit,
                                            contentDescription = "Editar",
                                            tint = Color(0xFF6200EE),
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                    IconButton(onClick = { viewModel.deleteHabit(habit) }) {
                                        Icon(
                                            Icons.Default.Delete,
                                            contentDescription = "Eliminar",
                                            tint = Color(0xFFCC0000),
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                }
                                if (habits.indexOf(habit) < habits.size - 1) {
                                    Divider(color = Color(0xFFEEEEEE))
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddHabitDialog(onDismiss: () -> Unit, onConfirm: (String) -> Unit) {
    var habitName by remember { mutableStateOf("") }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Nueva Tarea") },
        text = {
            OutlinedTextField(
                value = habitName,
                onValueChange = { habitName = it },
                label = { Text("Nombre de la tarea") },
                singleLine = true
            )
        },
        confirmButton = {
            Button(
                onClick = { onConfirm(habitName) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF6200EE)
                )
            ) { Text("Añadir") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancelar") }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditHabitDialog(habit: Habit, onDismiss: () -> Unit, onConfirm: (Habit) -> Unit) {
    var habitName by remember { mutableStateOf(habit.habitName) }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Editar Tarea") },
        text = {
            OutlinedTextField(
                value = habitName,
                onValueChange = { habitName = it },
                label = { Text("Nombre de la tarea") },
                singleLine = true
            )
        },
        confirmButton = {
            Button(
                onClick = { onConfirm(habit.copy(habitName = habitName)) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF6200EE)
                )
            ) { Text("Guardar") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancelar") }
        }
    )
}