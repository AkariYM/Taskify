package com.example.taskifya.recursos

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

// Entidad para Hábitos
@Entity(tableName = "habits")
data class Habit(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo(name = "habit_name")
    val habitName: String,

    @ColumnInfo(name = "created_date")
    val createdDate: Long = System.currentTimeMillis(),

    @ColumnInfo(name = "is_active")
    val isActive: Boolean = true,

    @ColumnInfo(name = "icon")
    val icon: String? = null,

    @ColumnInfo(name = "category")
    val category: String = "General"
)

// Entidad para Registro Diario de Hábitos
@Entity(
    tableName = "habit_records",
    foreignKeys = [
        ForeignKey(
            entity = Habit::class,
            parentColumns = ["id"],
            childColumns = ["habit_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("habit_id")]
)
data class HabitRecord(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo(name = "habit_id")
    val habitId: Int,

    @ColumnInfo(name = "date")
    val date: Long,

    @ColumnInfo(name = "is_completed")
    val isCompleted: Boolean = false,

    @ColumnInfo(name = "notes")
    val notes: String? = null
)

// Entidad para Enlaces y Recursos
@Entity(tableName = "links")
data class Link(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo(name = "title")
    val title: String,

    @ColumnInfo(name = "url")
    val url: String,

    @ColumnInfo(name = "description")
    val description: String? = null,

    @ColumnInfo(name = "created_date")
    val createdDate: Long = System.currentTimeMillis()
)

// Entidad para Recordatorios
@Entity(
    tableName = "reminders",
    foreignKeys = [
        ForeignKey(
            entity = Habit::class,
            parentColumns = ["id"],
            childColumns = ["habit_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Reminder(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo(name = "habit_id")
    val habitId: Int,

    @ColumnInfo(name = "time")
    val time: String,

    @ColumnInfo(name = "days")
    val days: String,

    @ColumnInfo(name = "is_enabled")
    val isEnabled: Boolean = true
)

// Clase de datos para consultas complejas
data class HabitWithRecord(
    @ColumnInfo(name = "id") val id: Int,
    @ColumnInfo(name = "habit_id") val habitId: Int,
    @ColumnInfo(name = "date") val date: Long,
    @ColumnInfo(name = "is_completed") val isCompleted: Boolean,
    @ColumnInfo(name = "habit_name") val habitName: String,
    @ColumnInfo(name = "category") val category: String
)
