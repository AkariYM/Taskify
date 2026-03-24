package com.example.taskifya

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.taskifya.activities.MainActivity as NotasActivity
import com.example.taskifya.database.DatabaseHelper
import com.example.taskifya.eventos.CalendarioMenuActivity
import com.example.taskifya.eventos.EventosRepository
import com.example.taskifya.personalizacion.PersonalizacionActivity
import com.example.taskifya.usuario.LoginActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.text.SimpleDateFormat
import java.util.*

class DashboardActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        // Recibir correo
        val correo = intent.getStringExtra("correo")
            ?: getSharedPreferences("sesion", MODE_PRIVATE).getString("correo", "") ?: ""

        // Guardar sesión
        getSharedPreferences("sesion", MODE_PRIVATE)
            .edit().putString("correo", correo).apply()

        // Obtener usuario
        val db = DatabaseHelper(this)
        val usuario = db.obtenerUsuario(correo)

        // Saludo
        findViewById<TextView>(R.id.tvSaludo).text =
            "Hola, ${usuario?.nombre ?: "Usuario"} "

        // Cargar notas recientes
        cargarNotasRecientes()

        // Cargar eventos de hoy
        cargarEventosHoy()

        // Bottom Navigation
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
                R.id.nav_eventos -> {
                    startActivity(Intent(this, CalendarioMenuActivity::class.java))
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

    private fun cargarNotasRecientes() {
        val contenedor = findViewById<LinearLayout>(R.id.layoutNotasRecientes)
        contenedor.removeAllViews()

        // Obtener las últimas 3 notas de SharedPreferences
        // Por ahora mostramos mensaje si no hay notas
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

    private fun cargarEventosHoy() {
        val contenedor = findViewById<LinearLayout>(R.id.layoutEventosHoy)
        contenedor.removeAllViews()

        val hoy = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        val repo = EventosRepository(this)
        val eventosHoy = repo.porFecha(hoy)

        if (eventosHoy.isEmpty()) {
            val tv = TextView(this)
            tv.text = "No tienes eventos para hoy"
            tv.textSize = 14f
            tv.setTextColor(0xFF666666.toInt())
            contenedor.addView(tv)
        } else {
            eventosHoy.take(3).forEach { evento ->
                val tv = TextView(this)
                tv.text = "• ${evento.titulo} — ${evento.hora}"
                tv.textSize = 15f
                tv.setTextColor(0xFF333333.toInt())
                tv.setPadding(0, 0, 0, 16)
                contenedor.addView(tv)
            }
        }

        // También cargar próximos eventos
        cargarProximosEventos()
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
                tv.text = "• ${evento.titulo} — ${evento.fechaIso}"
                tv.textSize = 15f
                tv.setTextColor(0xFF333333.toInt())
                tv.setPadding(0, 0, 0, 16)
                contenedor.addView(tv)
            }
        }
    }

    fun cerrarSesion() {
        getSharedPreferences("sesion", MODE_PRIVATE).edit().clear().apply()
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }
}