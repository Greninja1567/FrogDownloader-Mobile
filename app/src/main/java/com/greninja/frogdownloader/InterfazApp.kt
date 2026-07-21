@file:OptIn(ExperimentalMaterial3Api::class, UnstableApi::class)
package com.greninja.frogdownloader

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import android.view.View
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.VideoLibrary
import androidx.compose.material.icons.filled.FolderOpen
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.NavigationRailItemDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.exoplayer.source.DefaultMediaSourceFactory
import androidx.media3.ui.PlayerView
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.yausername.youtubedl_android.YoutubeDL
import com.yausername.youtubedl_android.YoutubeDLRequest
import org.json.JSONObject
import android.content.ContentUris
import android.os.Build
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import java.io.File
import kotlin.concurrent.thread

// --- DEFINICIÓN DE COLORES PERSONALIZADOS ---
val ColorLimon = Color(0xFFCDDC39)       // Verde Limón brillante para botones
val ColorGrisOscuro = Color(0xFF212121)  // Gris Oscuro para las letras y alta visibilidad
val ColorVerdeBase = Color(0xFF4CAF50)   // Verde suave para acentos de la app
val ColorVerdeFondo = Color(0xFFE8F5E9)  // Fondo verde muy claro para tarjetas

// Colores Tema Oscuro (Verde Pastel, Negro, Gris)
val ColorVerdePastel = Color(0xFFACE1AF)
val ColorNegroFondo = Color(0xFF000000)
val ColorGrisSuperficie = Color(0xFF1E1E1E)
val ColorGrisTexto = Color(0xFFB0B0B0)

enum class ModoTema { CLARO, OSCURO, AUTOMATICO }
var temaAppGlobal by mutableStateOf(ModoTema.AUTOMATICO)

// --- VARIABLE GLOBAL PARA DETENER DESCARGAS MASIVAS ---
var detenerDescargaMasivaGlobal by mutableStateOf(false)
// --- NUEVAS VARIABLES GLOBALES PERSISTENTES ---
var tituloVideoActualGlobal by mutableStateOf("Ningún video en reproducción")
var urlParaDescargarIndividualGlobal by mutableStateOf("")
// --- AJUSTES GLOBALES DE REPRODUCCIÓN ---
var resolucionReproduccionGlobal by mutableStateOf("480p")

// --- AJUSTES GLOBALES DE AUDIO ---
var formatoAudioGlobal by mutableStateOf("mp3")
var calidadAudioGlobal by mutableStateOf("320k") // Opciones: 128k, 192k, 320k
var rutaGuardadoAudioGlobal by mutableStateOf("MisDescargas/Musica")

// --- AJUSTES GLOBALES DE VIDEO ---
var formatoVideoGlobal by mutableStateOf("mp4")
var calidadVideoGlobal by mutableStateOf("720p") // Opciones: 360p, 480p, 720p, 1080p
var rutaGuardadoVideoGlobal by mutableStateOf("MisDescargas/Videos")
var androidAutoActivadoGlobal by mutableStateOf(true)

var mostrarBottomSheetGlobal by mutableStateOf(false)
var esPlaylistDetectadaGlobal by mutableStateOf(false)
var urlParaDescargarGlobal by mutableStateOf("")
var cargandoVideoGlobal by mutableStateOf(false)

val EsquemaColoresVerde = lightColorScheme(
    primary = ColorVerdeBase,
    secondary = ColorLimon,
    background = Color.Transparent,
    surface = ColorVerdeFondo.copy(alpha = 0.85f),
    onPrimary = Color.White,
    onSecondary = ColorGrisOscuro,
    onBackground = ColorGrisOscuro,
    onSurface = ColorGrisOscuro,
    primaryContainer = ColorVerdeBase.copy(alpha = 0.2f),
    secondaryContainer = ColorLimon
)

val EsquemaColoresOscuro = darkColorScheme(
    primary = ColorVerdePastel,
    secondary = ColorVerdePastel.copy(alpha = 0.7f),
    background = Color.Transparent,
    surface = ColorNegroFondo.copy(alpha = 0.85f),
    onPrimary = Color.Black,
    onSecondary = Color.Black,
    onBackground = Color.White,
    onSurface = Color.White,
    primaryContainer = ColorVerdePastel.copy(alpha = 0.3f),
    secondaryContainer = ColorGrisSuperficie
)
// Clase para almacenar la información completa del archivo
data class DescargaInfo(
    val id: String,
    val titulo: String,
    val progreso: Float,
    val esAudio: Boolean,
    val completado: Boolean
)

data class VideoPlaylistItem(
    val titulo: String,
    val urlOriginalVideo: String
)

// Función 1: Extrae la imagen del álbum oculta dentro del archivo MP3/MP4
fun obtenerPortadaArchivo(contexto: Context, ruta: String): Bitmap? {
    return BitmapFactory.decodeResource(contexto.resources, R.drawable.icono)
}

// Función 2: Lanza el reproductor de fábrica de tu celular Android para reproducir el archivo
fun reproducirEnSistemaExterno(contexto: Context, archivo: File) {
    try {
        val uriSegura: Uri = FileProvider.getUriForFile(
            contexto,
            "${contexto.packageName}.fileprovider",
            archivo
        )
        val extension = archivo.extension.lowercase()
        val tipoMime = when (extension) {
            "mp3", "m4a", "opus", "wav", "ogg" -> "audio/*"
            "mp4", "mkv", "webm", "avi", "3gp" -> "video/*"
            else -> "*/*"
        }

        val intent = Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(uriSegura, tipoMime)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        contexto.startActivity(intent)
    } catch (e: Exception) {
        android.util.Log.e("FROG_PLAYER", "Error al abrir: ${e.message}")
        Toast.makeText(contexto, "Error al abrir el archivo: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
    }
}

fun compartirArchivo(contexto: Context, archivo: File) {
    try {
        val uriSegura: Uri = FileProvider.getUriForFile(
            contexto,
            "${contexto.packageName}.fileprovider",
            archivo
        )
        val extension = archivo.extension.lowercase()
        val tipoMime = when (extension) {
            "mp3", "m4a", "opus" -> "audio/*"
            "mp4", "mkv", "webm" -> "video/*"
            else -> "*/*"
        }

        val intent = Intent(Intent.ACTION_SEND).apply {
            type = tipoMime
            putExtra(Intent.EXTRA_STREAM, uriSegura)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        contexto.startActivity(Intent.createChooser(intent, "Compartir con..."))
    } catch (e: Exception) {
        android.util.Log.e("FROG_SHARE", "Error al compartir: ${e.message}")
        Toast.makeText(contexto, "Error al compartir: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
    }
}

// Función auxiliar para encontrar el archivo en la base de datos de Android
fun obtenerUriMediaStore(contexto: Context, archivo: File): Uri? {
    val contentResolver = contexto.contentResolver
    val selection = MediaStore.MediaColumns.DATA + "=?"
    val selectionArgs = arrayOf(archivo.absolutePath)
    
    val bases = mutableListOf(
        MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
        MediaStore.Video.Media.EXTERNAL_CONTENT_URI
    )
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        bases.add(MediaStore.Downloads.EXTERNAL_CONTENT_URI)
    }

    for (base in bases) {
        try {
            contentResolver.query(base, arrayOf(MediaStore.MediaColumns._ID), selection, selectionArgs, null)?.use { cursor ->
                if (cursor.moveToFirst()) {
                    val id = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns._ID))
                    return ContentUris.withAppendedId(base, id)
                }
            }
        } catch (e: Exception) { /* Ignorar errores de tabla */ }
    }
    return null
}

// Lista global reactiva que usará la pestaña de Descargas
val listaDescargasActivas = mutableStateListOf<DescargaInfo>()
// Guarda si el usuario quiere descargar solo audio (MP3) o video completo
var formatoSoloAudioGlobal by mutableStateOf(false)

enum class Pantallas(val ruta: String, val titulo: String, val icono: ImageVector) {
    Video("video", "Stream", Icons.Default.VideoLibrary),
    Descargas("descargas", "Descargas", Icons.Default.Download),
    Configuraciones("config", "Ajustes", Icons.Default.Settings)
}

// --- VARIABLE GLOBAL PARA HACER QUE EL BOTÓN FLOTANTE SEPA QUÉ LINK DESCARGAR ---
var urlActualParaDescargarGlobal by mutableStateOf("")
// --- NUEVA VARIABLE GLOBAL PARA EL LINK COMPARTIDO ---
var urlCompartidaDesdeYoutubeGlobal by mutableStateOf("")

@Composable
fun InterfazPrincipal(windowSizeClass: WindowSizeClass) {
    val navController = rememberNavController()
    var pantallaActual by remember { mutableStateOf(Pantallas.Video) }
    val contexto = LocalContext.current

    val esTablet = windowSizeClass.widthSizeClass != WindowWidthSizeClass.Compact
    
    // --- CARGAR PREFERENCIAS AL INICIAR ---
    LaunchedEffect(Unit) {
        val prefs = PreferenciasApp(contexto)
        resolucionReproduccionGlobal = prefs.obtenerString(PreferenciasApp.KEY_RESOLUCION_REPRO, "480p")
        formatoAudioGlobal = prefs.obtenerString(PreferenciasApp.KEY_FORMATO_AUDIO, "mp3")
        calidadAudioGlobal = prefs.obtenerString(PreferenciasApp.KEY_CALIDAD_AUDIO, "320k")
        rutaGuardadoAudioGlobal = prefs.obtenerString(PreferenciasApp.KEY_RUTA_AUDIO, "MisDescargas/Musica")
        formatoVideoGlobal = prefs.obtenerString(PreferenciasApp.KEY_FORMATO_VIDEO, "mp4")
        calidadVideoGlobal = prefs.obtenerString(PreferenciasApp.KEY_CALIDAD_VIDEO, "720p")
        rutaGuardadoVideoGlobal = prefs.obtenerString(PreferenciasApp.KEY_RUTA_VIDEO, "MisDescargas/Videos")
        androidAutoActivadoGlobal = prefs.obtenerBoolean(PreferenciasApp.KEY_ANDROID_AUTO, true)
        val temaGuardado = prefs.obtenerString(PreferenciasApp.KEY_TEMA_APP, ModoTema.AUTOMATICO.name)
        temaAppGlobal = try { ModoTema.valueOf(temaGuardado) } catch (e: Exception) { ModoTema.AUTOMATICO }
    }

    val esOscuro = when (temaAppGlobal) {
        ModoTema.CLARO -> false
        ModoTema.OSCURO -> true
        ModoTema.AUTOMATICO -> isSystemInDarkTheme()
    }

    val colorSchemeActual = if (esOscuro) EsquemaColoresOscuro else EsquemaColoresVerde

    // 1. Aplicamos nuestra paleta de colores personalizada a toda la app
    MaterialTheme(colorScheme = colorSchemeActual) {

        // 2. Usamos un Box principal para colocar la imagen de fondo en una capa trasera
        Box(modifier = Modifier.fillMaxSize()) {
            Image(
                // REEMPLAZA "fondo_drive" por el nombre exacto que le pusiste a tu imagen en drawable
                painter = painterResource(id = contexto.resources.getIdentifier("fondo_app", "drawable", contexto.packageName)),
                contentDescription = "Fondo de pantalla",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop, // Fuerza a la imagen a cubrir toda la pantalla sin deformarse
                colorFilter = if (esOscuro) ColorFilter.tint(Color.Black.copy(alpha = 0.5f), BlendMode.Darken) else null
            )

            // 3. El contenedor visual flota de forma transparente sobre la imagen
            Row(modifier = Modifier.fillMaxSize()) {
                if (esTablet) {
                    // BARRA LATERAL PARA TABLETS
                    NavigationRail(
                        containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f),
                        header = {
                            Image(
                                painter = painterResource(id = R.drawable.icono),
                                contentDescription = "Logo",
                                modifier = Modifier.size(48.dp).padding(8.dp)
                            )
                        }
                    ) {
                        Pantallas.entries.forEach { pantalla ->
                            NavigationRailItem(
                                icon = { Icon(pantalla.icono, contentDescription = pantalla.titulo) },
                                label = { Text(pantalla.titulo) },
                                selected = pantallaActual == pantalla,
                                colors = NavigationRailItemDefaults.colors(
                                    selectedIconColor = MaterialTheme.colorScheme.primary,
                                    unselectedIconColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                                    indicatorColor = MaterialTheme.colorScheme.secondary
                                ),
                                onClick = {
                                    if (pantallaActual != pantalla) {
                                        pantallaActual = pantalla
                                        navController.navigate(pantalla.ruta) {
                                            popUpTo(navController.graph.startDestinationId) { saveState = true }
                                            launchSingleTop = true
                                            restoreState = true
                                        }
                                    }
                                }
                            )
                        }
                    }
                }

                Scaffold(
                    containerColor = Color.Transparent, // Obliga al Scaffold a ser transparente
                    bottomBar = {
                        if (!esTablet) {
                            NavigationBar(
                                // Barra inferior ligeramente translúcida para combinar con el fondo
                                containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f)
                            ) {
                                Pantallas.entries.forEach { pantalla ->
                                    NavigationBarItem(
                                        icon = { Icon(pantalla.icono, contentDescription = pantalla.titulo) },
                                        label = { Text(pantalla.titulo) },
                                        selected = pantallaActual == pantalla,
                                        colors = NavigationBarItemDefaults.colors(
                                            selectedIconColor = MaterialTheme.colorScheme.primary,
                                            unselectedIconColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                                            indicatorColor = MaterialTheme.colorScheme.secondary
                                        ),
                                        onClick = {
                                            if (pantallaActual != pantalla) {
                                                pantallaActual = pantalla
                                                navController.navigate(pantalla.ruta) {
                                                    popUpTo(navController.graph.startDestinationId) { saveState = true }
                                                    launchSingleTop = true
                                                    restoreState = true
                                                }
                                            }
                                        }
                                    )
                                }
                            }
                        }
                    },
                    floatingActionButton = {
                        if (pantallaActual == Pantallas.Video && urlActualParaDescargarGlobal.isNotEmpty()) {
                            // BOTÓN FLOTANTE LIMÓN CON ÍCONO GRIS OSCURO
                            FloatingActionButton(
                                onClick = {
                                    esPlaylistDetectadaGlobal = urlActualParaDescargarGlobal.contains("playlist") || urlActualParaDescargarGlobal.contains("&list=")
                                    urlParaDescargarGlobal = urlActualParaDescargarGlobal
                                    mostrarBottomSheetGlobal = true
                                },
                                containerColor = ColorLimon, // Color limón solicitado
                                contentColor = ColorGrisOscuro // Letras/ícono gris oscuro solicitado
                            ) {
                                Icon(Icons.Default.Download, contentDescription = "Descargar")
                            }
                        }
                    }
                ) { paddingInterno ->
                    NavHost(
                        navController = navController,
                        startDestination = Pantallas.Video.ruta,
                        modifier = Modifier.padding(paddingInterno)
                    ) {
                        composable(Pantallas.Video.ruta) { PantallaVideo(esTablet) }
                        composable(Pantallas.Descargas.ruta) { PantallaDescargas() }
                        composable(Pantallas.Configuraciones.ruta) { PantallaConfiguraciones() }
                    }
                }
            }
        }
    }
}

// --- VARIABLE ESTÁTICA GLOBAL PARA MANTENER AL REPRODUCTOR VIVO EN SEGUNDO PLANO ---
// --- VARIABLES GLOBALES PARA CONSERVAR LA PLAYLIST EN MEMORIA ---
//private var exoPlayerGlobal: ExoPlayer? = null
val videosPlaylistGlobal = mutableStateListOf<VideoPlaylistItem>()
var indiceActualVideoGlobal by mutableStateOf(-1)

object GestorReproduccion {
    fun cargarYReproducirIndice(index: Int, contexto: Context) {
        val playlistItems = videosPlaylistGlobal
        if (index < 0 || index >= playlistItems.size) return

        cargandoVideoGlobal = true
        indiceActualVideoGlobal = index
        val videoSeleccionado = playlistItems[index]

        tituloVideoActualGlobal = videoSeleccionado.titulo
        urlParaDescargarIndividualGlobal = videoSeleccionado.urlOriginalVideo.trim()

        thread {
            try {
                val urlLimpia = videoSeleccionado.urlOriginalVideo.trim()
                val request = YoutubeDLRequest(urlLimpia)
                // --- OPTIMIZACIÓN DE CARGA EXTREMA (MODO TURBO) ---
                request.addOption("--extractor-args", "youtube:player_client=android,mweb,ios,web;player_skip=webpage,configs")
                request.addOption("--no-check-formats")
                request.addOption("--youtube-skip-dash-manifest")
                request.addOption("--youtube-skip-hls-manifest")
                request.addOption("--no-check-certificate")
                request.addOption("--no-warnings")
                request.addOption("--no-playlist")
                request.addOption("--ignore-config")
                request.addOption("--no-call-home")
                request.addOption("--socket-timeout", "5")
                
                // Forzamos la obtención directa del link sin procesar metadatos pesados
                request.addOption("--get-url")
                
                val alturaMapeada = resolucionReproduccionGlobal.replace("p", "")
                request.addOption("-f", "best[height<=$alturaMapeada][ext=mp4]/best[height<=$alturaMapeada]/best")

                val response = YoutubeDL.getInstance().execute(request)
                val linkStreamingReal = response.out.split("\n").firstOrNull { it.startsWith("http") } ?: return@thread

                ContextCompat.getMainExecutor(contexto).execute {
                    val exoPlayer = MainApp.exoPlayerGlobal ?: return@execute
                    
                    // OPTIMIZACIÓN: User Agent más compatible con los clientes de streaming (iOS/Android)
                    val userAgentOficial = "Mozilla/5.0 (iPhone; CPU iPhone OS 17_5 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/17.5 Mobile/15E148 Safari/604.1"
                    val dataSourceFactory = DefaultHttpDataSource.Factory()
                        .setUserAgent(userAgentOficial)
                        .setDefaultRequestProperties(mapOf(
                            "Referer" to "https://youtube.com",
                            "Origin" to "https://youtube.com"
                        ))

                    val mediaSourceFactory = DefaultMediaSourceFactory(dataSourceFactory)
                    val metadata = MediaMetadata.Builder()
                        .setTitle(videoSeleccionado.titulo)
                        .setArtworkUri(Uri.parse("android.resource://${contexto.packageName}/drawable/icono"))
                        .build()
                    val mediaItem = MediaItem.Builder()
                        .setUri(linkStreamingReal)
                        .setMediaMetadata(metadata)
                        .build()

                    val mediaSource = mediaSourceFactory.createMediaSource(mediaItem)

                    exoPlayer.setMediaSource(mediaSource)
                    exoPlayer.prepare()
                    exoPlayer.play()

                    // Forzamos el encendido en el hilo multimedia del MainApp
                    MainApp.exoPlayerGlobal?.playWhenReady = true
                }
            } catch (e: Exception) {
                e.printStackTrace()
                ContextCompat.getMainExecutor(contexto).execute { cargandoVideoGlobal = false }
            }
        }
    }
}

@OptIn(UnstableApi::class, ExperimentalMaterial3Api::class)
@Composable
fun PantallaVideo(esTablet: Boolean = false) {
    val contexto = LocalContext.current
    val actividad = contexto as? Activity
    var urlTexto by remember { mutableStateOf("") }

    val playlistItems = videosPlaylistGlobal
    var cargandoPlaylist by remember { mutableStateOf(false) }
    var esPantallaCompleta by remember { mutableStateOf(false) }

    // Estado local para pintar el título del video que está sonando actualmente
    val sheetState = rememberModalBottomSheetState()

    val exoPlayer = remember { MainApp.exoPlayerGlobal!! }

    var urlPendiente by remember { mutableStateOf("") }
    var mostrarDialogoOpcionesCola by remember { mutableStateOf(false) }

    val procesarCarga = { url: String, reemplazar: Boolean ->
        cargandoPlaylist = true
        if (reemplazar) {
            playlistItems.clear()
            exoPlayer.clearMediaItems()
            indiceActualVideoGlobal = -1
            PlaybackService.actualizarListaEnAuto()
        }

        thread {
            try {
                val request = YoutubeDLRequest(url)
                // --- OPTIMIZACIÓN DE CARGA EXTREMA DE LISTAS ---
                request.addOption("--extractor-args", "youtube:player_client=android,mweb,ios,web;player_skip=webpage,configs")
                request.addOption("--flat-playlist")
                request.addOption("--dump-single-json")
                request.addOption("--no-check-certificate")
                request.addOption("--no-warnings")
                request.addOption("--yes-playlist")
                request.addOption("--ignore-config")
                request.addOption("--no-call-home")
                request.addOption("--no-check-formats")
                request.addOption("--playlist-items", "1-25") // Reducido a 25 para velocidad instantánea

                // Ejecución manual para obtener el JSON completo
                val response = YoutubeDL.getInstance().execute(request)
                val json = JSONObject(response.out)

                ContextCompat.getMainExecutor(contexto).execute {
                    val nuevasEntradas = mutableListOf<VideoPlaylistItem>()
                    val entriesJson = json.optJSONArray("entries")
                    if (entriesJson != null && entriesJson.length() > 0) {
                        for (i in 0 until entriesJson.length()) {
                            val video = entriesJson.getJSONObject(i)
                            val titulo = video.optString("title", "Video ${i + 1}")
                            val idVideo = video.optString("id", video.optString("url", ""))
                            if (idVideo.isNotEmpty()) {
                                val urlIndividual = if (idVideo.startsWith("http")) idVideo else "https://www.youtube.com/watch?v=$idVideo"
                                nuevasEntradas.add(VideoPlaylistItem(titulo, urlIndividual))
                            }
                        }
                    } else {
                        val titulo = json.optString("title", "Video")
                        val idVideo = json.optString("id", json.optString("webpage_url", ""))
                        if (idVideo.isNotEmpty()) {
                            val urlIndividual = if (idVideo.startsWith("http")) idVideo else "https://www.youtube.com/watch?v=$idVideo"
                            nuevasEntradas.add(VideoPlaylistItem(titulo, urlIndividual))
                        }
                    }

                    val indiceParaEmpezar = playlistItems.size
                    playlistItems.addAll(nuevasEntradas)

                    if (reemplazar && playlistItems.isNotEmpty()) {
                        GestorReproduccion.cargarYReproducirIndice(0, contexto)
                    } else if (!reemplazar && indiceActualVideoGlobal == -1 && playlistItems.isNotEmpty()) {
                        GestorReproduccion.cargarYReproducirIndice(indiceParaEmpezar, contexto)
                    }

                    cargandoPlaylist = false
                    PlaybackService.actualizarListaEnAuto()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                ContextCompat.getMainExecutor(contexto).execute { cargandoPlaylist = false }
            }
        }
    }

    // --- FUNCIÓN PARA ACCIONAR LA BÚSQUEDA ---
    val accionarBusqueda = { url: String ->
        if (url.isNotEmpty()) {
            if (playlistItems.isNotEmpty()) {
                urlPendiente = url
                mostrarDialogoOpcionesCola = true
            } else {
                procesarCarga(url, true)
            }
        }
    }

    // --- ESCUCHAR SI LLEGA UN LINK COMPARTIDO ---
    LaunchedEffect(urlCompartidaDesdeYoutubeGlobal) {
        if (urlCompartidaDesdeYoutubeGlobal.isNotEmpty()) {
            urlTexto = urlCompartidaDesdeYoutubeGlobal
            urlActualParaDescargarGlobal = urlCompartidaDesdeYoutubeGlobal
            accionarBusqueda(urlCompartidaDesdeYoutubeGlobal)
            // Limpiar para no repetir si se rota la pantalla
            urlCompartidaDesdeYoutubeGlobal = ""
        }
    }

    LaunchedEffect(exoPlayer) {
        exoPlayer.addListener(object : androidx.media3.common.Player.Listener {
            override fun onPlaybackStateChanged(state: Int) {
                if (state == androidx.media3.common.Player.STATE_ENDED) {
                    val siguienteIndice = indiceActualVideoGlobal + 1
                    if (siguienteIndice < playlistItems.size) {
                        GestorReproduccion.cargarYReproducirIndice(siguienteIndice, contexto)
                    }
                }
                if (state == androidx.media3.common.Player.STATE_READY) {
                    cargandoVideoGlobal = false
                }
            }
        })
    }

    LaunchedEffect(esPantallaCompleta) {
        actividad?.let { act ->
            if (esPantallaCompleta) {
                act.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
                act.window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_FULLSCREEN or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
            } else {
                act.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
                act.window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
            }
        }
    }

    val componenteReproductor = @Composable {
        Box(modifier = Modifier.fillMaxSize().background(Color.Black), contentAlignment = Alignment.Center) {
            AndroidView(
                factory = { ctx ->
                    PlayerView(ctx).apply {
                        player = exoPlayer
                        useController = true
                        setFullscreenButtonClickListener { esFull -> esPantallaCompleta = esFull }
                    }
                },
                modifier = Modifier.fillMaxSize(),
                update = { playerView -> playerView.setFullscreenButtonState(esPantallaCompleta) }
            )
            if (cargandoVideoGlobal) {
                Box(modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.7f)), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Descifrando enlace...", color = Color.White)
                    }
                }
            }
        }
    }

    if (esPantallaCompleta) {
        Dialog(onDismissRequest = { esPantallaCompleta = false }, properties = DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = false, usePlatformDefaultWidth = false)) {
            Box(modifier = Modifier.fillMaxSize()) { componenteReproductor() }
        }
    } else {
        Column(modifier = Modifier.fillMaxSize()) {
            if (!esTablet) {
                TopAppBar(
                    title = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // 1. DIBUJAMOS TU LOGO
                            Image(
                                painter = painterResource(id = R.drawable.icono),
                                contentDescription = "Logo de la app",
                                modifier = Modifier
                                    .size(55.dp)
                                    .padding(end = 8.dp)
                            )

                            // 2. TEXTO DEL NOMBRE DE TU APP
                            Text(
                                text = "FrogDownloader Pro",
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                                style = MaterialTheme.typography.titleLarge
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f)
                    )
                )
            }

            // CONTENIDO ADAPTATIVO
            if (esTablet) {
                Row(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                    // Columna Izquierda: Input y Reproductor
                    Column(modifier = Modifier.weight(1.2f)) {
                        OutlinedTextField(
                            value = urlTexto,
                            onValueChange = {
                                urlTexto = it
                                urlActualParaDescargarGlobal = it
                            },
                            label = { Text("Pegar Link de YouTube") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            trailingIcon = {
                                IconButton(onClick = { accionarBusqueda(urlTexto) }) {
                                    Icon(Icons.Default.ArrowForward, contentDescription = "Ver")
                                }
                            }
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Card(modifier = Modifier.fillMaxWidth().height(350.dp)) { componenteReproductor() }
                        Spacer(modifier = Modifier.height(16.dp))
                        InfoVideoActual()
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    // Columna Derecha: Playlist
                    Column(modifier = Modifier.weight(0.8f)) {
                        ControlesPlaylist(contexto)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Lista de Reproducción", style = MaterialTheme.typography.titleMedium)
                        ListaVideosPlaylist(cargandoPlaylist, playlistItems, contexto)
                    }
                }
            } else {
                // DISEÑO MÓVIL ORIGINAL
                Column(modifier = Modifier.padding(16.dp)) {
                    OutlinedTextField(
                        value = urlTexto,
                        onValueChange = {
                            urlTexto = it
                            urlActualParaDescargarGlobal = it
                        },
                        label = { Text("Pegar Link de YouTube") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        trailingIcon = {
                            IconButton(onClick = { accionarBusqueda(urlTexto) }) {
                                Icon(Icons.Default.ArrowForward, contentDescription = "Ver")
                            }
                        }
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Card(modifier = Modifier.fillMaxWidth().height(200.dp)) { componenteReproductor() }
                    Spacer(modifier = Modifier.height(16.dp))
                    InfoVideoActual()
                    Spacer(modifier = Modifier.height(12.dp))
                    ControlesPlaylist(contexto)
                    Spacer(modifier = Modifier.height(8.dp))
                    HorizontalDivider()
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Contenido de la Lista de Reproducción", style = MaterialTheme.typography.titleMedium)
                    ListaVideosPlaylist(cargandoPlaylist, playlistItems, contexto)
                }
            }
        }
    }
    if (mostrarDialogoOpcionesCola) {
        AlertDialog(
            onDismissRequest = { mostrarDialogoOpcionesCola = false },
            title = { Text("Contenido Detectado") },
            text = { Text("¿Qué deseas hacer con el nuevo contenido?") },
            confirmButton = {
                TextButton(onClick = {
                    mostrarDialogoOpcionesCola = false
                    procesarCarga(urlPendiente, true)
                }) {
                    Text("Reproducir Ahora")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    mostrarDialogoOpcionesCola = false
                    procesarCarga(urlPendiente, false)
                }) {
                    Text("Agregar a la fila")
                }
            }
        )
    }

    if (mostrarBottomSheetGlobal) {
        ModalBottomSheet(
            onDismissRequest = { mostrarBottomSheetGlobal = false },
            sheetState = sheetState
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Opciones de Descarga", style = MaterialTheme.typography.titleLarge)
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = if (esPlaylistDetectadaGlobal) "Se detectó una Lista de Reproducción activa" else "Enlace individual detectado",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(16.dp))

                // APARTADO 1: CHIPS SELECCIONABLES DE AUDIO O VIDEO
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    FilterChip(
                        selected = !formatoSoloAudioGlobal,
                        onClick = { formatoSoloAudioGlobal = false },
                        label = { Text("Video (${formatoVideoGlobal.uppercase()})") }
                    )
                    FilterChip(
                        selected = formatoSoloAudioGlobal,
                        onClick = { formatoSoloAudioGlobal = true },
                        label = { Text("Música (${formatoAudioGlobal.uppercase()})") }
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))
                HorizontalDivider()
                Spacer(modifier = Modifier.height(20.dp))

                // APARTADO 2: ACCIONES CORREGIDAS
                val calidadActualTexto = if (formatoSoloAudioGlobal) calidadAudioGlobal else calidadVideoGlobal

                if (esPlaylistDetectadaGlobal) {
                    // OPCIÓN B: CORRECCIÓN CRÍTICA - Descargar SOLO la canción actual con su link limpio e individual
                    Button(
                        onClick = {
                            mostrarBottomSheetGlobal = false
                            // Forzamos a pasar urlParaDescargarGlobal, que contiene el link individual puro
                            iniciarDescarga(urlParaDescargarIndividualGlobal, contexto, descargarTodoElCanal = false)
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Descargar la canción actual ($calidadActualTexto)")
                    }
                } else {
                    // OPCIÓN C: Descarga tradicional si pegaste un video único desde el principio
                    Button(
                        onClick = {
                            mostrarBottomSheetGlobal = false
                            iniciarDescarga(urlParaDescargarGlobal, contexto, descargarTodoElCanal = false)
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        val formatoActualTexto = if (formatoSoloAudioGlobal) formatoAudioGlobal else formatoVideoGlobal
                        Text("Confirmar y Descargar en ${formatoActualTexto.uppercase()} ($calidadActualTexto)")
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
fun InfoVideoActual() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.4f))
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text(
                text = "Reproduciendo ahora:",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = tituloVideoActualGlobal, style = MaterialTheme.typography.bodyLarge)
        }
    }
}

@Composable
fun ControlesPlaylist(contexto: Context) {
    if (videosPlaylistGlobal.size > 1) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
            IconButton(
                onClick = { GestorReproduccion.cargarYReproducirIndice(indiceActualVideoGlobal - 1, contexto) }, enabled = indiceActualVideoGlobal > 0) {
                Icon(Icons.Default.SkipPrevious, contentDescription = "Anterior", modifier = Modifier.size(32.dp))
            }
            Text(text = "Video ${indiceActualVideoGlobal + 1} de ${videosPlaylistGlobal.size}", style = MaterialTheme.typography.bodyMedium, modifier = Modifier.padding(horizontal = 16.dp))
            IconButton(onClick = { GestorReproduccion.cargarYReproducirIndice(indiceActualVideoGlobal + 1, contexto) }, enabled = indiceActualVideoGlobal < videosPlaylistGlobal.size - 1) {
                Icon(Icons.Default.SkipNext, contentDescription = "Siguiente", modifier = Modifier.size(32.dp))
            }
        }
    }
}

@Composable
fun ListaVideosPlaylist(cargando: Boolean, items: List<VideoPlaylistItem>, contexto: Context) {
    Spacer(modifier = Modifier.height(4.dp))
    if (cargando) {
        Box(modifier = Modifier.fillMaxWidth().padding(16.dp), contentAlignment = Alignment.Center) { CircularProgressIndicator() }
    }
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        itemsIndexed(items, key = { _, item -> item.urlOriginalVideo + item.titulo }) { index, item ->
            val esElVideoActual = index == indiceActualVideoGlobal
            
            val dismissState = rememberSwipeToDismissBoxState(
                confirmValueChange = { valor ->
                    if (valor == SwipeToDismissBoxValue.EndToStart) {
                        // LÓGICA DE ELIMINACIÓN
                        if (index == indiceActualVideoGlobal) {
                            // Si borramos el que suena, intentamos pasar al siguiente si existe
                            if (index < videosPlaylistGlobal.size - 1) {
                                GestorReproduccion.cargarYReproducirIndice(index, contexto)
                            } else {
                                MainApp.exoPlayerGlobal?.stop()
                                indiceActualVideoGlobal = -1
                                tituloVideoActualGlobal = "Reproducción detenida"
                            }
                        } else if (index < indiceActualVideoGlobal) {
                            indiceActualVideoGlobal--
                        }
                        
                        videosPlaylistGlobal.removeAt(index)
                        PlaybackService.actualizarListaEnAuto()
                        true
                    } else {
                        false
                    }
                }
            )

            SwipeToDismissBox(
                state = dismissState,
                enableDismissFromStartToEnd = false,
                backgroundContent = {
                    val color = if (dismissState.dismissDirection == SwipeToDismissBoxValue.EndToStart) Color.Red.copy(alpha = 0.8f) else Color.Transparent
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(vertical = 2.dp)
                            .clip(MaterialTheme.shapes.medium)
                            .background(color),
                        contentAlignment = Alignment.CenterEnd
                    ) {
                        // Icono eliminado a petición del usuario
                    }
                }
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 2.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (esElVideoActual) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant
                    ),
                    onClick = { GestorReproduccion.cargarYReproducirIndice(index, contexto) }
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "${index + 1}.",
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(end = 8.dp),
                            color = if (esElVideoActual) MaterialTheme.colorScheme.primary else Color.Unspecified
                        )
                        Text(
                            text = item.titulo,
                            style = MaterialTheme.typography.bodyMedium,
                            maxLines = 1,
                            color = if (esElVideoActual) MaterialTheme.colorScheme.primary else Color.Unspecified
                        )
                    }
                }
            }
        }
    }
}


// Clase local para recordar los datos de tus canciones viejas guardadas en memoria
data class ArchivoGuardadoInfo(
    val nombreCompleto: String,
    val formato: String,
    val portada: Bitmap?,
    val archivoReal: File
)

@OptIn(androidx.compose.foundation.ExperimentalFoundationApi::class)
@Composable
fun PantallaDescargas() {
    val contexto = LocalContext.current
    var archivosLocalesHistoricos by remember { mutableStateOf(listOf<ArchivoGuardadoInfo>()) }
    var refrescando by remember { mutableStateOf(0) }

    // --- LANZADOR PARA SOLICITAR PERMISO DE BORRADO EN ANDROID 11+ ---
    val launcherEliminar = rememberLauncherForActivityResult(
        ActivityResultContracts.StartIntentSenderForResult()
    ) { resultado ->
        if (resultado.resultCode == Activity.RESULT_OK) {
            Toast.makeText(contexto, "Archivo eliminado", Toast.LENGTH_SHORT).show()
            refrescando++
        }
    }

    // ESCÁNER INTELIGENTE REPARADO Y DINÁMICO
    LaunchedEffect(listaDescargasActivas.size, refrescando) {
        val carpetaDescargas = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)

        // Usamos las rutas globales configuradas por el usuario + la base
        val carpetasABuscar = mutableSetOf(
            File(carpetaDescargas, "MisDescargas"),
            File(carpetaDescargas, rutaGuardadoAudioGlobal),
            File(carpetaDescargas, rutaGuardadoVideoGlobal)
        )

        val listaTemporal = mutableListOf<ArchivoGuardadoInfo>()

        carpetasABuscar.forEach { rutaDestino ->
            if (rutaDestino.exists() && rutaDestino.isDirectory) {
                rutaDestino.listFiles()?.forEach { file ->
                    if (file.isFile && !file.name.startsWith(".")) {
                        val extension = file.extension.uppercase()
                        if (extension in listOf("MP3", "MP4", "M4A", "OPUS", "MKV", "WEBM")) {
                            val bitmapPortada = obtenerPortadaArchivo(contexto, file.absolutePath)
                            listaTemporal.add(
                                ArchivoGuardadoInfo(
                                    nombreCompleto = file.nameWithoutExtension,
                                    formato = extension,
                                    portada = bitmapPortada,
                                    archivoReal = file
                                )
                            )
                        }
                    }
                }
            }
        }
        archivosLocalesHistoricos = listaTemporal.distinctBy { it.archivoReal.absolutePath }.sortedByDescending { it.archivoReal.lastModified() }
    }


    LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp)) {

        // --- BOTÓN DE REFRESCAR MANUAL ---
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Administrar Archivos",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onSurface
                )
                IconButton(onClick = { refrescando++ }) {
                    Icon(Icons.Default.Refresh, contentDescription = "Refrescar lista")
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        // ====================================================================
        // SECCIÓN 1: DESCARGAS EN CURSO (Muestra barras de progreso y porcentaje)
        // ====================================================================
        val descargasCorriendo = listaDescargasActivas.filter { !it.completado }
        if (descargasCorriendo.isNotEmpty()) {
            item {
                Text("Descargas en Curso", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)
                Spacer(modifier = Modifier.height(8.dp))
            }
            items(descargasCorriendo) { descarga ->
                Card(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                ) {
                    // Usamos una fila para poner el botón detener al lado de la barra
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(descarga.titulo, style = MaterialTheme.typography.bodyLarge, maxLines = 1)
                            Spacer(modifier = Modifier.height(8.dp))

                            LinearProgressIndicator(
                                progress = descarga.progreso / 100f,
                                modifier = Modifier.fillMaxWidth(),
                                color = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.height(4.dp))

                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text(
                                    text = if (descarga.esAudio) "Extrayendo Música..." else "Descargando Video...",
                                    style = MaterialTheme.typography.bodySmall
                                )
                                Text("${descarga.progreso.toInt()}%", style = MaterialTheme.typography.labelMedium)
                            }
                        }

                        // --- ADICIÓN DEL BOTÓN ESTÍTICO DE DETENER / CANCELAR ---
                        // Solo se muestra si es una descarga masiva de playlist
                        if (descarga.titulo.contains("Lista")) {
                            Spacer(modifier = Modifier.width(12.dp))
                            IconButton(
                                onClick = {
                                    // Activamos el freno de emergencia global
                                    detenerDescargaMasivaGlobal = true
                                    // Removemos la tarjeta visual de la pantalla inmediatamente
                                    listaDescargasActivas.remove(descarga)
                                },
                                colors = IconButtonDefaults.iconButtonColors(
                                    containerColor = MaterialTheme.colorScheme.errorContainer,
                                    contentColor = MaterialTheme.colorScheme.error
                                )
                            ) {
                                Icon(Icons.Default.Stop, contentDescription = "Detener descarga masiva")
                            }
                        }
                    }
                }
            }
            item { Spacer(modifier = Modifier.height(16.dp)) }
        }

        // ====================================================================
        // SECCIÓN 2: HISTORIAL DE ARCHIVOS COMPLETADOS (Persistentes)
        // ====================================================================
        item {
            Text("Historial en Almacenamiento", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.height(8.dp))
        }

        if (archivosLocalesHistoricos.isEmpty()) {
            item {
                Box(modifier = Modifier.fillMaxWidth().padding(32.dp), contentAlignment = Alignment.Center) {
                    Text("No se encontraron canciones en el directorio.", style = MaterialTheme.typography.bodyMedium)
                }
            }
        } else {
            items(archivosLocalesHistoricos) { itemCancion ->
                var mostrarMenuContextual by remember { mutableStateOf(false) }
                var mostrarConfirmacionEliminar by remember { mutableStateOf(false) }

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .combinedClickable(
                            onLongClick = { mostrarMenuContextual = true },
                            onClick = { reproducirEnSistemaExterno(contexto, itemCancion.archivoReal) }
                        )
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // MINIATURA / IMAGEN DE ÁLBUM
                        Card(
                            modifier = Modifier.size(50.dp),
                            shape = MaterialTheme.shapes.small
                        ) {
                            if (itemCancion.portada != null) {
                                Image(
                                    bitmap = itemCancion.portada.asImageBitmap(),
                                    contentDescription = "Portada",
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Crop
                                )
                            } else {
                                Box(
                                    modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.secondaryContainer),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text("🎵", style = MaterialTheme.typography.titleLarge)
                                }
                            }
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        // INFORMACIÓN DE LA CANCIÓN Y SU FORMATO
                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = itemCancion.nombreCompleto, style = MaterialTheme.typography.bodyLarge, maxLines = 1)
                            Spacer(modifier = Modifier.height(2.dp))
                            SuggestionChip(
                                onClick = { },
                                label = { Text(itemCancion.formato) },
                                modifier = Modifier.height(24.dp)
                            )
                        }

                        // MENÚ DESPLEGABLE (CONTEXTUAL)
                        androidx.compose.material3.DropdownMenu(
                            expanded = mostrarMenuContextual,
                            onDismissRequest = { mostrarMenuContextual = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text("Abrir") },
                                onClick = {
                                    mostrarMenuContextual = false
                                    reproducirEnSistemaExterno(contexto, itemCancion.archivoReal)
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Compartir") },
                                onClick = {
                                    mostrarMenuContextual = false
                                    compartirArchivo(contexto, itemCancion.archivoReal)
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Eliminar", color = Color.Red) },
                                onClick = {
                                    mostrarMenuContextual = false
                                    mostrarConfirmacionEliminar = true
                                }
                            )
                        }
                    }
                }

                // DIÁLOGO DE CONFIRMACIÓN PARA ELIMINAR
                if (mostrarConfirmacionEliminar) {
                    AlertDialog(
                        onDismissRequest = { mostrarConfirmacionEliminar = false },
                        title = { Text("¿Eliminar archivo?") },
                        text = { Text("Esta acción borrará '${itemCancion.nombreCompleto}' de forma permanente de tu dispositivo.") },
                        confirmButton = {
                            TextButton(
                                onClick = {
                                    mostrarConfirmacionEliminar = false
                                    // 1. Intentamos el borrado tradicional
                                    if (itemCancion.archivoReal.delete()) {
                                        Toast.makeText(contexto, "Archivo eliminado", Toast.LENGTH_SHORT).show()
                                        refrescando++ 
                                    } else {
                                        // 2. Si falla (común en Android 11+ con archivos antiguos), usamos MediaStore
                                        try {
                                            val uri = obtenerUriMediaStore(contexto, itemCancion.archivoReal)
                                            if (uri != null) {
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                                                    // En Android 11+, pedimos permiso al sistema para borrarlo si no somos dueños
                                                    val pendingIntent = MediaStore.createDeleteRequest(contexto.contentResolver, listOf(uri))
                                                    launcherEliminar.launch(IntentSenderRequest.Builder(pendingIntent.intentSender).build())
                                                } else {
                                                    // En versiones anteriores, intentamos borrar directamente vía resolver
                                                    contexto.contentResolver.delete(uri, null, null)
                                                    Toast.makeText(contexto, "Archivo eliminado", Toast.LENGTH_SHORT).show()
                                                    refrescando++
                                                }
                                            } else {
                                                Toast.makeText(contexto, "No se encontró el archivo en la galería", Toast.LENGTH_SHORT).show()
                                            }
                                        } catch (e: Exception) {
                                            Toast.makeText(contexto, "Error al eliminar: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                }
                            ) {
                                Text("Eliminar", color = Color.Red)
                            }
                        },
                        dismissButton = {
                            TextButton(onClick = { mostrarConfirmacionEliminar = false }) {
                                Text("Cancelar")
                            }
                        }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaConfiguraciones() {
    val contexto = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(androidx.compose.foundation.rememberScrollState()) // Permite scrollear si la pantalla es chica
    ) {
        Text("Personalización", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)
        Spacer(modifier = Modifier.height(8.dp))

        var expandidoTema by remember { mutableStateOf(false) }
        ExposedDropdownMenuBox(expanded = expandidoTema, onExpandedChange = { expandidoTema = !expandidoTema }) {
            OutlinedTextField(
                value = when(temaAppGlobal) {
                    ModoTema.CLARO -> "Tema Claro"
                    ModoTema.OSCURO -> "Tema Oscuro"
                    ModoTema.AUTOMATICO -> "Automático (Sistema)"
                },
                onValueChange = {},
                readOnly = true,
                label = { Text("Modo de la Aplicación") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandidoTema) },
                modifier = Modifier.fillMaxWidth().menuAnchor()
            )
            ExposedDropdownMenu(expanded = expandidoTema, onDismissRequest = { expandidoTema = false }) {
                ModoTema.entries.forEach { modo ->
                    DropdownMenuItem(
                        text = {
                            Text(when(modo) {
                                ModoTema.CLARO -> "Tema Claro"
                                ModoTema.OSCURO -> "Tema Oscuro"
                                ModoTema.AUTOMATICO -> "Automático (Sistema)"
                            })
                        },
                        onClick = {
                            temaAppGlobal = modo
                            expandidoTema = false
                            PreferenciasApp(contexto).guardarString(PreferenciasApp.KEY_TEMA_APP, modo.name)
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
        HorizontalDivider()
        Spacer(modifier = Modifier.height(16.dp))

        Text("Ajustes de Reproducción", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)
        Spacer(modifier = Modifier.height(8.dp))

        // AJUSTE 1: RESOLUCIÓN POR DEFECTO DEL REPRODUCTOR
        var expandidoRes by remember { mutableStateOf(false) }
        ExposedDropdownMenuBox(expanded = expandidoRes, onExpandedChange = { expandidoRes = !expandidoRes }) {
            OutlinedTextField(
                value = resolucionReproduccionGlobal,
                onValueChange = {},
                readOnly = true,
                label = { Text("Resolución por defecto al Ver en Vivo") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandidoRes) },
                modifier = Modifier.fillMaxWidth().menuAnchor()
            )
            ExposedDropdownMenu(expanded = expandidoRes, onDismissRequest = { expandidoRes = false }) {
                listOf("360p", "480p", "720p").forEach { res ->
                    DropdownMenuItem(text = { Text(res) }, onClick = {
                        resolucionReproduccionGlobal = res
                        expandidoRes = false
                        PreferenciasApp(contexto).guardarString(PreferenciasApp.KEY_RESOLUCION_REPRO, res)
                    })
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // --- NUEVA OPCIÓN: ANDROID AUTO ---
        ListItem(
            headlineContent = { Text("Compatibilidad con Android Auto") },
            supportingContent = { Text("Permite controlar la música y ver la lista desde el coche.") },
            trailingContent = {
                Switch(
                    checked = androidAutoActivadoGlobal,
                    onCheckedChange = { nuevoValor ->
                        androidAutoActivadoGlobal = nuevoValor
                        PreferenciasApp(contexto).guardarBoolean(PreferenciasApp.KEY_ANDROID_AUTO, nuevoValor)
                        Toast.makeText(contexto, "Reinicia la app para aplicar cambios", Toast.LENGTH_SHORT).show()
                    }
                )
            },
            colors = ListItemDefaults.colors(containerColor = Color.Transparent)
        )

        Spacer(modifier = Modifier.height(24.dp))
        HorizontalDivider()
        Spacer(modifier = Modifier.height(16.dp))

        Text("Ajustes de Descarga de Audio (MP3)", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)
        Spacer(modifier = Modifier.height(8.dp))

        // AJUSTE 2: FORMATO DE AUDIO
        var expandidoFormatoAudio by remember { mutableStateOf(false) }
        ExposedDropdownMenuBox(expanded = expandidoFormatoAudio, onExpandedChange = { expandidoFormatoAudio = !expandidoFormatoAudio }) {
            OutlinedTextField(
                value = formatoAudioGlobal,
                onValueChange = {},
                readOnly = true,
                label = { Text("Formato de extracción") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandidoFormatoAudio) },
                modifier = Modifier.fillMaxWidth().menuAnchor()
            )
            ExposedDropdownMenu(expanded = expandidoFormatoAudio, onDismissRequest = { expandidoFormatoAudio = false }) {
                listOf("mp3", "m4a", "opus").forEach { formato ->
                    DropdownMenuItem(text = { Text(formato.uppercase()) }, onClick = { 
                        formatoAudioGlobal = formato 
                        expandidoFormatoAudio = false
                        PreferenciasApp(contexto).guardarString(PreferenciasApp.KEY_FORMATO_AUDIO, formato)
                    })
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // AJUSTE 3: CALIDAD DE AUDIO
        var expandidoCalidadAudio by remember { mutableStateOf(false) }
        ExposedDropdownMenuBox(expanded = expandidoCalidadAudio, onExpandedChange = { expandidoCalidadAudio = !expandidoCalidadAudio }) {
            OutlinedTextField(
                value = calidadAudioGlobal,
                onValueChange = {},
                readOnly = true,
                label = { Text("Calidad / Bitrate") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandidoCalidadAudio) },
                modifier = Modifier.fillMaxWidth().menuAnchor()
            )
            ExposedDropdownMenu(expanded = expandidoCalidadAudio, onDismissRequest = { expandidoCalidadAudio = false }) {
                listOf("128k", "192k", "320k").forEach { bits ->
                    DropdownMenuItem(text = { Text(bits) }, onClick = { 
                        calidadAudioGlobal = bits 
                        expandidoCalidadAudio = false
                        PreferenciasApp(contexto).guardarString(PreferenciasApp.KEY_CALIDAD_AUDIO, bits)
                    })
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // AJUSTE 4: RUTA DE ALMACENAMIENTO DE AUDIO
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = rutaGuardadoAudioGlobal,
                onValueChange = { 
                    rutaGuardadoAudioGlobal = it
                    PreferenciasApp(contexto).guardarString(PreferenciasApp.KEY_RUTA_AUDIO, it)
                },
                label = { Text("Carpeta de Audio") },
                modifier = Modifier.weight(1f),
                singleLine = true
            )
            Spacer(modifier = Modifier.width(8.dp))
            IconButton(onClick = { MainActivity.launcherCarpetaAudio?.launch(null) }) {
                Icon(Icons.Default.FolderOpen, contentDescription = "Buscar carpeta")
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
        HorizontalDivider()
        Spacer(modifier = Modifier.height(16.dp))

        Text("Ajustes de Descarga de Video (MP4)", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)
        Spacer(modifier = Modifier.height(8.dp))

        // AJUSTE 5: FORMATO DE VIDEO
        var expandidoFormatoVideo by remember { mutableStateOf(false) }
        ExposedDropdownMenuBox(expanded = expandidoFormatoVideo, onExpandedChange = { expandidoFormatoVideo = !expandidoFormatoVideo }) {
            OutlinedTextField(
                value = formatoVideoGlobal,
                onValueChange = {},
                readOnly = true,
                label = { Text("Contenedor de Video") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandidoFormatoVideo) },
                modifier = Modifier.fillMaxWidth().menuAnchor()
            )
            ExposedDropdownMenu(expanded = expandidoFormatoVideo, onDismissRequest = { expandidoFormatoVideo = false }) {
                listOf("mp4", "mkv", "webm").forEach { formato ->
                    DropdownMenuItem(text = { Text(formato.uppercase()) }, onClick = { 
                        formatoVideoGlobal = formato 
                        expandidoFormatoVideo = false
                        PreferenciasApp(contexto).guardarString(PreferenciasApp.KEY_FORMATO_VIDEO, formato)
                    })
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // AJUSTE 6: CALIDAD DE VIDEO DESCARGADO
        var expandidoCalidadVideo by remember { mutableStateOf(false) }
        ExposedDropdownMenuBox(expanded = expandidoCalidadVideo, onExpandedChange = { expandidoCalidadVideo = !expandidoCalidadVideo }) {
            OutlinedTextField(
                value = calidadVideoGlobal,
                onValueChange = {},
                readOnly = true,
                label = { Text("Calidad máxima de descarga") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandidoCalidadVideo) },
                modifier = Modifier.fillMaxWidth().menuAnchor()
            )
            ExposedDropdownMenu(expanded = expandidoCalidadVideo, onDismissRequest = { expandidoCalidadVideo = false }) {
                listOf("360p", "480p", "720p", "1080p").forEach { cal ->
                    DropdownMenuItem(text = { Text(cal) }, onClick = { 
                        calidadVideoGlobal = cal
                        expandidoCalidadVideo = false
                        PreferenciasApp(contexto).guardarString(PreferenciasApp.KEY_CALIDAD_VIDEO, cal)
                    })
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // AJUSTE 7: RUTA DE ALMACENAMIENTO DE VIDEO
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = rutaGuardadoVideoGlobal,
                onValueChange = { 
                    rutaGuardadoVideoGlobal = it
                    PreferenciasApp(contexto).guardarString(PreferenciasApp.KEY_RUTA_VIDEO, it)
                },
                label = { Text("Carpeta de Video") },
                modifier = Modifier.weight(1f),
                singleLine = true
            )
            Spacer(modifier = Modifier.width(8.dp))
            IconButton(onClick = { MainActivity.launcherCarpetaVideo?.launch(null) }) {
                Icon(Icons.Default.FolderOpen, contentDescription = "Buscar carpeta")
            }
        }
        Spacer(modifier = Modifier.height(24.dp))
        HorizontalDivider()
        Spacer(modifier = Modifier.height(16.dp))

        // --- NUEVA SECCIÓN: SOPORTE Y ACTUALIZACIONES ---
        Text("Soporte y Actualizaciones", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)
        Spacer(modifier = Modifier.height(12.dp))

        // BOTÓN C: ACTUALIZAR APP DESDE GIT
        var buscandoAppUpdate by remember { mutableStateOf(false) }
        var releaseInfo by remember { mutableStateOf<ActualizadorApp.ReleaseInfo?>(null) }
        var mostrarDialogoUpdate by remember { mutableStateOf(false) }

        Button(
            onClick = {
                buscandoAppUpdate = true
                ActualizadorApp.buscarActualizacion(contexto) { info ->
                    buscandoAppUpdate = false
                    if (info != null) {
                        releaseInfo = info
                        mostrarDialogoUpdate = true
                    } else {
                        (contexto as? Activity)?.runOnUiThread {
                            Toast.makeText(contexto, "Estás actualizado a la última versión", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            },
            enabled = !buscandoAppUpdate,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
        ) {
            if (buscandoAppUpdate) {
                CircularProgressIndicator(modifier = Modifier.size(20.dp), color = Color.White, strokeWidth = 2.dp)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Buscando...")
            } else {
                Text("Buscar Actualizaciones de FrogDownloader")
            }
        }

        if (mostrarDialogoUpdate && releaseInfo != null) {
            AlertDialog(
                onDismissRequest = { mostrarDialogoUpdate = false },
                title = { Text("¡Nueva Versión Disponible! (${releaseInfo!!.version})") },
                text = {
                    Column {
                        Text("Se ha encontrado una nueva actualización en GitHub.")
                        if (releaseInfo!!.body.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(releaseInfo!!.body, style = MaterialTheme.typography.bodySmall)
                        }
                    }
                },
                confirmButton = {
                    TextButton(onClick = {
                        mostrarDialogoUpdate = false
                        ActualizadorApp.descargarEInstalar(contexto, releaseInfo!!.downloadUrl)
                    }) {
                        Text("Actualizar Ahora")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { mostrarDialogoUpdate = false }) {
                        Text("Más tarde")
                    }
                }
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // BOTÓN A: ACTUALIZAR MOTOR DE DESCARGAS
        var textoBotonActualizar by remember { mutableStateOf("Actualizar Motor de Descargas") }
        var cargandoActualizacion by remember { mutableStateOf(false) }

        Button(
            onClick = {
                cargandoActualizacion = true
                textoBotonActualizar = "Buscando actualizaciones..."

                // Forzamos la actualización del binario en un hilo secundario
                thread {
                    try {
                        com.yausername.youtubedl_android.YoutubeDL.getInstance().updateYoutubeDL(
                            contexto,
                            com.yausername.youtubedl_android.YoutubeDL.UpdateChannel.STABLE
                        )
                        ContextCompat.getMainExecutor(contexto).execute {
                            textoBotonActualizar = "¡Motor al día!"
                            cargandoActualizacion = false
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                        ContextCompat.getMainExecutor(contexto).execute {
                            textoBotonActualizar = "Error al actualizar"
                            cargandoActualizacion = false
                        }
                    }
                }
            },
            enabled = !cargandoActualizacion,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(textoBotonActualizar)
        }

        Spacer(modifier = Modifier.height(8.dp))

        // BOTÓN B: DONAR (Abre tu link de PayPal / Ko-fi en el navegador)
        var mostrarTarjetaDonacion by remember { mutableStateOf(false) }

        OutlinedButton(
            onClick = { mostrarTarjetaDonacion = true },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("☕ Invita un cafe al desarrollador")
        }

        if (mostrarTarjetaDonacion) {
            Dialog(
                onDismissRequest = { mostrarTarjetaDonacion = false },
                properties = DialogProperties(usePlatformDefaultWidth = false)
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .padding(16.dp),
                    shape = MaterialTheme.shapes.extraLarge,
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column {
                        // 1. PORTADA
                        Box(modifier = Modifier.fillMaxWidth().height(150.dp)) {
                            Image(
                                painter = painterResource(id = contexto.resources.getIdentifier("fondo_app", "drawable", contexto.packageName)),
                                contentDescription = "Portada",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                            // 2. FOTO DE PERFIL (Superpuesta)
                            Box(
                                modifier = Modifier
                                    .align(Alignment.BottomCenter)
                                    .offset(y = 40.dp)
                                    .size(100.dp)
                                    .clip(CircleShape)
                                    .background(Color.White)
                                    .padding(4.dp)
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.avatar),
                                    contentDescription = "Foto de perfil",
                                    modifier = Modifier.fillMaxSize().clip(CircleShape),
                                    contentScale = ContentScale.Crop
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(50.dp))

                        // 3. INFORMACIÓN DEL PERFIL
                        Column(
                            modifier = Modifier.fillMaxWidth().padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "Alejandro Corona",
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )
                            Text(
                                text = "Desarrollador Senior C",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.Gray
                            )
                            Text(
                                text = "Tandem",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.Gray
                            )
                            
                            Spacer(modifier = Modifier.height(16.dp))
                            HorizontalDivider(modifier = Modifier.padding(horizontal = 32.dp))
                            Spacer(modifier = Modifier.height(16.dp))

                            Text(
                                text = "Si te gusta mi trabajo y quieres apoyar el desarrollo de FrogDownloader, ¡invítame un café!",
                                style = MaterialTheme.typography.bodyMedium,
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                                color = Color.DarkGray,
                                modifier = Modifier.padding(horizontal = 8.dp)
                            )

                            Spacer(modifier = Modifier.height(24.dp))

                            // 4. BOTÓN DE ACCIÓN (DONAR)
                            Button(
                                onClick = {
                                    val urlDonacion = "https://paypal.me/erickalejandro1567"
                                    val intentWeb = Intent(Intent.ACTION_VIEW, Uri.parse(urlDonacion))
                                    contexto.startActivity(intentWeb)
                                    mostrarTarjetaDonacion = false
                                },
                                modifier = Modifier.fillMaxWidth(),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1877F2)) // Azul Facebook
                            ) {
                                Text("Donar vía PayPal", color = Color.White, fontWeight = FontWeight.Bold)
                            }
                            
                            TextButton(onClick = { mostrarTarjetaDonacion = false }) {
                                Text("Cerrar", color = Color.Gray)
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))
        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
        Spacer(modifier = Modifier.height(16.dp))

        // ====================================================================
        // --- EASTER EGG: APARTADO PROFESIONAL DE VERSIÓN Y MENSAJE SECRETO ---
        // ====================================================================
        var contadorToques by remember { mutableStateOf(0) }
        var mostrarSorpresaRomantica by remember { mutableStateOf(false) }

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Texto principal con el número de versión solicitado
            Text(
                text = "FrogDownloader Pro",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
            )

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = "Versión 1.0.6",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
                modifier = Modifier
                    .clickable(
                        // Desactivamos el molesto efecto de parpadeo gris al tocar para que sea 100% secreto
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) {
                        contadorToques += 1
                        if (contadorToques == 6) {
                            mostrarSorpresaRomantica = true
                            contadorToques = 0 // Reiniciamos el contador por seguridad
                        }
                    }
                    .padding(8.dp)
            )
        }

        // DIÁLOGO EMERGENTE MATERIAL DESIGN 3 CON LA SORPRESA ROMÁNTICA
        if (mostrarSorpresaRomantica) {
            AlertDialog(
                onDismissRequest = { mostrarSorpresaRomantica = false },
                confirmButton = {
                    TextButton(onClick = { mostrarBottomSheetGlobal = false; mostrarSorpresaRomantica = false }) {
                        Text("Te amo ❤️", color = Color(0xFFE91E63))
                    }
                },
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("¡Cofre Secreto Activado! 🧸", style = MaterialTheme.typography.titleLarge)
                    }
                },
                text = {
                    Column {
                        Text(
                            text = "Hola mi amor, si estás leyendo esto es porque encontraste el secreto que escondí en el código para ti...",
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = Color(0xFFFCE4EC) // Fondo rosa pastel muy tierno
                            )
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text = "Gracias por ser mi mayor apoyo mientras programo y por inspirarme todos los días. Esta aplicación fue hecha con líneas de código, pero mi corazón está lleno de ti. ¡Eres mi persona favorita Mi Mary! 💖",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = Color(0xFF880E4F) // Texto vino oscuro para contrastar el rosa
                                )
                            }
                        }
                    }
                },
                containerColor = MaterialTheme.colorScheme.surface,
                shape = MaterialTheme.shapes.extraLarge
            )
        }
        // ====================================================================

        Spacer(modifier = Modifier.height(40.dp))
    }
}

fun iniciarDescarga(url: String, contexto: android.content.Context, descargarTodoElCanal: Boolean = false) {
    if (url.isEmpty()) return

    // Reiniciamos la bandera antes de empezar
    detenerDescargaMasivaGlobal = false

    thread {
        val idUnico = url.hashCode().toString()
        try {
            val carpetaPublica = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            val subCarpetaElegida = if (formatoSoloAudioGlobal) rutaGuardadoAudioGlobal else rutaGuardadoVideoGlobal
            val rutaDestino = File(carpetaPublica, subCarpetaElegida)
            if (!rutaDestino.exists()) rutaDestino.mkdirs()

            // ====================================================================
            // LÓGICA DE CANCELACIÓN EN CASO DE PLAYLIST MASIVA
            // ====================================================================
            if (descargarTodoElCanal && (url.contains("playlist") || url.contains("&list="))) {
                // 1. Obtenemos rápido la lista plana de IDs de la playlist usando execute (VideoInfo no tiene 'entries')
                val reqPlaylist = YoutubeDLRequest(url)
                // --- SOLUCIÓN ERROR ATTESTATION (adaq) ---
                reqPlaylist.addOption("--extractor-args", "youtube:player_client=android,mweb,ios,web;player_skip=webpage,configs")
                // ------------------------------------------
                reqPlaylist.addOption("--flat-playlist")
                reqPlaylist.addOption("--yes-playlist")
                reqPlaylist.addOption("--dump-single-json")
                
                val response = YoutubeDL.getInstance().execute(reqPlaylist)
                val json = JSONObject(response.out)
                val entriesJson = json.optJSONArray("entries")
                val totalVideos = entriesJson?.length() ?: 0

                // Registrar la descarga masiva en la interfaz de "En Curso"
                val nuevaDescargaMasiva = DescargaInfo(idUnico, "Lista ($totalVideos elementos)", 0f, formatoSoloAudioGlobal, false)
                androidx.core.content.ContextCompat.getMainExecutor(contexto).execute {
                    listaDescargasActivas.add(nuevaDescargaMasiva)
                }

                // 2. Descargamos video por video de la playlist de forma controlada
                if (entriesJson != null) {
                    for (index in 0 until entriesJson.length()) {
                        // ¡FRENO DE EMERGENCIA! Si el usuario presionó el botón de Stop, rompemos el bucle
                        if (detenerDescargaMasivaGlobal) {
                            println("YOUTUBEDL: Descarga masiva cancelada por el usuario.")
                            return@thread
                        }

                        val video = entriesJson.getJSONObject(index)
                        val idVideo = video.optString("id", video.optString("url", video.optString("webpage_url", "")))
                        
                        if (idVideo.isNotEmpty()) {
                            try {
                                val urlIndividual = if (idVideo.contains("http")) idVideo else "https://www.youtube.com/watch?v=$idVideo"
                                val requestIndividual = YoutubeDLRequest(urlIndividual)
                                // --- SOLUCIÓN ERROR ATTESTATION (adaq) ---
                                requestIndividual.addOption("--extractor-args", "youtube:player_client=android,mweb,ios,web;player_skip=webpage,configs")
                                // ------------------------------------------

                                // Configuramos formato individual
                                requestIndividual.addOption("--no-mtime")

                                if (formatoSoloAudioGlobal) {
                                    requestIndividual.addOption("-f", "bestaudio/best")
                                    requestIndividual.addOption("--extract-audio")
                                    requestIndividual.addOption("--audio-format", formatoAudioGlobal)
                                    requestIndividual.addOption("--audio-quality", calidadAudioGlobal.replace("k", ""))
                                    requestIndividual.addOption("--no-check-certificates")
                                    // Dejamos que youtube-dl ponga la extensión correcta según el formato solicitado
                                    requestIndividual.addOption("-o", "${rutaDestino.absolutePath}/%(title)s.%(ext)s")
                                } else {
                                    val alturaVideo = calidadVideoGlobal.replace("p", "")
                                    requestIndividual.addOption("-f", "bestvideo[height<=$alturaVideo]+bestaudio/best[height<=$alturaVideo]/best")
                                    requestIndividual.addOption("--merge-output-format", formatoVideoGlobal)
                                    requestIndividual.addOption("--no-check-certificates")
                                    requestIndividual.addOption("-o", "${rutaDestino.absolutePath}/%(title)s.%(ext)s")
                                }

                                // Ejecutamos la descarga del video actual y calculamos el progreso global de la playlist
                                YoutubeDL.getInstance().execute(requestIndividual) { progresoVideo, _, _ ->
                                    val progresoGlobal = ((index.toFloat() / totalVideos.toFloat()) * 100f) + (progresoVideo / totalVideos.toFloat())
                                    val idx = listaDescargasActivas.indexOfFirst { it.id == idUnico }
                                    if (idx != -1) {
                                        listaDescargasActivas[idx] = listaDescargasActivas[idx].copy(progreso = progresoGlobal)
                                    }
                                }
                            } catch (e: Exception) {
                                android.util.Log.e("FROG_DOWNLOAD", "Error en video de playlist: ${e.message}")
                            }
                        }
                    }
                }
            } else {
                // ====================================================================
                // DESCARGA TRADICIONAL DE UN SOLO VIDEO INDIVIDUAL
                // ====================================================================
                val requestInfo = YoutubeDLRequest(url)
                // --- SOLUCIÓN ERROR ATTESTATION (adaq) ---
                requestInfo.addOption("--extractor-args", "youtube:player_client=android,mweb,ios,web;player_skip=webpage,configs")
                // ------------------------------------------
                val streamInfo = YoutubeDL.getInstance().getInfo(requestInfo)

                val tituloVideo = streamInfo.title ?: "Descarga"
                val requestUnico = YoutubeDLRequest(url)
                requestUnico.addOption("--no-mtime")
                // --- SOLUCIÓN ERROR ATTESTATION (adaq) ---
                requestUnico.addOption("--extractor-args", "youtube:player_client=android,mweb,ios,web;player_skip=webpage,configs")
                // ------------------------------------------
                requestUnico.addOption("--no-playlist")

                if (formatoSoloAudioGlobal) {
                    requestUnico.addOption("-f", "bestaudio/best")
                    requestUnico.addOption("--extract-audio")
                    requestUnico.addOption("--audio-format", formatoAudioGlobal)
                    requestUnico.addOption("--audio-quality", calidadAudioGlobal.replace("k", ""))
                    requestUnico.addOption("-o", "${rutaDestino.absolutePath}/%(title)s.%(ext)s")
                } else {
                    val alturaVideo = calidadVideoGlobal.replace("p", "")
                    requestUnico.addOption("-f", "bestvideo[height<=$alturaVideo]+bestaudio/best[height<=$alturaVideo]/best")
                    requestUnico.addOption("--merge-output-format", formatoVideoGlobal)
                    requestUnico.addOption("-o", "${rutaDestino.absolutePath}/%(title)s.%(ext)s")
                }

                val nuevaDescarga = DescargaInfo(idUnico, tituloVideo, 0f, formatoSoloAudioGlobal, false)
                androidx.core.content.ContextCompat.getMainExecutor(contexto).execute {
                    listaDescargasActivas.add(nuevaDescarga)
                }

                YoutubeDL.getInstance().execute(requestUnico) { progreso, _, _ ->
                    val index = listaDescargasActivas.indexOfFirst { it.id == idUnico }
                    if (index != -1) {
                        listaDescargasActivas[index] = listaDescargasActivas[index].copy(progreso = progreso)
                    }
                }
            }

            // --- CIERRE EXITOSO Y LIMPIEZA DE LA INTERFAZ ---
            androidx.core.content.ContextCompat.getMainExecutor(contexto).execute {
                val indexFin = listaDescargasActivas.indexOfFirst { it.id == idUnico }
                if (indexFin != -1) {
                    listaDescargasActivas.removeAt(indexFin)
                }
            }

            // Notificación de éxito
            val builder = androidx.core.app.NotificationCompat.Builder(contexto, "descargas_channel")
                .setSmallIcon(android.R.drawable.stat_sys_download_done)
                .setContentTitle("¡Descarga Finalizada!")
                .setContentText("Tus archivos se guardaron correctamente.")
                .setPriority(androidx.core.app.NotificationCompat.PRIORITY_DEFAULT)
            val notificationManager = contexto.getSystemService(android.content.Context.NOTIFICATION_SERVICE) as android.app.NotificationManager
            notificationManager.notify(idUnico.hashCode(), builder.build())

        } catch (e: Exception) {
            e.printStackTrace()
            android.util.Log.e("FROG_DOWNLOAD", "Error descargando: ${e.message}", e)

            // Capturamos el mensaje de error para entender qué pasó
            val mensajeError = e.localizedMessage ?: "Error desconocido en la red"

            androidx.core.content.ContextCompat.getMainExecutor(contexto).execute {
                // 1. Quitamos la barra de carga trabada de la pantalla
                val indexError = listaDescargasActivas.indexOfFirst { it.id == idUnico }
                if (indexError != -1) {
                    listaDescargasActivas.removeAt(indexError)
                }

                // 2. MOSTRAMOS EL AVISO EN PANTALLA AL USUARIO (Toast)
                Toast.makeText(
                    contexto,
                    "❌ Falló la descarga: $mensajeError",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
}
