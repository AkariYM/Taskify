package com.example.taskifya.recursos

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.taskifya.recursos.Habit
import com.example.taskifya.recursos.HabitRecord

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HabitScreen(viewModel: HabitViewModel) {
    var showAddDialog by remember { mutableStateOf(false) }
    var showEditDialog by remember { mutableStateOf<Habit?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Seguimiento de actividades") })
        }
    ) { innerPadding ->
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

        LazyColumn(modifier = Modifier.padding(innerPadding)) {
            item {
                HabitsSection(
                    habits = habits,
                    todaysRecords = todaysRecords,
                    onHabitCheckedChanged = { habit ->
                        viewModel.toggleHabitForToday(habit.id)
                    },
                    onAddHabitClicked = { showAddDialog = true },
                    onEditHabitClicked = { habit -> showEditDialog = habit },
                    onDeleteHabitClicked = { habit -> viewModel.deleteHabit(habit) }
                )
            }
            item {
                ProgressSection(progress = progress)
            }
        }
    }
}

@Composable
fun HabitsSection(
    habits: List<Habit>,
    todaysRecords: List<HabitRecord>,
    onHabitCheckedChanged: (Habit) -> Unit,
    onAddHabitClicked: () -> Unit,
    onEditHabitClicked: (Habit) -> Unit,
    onDeleteHabitClicked: (Habit) -> Unit
) {
    Card(modifier = Modifier.padding(16.dp)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Tareas Pendientes", style = MaterialTheme.typography.titleMedium)
            habits.forEach { habit ->
                val isChecked = todaysRecords.find { it.habitId == habit.id }?.isCompleted ?: false
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Checkbox(
                        checked = isChecked,
                        onCheckedChange = { onHabitCheckedChanged(habit) }
                    )
                    Text(text = habit.habitName, modifier = Modifier.weight(1f))
                    IconButton(onClick = { onEditHabitClicked(habit) }) {
                        Icon(Icons.Default.Edit, contentDescription = "Editar hábito")
                    }
                    IconButton(onClick = { onDeleteHabitClicked(habit) }) {
                        Icon(Icons.Default.Delete, contentDescription = "Eliminar hábito")
                    }
                }
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End,
                modifier = Modifier.fillMaxWidth()
            ) {
                IconButton(onClick = onAddHabitClicked) {
                    Icon(Icons.Default.Add, contentDescription = "Añadir Tarea")
                }
            }
        }
    }
}

@Composable
fun ProgressSection(progress: Float) {
    Card(modifier = Modifier.padding(16.dp)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Progreso", style = MaterialTheme.typography.titleMedium)
            Box(contentAlignment = Alignment.Center, modifier = Modifier.size(120.dp).align(Alignment.CenterHorizontally)) {
                val animatedProgress by animateFloatAsState(targetValue = progress, label = "")
                CircularProgressIndicator(progress = animatedProgress, modifier = Modifier.size(120.dp), strokeWidth = 8.dp)
                Text(text = "${(animatedProgress * 100).toInt()}%", style = MaterialTheme.typography.headlineSmall)
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
        title = { Text("Añadir Tarea") },
        text = {
            OutlinedTextField(
                value = habitName,
                onValueChange = { habitName = it },
                label = { Text("Añadir Tarea") },
                singleLine = true
            )
        },
        confirmButton = {
            Button(onClick = { onConfirm(habitName) }) {
                Text("Añadir")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
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
                label = { Text("Nombre de la Tarea") },
                singleLine = true
            )
        },
        confirmButton = {
            Button(onClick = { onConfirm(habit.copy(habitName = habitName)) }) {
                Text("Guardar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}
