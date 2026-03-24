package com.example.taskifya

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.example.taskifya.activities.MainActivity as NotasActivity
import com.example.taskifya.database.DatabaseHelper
import com.example.taskifya.eventos.CalendarioMenuActivity
import com.example.taskifya.eventos.EventosRepository
import com.example.taskifya.personalizacion.PersonalizacionActivity
import com.example.taskifya.recursos.HabitDatabase
import com.example.taskifya.usuario.LoginActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

class DashboardActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        val correo = intent.getStringExtra("correo")
            ?: getSharedPreferences("sesion", MODE_PRIVATE).getString("correo", "") ?: ""

        getSharedPreferences("sesion", MODE_PRIVATE)
            .edit().putString("correo", correo).apply()

        val db = DatabaseHelper(this)
        val usuario = db.obtenerUsuario(correo)

        findViewById<TextView>(R.id.tvSaludo).text =
            "Hola, ${usuario?.nombre ?: "Usuario"} 👋"

        findViewById<CardView>(R.id.cardCerrarSesion).setOnClickListener {
            cerrarSesion()
        }

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigation)
        bottomNav.selectedItemId = R.id.nav_inicio
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_inicio -> true
                R.id.nav_notas -> {
                    startActivity(Intent(this, NotasActivity::class.java))
                    true
                }
                R.id.nav_calendario -> {
                    startActivity(Intent(this, CalendarioMenuActivity::class.java))
                    true
                }
                R.id.nav_recursos -> {
                    startActivity(Intent(this, RecursosActivity::class.java))
                    true
                }
                R.id.nav_mas -> {
                    startActivity(Intent(this, PersonalizacionActivity::class.java))
                    true
                }
                else -> false
            }
        }
    }

    override fun onResume() {
        super.onResume()
        cargarNotasRecientes()
        cargarProximosEventos()
        cargarTareasPendientes()
    }

    private fun cargarNotasRecientes() {
        val contenedor = findViewById<LinearLayout>(R.id.layoutNotasRecientes)
        contenedor.removeAllViews()
        val prefs = getSharedPreferences("notas_prefs", MODE_PRIVATE)
        val totalNotas = prefs.getInt("total_notas", 0)
        if (totalNotas == 0) {
            val tv = TextView(this)
            tv.text = "No tienes notas recientes"
            tv.textSize = 14f
            tv.setTextColor(0xFF666666.toInt())
            contenedor.addView(tv)
        } else {
            for (i in 0 until minOf(totalNotas, 3)) {
                val titulo = prefs.getString("nota_titulo_$i", "") ?: ""
                val tv = TextView(this)
                tv.text = "• $titulo"
                tv.textSize = 15f
                tv.setTextColor(0xFF333333.toInt())
                tv.setPadding(0, 0, 0, 16)
                contenedor.addView(tv)
            }
        }
    }

    private fun cargarProximosEventos() {
        val contenedor = findViewById<LinearLayout>(R.id.layoutProximosEventos)
        contenedor.removeAllViews()
        val repo = EventosRepository(this)
        val todos = repo.todos()
        val hoy = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        val proximos = todos.filter { it.fechaIso >= hoy }.take(3)
        if (proximos.isEmpty()) {
            val tv = TextView(this)
            tv.text = "No tienes próximos eventos"
            tv.textSize = 14f
            tv.setTextColor(0xFF666666.toInt())
            contenedor.addView(tv)
        } else {
            proximos.forEach { evento ->
                val tv = TextView(this)
                tv.text = "• ${evento.titulo} — ${evento.fechaIso} ${evento.hora}"
                tv.textSize = 15f
                tv.setTextColor(0xFF333333.toInt())
                tv.setPadding(0, 0, 0, 16)
                contenedor.addView(tv)
            }
        }
    }

    private fun cargarTareasPendientes() {
        val tvTareas = findViewById<TextView>(R.id.tvTareasPendientes)
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val habitDb = HabitDatabase.getDatabase(applicationContext)
                val calendar = Calendar.getInstance()
                calendar.set(Calendar.HOUR_OF_DAY, 0)
                calendar.set(Calendar.MINUTE, 0)
                calendar.set(Calendar.SECOND, 0)
                calendar.set(Calendar.MILLISECOND, 0)
                val hoy = calendar.timeInMillis
                val todasTareas = habitDb.habitDao().getAllHabitsSync()
                val registrosHoy = habitDb.habitRecordDao().getRecordsForDateSync(hoy)
                val completadas = registrosHoy.count { it.isCompleted }
                val pendientes = todasTareas.size - completadas
                withContext(Dispatchers.Main) {
                    tvTareas.text = if (pendientes > 0)
                        "$pendientes tarea(s) pendiente(s)"
                    else
                        "¡Todo al día! ✅"
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    tvTareas.text = "Sin tareas registradas"
                }
            }
        }
    }

    fun cerrarSesion() {
        getSharedPreferences("sesion", MODE_PRIVATE).edit().clear().apply()
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }
}