package com.example.taskifya.recursos

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [Habit::class, HabitRecord::class, Link::class, Reminder::class],
    version = 1,
    exportSchema = false
)
abstract class HabitDatabase : RoomDatabase() {

    abstract fun habitDao(): HabitDao
    abstract fun habitRecordDao(): HabitRecordDao
    abstract fun linkDao(): LinkDao
    abstract fun reminderDao(): ReminderDao

    companion object {
        private val instances = mutableMapOf<String, HabitDatabase>()

        fun getDatabase(context: Context, correoUsuario: String = "default"): HabitDatabase {
            val key = correoUsuario.replace("@", "_").replace(".", "_")
            return instances[key] ?: synchronized(this) {
                instances[key] ?: Room.databaseBuilder(
                    context.applicationContext,
                    HabitDatabase::class.java,
                    "habit_database_$key"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { instances[key] = it }
            }
        }
    }
}