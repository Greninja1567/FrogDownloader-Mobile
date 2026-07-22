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
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
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
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.Fullscreen
import androidx.compose.material.icons.filled.FullscreenExit
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
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
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.runtime.*
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
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds
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

// --- COLORES TEMA GLASS iOS ---
val ColorGlassVerdePrimario = Color(0xFF2ECC71) // Verde esmeralda iOS
val ColorGlassBlancoTransp = Color(0x66FFFFFF)  // Blanco con 40% opacidad (más glass)
val ColorGlassVerdeTransp = Color(0x80E8F5E9)   // Verde muy claro con 50% opacidad
val ColorGlassBorde = Color(0x80FFFFFF)        // Borde blanco más nítido

// --- EXTENSIÓN PARA GLASSMORFISMO ---
fun Modifier.glass(
    enabled: Boolean,
    shape: androidx.compose.ui.graphics.Shape = androidx.compose.foundation.shape.RoundedCornerShape(32.dp),
    opacity: Float = 0.15f,
    esOscuro: Boolean = false
): Modifier = if (enabled) {
    val colorBase = if (esOscuro) Color.Black else Color.White
    val colorBorde = if (esOscuro) Color.White.copy(alpha = 0.15f) else Color.White.copy(alpha = 0.3f)
    this.then(
        Modifier
            .clip(shape)
            .background(colorBase.copy(alpha = opacity))
            .border(0.5.dp, colorBorde, shape)
    )
} else {
    this
}

// Función auxiliar para formatear milisegundos a MM:SS
fun formatTime(ms: Long): String {
    val totalSeconds = (ms / 1000).coerceAtLeast(0)
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    return "%02d:%02d".format(minutes, seconds)
}

enum class ModoTema { CLARO, OSCURO, AUTOMATICO, CRISTAL_IOS }
var temaAppGlobal by mutableStateOf(ModoTema.AUTOMATICO)
var appEnPrimerPlanoGlobal by mutableStateOf(true)

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
var cargandoPlaylistGlobal by mutableStateOf(false)

@Composable
fun esModoOscuroActivo(): Boolean {
    return when (temaAppGlobal) {
        ModoTema.CLARO -> false
        ModoTema.OSCURO -> true
        ModoTema.AUTOMATICO -> isSystemInDarkTheme()
        ModoTema.CRISTAL_IOS -> isSystemInDarkTheme()
    }
}

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

val EsquemaColoresGlass = lightColorScheme(
    primary = ColorGlassVerdePrimario,
    secondary = ColorGlassVerdePrimario.copy(alpha = 0.8f),
    background = Color.Transparent,
    surface = Color.White.copy(alpha = 0.3f),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = Color.White,
    onSurface = Color.White,
    onSurfaceVariant = Color.White.copy(alpha = 0.7f),
    primaryContainer = ColorGlassVerdePrimario.copy(alpha = 0.3f),
    onPrimaryContainer = Color.White,
    secondaryContainer = Color.White.copy(alpha = 0.2f),
    onSecondaryContainer = Color.White,
    surfaceVariant = Color.White.copy(alpha = 0.15f),
    outline = Color.White.copy(alpha = 0.5f),
    outlineVariant = Color.White.copy(alpha = 0.2f)
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

@Composable
fun LinearWavyProgressIndicator(
    progress: Float,
    estaActivo: Boolean = true,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.primary,
    trackColor: Color = MaterialTheme.colorScheme.surfaceVariant
) {
    val infiniteTransition = rememberInfiniteTransition(label = "wavy")
    val phase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 2f * Math.PI.toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "phase"
    )

    val amplitudAnimada by animateFloatAsState(
        targetValue = if (estaActivo) 4f else 0f,
        animationSpec = tween(500),
        label = "amplitude"
    )

    Canvas(modifier = modifier.height(12.dp)) {
        val width = size.width
        val height = size.height
        val centerY = height / 2
        val amplitude = amplitudAnimada.dp.toPx()
        val wavelength = 25.dp.toPx()
        
        // Dibuja el fondo (track)
        drawLine(
            color = trackColor,
            start = Offset(0f, centerY),
            end = Offset(width, centerY),
            strokeWidth = 3.dp.toPx(),
            cap = StrokeCap.Round
        )

        val path = Path()
        val segments = 80
        val activeWidth = width * progress
        
        if (activeWidth > 0f) {
            for (i in 0..segments) {
                val x = (i.toFloat() / segments) * activeWidth
                val y = centerY + amplitude * kotlin.math.sin((x / wavelength) * 2 * Math.PI.toFloat() - phase)
                if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)
            }

            drawPath(
                path = path,
                color = color,
                style = Stroke(width = 4.dp.toPx(), cap = StrokeCap.Round, join = StrokeJoin.Round)
            )
        }
    }
}

enum class Pantallas(val ruta: String, val titulo: String, val icono: ImageVector) {
    Video("video", "Stream", Icons.Default.VideoLibrary),
    Descargas("descargas", "Descargas", Icons.Default.Download),
    Configuraciones("config", "Ajustes", Icons.Default.Settings)
}

// --- VARIABLE GLOBAL PARA HACER QUE EL BOTÓN FLOTANTE SEPA QUÉ LINK DESCARGAR ---
var urlActualParaDescargarGlobal by mutableStateOf("")

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

    val esOscuro = esModoOscuroActivo()

    val colorSchemeActual = when (temaAppGlobal) {
        ModoTema.CRISTAL_IOS -> EsquemaColoresGlass
        else -> if (esOscuro) EsquemaColoresOscuro else EsquemaColoresVerde
    }

    // 1. Aplicamos nuestra paleta de colores personalizada a toda la app
    MaterialTheme(colorScheme = colorSchemeActual) {

        // 2. Usamos un Box principal para colocar la imagen de fondo en una capa trasera
        Box(modifier = Modifier.fillMaxSize()) {
            Image(
                // REEMPLAZA "fondo_drive" por el nombre exacto que le pusiste a tu imagen en drawable
                painter = painterResource(id = contexto.resources.getIdentifier("fondo_app", "drawable", contexto.packageName)),
                contentDescription = "Fondo de pantalla",
                modifier = Modifier
                    .fillMaxSize()
                    .then(
                        if (temaAppGlobal == ModoTema.CRISTAL_IOS && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                            Modifier.blur(20.dp)
                        } else {
                            Modifier
                        }
                    ),
                contentScale = ContentScale.Crop, // Fuerza a la imagen a cubrir toda la pantalla sin deformarse
                colorFilter = if (esOscuro) ColorFilter.tint(Color.Black.copy(alpha = 0.75f), BlendMode.Darken) else null
            )

            // 3. El contenedor visual flota de forma transparente sobre la imagen
            Row(modifier = Modifier.fillMaxSize()) {
                if (esTablet) {
                    // BARRA LATERAL PARA TABLETS
                    NavigationRail(
                        containerColor = if (temaAppGlobal == ModoTema.CRISTAL_IOS) Color.White.copy(alpha = 0.2f) else MaterialTheme.colorScheme.surface.copy(alpha = 0.9f),
                        modifier = if (temaAppGlobal == ModoTema.CRISTAL_IOS) Modifier.padding(8.dp).glass(true, esOscuro = esOscuro) else Modifier,
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
                                    selectedIconColor = if (temaAppGlobal == ModoTema.CRISTAL_IOS) Color.White else MaterialTheme.colorScheme.primary,
                                    unselectedIconColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                                    indicatorColor = if (temaAppGlobal == ModoTema.CRISTAL_IOS) ColorGlassVerdePrimario.copy(alpha = 0.5f) else MaterialTheme.colorScheme.secondary
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
                    modifier = Modifier.fillMaxSize(),
                    containerColor = Color.Transparent, // Obliga al Scaffold a ser transparente
                    bottomBar = {
                        if (!esTablet) {
                            NavigationBar(
                                // Barra inferior ligeramente translúcida para combinar con el fondo
                                containerColor = if (temaAppGlobal == ModoTema.CRISTAL_IOS) Color.White.copy(alpha = 0.05f) else MaterialTheme.colorScheme.surface.copy(alpha = 0.9f),
                                modifier = if (temaAppGlobal == ModoTema.CRISTAL_IOS) Modifier.padding(horizontal = 24.dp, vertical = 12.dp).glass(true, shape = CircleShape, opacity = 0.1f, esOscuro = esOscuro) else Modifier
                            ) {
                                Pantallas.entries.forEach { pantalla ->
                                    val seleccionada = pantallaActual == pantalla
                                    NavigationBarItem(
                                        icon = { Icon(pantalla.icono, contentDescription = pantalla.titulo, modifier = Modifier.size(28.dp)) },
                                        label = { Text(pantalla.titulo, fontWeight = if (seleccionada) FontWeight.Bold else FontWeight.Normal) },
                                        selected = seleccionada,
                                        colors = NavigationBarItemDefaults.colors(
                                            selectedIconColor = if (temaAppGlobal == ModoTema.CRISTAL_IOS) ColorGlassVerdePrimario else MaterialTheme.colorScheme.primary,
                                            unselectedIconColor = if (temaAppGlobal == ModoTema.CRISTAL_IOS) Color.White.copy(alpha = 0.7f) else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                                            indicatorColor = Color.Transparent,
                                            selectedTextColor = if (temaAppGlobal == ModoTema.CRISTAL_IOS) ColorGlassVerdePrimario else MaterialTheme.colorScheme.primary,
                                            unselectedTextColor = if (temaAppGlobal == ModoTema.CRISTAL_IOS) Color.White.copy(alpha = 0.7f) else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
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
                        val urlADescargar = if (urlActualParaDescargarGlobal.isNotEmpty()) urlActualParaDescargarGlobal else urlParaDescargarIndividualGlobal
                        
                        if (pantallaActual == Pantallas.Video && urlADescargar.isNotEmpty()) {
                            // BOTÓN FLOTANTE LIMÓN CON ÍCONO GRIS OSCURO
                            FloatingActionButton(
                                onClick = {
                                    esPlaylistDetectadaGlobal = urlADescargar.contains("playlist") || urlADescargar.contains("&list=")
                                    urlParaDescargarGlobal = urlADescargar
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
    fun procesarCarga(url: String, reemplazar: Boolean, contexto: Context) {
        val appContext = contexto.applicationContext
        cargandoPlaylistGlobal = true
        val playlistItems = videosPlaylistGlobal
        val exoPlayer = MainApp.exoPlayerGlobal ?: return

        // --- MANTENER PROCESO VIVO: INICIAR SERVICIO INMEDIATAMENTE ---
        try {
            val serviceIntent = Intent(appContext, PlaybackService::class.java)
            ContextCompat.startForegroundService(appContext, serviceIntent)
        } catch (e: Exception) { e.printStackTrace() }

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
                request.addOption("--no-check-formats")
                request.addOption("--playlist-items", "1-25") // Reducido a 25 para velocidad instantánea

                // Ejecución manual para obtener el JSON completo
                val response = YoutubeDL.getInstance().execute(request)
                val json = JSONObject(response.out)

                ContextCompat.getMainExecutor(appContext).execute {
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
                        cargarYReproducirIndice(0, appContext)
                    } else if (!reemplazar && indiceActualVideoGlobal == -1 && playlistItems.isNotEmpty()) {
                        cargarYReproducirIndice(indiceParaEmpezar, appContext)
                    }

                    cargandoPlaylistGlobal = false
                    PlaybackService.actualizarListaEnAuto()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                ContextCompat.getMainExecutor(appContext).execute { cargandoPlaylistGlobal = false }
            }
        }
    }

    fun cargarYReproducirIndice(index: Int, contexto: Context) {
        val playlistItems = videosPlaylistGlobal
        if (index < 0 || index >= playlistItems.size) return

        cargandoVideoGlobal = true
        indiceActualVideoGlobal = index
        val videoSeleccionado = playlistItems[index]

        tituloVideoActualGlobal = videoSeleccionado.titulo
        urlParaDescargarIndividualGlobal = videoSeleccionado.urlOriginalVideo.trim()
        // Sincronizamos también la global de descarga para que el botón aparezca
        urlActualParaDescargarGlobal = videoSeleccionado.urlOriginalVideo.trim()

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
                request.addOption("--socket-timeout", "5")
                
                // Forzamos la obtención directa del link sin procesar metadatos pesados
                request.addOption("--get-url")
                
                val alturaMapeada = resolucionReproduccionGlobal.replace("p", "")
                
                // --- OPTIMIZACIÓN TURBO: SI NO ESTAMOS EN APP, SOLO PEDIMOS AUDIO (MUCHO MÁS RÁPIDO) ---
                if (appEnPrimerPlanoGlobal) {
                    // PRIORIDAD: Opus 251 (Mejor audio) + Video en resolución elegida
                    request.addOption("-f", "bestvideo[height<=$alturaMapeada]+251/bestvideo[height<=$alturaMapeada]+bestaudio/best[height<=$alturaMapeada]/best")
                } else {
                    // SOLO AUDIO: Mucho más rápido de cargar y obtener link
                    request.addOption("-f", "251/bestaudio/best")
                }

                val response = YoutubeDL.getInstance().execute(request)
                val linkStreamingReal = response.out.split("\n").firstOrNull { it.startsWith("http") } ?: return@thread

                ContextCompat.getMainExecutor(contexto).execute {
                    // Asegurar que el servicio esté encendido para reproducción en background
                    try {
                        val serviceIntent = Intent(contexto.applicationContext, PlaybackService::class.java)
                        ContextCompat.startForegroundService(contexto.applicationContext, serviceIntent)
                    } catch (e: Exception) { e.printStackTrace() }

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
                        .setArtist("YouTube")
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
    val esOscuro = esModoOscuroActivo()
    val actividad = contexto as? Activity
    var urlTexto by remember { mutableStateOf("") }

    val playlistItems = videosPlaylistGlobal
    var esPantallaCompleta by remember { mutableStateOf(false) }

    // Estado local para pintar el título del video que está sonando actualmente
    val sheetState = rememberModalBottomSheetState()

    val exoPlayer = remember { MainApp.exoPlayerGlobal!! }

    var urlPendiente by remember { mutableStateOf("") }
    var mostrarDialogoOpcionesCola by remember { mutableStateOf(false) }

    // --- ESTADOS PARA CONTROLES PERSONALIZADOS ---
    var mostrarControles by remember { mutableStateOf(true) }
    var estaReproduciendo by remember { mutableStateOf(exoPlayer.isPlaying) }
    var posicionActual by remember { mutableLongStateOf(0L) }
    var duracionTotal by remember { mutableLongStateOf(0L) }

    LaunchedEffect(mostrarControles) {
        if (mostrarControles) {
            delay(3000.milliseconds)
            mostrarControles = false
        }
    }

    LaunchedEffect(exoPlayer) {
        while (true) {
            posicionActual = exoPlayer.currentPosition
            duracionTotal = exoPlayer.duration
            estaReproduciendo = exoPlayer.isPlaying
            delay(500)
        }
    }

    // --- FUNCIÓN PARA ACCIONAR LA BÚSQUEDA ---
    val accionarBusqueda = { url: String ->
        if (url.isNotEmpty()) {
            if (playlistItems.isNotEmpty()) {
                urlPendiente = url
                mostrarDialogoOpcionesCola = true
            } else {
                GestorReproduccion.procesarCarga(url, true, contexto)
            }
        }
    }

    LaunchedEffect(exoPlayer) {
        // Sincronización inmediata: si el reproductor ya está listo, quitamos la leyenda "Descifrando"
        if (exoPlayer.playbackState == androidx.media3.common.Player.STATE_READY) {
            cargandoVideoGlobal = false
        }
        
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
        val esGlass = temaAppGlobal == ModoTema.CRISTAL_IOS
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) { mostrarControles = !mostrarControles },
            contentAlignment = Alignment.Center
        ) {
            AndroidView(
                factory = { ctx ->
                    PlayerView(ctx).apply {
                        player = exoPlayer
                        useController = false // Desactivamos controles nativos
                    }
                },
                modifier = Modifier.fillMaxSize()
            )

            // CAPA DE CONTROLES PERSONALIZADOS
            AnimatedVisibility(
                visible = mostrarControles,
                enter = fadeIn() + scaleIn(initialScale = 0.9f),
                exit = fadeOut() + scaleOut(targetScale = 0.9f)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.4f))
                ) {
                    // BOTONES CENTRALES
                    Row(
                        modifier = Modifier.align(Alignment.Center),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(32.dp)
                    ) {
                        IconButton(
                            onClick = { GestorReproduccion.cargarYReproducirIndice(indiceActualVideoGlobal - 1, contexto) },
                            enabled = indiceActualVideoGlobal > 0,
                            colors = IconButtonDefaults.iconButtonColors(contentColor = Color.White)
                        ) {
                            Icon(Icons.Default.SkipPrevious, contentDescription = "Anterior", modifier = Modifier.size(36.dp))
                        }

                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .clip(CircleShape)
                                .background(if (esGlass) ColorGlassVerdePrimario.copy(alpha = 0.8f) else MaterialTheme.colorScheme.primary)
                                .clickable {
                                    if (estaReproduciendo) exoPlayer.pause() else exoPlayer.play()
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            val escala by animateFloatAsState(if (estaReproduciendo) 1f else 1.2f, label = "playScale")
                            Icon(
                                imageVector = if (estaReproduciendo) Icons.Default.Pause else Icons.Default.PlayArrow,
                                contentDescription = "Play/Pause",
                                modifier = Modifier.size(40.dp).scale(escala),
                                tint = if (esGlass) Color.White else MaterialTheme.colorScheme.onPrimary
                            )
                        }

                        IconButton(
                            onClick = { GestorReproduccion.cargarYReproducirIndice(indiceActualVideoGlobal + 1, contexto) },
                            enabled = indiceActualVideoGlobal < videosPlaylistGlobal.size - 1,
                            colors = IconButtonDefaults.iconButtonColors(contentColor = Color.White)
                        ) {
                            Icon(Icons.Default.SkipNext, contentDescription = "Siguiente", modifier = Modifier.size(36.dp))
                        }
                    }

                    // BARRA INFERIOR (Ondulada y Tiempo)
                    Column(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        if (duracionTotal > 0L) {
                            LinearWavyProgressIndicator(
                                progress = (posicionActual.toFloat() / duracionTotal.toFloat()).coerceIn(0f, 1f),
                                estaActivo = estaReproduciendo,
                                modifier = Modifier.fillMaxWidth(),
                                color = if (esGlass) ColorGlassVerdePrimario else MaterialTheme.colorScheme.primary,
                                trackColor = Color.White.copy(alpha = 0.3f)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text(formatTime(posicionActual), color = Color.White, style = MaterialTheme.typography.labelSmall)
                                Text(formatTime(duracionTotal), color = Color.White, style = MaterialTheme.typography.labelSmall)
                            }
                        }
                    }

                    // BOTÓN FULLSCREEN
                    IconButton(
                        onClick = { esPantallaCompleta = !esPantallaCompleta },
                        modifier = Modifier.align(Alignment.TopEnd).padding(8.dp),
                        colors = IconButtonDefaults.iconButtonColors(contentColor = Color.White)
                    ) {
                        Icon(
                            imageVector = if (esPantallaCompleta) Icons.Default.FullscreenExit else Icons.Default.Fullscreen,
                            contentDescription = "Pantalla Completa"
                        )
                    }
                }
            }

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
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            // 1. DIBUJAMOS TU LOGO
                            Image(
                                painter = painterResource(id = R.drawable.icono),
                                contentDescription = "Logo de la app",
                                modifier = Modifier
                                    .size(45.dp)
                                    .padding(end = 8.dp)
                            )

                            // 2. TEXTO DEL NOMBRE DE TU APP
                            Text(
                                text = "FrogDownloader",
                                color = if (temaAppGlobal == ModoTema.CRISTAL_IOS) Color.White else MaterialTheme.colorScheme.onSurface,
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent
                    ),
                    modifier = if (temaAppGlobal == ModoTema.CRISTAL_IOS) Modifier.padding(top = 8.dp) else Modifier
                )
            }

            // CONTENIDO ADAPTATIVO
            if (esTablet) {
                Row(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                    // Columna Izquierda: Input y Reproductor
                    Column(modifier = Modifier.weight(1.2f)) {
                        val colorTexto = if (temaAppGlobal == ModoTema.CRISTAL_IOS) Color.White else MaterialTheme.colorScheme.onSurface
                        
                        OutlinedTextField(
                            value = urlTexto,
                            onValueChange = {
                                urlTexto = it
                                urlActualParaDescargarGlobal = it
                            },
                            placeholder = { Text("Pegar Link de YouTube", color = colorTexto.copy(alpha = 0.6f)) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .glass(temaAppGlobal == ModoTema.CRISTAL_IOS, shape = CircleShape, opacity = 0.1f, esOscuro = esOscuro),
                            singleLine = true,
                            shape = CircleShape,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = if (temaAppGlobal == ModoTema.CRISTAL_IOS) Color.White.copy(alpha = 0.5f) else MaterialTheme.colorScheme.primary,
                                unfocusedBorderColor = if (temaAppGlobal == ModoTema.CRISTAL_IOS) Color.White.copy(alpha = 0.3f) else MaterialTheme.colorScheme.outline,
                                focusedTextColor = colorTexto,
                                unfocusedTextColor = colorTexto,
                                cursorColor = colorTexto
                            ),
                            trailingIcon = {
                                IconButton(onClick = { accionarBusqueda(urlTexto) }) {
                                    Icon(Icons.Default.ArrowForward, contentDescription = "Ver", tint = colorTexto)
                                }
                            }
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(350.dp)
                                .glass(temaAppGlobal == ModoTema.CRISTAL_IOS, shape = MaterialTheme.shapes.extraLarge, esOscuro = esOscuro),
                            shape = if (temaAppGlobal == ModoTema.CRISTAL_IOS) MaterialTheme.shapes.extraLarge else MaterialTheme.shapes.medium
                        ) { componenteReproductor() }
                        Spacer(modifier = Modifier.height(16.dp))
                        InfoVideoActual()
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    // Columna Derecha: Playlist
                    Column(modifier = Modifier.weight(0.8f)) {
                        ControlesPlaylist(contexto)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Lista de Reproducción", style = MaterialTheme.typography.titleMedium)
                        ListaVideosPlaylist(cargandoPlaylistGlobal, playlistItems, contexto)
                    }
                }
            } else {
                // DISEÑO MÓVIL ORIGINAL
                Column(modifier = Modifier.padding(16.dp)) {
                    val colorTexto = if (temaAppGlobal == ModoTema.CRISTAL_IOS) Color.White else MaterialTheme.colorScheme.onSurface
                    
                    OutlinedTextField(
                        value = urlTexto,
                        onValueChange = {
                            urlTexto = it
                            urlActualParaDescargarGlobal = it
                        },
                        placeholder = { Text("Pegar Link de YouTube", color = colorTexto.copy(alpha = 0.6f)) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .glass(temaAppGlobal == ModoTema.CRISTAL_IOS, shape = CircleShape, opacity = 0.1f, esOscuro = esOscuro),
                        singleLine = true,
                        shape = CircleShape,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = if (temaAppGlobal == ModoTema.CRISTAL_IOS) Color.White.copy(alpha = 0.5f) else MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = if (temaAppGlobal == ModoTema.CRISTAL_IOS) Color.White.copy(alpha = 0.3f) else MaterialTheme.colorScheme.outline,
                            focusedTextColor = colorTexto,
                            unfocusedTextColor = colorTexto,
                            cursorColor = colorTexto
                        ),
                        trailingIcon = {
                            IconButton(onClick = { accionarBusqueda(urlTexto) }) {
                                Icon(Icons.Default.ArrowForward, contentDescription = "Ver", tint = colorTexto)
                            }
                        }
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .glass(temaAppGlobal == ModoTema.CRISTAL_IOS, shape = MaterialTheme.shapes.extraLarge, esOscuro = esOscuro),
                        shape = if (temaAppGlobal == ModoTema.CRISTAL_IOS) MaterialTheme.shapes.extraLarge else MaterialTheme.shapes.medium
                    ) { componenteReproductor() }
                    Spacer(modifier = Modifier.height(16.dp))
                    InfoVideoActual()
                    Spacer(modifier = Modifier.height(20.dp))
                    ControlesPlaylist(contexto)
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Contenido de la Lista de Reproducción", 
                        style = MaterialTheme.typography.titleMedium,
                        color = if (temaAppGlobal == ModoTema.CRISTAL_IOS) Color.White else MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    ListaVideosPlaylist(cargandoPlaylistGlobal, playlistItems, contexto)
                }
            }
        }
    }
    if (mostrarDialogoOpcionesCola) {
        val colorTexto = if (temaAppGlobal == ModoTema.CRISTAL_IOS) Color.White else MaterialTheme.colorScheme.onSurface
        AlertDialog(
            onDismissRequest = { mostrarDialogoOpcionesCola = false },
            containerColor = if (temaAppGlobal == ModoTema.CRISTAL_IOS) (if (esOscuro) Color.Black.copy(alpha = 0.9f) else Color.White.copy(alpha = 0.9f)) else MaterialTheme.colorScheme.surface,
            modifier = if (temaAppGlobal == ModoTema.CRISTAL_IOS) Modifier.border(0.5.dp, Color.White.copy(alpha = 0.2f), MaterialTheme.shapes.extraLarge) else Modifier,
            title = { Text("Contenido Detectado", color = colorTexto) },
            text = { Text("¿Qué deseas hacer con el nuevo contenido?", color = colorTexto.copy(alpha = 0.8f)) },
            confirmButton = {
                TextButton(onClick = {
                    mostrarDialogoOpcionesCola = false
                    GestorReproduccion.procesarCarga(urlPendiente, true, contexto)
                }) {
                    Text("Reproducir Ahora", color = if (temaAppGlobal == ModoTema.CRISTAL_IOS) ColorGlassVerdePrimario else MaterialTheme.colorScheme.primary)
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    mostrarDialogoOpcionesCola = false
                    GestorReproduccion.procesarCarga(urlPendiente, false, contexto)
                }) {
                    Text("Agregar a la fila", color = if (temaAppGlobal == ModoTema.CRISTAL_IOS) Color.White.copy(alpha = 0.7f) else Color.Unspecified)
                }
            }
        )
    }

    if (mostrarBottomSheetGlobal) {
        ModalBottomSheet(
            onDismissRequest = { mostrarBottomSheetGlobal = false },
            sheetState = sheetState,
            containerColor = if (temaAppGlobal == ModoTema.CRISTAL_IOS) (if (esOscuro) Color.Black.copy(alpha = 0.95f) else Color.White.copy(alpha = 0.95f)) else MaterialTheme.colorScheme.surface,
            scrimColor = if (temaAppGlobal == ModoTema.CRISTAL_IOS) Color.Black.copy(alpha = 0.4f) else Color.Black.copy(alpha = 0.32f)
        ) {
            val colorTexto = if (temaAppGlobal == ModoTema.CRISTAL_IOS) Color.White else MaterialTheme.colorScheme.onSurface
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 32.dp, start = 24.dp, end = 24.dp, top = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Opciones de Descarga", style = MaterialTheme.typography.titleLarge, color = colorTexto)
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = if (esPlaylistDetectadaGlobal) "Se detectó una Lista de Reproducción activa" else "Enlace individual detectado",
                    style = MaterialTheme.typography.bodyMedium,
                    color = colorTexto.copy(alpha = 0.7f)
                )
                Spacer(modifier = Modifier.height(24.dp))

                // APARTADO 1: CHIPS SELECCIONABLES DE AUDIO O VIDEO
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    FilterChip(
                        selected = !formatoSoloAudioGlobal,
                        onClick = { formatoSoloAudioGlobal = false },
                        label = { Text("Video (${formatoVideoGlobal.uppercase()})") },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = if (temaAppGlobal == ModoTema.CRISTAL_IOS) ColorGlassVerdePrimario else MaterialTheme.colorScheme.primaryContainer,
                            selectedLabelColor = if (temaAppGlobal == ModoTema.CRISTAL_IOS) Color.White else MaterialTheme.colorScheme.onPrimaryContainer,
                            labelColor = colorTexto.copy(alpha = 0.7f)
                        ),
                        border = FilterChipDefaults.filterChipBorder(
                            enabled = true,
                            selected = !formatoSoloAudioGlobal,
                            borderColor = if (temaAppGlobal == ModoTema.CRISTAL_IOS) Color.White.copy(alpha = 0.2f) else MaterialTheme.colorScheme.outline,
                            selectedBorderColor = Color.Transparent
                        )
                    )
                    FilterChip(
                        selected = formatoSoloAudioGlobal,
                        onClick = { formatoSoloAudioGlobal = true },
                        label = { Text("Música (${formatoAudioGlobal.uppercase()})") },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = if (temaAppGlobal == ModoTema.CRISTAL_IOS) ColorGlassVerdePrimario else MaterialTheme.colorScheme.primaryContainer,
                            selectedLabelColor = if (temaAppGlobal == ModoTema.CRISTAL_IOS) Color.White else MaterialTheme.colorScheme.onPrimaryContainer,
                            labelColor = colorTexto.copy(alpha = 0.7f)
                        ),
                        border = FilterChipDefaults.filterChipBorder(
                            enabled = true,
                            selected = formatoSoloAudioGlobal,
                            borderColor = if (temaAppGlobal == ModoTema.CRISTAL_IOS) Color.White.copy(alpha = 0.2f) else MaterialTheme.colorScheme.outline,
                            selectedBorderColor = Color.Transparent
                        )
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))
                HorizontalDivider(color = colorTexto.copy(alpha = 0.1f))
                Spacer(modifier = Modifier.height(24.dp))

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
                        modifier = Modifier.fillMaxWidth().height(56.dp),
                        shape = CircleShape,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (temaAppGlobal == ModoTema.CRISTAL_IOS) ColorGlassVerdePrimario else MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Text("Descargar la canción actual ($calidadActualTexto)", fontWeight = FontWeight.Bold)
                    }
                } else {
                    // OPCIÓN C: Descarga tradicional si pegaste un video único desde el principio
                    Button(
                        onClick = {
                            mostrarBottomSheetGlobal = false
                            iniciarDescarga(urlParaDescargarGlobal, contexto, descargarTodoElCanal = false)
                        },
                        modifier = Modifier.fillMaxWidth().height(56.dp),
                        shape = CircleShape,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (temaAppGlobal == ModoTema.CRISTAL_IOS) ColorGlassVerdePrimario else MaterialTheme.colorScheme.primary
                        )
                    ) {
                        val formatoActualTexto = if (formatoSoloAudioGlobal) formatoAudioGlobal else formatoVideoGlobal
                        Text("Confirmar y Descargar en ${formatoActualTexto.uppercase()} ($calidadActualTexto)", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
fun InfoVideoActual() {
    val esGlass = temaAppGlobal == ModoTema.CRISTAL_IOS
    val esOscuro = esModoOscuroActivo()
    Card(
        modifier = Modifier.fillMaxWidth().glass(esGlass, shape = MaterialTheme.shapes.extraLarge, opacity = 0.1f, esOscuro = esOscuro),
        shape = MaterialTheme.shapes.extraLarge,
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        )
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text(
                text = "Reproduciendo ahora:",
                style = MaterialTheme.typography.labelMedium,
                color = if (esGlass) ColorGlassVerdePrimario else MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = tituloVideoActualGlobal, 
                style = MaterialTheme.typography.bodyLarge,
                color = if (esGlass) Color.White else MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
fun ControlesPlaylist(contexto: Context) {
    if (videosPlaylistGlobal.size > 1) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
            val colorTexto = if (temaAppGlobal == ModoTema.CRISTAL_IOS) Color.White else MaterialTheme.colorScheme.onSurface
            IconButton(
                onClick = { GestorReproduccion.cargarYReproducirIndice(indiceActualVideoGlobal - 1, contexto) }, 
                enabled = indiceActualVideoGlobal > 0,
                colors = IconButtonDefaults.iconButtonColors(contentColor = colorTexto)
            ) {
                Icon(Icons.Default.SkipPrevious, contentDescription = "Anterior", modifier = Modifier.size(32.dp))
            }
            Text(
                text = "Video ${indiceActualVideoGlobal + 1} de ${videosPlaylistGlobal.size}", 
                style = MaterialTheme.typography.bodyMedium, 
                modifier = Modifier.padding(horizontal = 16.dp),
                color = colorTexto
            )
            IconButton(
                onClick = { GestorReproduccion.cargarYReproducirIndice(indiceActualVideoGlobal + 1, contexto) }, 
                enabled = indiceActualVideoGlobal < videosPlaylistGlobal.size - 1,
                colors = IconButtonDefaults.iconButtonColors(contentColor = colorTexto)
            ) {
                Icon(Icons.Default.SkipNext, contentDescription = "Siguiente", modifier = Modifier.size(32.dp))
            }
        }
    }
}

@Composable
fun ListaVideosPlaylist(cargando: Boolean, items: List<VideoPlaylistItem>, contexto: Context) {
    Spacer(modifier = Modifier.height(4.dp))
    if (cargandoPlaylistGlobal) {
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
                val esGlass = temaAppGlobal == ModoTema.CRISTAL_IOS
                val esOscuro = esModoOscuroActivo()
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .glass(esGlass, shape = MaterialTheme.shapes.extraLarge, opacity = 0.08f, esOscuro = esOscuro),
                    shape = MaterialTheme.shapes.extraLarge,
                    colors = CardDefaults.cardColors(
                        containerColor = if (esElVideoActual) {
                            if (esGlass) ColorGlassVerdePrimario.copy(alpha = 0.2f) else MaterialTheme.colorScheme.primaryContainer
                        } else Color.Transparent
                    ),
                    onClick = { GestorReproduccion.cargarYReproducirIndice(index, contexto) }
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "${index + 1}.",
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(end = 8.dp),
                            color = if (esElVideoActual) {
                                if (esGlass) ColorGlassVerdePrimario else MaterialTheme.colorScheme.primary
                            } else {
                                if (esGlass) Color.White.copy(alpha = 0.7f) else Color.Unspecified
                            }
                        )
                        Text(
                            text = item.titulo,
                            style = MaterialTheme.typography.bodyMedium,
                            maxLines = 1,
                            color = if (esElVideoActual) {
                                if (esGlass) ColorGlassVerdePrimario else MaterialTheme.colorScheme.primary
                            } else {
                                if (esGlass) Color.White else Color.Unspecified
                            }
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
    val esOscuro = esModoOscuroActivo()
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
                val colorTexto = if (temaAppGlobal == ModoTema.CRISTAL_IOS) Color.White else MaterialTheme.colorScheme.onSurface
                Text(
                    "Administrar Archivos",
                    style = MaterialTheme.typography.headlineSmall,
                    color = colorTexto
                )
                IconButton(onClick = { refrescando++ }) {
                    Icon(Icons.Default.Refresh, contentDescription = "Refrescar lista", tint = colorTexto)
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
                val esGlass = temaAppGlobal == ModoTema.CRISTAL_IOS
                Card(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp).glass(esGlass, shape = MaterialTheme.shapes.extraLarge, esOscuro = esOscuro),
                    shape = if (esGlass) MaterialTheme.shapes.extraLarge else MaterialTheme.shapes.medium,
                    colors = CardDefaults.cardColors(
                        containerColor = if (esGlass) Color.Transparent else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                    )
                ) {
                    // Usamos una fila para poner el botón detener al lado de la barra
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
                    ) {
                        val colorTexto = if (temaAppGlobal == ModoTema.CRISTAL_IOS) Color.White else MaterialTheme.colorScheme.onSurface
                        Column(modifier = Modifier.weight(1f)) {
                            Text(descarga.titulo, style = MaterialTheme.typography.bodyLarge, maxLines = 1, color = colorTexto)
                            Spacer(modifier = Modifier.height(8.dp))

                            LinearWavyProgressIndicator(
                                progress = descarga.progreso / 100f,
                                modifier = Modifier.fillMaxWidth(),
                                color = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.height(4.dp))

                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text(
                                    text = if (descarga.esAudio) "Extrayendo Música..." else "Descargando Video...",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = colorTexto.copy(alpha = 0.7f)
                                )
                                Text("${descarga.progreso.toInt()}%", style = MaterialTheme.typography.labelMedium, color = colorTexto)
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
                val colorTexto = if (temaAppGlobal == ModoTema.CRISTAL_IOS) Color.White else MaterialTheme.colorScheme.onSurface
                Box(modifier = Modifier.fillMaxWidth().padding(32.dp), contentAlignment = Alignment.Center) {
                    Text("No se encontraron canciones en el directorio.", style = MaterialTheme.typography.bodyMedium, color = colorTexto.copy(alpha = 0.7f))
                }
            }
        } else {
            items(archivosLocalesHistoricos) { itemCancion ->
                var mostrarMenuContextual by remember { mutableStateOf(false) }
                var mostrarConfirmacionEliminar by remember { mutableStateOf(false) }
                val esGlass = temaAppGlobal == ModoTema.CRISTAL_IOS

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .glass(esGlass, shape = MaterialTheme.shapes.extraLarge, esOscuro = esOscuro)
                        .combinedClickable(
                            onLongClick = { mostrarMenuContextual = true },
                            onClick = { reproducirEnSistemaExterno(contexto, itemCancion.archivoReal) }
                        ),
                    shape = if (esGlass) MaterialTheme.shapes.extraLarge else MaterialTheme.shapes.medium,
                    colors = CardDefaults.cardColors(
                        containerColor = if (esGlass) Color.Transparent else MaterialTheme.colorScheme.surfaceVariant
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
                        val colorTexto = if (temaAppGlobal == ModoTema.CRISTAL_IOS) Color.White else MaterialTheme.colorScheme.onSurface
                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = itemCancion.nombreCompleto, style = MaterialTheme.typography.bodyLarge, maxLines = 1, color = colorTexto)
                            Spacer(modifier = Modifier.height(2.dp))
                            SuggestionChip(
                                onClick = { },
                                label = { Text(itemCancion.formato, color = if (temaAppGlobal == ModoTema.CRISTAL_IOS) Color.White else Color.Unspecified) },
                                modifier = Modifier.height(24.dp)
                            )
                        }

                        // MENÚ DESPLEGABLE (CONTEXTUAL)
                        androidx.compose.material3.DropdownMenu(
                            expanded = mostrarMenuContextual,
                            onDismissRequest = { mostrarMenuContextual = false },
                            modifier = if (temaAppGlobal == ModoTema.CRISTAL_IOS) Modifier.background(Color.Black.copy(alpha = 0.9f)).border(0.5.dp, Color.White.copy(alpha = 0.2f), MaterialTheme.shapes.medium) else Modifier
                        ) {
                            DropdownMenuItem(
                                text = { Text("Abrir", color = if (temaAppGlobal == ModoTema.CRISTAL_IOS) Color.White else Color.Unspecified) },
                                onClick = {
                                    mostrarMenuContextual = false
                                    reproducirEnSistemaExterno(contexto, itemCancion.archivoReal)
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Compartir", color = if (temaAppGlobal == ModoTema.CRISTAL_IOS) Color.White else Color.Unspecified) },
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
                    val colorTextoDialog = if (temaAppGlobal == ModoTema.CRISTAL_IOS) Color.White else MaterialTheme.colorScheme.onSurface
                    AlertDialog(
                        onDismissRequest = { mostrarConfirmacionEliminar = false },
                        containerColor = if (temaAppGlobal == ModoTema.CRISTAL_IOS) (if (esOscuro) Color.Black.copy(alpha = 0.9f) else Color.White.copy(alpha = 0.9f)) else MaterialTheme.colorScheme.surface,
                        modifier = if (temaAppGlobal == ModoTema.CRISTAL_IOS) Modifier.border(0.5.dp, Color.White.copy(alpha = 0.2f), MaterialTheme.shapes.extraLarge) else Modifier,
                        title = { Text("¿Eliminar archivo?", color = colorTextoDialog) },
                        text = { Text("Esta acción borrará '${itemCancion.nombreCompleto}' de forma permanente de tu dispositivo.", color = colorTextoDialog.copy(alpha = 0.8f)) },
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
                                Text("Cancelar", color = if (temaAppGlobal == ModoTema.CRISTAL_IOS) Color.White.copy(alpha = 0.7f) else Color.Unspecified)
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
    val esOscuro = esModoOscuroActivo()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(androidx.compose.foundation.rememberScrollState()) // Permite scrollear si la pantalla es chica
    ) {
        Text("Personalización", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)
        Spacer(modifier = Modifier.height(8.dp))

        var expandidoTema by remember { mutableStateOf(false) }
        val colorTexto = if (temaAppGlobal == ModoTema.CRISTAL_IOS) Color.White else MaterialTheme.colorScheme.onSurface
        
        ExposedDropdownMenuBox(expanded = expandidoTema, onExpandedChange = { expandidoTema = !expandidoTema }) {
            OutlinedTextField(
                value = when(temaAppGlobal) {
                    ModoTema.CLARO -> "Tema Claro"
                    ModoTema.OSCURO -> "Tema Oscuro"
                    ModoTema.AUTOMATICO -> "Automático (Sistema)"
                    ModoTema.CRISTAL_IOS -> "Cristal iOS (Estilo Glass)"
                },
                onValueChange = {},
                readOnly = true,
                placeholder = { Text("Modo de la Aplicación", color = colorTexto.copy(alpha = 0.6f)) },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandidoTema) },
                shape = CircleShape,
                modifier = Modifier.fillMaxWidth().menuAnchor().glass(temaAppGlobal == ModoTema.CRISTAL_IOS, shape = CircleShape, opacity = 0.1f, esOscuro = esOscuro),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = if (temaAppGlobal == ModoTema.CRISTAL_IOS) Color.White.copy(alpha = 0.5f) else MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = if (temaAppGlobal == ModoTema.CRISTAL_IOS) Color.White.copy(alpha = 0.3f) else MaterialTheme.colorScheme.outline,
                    focusedTextColor = colorTexto,
                    unfocusedTextColor = colorTexto,
                    cursorColor = colorTexto,
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent
                )
            )
            ExposedDropdownMenu(
                expanded = expandidoTema, 
                onDismissRequest = { expandidoTema = false },
                modifier = if (temaAppGlobal == ModoTema.CRISTAL_IOS) Modifier.background(Color.Black.copy(alpha = 0.9f)).border(0.5.dp, Color.White.copy(alpha = 0.2f), MaterialTheme.shapes.medium) else Modifier
            ) {
                ModoTema.entries.forEach { modo ->
                    val esSeleccionado = temaAppGlobal == modo
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = when(modo) {
                                    ModoTema.CLARO -> "Tema Claro"
                                    ModoTema.OSCURO -> "Tema Oscuro"
                                    ModoTema.AUTOMATICO -> "Automático (Sistema)"
                                    ModoTema.CRISTAL_IOS -> "Cristal iOS (Estilo Glass)"
                                },
                                color = if (esSeleccionado && temaAppGlobal == ModoTema.CRISTAL_IOS) ColorGlassVerdePrimario else colorTexto
                            )
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
                placeholder = { Text("Resolución por defecto al Ver en Vivo", color = colorTexto.copy(alpha = 0.6f)) },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandidoRes) },
                shape = CircleShape,
                modifier = Modifier.fillMaxWidth().menuAnchor().glass(temaAppGlobal == ModoTema.CRISTAL_IOS, shape = CircleShape, opacity = 0.1f, esOscuro = esOscuro),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = if (temaAppGlobal == ModoTema.CRISTAL_IOS) Color.White.copy(alpha = 0.5f) else MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = if (temaAppGlobal == ModoTema.CRISTAL_IOS) Color.White.copy(alpha = 0.3f) else MaterialTheme.colorScheme.outline,
                    focusedTextColor = colorTexto,
                    unfocusedTextColor = colorTexto,
                    cursorColor = colorTexto,
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent
                )
            )
            ExposedDropdownMenu(
                expanded = expandidoRes, 
                onDismissRequest = { expandidoRes = false },
                modifier = if (temaAppGlobal == ModoTema.CRISTAL_IOS) Modifier.background(Color.Black.copy(alpha = 0.9f)).border(0.5.dp, Color.White.copy(alpha = 0.2f), MaterialTheme.shapes.medium) else Modifier
            ) {
                listOf("360p", "480p", "720p").forEach { res ->
                    val esSeleccionado = resolucionReproduccionGlobal == res
                    DropdownMenuItem(
                        text = { Text(res, color = if (esSeleccionado && temaAppGlobal == ModoTema.CRISTAL_IOS) ColorGlassVerdePrimario else colorTexto) }, 
                        onClick = {
                            resolucionReproduccionGlobal = res
                            expandidoRes = false
                            PreferenciasApp(contexto).guardarString(PreferenciasApp.KEY_RESOLUCION_REPRO, res)
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // --- NUEVA OPCIÓN: ANDROID AUTO ---
        ListItem(
            headlineContent = { Text("Compatibilidad con Android Auto", color = colorTexto) },
            supportingContent = { Text("Permite controlar la música y ver la lista desde el coche.", color = colorTexto.copy(alpha = 0.7f)) },
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

        Spacer(modifier = Modifier.height(16.dp))

        // --- NUEVA SECCIÓN: AUDIO PREMIUM ---
        Text("Mejora de Sonido Premium", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)
        Spacer(modifier = Modifier.height(8.dp))

        val prefs = PreferenciasApp(contexto)
        var audioPremiumActivado by remember { mutableStateOf(prefs.obtenerBoolean(PreferenciasApp.KEY_AUDIO_PREMIUM, false)) }
        var nivelBajos by remember { mutableStateOf(prefs.obtenerString(PreferenciasApp.KEY_NIVEL_BAJOS, "Medio")) }

        ListItem(
            headlineContent = { Text("Sonido Hi-Fi y Loudness", color = colorTexto) },
            supportingContent = { Text("Aumenta la potencia y nitidez (Opus 251).", color = colorTexto.copy(alpha = 0.7f)) },
            trailingContent = {
                Switch(
                    checked = audioPremiumActivado,
                    onCheckedChange = { nuevoValor ->
                        audioPremiumActivado = nuevoValor
                        prefs.guardarBoolean(PreferenciasApp.KEY_AUDIO_PREMIUM, nuevoValor)
                        GestorAudio.actualizarEstado(nuevoValor, contexto)
                    }
                )
            },
            colors = ListItemDefaults.colors(containerColor = Color.Transparent)
        )

        var expandidoBajos by remember { mutableStateOf(false) }
        ExposedDropdownMenuBox(expanded = expandidoBajos, onExpandedChange = { expandidoBajos = !expandidoBajos }) {
            OutlinedTextField(
                value = "Refuerzo de Bajos: $nivelBajos",
                onValueChange = {},
                readOnly = true,
                placeholder = { Text("Potencia de Graves", color = colorTexto.copy(alpha = 0.6f)) },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandidoBajos) },
                shape = CircleShape,
                modifier = Modifier.fillMaxWidth().menuAnchor().glass(temaAppGlobal == ModoTema.CRISTAL_IOS, shape = CircleShape, opacity = 0.1f, esOscuro = esOscuro),
                enabled = audioPremiumActivado,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = if (temaAppGlobal == ModoTema.CRISTAL_IOS) Color.White.copy(alpha = 0.5f) else MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = if (temaAppGlobal == ModoTema.CRISTAL_IOS) Color.White.copy(alpha = 0.3f) else MaterialTheme.colorScheme.outline,
                    focusedTextColor = colorTexto,
                    unfocusedTextColor = colorTexto,
                    disabledTextColor = colorTexto.copy(alpha = 0.5f),
                    cursorColor = colorTexto,
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent
                )
            )
            ExposedDropdownMenu(
                expanded = expandidoBajos, 
                onDismissRequest = { expandidoBajos = false },
                modifier = if (temaAppGlobal == ModoTema.CRISTAL_IOS) Modifier.background(Color.Black.copy(alpha = 0.9f)).border(0.5.dp, Color.White.copy(alpha = 0.2f), MaterialTheme.shapes.medium) else Modifier
            ) {
                listOf("Bajo", "Medio", "Alto").forEach { nivel ->
                    val esSeleccionado = nivelBajos == nivel
                    DropdownMenuItem(
                        text = { Text(nivel, color = if (esSeleccionado && temaAppGlobal == ModoTema.CRISTAL_IOS) ColorGlassVerdePrimario else colorTexto) }, 
                        onClick = {
                            nivelBajos = nivel
                            expandidoBajos = false
                            prefs.guardarString(PreferenciasApp.KEY_NIVEL_BAJOS, nivel)
                            GestorAudio.actualizarEstado(audioPremiumActivado, contexto)
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
        HorizontalDivider()
        Spacer(modifier = Modifier.height(16.dp))

        Text("Ajustes de Descarga de Audio (MP3)", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)
        Spacer(modifier = Modifier.height(8.dp))

        // AJUSTE 2: FORMATO DE AUDIO
        var expandidoFormatoAudio by remember { mutableStateOf(false) }
        ExposedDropdownMenuBox(expanded = expandidoFormatoAudio, onExpandedChange = { expandidoFormatoAudio = !expandidoFormatoAudio }) {
            OutlinedTextField(
                value = formatoAudioGlobal.uppercase(),
                onValueChange = {},
                readOnly = true,
                placeholder = { Text("Formato de extracción", color = colorTexto.copy(alpha = 0.6f)) },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandidoFormatoAudio) },
                shape = CircleShape,
                modifier = Modifier.fillMaxWidth().menuAnchor().glass(temaAppGlobal == ModoTema.CRISTAL_IOS, shape = CircleShape, opacity = 0.1f, esOscuro = esOscuro),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = if (temaAppGlobal == ModoTema.CRISTAL_IOS) Color.White.copy(alpha = 0.5f) else MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = if (temaAppGlobal == ModoTema.CRISTAL_IOS) Color.White.copy(alpha = 0.3f) else MaterialTheme.colorScheme.outline,
                    focusedTextColor = colorTexto,
                    unfocusedTextColor = colorTexto,
                    cursorColor = colorTexto,
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent
                )
            )
            ExposedDropdownMenu(
                expanded = expandidoFormatoAudio, 
                onDismissRequest = { expandidoFormatoAudio = false },
                modifier = if (temaAppGlobal == ModoTema.CRISTAL_IOS) Modifier.background(Color.Black.copy(alpha = 0.9f)).border(0.5.dp, Color.White.copy(alpha = 0.2f), MaterialTheme.shapes.medium) else Modifier
            ) {
                listOf("mp3", "m4a", "opus").forEach { formato ->
                    val esSeleccionado = formatoAudioGlobal == formato
                    DropdownMenuItem(
                        text = { Text(formato.uppercase(), color = if (esSeleccionado && temaAppGlobal == ModoTema.CRISTAL_IOS) ColorGlassVerdePrimario else colorTexto) }, 
                        onClick = { 
                            formatoAudioGlobal = formato 
                            expandidoFormatoAudio = false
                            PreferenciasApp(contexto).guardarString(PreferenciasApp.KEY_FORMATO_AUDIO, formato)
                        }
                    )
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
                placeholder = { Text("Calidad / Bitrate", color = colorTexto.copy(alpha = 0.6f)) },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandidoCalidadAudio) },
                shape = CircleShape,
                modifier = Modifier.fillMaxWidth().menuAnchor().glass(temaAppGlobal == ModoTema.CRISTAL_IOS, shape = CircleShape, opacity = 0.1f, esOscuro = esOscuro),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = if (temaAppGlobal == ModoTema.CRISTAL_IOS) Color.White.copy(alpha = 0.5f) else MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = if (temaAppGlobal == ModoTema.CRISTAL_IOS) Color.White.copy(alpha = 0.3f) else MaterialTheme.colorScheme.outline,
                    focusedTextColor = colorTexto,
                    unfocusedTextColor = colorTexto,
                    cursorColor = colorTexto,
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent
                )
            )
            ExposedDropdownMenu(
                expanded = expandidoCalidadAudio, 
                onDismissRequest = { expandidoCalidadAudio = false },
                modifier = if (temaAppGlobal == ModoTema.CRISTAL_IOS) Modifier.background(Color.Black.copy(alpha = 0.9f)).border(0.5.dp, Color.White.copy(alpha = 0.2f), MaterialTheme.shapes.medium) else Modifier
            ) {
                listOf("128k", "192k", "320k").forEach { bits ->
                    val esSeleccionado = calidadAudioGlobal == bits
                    DropdownMenuItem(
                        text = { Text(bits, color = if (esSeleccionado && temaAppGlobal == ModoTema.CRISTAL_IOS) ColorGlassVerdePrimario else colorTexto) }, 
                        onClick = { 
                            calidadAudioGlobal = bits 
                            expandidoCalidadAudio = false
                            PreferenciasApp(contexto).guardarString(PreferenciasApp.KEY_CALIDAD_AUDIO, bits)
                        }
                    )
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
                placeholder = { Text("Carpeta de Audio", color = colorTexto.copy(alpha = 0.6f)) },
                shape = CircleShape,
                modifier = Modifier.weight(1f).glass(temaAppGlobal == ModoTema.CRISTAL_IOS, shape = CircleShape, opacity = 0.1f, esOscuro = esOscuro),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = if (temaAppGlobal == ModoTema.CRISTAL_IOS) Color.White.copy(alpha = 0.5f) else MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = if (temaAppGlobal == ModoTema.CRISTAL_IOS) Color.White.copy(alpha = 0.3f) else MaterialTheme.colorScheme.outline,
                    focusedTextColor = colorTexto,
                    unfocusedTextColor = colorTexto,
                    cursorColor = colorTexto,
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent
                )
            )
            Spacer(modifier = Modifier.width(8.dp))
            IconButton(onClick = { MainActivity.launcherCarpetaAudio?.launch(null) }) {
                Icon(Icons.Default.FolderOpen, contentDescription = "Buscar carpeta", tint = colorTexto)
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
                value = formatoVideoGlobal.uppercase(),
                onValueChange = {},
                readOnly = true,
                placeholder = { Text("Contenedor de Video", color = colorTexto.copy(alpha = 0.6f)) },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandidoFormatoVideo) },
                shape = CircleShape,
                modifier = Modifier.fillMaxWidth().menuAnchor().glass(temaAppGlobal == ModoTema.CRISTAL_IOS, shape = CircleShape, opacity = 0.1f, esOscuro = esOscuro),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = if (temaAppGlobal == ModoTema.CRISTAL_IOS) Color.White.copy(alpha = 0.5f) else MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = if (temaAppGlobal == ModoTema.CRISTAL_IOS) Color.White.copy(alpha = 0.3f) else MaterialTheme.colorScheme.outline,
                    focusedTextColor = colorTexto,
                    unfocusedTextColor = colorTexto,
                    cursorColor = colorTexto,
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent
                )
            )
            ExposedDropdownMenu(
                expanded = expandidoFormatoVideo, 
                onDismissRequest = { expandidoFormatoVideo = false },
                modifier = if (temaAppGlobal == ModoTema.CRISTAL_IOS) Modifier.background(Color.Black.copy(alpha = 0.9f)).border(0.5.dp, Color.White.copy(alpha = 0.2f), MaterialTheme.shapes.medium) else Modifier
            ) {
                listOf("mp4", "mkv", "webm").forEach { formato ->
                    val esSeleccionado = formatoVideoGlobal == formato
                    DropdownMenuItem(
                        text = { Text(formato.uppercase(), color = if (esSeleccionado && temaAppGlobal == ModoTema.CRISTAL_IOS) ColorGlassVerdePrimario else colorTexto) }, 
                        onClick = { 
                            formatoVideoGlobal = formato 
                            expandidoFormatoVideo = false
                            PreferenciasApp(contexto).guardarString(PreferenciasApp.KEY_FORMATO_VIDEO, formato)
                        }
                    )
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
                placeholder = { Text("Calidad máxima de descarga", color = colorTexto.copy(alpha = 0.6f)) },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandidoCalidadVideo) },
                shape = CircleShape,
                modifier = Modifier.fillMaxWidth().menuAnchor().glass(temaAppGlobal == ModoTema.CRISTAL_IOS, shape = CircleShape, opacity = 0.1f, esOscuro = esOscuro),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = if (temaAppGlobal == ModoTema.CRISTAL_IOS) Color.White.copy(alpha = 0.5f) else MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = if (temaAppGlobal == ModoTema.CRISTAL_IOS) Color.White.copy(alpha = 0.3f) else MaterialTheme.colorScheme.outline,
                    focusedTextColor = colorTexto,
                    unfocusedTextColor = colorTexto,
                    cursorColor = colorTexto,
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent
                )
            )
            ExposedDropdownMenu(
                expanded = expandidoCalidadVideo, 
                onDismissRequest = { expandidoCalidadVideo = false },
                modifier = if (temaAppGlobal == ModoTema.CRISTAL_IOS) Modifier.background(Color.Black.copy(alpha = 0.9f)).border(0.5.dp, Color.White.copy(alpha = 0.2f), MaterialTheme.shapes.medium) else Modifier
            ) {
                listOf("360p", "480p", "720p", "1080p").forEach { cal ->
                    val esSeleccionado = calidadVideoGlobal == cal
                    DropdownMenuItem(
                        text = { Text(cal, color = if (esSeleccionado && temaAppGlobal == ModoTema.CRISTAL_IOS) ColorGlassVerdePrimario else colorTexto) }, 
                        onClick = { 
                            calidadVideoGlobal = cal
                            expandidoCalidadVideo = false
                            PreferenciasApp(contexto).guardarString(PreferenciasApp.KEY_CALIDAD_VIDEO, cal)
                        }
                    )
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
                placeholder = { Text("Carpeta de Video", color = colorTexto.copy(alpha = 0.6f)) },
                shape = CircleShape,
                modifier = Modifier.weight(1f).glass(temaAppGlobal == ModoTema.CRISTAL_IOS, shape = CircleShape, opacity = 0.1f, esOscuro = esOscuro),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = if (temaAppGlobal == ModoTema.CRISTAL_IOS) Color.White.copy(alpha = 0.5f) else MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = if (temaAppGlobal == ModoTema.CRISTAL_IOS) Color.White.copy(alpha = 0.3f) else MaterialTheme.colorScheme.outline,
                    focusedTextColor = colorTexto,
                    unfocusedTextColor = colorTexto,
                    cursorColor = colorTexto,
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent
                )
            )
            Spacer(modifier = Modifier.width(8.dp))
            IconButton(onClick = { MainActivity.launcherCarpetaVideo?.launch(null) }) {
                Icon(Icons.Default.FolderOpen, contentDescription = "Buscar carpeta", tint = colorTexto)
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
            modifier = Modifier.fillMaxWidth().glass(temaAppGlobal == ModoTema.CRISTAL_IOS, shape = if (temaAppGlobal == ModoTema.CRISTAL_IOS) MaterialTheme.shapes.extraLarge else ButtonDefaults.shape, esOscuro = esOscuro),
            shape = if (temaAppGlobal == ModoTema.CRISTAL_IOS) MaterialTheme.shapes.extraLarge else ButtonDefaults.shape,
            colors = ButtonDefaults.buttonColors(containerColor = if (temaAppGlobal == ModoTema.CRISTAL_IOS) MaterialTheme.colorScheme.primary.copy(alpha = 0.7f) else MaterialTheme.colorScheme.primary)
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
            val colorTextoDialog = if (temaAppGlobal == ModoTema.CRISTAL_IOS) Color.White else MaterialTheme.colorScheme.onSurface
            AlertDialog(
                onDismissRequest = { mostrarDialogoUpdate = false },
                containerColor = if (temaAppGlobal == ModoTema.CRISTAL_IOS) (if (esOscuro) Color.Black.copy(alpha = 0.9f) else Color.White.copy(alpha = 0.9f)) else MaterialTheme.colorScheme.surface,
                modifier = if (temaAppGlobal == ModoTema.CRISTAL_IOS) Modifier.border(0.5.dp, Color.White.copy(alpha = 0.2f), MaterialTheme.shapes.extraLarge) else Modifier,
                title = { Text("¡Nueva Versión Disponible! (${releaseInfo!!.version})", color = colorTextoDialog) },
                text = {
                    Column {
                        Text("Se ha encontrado una nueva actualización en GitHub.", color = colorTextoDialog.copy(alpha = 0.8f))
                        if (releaseInfo!!.body.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(releaseInfo!!.body, style = MaterialTheme.typography.bodySmall, color = colorTextoDialog.copy(alpha = 0.6f))
                        }
                    }
                },
                confirmButton = {
                    TextButton(onClick = {
                        mostrarDialogoUpdate = false
                        ActualizadorApp.descargarEInstalar(contexto, releaseInfo!!.downloadUrl)
                    }) {
                        Text("Actualizar Ahora", color = if (temaAppGlobal == ModoTema.CRISTAL_IOS) ColorGlassVerdePrimario else MaterialTheme.colorScheme.primary)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { mostrarDialogoUpdate = false }) {
                        Text("Más tarde", color = colorTextoDialog.copy(alpha = 0.7f))
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
            modifier = Modifier.fillMaxWidth().glass(temaAppGlobal == ModoTema.CRISTAL_IOS, shape = if (temaAppGlobal == ModoTema.CRISTAL_IOS) MaterialTheme.shapes.extraLarge else ButtonDefaults.shape, esOscuro = esOscuro),
            shape = if (temaAppGlobal == ModoTema.CRISTAL_IOS) MaterialTheme.shapes.extraLarge else ButtonDefaults.shape,
            colors = ButtonDefaults.buttonColors(containerColor = if (temaAppGlobal == ModoTema.CRISTAL_IOS) MaterialTheme.colorScheme.primary.copy(alpha = 0.7f) else MaterialTheme.colorScheme.primary)
        ) {
            Text(textoBotonActualizar)
        }

        Spacer(modifier = Modifier.height(8.dp))

        // BOTÓN B: DONAR (Abre tu link de PayPal / Ko-fi en el navegador)
        var mostrarTarjetaDonacion by remember { mutableStateOf(false) }

        OutlinedButton(
            onClick = { mostrarTarjetaDonacion = true },
            modifier = Modifier.fillMaxWidth().glass(temaAppGlobal == ModoTema.CRISTAL_IOS, shape = if (temaAppGlobal == ModoTema.CRISTAL_IOS) MaterialTheme.shapes.extraLarge else ButtonDefaults.outlinedShape, esOscuro = esOscuro),
            shape = if (temaAppGlobal == ModoTema.CRISTAL_IOS) MaterialTheme.shapes.extraLarge else ButtonDefaults.outlinedShape
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
                        .padding(16.dp)
                        .glass(temaAppGlobal == ModoTema.CRISTAL_IOS, shape = MaterialTheme.shapes.extraLarge, esOscuro = esOscuro),
                    shape = MaterialTheme.shapes.extraLarge,
                    colors = CardDefaults.cardColors(containerColor = if (temaAppGlobal == ModoTema.CRISTAL_IOS) Color.Transparent else Color.White)
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
                        val colorTextoDialog = if (temaAppGlobal == ModoTema.CRISTAL_IOS) Color.Black else Color.Black // In a dialog with white background, black text is best. But the user said "gris oscuro se vuelvan gris claro". 
                        // Wait, the donation dialog card has containerColor = Color.White. 
                        // If the user wants ALL texts light, maybe I should make the card glassy too?
                        // "quiero que los textos que aun estan en gris oscuro se vuelvan gris claro y sean legibles"
                        // On a white background (CardDefaults.cardColors(containerColor = Color.White)), light grey is NOT legible.
                        // I will make the donation card Glassy if the theme is Glass.
                        
                        Column(
                            modifier = Modifier.fillMaxWidth().padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "Alejandro Corona",
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold,
                                color = if (temaAppGlobal == ModoTema.CRISTAL_IOS) Color.White else Color.Black
                            )
                            Text(
                                text = "Desarrollador Senior C",
                                style = MaterialTheme.typography.bodyMedium,
                                color = if (temaAppGlobal == ModoTema.CRISTAL_IOS) Color.White.copy(alpha = 0.7f) else Color.Gray
                            )
                            Text(
                                text = "Tandem",
                                style = MaterialTheme.typography.bodyMedium,
                                color = if (temaAppGlobal == ModoTema.CRISTAL_IOS) Color.White.copy(alpha = 0.7f) else Color.Gray
                            )
                            
                            Spacer(modifier = Modifier.height(16.dp))
                            HorizontalDivider(modifier = Modifier.padding(horizontal = 32.dp))
                            Spacer(modifier = Modifier.height(16.dp))

                            Text(
                                text = "Si te gusta mi trabajo y quieres apoyar el desarrollo de FrogDownloader, ¡invítame un café!",
                                style = MaterialTheme.typography.bodyMedium,
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                                color = if (temaAppGlobal == ModoTema.CRISTAL_IOS) Color.White else Color.DarkGray,
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
                                Text("Cerrar", color = if (temaAppGlobal == ModoTema.CRISTAL_IOS) Color.White.copy(alpha = 0.6f) else Color.Gray)
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
                text = "FrogDownloader",
                style = MaterialTheme.typography.labelMedium,
                color = if (temaAppGlobal == ModoTema.CRISTAL_IOS) Color.White.copy(alpha = 0.6f) else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
            )

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = "Versión ${ActualizadorApp.obtenerVersionLocal(contexto)}",
                style = MaterialTheme.typography.bodySmall,
                color = if (temaAppGlobal == ModoTema.CRISTAL_IOS) Color.White.copy(alpha = 0.5f) else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
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
            val colorTextoDialog = if (temaAppGlobal == ModoTema.CRISTAL_IOS) Color.White else MaterialTheme.colorScheme.onSurface
            AlertDialog(
                onDismissRequest = { mostrarSorpresaRomantica = false },
                containerColor = if (temaAppGlobal == ModoTema.CRISTAL_IOS) (if (esOscuro) Color.Black.copy(alpha = 0.9f) else Color.White.copy(alpha = 0.9f)) else MaterialTheme.colorScheme.surface,
                modifier = if (temaAppGlobal == ModoTema.CRISTAL_IOS) Modifier.border(0.5.dp, Color.White.copy(alpha = 0.2f), MaterialTheme.shapes.extraLarge) else Modifier,
                confirmButton = {
                    TextButton(onClick = { mostrarBottomSheetGlobal = false; mostrarSorpresaRomantica = false }) {
                        Text("Te amo ❤️", color = Color(0xFFE91E63))
                    }
                },
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("¡Cofre Secreto Activado! 🧸", style = MaterialTheme.typography.titleLarge, color = colorTextoDialog)
                    }
                },
                text = {
                    Column {
                        Text(
                            text = "Hola mi amor, si estás leyendo esto es porque encontraste el secreto que escondí en el código para ti...",
                            style = MaterialTheme.typography.bodyLarge,
                            color = colorTextoDialog.copy(alpha = 0.8f)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = if (temaAppGlobal == ModoTema.CRISTAL_IOS) Color(0xFFFCE4EC).copy(alpha = 0.2f) else Color(0xFFFCE4EC)
                            )
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text = "Gracias por ser mi mayor apoyo mientras programo y por inspirarme todos los días. Esta aplicación fue hecha con líneas de código, pero mi corazón está lleno de ti. ¡Eres mi persona favorita Mi Mary! 💖",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = if (temaAppGlobal == ModoTema.CRISTAL_IOS) Color.White else Color(0xFF880E4F)
                                )
                            }
                        }
                    }
                }
            )
        }
        // ====================================================================

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
