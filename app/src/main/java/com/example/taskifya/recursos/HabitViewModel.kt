package com.example.taskifya.recursos

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.taskifya.recursos.Habit
import com.example.taskifya.recursos.HabitDatabase
import com.example.taskifya.recursos.HabitRecord
import com.example.taskifya.recursos.HabitRepository
import kotlinx.coroutines.launch
import java.util.Calendar

class HabitViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: HabitRepository
    val allHabits: LiveData<List<Habit>>
    val todaysHabitRecords: LiveData<List<HabitRecord>>
    val dailyProgress = MediatorLiveData<Float>()

    init {
        val database = HabitDatabase.getDatabase(application)
        repository = HabitRepository(database)
        allHabits = repository.allHabits.asLiveData()
        todaysHabitRecords = repository.getRecordsForDate(getTodayDateInMillis()).asLiveData()

        dailyProgress.addSource(allHabits) { recalculateProgress() }
        dailyProgress.addSource(todaysHabitRecords) { recalculateProgress() }
    }

    private fun recalculateProgress() {
        val completedCount = todaysHabitRecords.value?.count { it.isCompleted } ?: 0
        val totalHabits = allHabits.value?.size ?: 0
        dailyProgress.value = if (totalHabits > 0) {
            completedCount.toFloat() / totalHabits.toFloat()
        } else {
            0f
        }
    }

    fun insertHabit(habitName: String, category: String) = viewModelScope.launch {
        val habit = Habit(habitName = habitName, category = category)
        repository.insertHabit(habit)
    }

    fun updateHabit(habit: Habit) = viewModelScope.launch {
        repository.updateHabit(habit)
    }

    fun deleteHabit(habit: Habit) = viewModelScope.launch {
        repository.deleteHabit(habit)
    }

    fun toggleHabitForToday(habitId: Int) = viewModelScope.launch {
        val today = getTodayDateInMillis()
        repository.toggleHabitCompletion(habitId, today)
    }

    fun getProgressForHabit(habitId: Int): LiveData<List<HabitRecord>> {
        return repository.getHabitProgress(habitId).asLiveData()
    }

    private fun getTodayDateInMillis(): Long {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        return calendar.timeInMillis
    }
}
