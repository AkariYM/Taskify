package com.example.taskifya.personalizacion

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.taskifya.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

class PersonalizacionCopiaDeSeguridadActivity : AppCompatActivity() {

    private lateinit var progressBar: ProgressBar
    private lateinit var statusText: TextView
    private lateinit var loadingIcon: ImageView
    private lateinit var backupButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {


        aplicarTema()

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_personalizacion_copia_de_seguridad)


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        progressBar = findViewById(R.id.progressBar)
        statusText = findViewById(R.id.statusText)
        loadingIcon = findViewById(R.id.loadingIcon)
        backupButton = findViewById(R.id.backupButton)

        backupButton.setOnClickListener {
            startBackup()
        }
    }



    private fun aplicarTema() {
        val prefs = getSharedPreferences("temas", MODE_PRIVATE)
        val rosaActivo = prefs.getBoolean("temaRosa", false)

        if (rosaActivo) {
            setTheme(R.style.Theme_TaskifyA_Rose)
        } else {
            setTheme(R.style.Theme_TaskifyA)
        }
    }


    @SuppressLint("SetTextI18n")
    private fun startBackup() {
        progressBar.visibility = View.VISIBLE
        loadingIcon.visibility = View.VISIBLE
        statusText.text = "Iniciando copia..."
        backupButton.isEnabled = false

        CoroutineScope(Dispatchers.IO).launch {
            val result = performBackup()
            withContext(Dispatchers.Main) {
                progressBar.visibility = View.GONE
                loadingIcon.visibility = View.GONE
                backupButton.isEnabled = true
                statusText.text = result
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private suspend fun performBackup(): String {
        return try {
            val sourceDir = filesDir
            val backupDir = File(getExternalFilesDir(null), "Backup")
            if (!backupDir.exists()) backupDir.mkdirs()

            val files = sourceDir.listFiles() ?: return "No hay archivos para copiar"
            val totalFiles = files.size
            var copied = 0

            for (file in files) {
                val destFile = File(backupDir, file.name)
                FileInputStream(file).use { input ->
                    FileOutputStream(destFile).use { output ->
                        input.copyTo(output)
                    }
                }
                copied++
                val progress = (copied * 100) / totalFiles
                withContext(Dispatchers.Main) {
                    progressBar.progress = progress
                    statusText.text = "Copiando ${file.name} ($progress%)"
                }
                delay(200)
            }
            "Copia de seguridad completada: ${backupDir.absolutePath}"
        } catch (e: Exception) {
            e.printStackTrace()
            "Error: ${e.message}"
        }
    }
}
