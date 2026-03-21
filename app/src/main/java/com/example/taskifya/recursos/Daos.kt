package com.example.taskifya.recursos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface HabitDao {
    @Insert
    suspend fun insert(habit: Habit): Long

    @Update
    suspend fun update(habit: Habit)

    @Delete
    suspend fun delete(habit: Habit)

    @Query("SELECT * FROM habits WHERE is_active = 1 ORDER BY created_date DESC")
    fun getAllActiveHabits(): Flow<List<Habit>>

    @Query("SELECT * FROM habits WHERE id = :habitId")
    suspend fun getHabitById(habitId: Int): Habit?

    @Query("SELECT * FROM habits WHERE category = :category AND is_active = 1")
    fun getHabitsByCategory(category: String): Flow<List<Habit>>
}

@Dao
interface HabitRecordDao {
    @Insert
    suspend fun insert(record: HabitRecord)

    @Update
    suspend fun update(record: HabitRecord)

    @Query("SELECT * FROM habit_records WHERE date = :date")
    fun getRecordsForDate(date: Long): Flow<List<HabitRecord>>

    @Query("SELECT * FROM habit_records WHERE habit_id = :habitId AND date = :date LIMIT 1")
    suspend fun getRecordForHabitAndDate(habitId: Int, date: Long): HabitRecord?

    @Query("""
        SELECT * FROM habit_records
        WHERE habit_id = :habitId
        AND date >= :startDate
        ORDER BY date DESC
    """)
    fun getHabitProgress(habitId: Int, startDate: Long): Flow<List<HabitRecord>>

    @Query("""
        SELECT COUNT(*) FROM habit_records
        WHERE habit_id = :habitId
        AND is_completed = 1
        AND date >= :startDate
    """)
    suspend fun getCompletedCount(habitId: Int, startDate: Long): Int

    @Query("""
        SELECT hr.*, h.habit_name, h.category
        FROM habit_records hr
        INNER JOIN habits h ON hr.habit_id = h.id
        WHERE hr.date = :date
    """)
    fun getRecordsWithHabitInfo(date: Long): Flow<List<HabitWithRecord>>
}

@Dao
interface LinkDao {
    @Insert
    suspend fun insert(link: Link)

    @Update
    suspend fun update(link: Link)

    @Delete
    suspend fun delete(link: Link)

    @Query("SELECT * FROM links ORDER BY created_date DESC")
    fun getAllLinks(): Flow<List<Link>>
}

@Dao
interface ReminderDao {
    @Insert
    suspend fun insert(reminder: Reminder)

    @Update
    suspend fun update(reminder: Reminder)

    @Delete
    suspend fun delete(reminder: Reminder)

    @Query("SELECT * FROM reminders WHERE habit_id = :habitId")
    fun getRemindersForHabit(habitId: Int): Flow<List<Reminder>>

    @Query("SELECT * FROM reminders WHERE is_enabled = 1")
    fun getAllActiveReminders(): Flow<List<Reminder>>
}
