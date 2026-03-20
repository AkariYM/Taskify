package com.example.taskifya.personalizacion

import android.Manifest
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Switch
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.core.content.edit
import com.example.taskifya.MainActivity
import com.example.taskifya.R


class PersonalizacionNotificacionesActivity : AppCompatActivity() {

    companion object {
        private const val PREFS_NAME = "prefs_notificaciones"
        private const val KEY_ACTIVADAS = "notificaciones_activadas"
        private const val KEY_INTERVALO = "intervalo_recordatorio"

        private const val CANAL_ID = "canal_taskify_high"  // 🔥 canal nuevo (IMPORTANTE)
        private const val NOTIF_ID = 101
        private const val REQUEST_NOTIF_PERMISSION = 100
        private const val ALARM_REQUEST_CODE = 200
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_personalizacion_notificaciones)

        pedirPermisoNotificaciones()
        crearCanalNotificaciones()

        val switchNotificaciones =
            findViewById<com.google.android.material.materialswitch.MaterialSwitch>(R.id.switchNotificaciones)

        val prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
        val estadoGuardado = prefs.getBoolean(KEY_ACTIVADAS, false)
        switchNotificaciones.isChecked = estadoGuardado

        // Si ya estaba activado antes, reprograma alarma
        val intervaloGuardado = prefs.getLong(KEY_INTERVALO, 0L)
        if (estadoGuardado && intervaloGuardado > 0L) {
            programarRecordatorio(intervaloGuardado)
        }

        switchNotificaciones.setOnCheckedChangeListener { _, isChecked ->
            prefs.edit { putBoolean(KEY_ACTIVADAS, isChecked) }

            if (isChecked) {
                mostrarDialogoFrecuencia()
            } else {
                cancelarRecordatorios()
            }
        }
    }

    private fun pedirPermisoNotificaciones() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val permiso = Manifest.permission.POST_NOTIFICATIONS
            if (ContextCompat.checkSelfPermission(this, permiso)
                != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(permiso),
                    REQUEST_NOTIF_PERMISSION
                )
            }
        }
    }

    private fun crearCanalNotificaciones() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val canal = NotificationChannel(
                CANAL_ID,
                "Notificaciones Taskify Emergentes",
                NotificationManager.IMPORTANCE_HIGH   // 🔥 PARA HEADS UP
            )
            canal.description = "Canal para recordatorios y pendientes"

            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(canal)
        }
    }

    private fun mostrarDialogoFrecuencia() {
        val opciones = arrayOf(
            "Cada hora",
            "Cada 2 horas",
            "Cada 4 horas",
            "Cada 6 horas"
        )

        AlertDialog.Builder(this)
            .setTitle("¿Cada cuánto quieres recordatorios?")
            .setItems(opciones) { _, which ->
                val intervalo = when (which) {
                    0 -> 1L * 60L * 60L * 1000L
                    1 -> 2L * 60L * 60L * 1000L
                    2 -> 4L * 60L * 60L * 1000L
                    else -> 6L * 60L * 60L * 1000L
                }

                val prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
                prefs.edit { putLong(KEY_INTERVALO, intervalo) }

                programarRecordatorio(intervalo)
                mostrarNotificacionInmediata()
            }
            .setNegativeButton("Cancelar") { dialog, _ ->
                dialog.dismiss()
                cancelarRecordatorios()
                val switch = findViewById<Switch>(R.id.switchNotificaciones)
                switch.isChecked = false
            }
            .show()
    }

    private fun programarRecordatorio(intervalo: Long) {
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val intent = Intent(this, RecordatorioReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            this,
            ALARM_REQUEST_CODE,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            System.currentTimeMillis() + intervalo,
            intervalo,
            pendingIntent
        )
    }

    private fun cancelarRecordatorios() {
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, RecordatorioReceiver::class.java)

        val pendingIntent = PendingIntent.getBroadcast(
            this,
            ALARM_REQUEST_CODE,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        alarmManager.cancel(pendingIntent)
    }

    private fun mostrarNotificacionInmediata() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
            != PackageManager.PERMISSION_GRANTED
        ) return

        val intent = Intent(this, MainActivity::class.java)
        val pIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val builder = NotificationCompat.Builder(this, CANAL_ID)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle("Taskify")
            .setContentText("Vuelve a Taskify a revisar tus pendientes")
            .setPriority(NotificationCompat.PRIORITY_HIGH)   // 🔥 HEADS UP
            .setCategory(NotificationCompat.CATEGORY_REMINDER)
            .setFullScreenIntent(pIntent, true)
            .setAutoCancel(true)

        NotificationManagerCompat.from(this).notify(NOTIF_ID, builder.build())
    }
}
