package com.example.taskifya.eventos

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.taskifya.R
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.Executors

class CalendarioActivity : AppCompatActivity() {

    private lateinit var repo: EventosRepository
    private val executor = Executors.newSingleThreadExecutor()
    private val fmt = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendario)

        repo = EventosRepository(this)

        val calendarView = findViewById<CalendarView>(R.id.calendarView)
        val listView = findViewById<ListView>(R.id.listEventos)
        val btnCrear = findViewById<Button>(R.id.btnCrearEvent)

        fun loadFor(dateIso: String) {
            executor.execute {
                val items = repo.porFecha(dateIso)
                runOnUiThread {
                    listView.adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1,
                        items.map { "${it.hora} - ${it.titulo}" })
                }
            }
        }

        val today = fmt.format(Date())
        loadFor(today)

        calendarView.setOnDateChangeListener { _, y, m, d ->
            val iso = String.format("%04d-%02d-%02d", y, m + 1, d)
            loadFor(iso)
        }

        btnCrear.setOnClickListener {
            // abrir CrearEventoActivity
            startActivity(android.content.Intent(this, CrearEventoActivity::class.java))
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        executor.shutdown()
    }
}
