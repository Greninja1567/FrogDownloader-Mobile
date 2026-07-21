package com.greninja.frogdownloader

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.widget.Toast
import androidx.core.content.FileProvider
import org.json.JSONObject
import java.io.File
import java.net.HttpURLConnection
import java.net.URL
import kotlin.concurrent.thread

object ActualizadorApp {

    private const val GITHUB_API_URL = "https://api.github.com/repos/Greninja1567/FrogDownloader-Mobile/releases/latest"
    private const val VERSION_ACTUAL = "1.0.6"

    data class ReleaseInfo(val version: String, val downloadUrl: String, val body: String)

    fun buscarActualizacion(contexto: Context, alFinalizar: (ReleaseInfo?) -> Unit) {
        thread {
            try {
                val url = URL(GITHUB_API_URL)
                val conexion = url.openConnection() as HttpURLConnection
                conexion.requestMethod = "GET"
                conexion.connect()

                if (conexion.responseCode == 200) {
                    val reader = conexion.inputStream.bufferedReader()
                    val response = reader.readText()
                    val json = JSONObject(response)
                    val tagName = json.getString("tag_name").replace("v", "")
                    val body = json.optString("body", "")
                    
                    val assets = json.getJSONArray("assets")
                    var downloadUrl = ""
                    for (i in 0 until assets.length()) {
                        val asset = assets.getJSONObject(i)
                        if (asset.getString("name").endsWith(".apk")) {
                            downloadUrl = asset.getString("browser_download_url")
                            break
                        }
                    }

                    if (downloadUrl.isNotEmpty() && esVersionNueva(tagName, VERSION_ACTUAL)) {
                        alFinalizar(ReleaseInfo(tagName, downloadUrl, body))
                    } else {
                        alFinalizar(null)
                    }
                } else {
                    alFinalizar(null)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                alFinalizar(null)
            }
        }
    }

    private fun esVersionNueva(remota: String, local: String): Boolean {
        val partesRemota = remota.split(".").mapNotNull { it.toIntOrNull() }
        val partesLocal = local.split(".").mapNotNull { it.toIntOrNull() }
        
        val maxLen = maxOf(partesRemota.size, partesLocal.size)
        for (i in 0 until maxLen) {
            val vRemota = partesRemota.getOrElse(i) { 0 }
            val vLocal = partesLocal.getOrElse(i) { 0 }
            if (vRemota > vLocal) return true
            if (vRemota < vLocal) return false
        }
        return false
    }

    fun descargarEInstalar(contexto: Context, downloadUrl: String) {
        val nombreArchivo = "FrogDownloader_Update.apk"
        val rutaDestino = File(contexto.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), nombreArchivo)
        
        if (rutaDestino.exists()) rutaDestino.delete()

        val request = DownloadManager.Request(Uri.parse(downloadUrl))
            .setTitle("Actualizando FrogDownloader")
            .setDescription("Descargando nueva versión...")
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            .setDestinationUri(Uri.fromFile(rutaDestino))

        val manager = contexto.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        val downloadId = manager.enqueue(request)

        val receiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                val id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
                if (id == downloadId) {
                    instalarApk(contexto, rutaDestino)
                    try {
                        contexto.unregisterReceiver(this)
                    } catch (e: Exception) { e.printStackTrace() }
                }
            }
        }
        
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            contexto.registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE), Context.RECEIVER_EXPORTED)
        } else {
            contexto.registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
        }
    }

    private fun instalarApk(contexto: Context, archivo: File) {
        val uri = FileProvider.getUriForFile(contexto, "${contexto.packageName}.fileprovider", archivo)
        val intent = Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(uri, "application/vnd.android.package-archive")
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (!contexto.packageManager.canRequestPackageInstalls()) {
                val settingsIntent = Intent(android.provider.Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES).apply {
                    data = Uri.parse("package:${contexto.packageName}")
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }
                contexto.startActivity(settingsIntent)
                Toast.makeText(contexto, "Por favor, autoriza la instalación de FrogDownloader", Toast.LENGTH_LONG).show()
                return
            }
        }
        
        try {
            contexto.startActivity(intent)
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(contexto, "Error al abrir el instalador", Toast.LENGTH_SHORT).show()
        }
    }
}
