package com.greninja.frogdownloader

import android.Manifest
import android.content.ComponentName
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.ActivityResultLauncher
import androidx.annotation.OptIn as AndroidOptIn
import kotlin.OptIn
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import androidx.media3.common.util.UnstableApi
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.google.common.util.concurrent.MoreExecutors

class MainActivity : ComponentActivity() {

    private var controladorMultimedia: MediaController? = null

    companion object {
        var launcherCarpetaAudio: ActivityResultLauncher<android.net.Uri?>? = null
        var launcherCarpetaVideo: ActivityResultLauncher<android.net.Uri?>? = null
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { _ ->
        // Aquí podrías manejar la respuesta si quisieras avisar al usuario
    }

    private fun checkAndRequestPermissions() {
        val permissions = mutableListOf<String>()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissions.add(Manifest.permission.READ_MEDIA_AUDIO)
            permissions.add(Manifest.permission.READ_MEDIA_VIDEO)
            permissions.add(Manifest.permission.POST_NOTIFICATIONS)
        } else {
            permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE)
            permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }

        val toRequest = permissions.filter {
            ContextCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED
        }

        if (toRequest.isNotEmpty()) {
            requestPermissionLauncher.launch(toRequest.toTypedArray())
        }
    }

    override fun onStart() {
        super.onStart()
        checkAndRequestPermissions()
        // Creamos un token de enlace seguro hacia el PlaybackService que creamos antes
        val tokenServicio = SessionToken(this, ComponentName(this, PlaybackService::class.java))
        val constructorControlador = MediaController.Builder(this, tokenServicio).buildAsync()

        // Android enciende el puente en background y dibuja la notificación de forma nativa e invisible
        constructorControlador.addListener({
            if (constructorControlador.isDone) {
                controladorMultimedia = constructorControlador.get()
            }
        }, MoreExecutors.directExecutor())
    }

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    @AndroidOptIn(UnstableApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        launcherCarpetaAudio = registerForActivityResult(ActivityResultContracts.OpenDocumentTree()) { uri ->
            uri?.let {
                val ruta = obtenerRutaRelativa(it)
                rutaGuardadoAudioGlobal = ruta
                PreferenciasApp(this).guardarString(PreferenciasApp.KEY_RUTA_AUDIO, ruta)
            }
        }

        launcherCarpetaVideo = registerForActivityResult(ActivityResultContracts.OpenDocumentTree()) { uri ->
            uri?.let {
                val ruta = obtenerRutaRelativa(it)
                rutaGuardadoVideoGlobal = ruta
                PreferenciasApp(this).guardarString(PreferenciasApp.KEY_RUTA_VIDEO, ruta)
            }
        }

        // --- MANEJAR EL LINK COMPARTIDO DESDE YOUTUBE ---
        handleIntent(intent)

        setContent {
            val windowSizeClass = calculateWindowSizeClass(this)
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    InterfazPrincipal(windowSizeClass)
                }
            }
        }
    }

    private fun handleIntent(intent: Intent?) {
        if (intent?.action == Intent.ACTION_SEND && intent.type == "text/plain") {
            intent.getStringExtra(Intent.EXTRA_TEXT)?.let { sharedText ->
                // Extraer la URL del texto (YouTube a veces envía texto + link)
                val url = sharedText.split(" ").firstOrNull { it.startsWith("http") } ?: sharedText
                urlCompartidaDesdeYoutubeGlobal = url
            }
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        handleIntent(intent)
    }

    override fun onStop() {
        // Liberamos la comunicación al cerrar la ventana para no consumir batería
        controladorMultimedia?.release()
        super.onStop()
    }

    private fun obtenerRutaRelativa(uri: android.net.Uri): String {
        val path = uri.path ?: return ""
        // Intentar extraer la ruta después de "primary:" o "msf:"
        return when {
            path.contains("primary:") -> path.substringAfter("primary:")
            path.contains("msf:") -> path.substringAfter("msf:")
            else -> path
        }
    }
}
