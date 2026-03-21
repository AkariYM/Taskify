package com.example.taskifya.recursos

import kotlinx.coroutines.flow.Flow

class HabitRepository(private val database: HabitDatabase) {

    private val habitDao = database.habitDao()
    private val recordDao = database.habitRecordDao()
    private val linkDao = database.linkDao()
    private val reminderDao = database.reminderDao()

    // Hábitos
    val allHabits = habitDao.getAllActiveHabits()

    suspend fun insertHabit(habit: Habit) = habitDao.insert(habit)

    suspend fun updateHabit(habit: Habit) = habitDao.update(habit)

    suspend fun deleteHabit(habit: Habit) = habitDao.delete(habit)

    // Registros
    fun getRecordsForDate(date: Long): Flow<List<HabitRecord>> = recordDao.getRecordsForDate(date)

    suspend fun toggleHabitCompletion(habitId: Int, date: Long) {
        val record = recordDao.getRecordForHabitAndDate(habitId, date)
        if (record != null) {
            recordDao.update(record.copy(isCompleted = !record.isCompleted))
        } else {
            recordDao.insert(HabitRecord(habitId = habitId, date = date, isCompleted = true))
        }
    }

    fun getHabitProgress(habitId: Int, daysAgo: Int = 30): Flow<List<HabitRecord>> {
        val startDate = System.currentTimeMillis() - (daysAgo * 24 * 60 * 60 * 1000L)
        return recordDao.getHabitProgress(habitId, startDate)
    }

    // Enlaces
    val allLinks = linkDao.getAllLinks()

    suspend fun insertLink(link: Link) = linkDao.insert(link)

    // Recordatorios
    fun getRemindersForHabit(habitId: Int) = reminderDao.getRemindersForHabit(habitId)

    suspend fun insertReminder(reminder: Reminder) = reminderDao.insert(reminder)
}
