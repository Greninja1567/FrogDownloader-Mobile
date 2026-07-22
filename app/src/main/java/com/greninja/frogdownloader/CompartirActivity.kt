package com.greninja.frogdownloader

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat

class CompartirActivity : ComponentActivity() {

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (!isGranted) {
            Toast.makeText(this, "Se necesita permiso de notificaciones para los controles multimedia", Toast.LENGTH_LONG).show()
        }
    }
    
    private fun checkNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkNotificationPermission()
        
        val intentRecibido = intent
        if (intentRecibido?.action == Intent.ACTION_SEND && intentRecibido.type == "text/plain") {
            val sharedText = intentRecibido.getStringExtra(Intent.EXTRA_TEXT) ?: ""
            val url = sharedText.split(" ").firstOrNull { it.startsWith("http") } ?: sharedText
            
            if (url.isNotEmpty()) {
                mostrarDialogoDecisión(url)
            } else {
                finish()
            }
        } else {
            finish()
        }
    }

    private fun mostrarDialogoDecisión(url: String) {
        setContent {
            MaterialTheme {
                Dialog(
                    onDismissRequest = { finish() },
                    properties = DialogProperties(usePlatformDefaultWidth = false)
                ) {
                    Surface(
                        modifier = Modifier
                            .padding(24.dp)
                            .fillMaxWidth(),
                        shape = RoundedCornerShape(28.dp),
                        color = MaterialTheme.colorScheme.surface,
                        tonalElevation = 6.dp
                    ) {
                        Column(
                            modifier = Modifier.padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                "FrogDownloader",
                                style = MaterialTheme.typography.headlineSmall,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                "¿Qué deseas hacer con este video?",
                                style = MaterialTheme.typography.bodyLarge,
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(24.dp))

                            val estaReproduciendo = MainApp.exoPlayerGlobal?.isPlaying ?: false

                            if (estaReproduciendo) {
                                Button(
                                    onClick = {
                                        GestorReproduccion.procesarCarga(url, true, this@CompartirActivity)
                                        Toast.makeText(this@CompartirActivity, "Reproduciendo ahora...", Toast.LENGTH_SHORT).show()
                                        finish()
                                    },
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text("Reproducir Ahora")
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                                OutlinedButton(
                                    onClick = {
                                        GestorReproduccion.procesarCarga(url, false, this@CompartirActivity)
                                        Toast.makeText(this@CompartirActivity, "Agregado a la fila", Toast.LENGTH_SHORT).show()
                                        finish()
                                    },
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text("Agregar a la Fila")
                                }
                            } else {
                                Button(
                                    onClick = {
                                        GestorReproduccion.procesarCarga(url, true, this@CompartirActivity)
                                        Toast.makeText(this@CompartirActivity, "Reproduciendo en segundo plano...", Toast.LENGTH_SHORT).show()
                                        finish()
                                    },
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text("Reproducir en Segundo Plano")
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                                OutlinedButton(
                                    onClick = {
                                        val mainIntent = Intent(this@CompartirActivity, MainActivity::class.java).apply {
                                            action = Intent.ACTION_SEND
                                            type = "text/plain"
                                            putExtra(Intent.EXTRA_TEXT, url)
                                            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
                                        }
                                        startActivity(mainIntent)
                                        finish()
                                    },
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text("Ver en FrogDownloader")
                                }
                            }
                            
                            TextButton(
                                onClick = { finish() },
                                modifier = Modifier.align(Alignment.End)
                            ) {
                                Text("Cancelar")
                            }
                        }
                    }
                }
            }
        }
    }
}
