package com.greninja.frogdownloader

import android.app.Application
import android.util.Log
import androidx.media3.common.AudioAttributes
import androidx.media3.common.C
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.DefaultLoadControl
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaSession
import com.yausername.ffmpeg.FFmpeg
import com.yausername.youtubedl_android.YoutubeDL
import com.yausername.youtubedl_android.YoutubeDLException
import com.yausername.youtubedl_android.mapper.VideoInfo // Importación por si la necesitas luego
import kotlin.concurrent.thread
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build

val CHANNEL_ID = "descargas_channel"
val MEDIA_CHANNEL_ID = "media_playback_channel"

@OptIn(UnstableApi::class)
class MainApp : Application() {

    companion object {
        var exoPlayerGlobal: ExoPlayer? = null
    }

    override fun onCreate() {
        super.onCreate()
        crearCanalNotificaciones()
        try {
            // Inicialización de los motores nativos
            YoutubeDL.getInstance().init(this)
            FFmpeg.getInstance().init(this)

            val controlDeCargaOptimizado = DefaultLoadControl.Builder()
                .setBufferDurationsMs(
                    1500,  // Min buffer antes de empezar (reducido de 15000+)
                    5000,  // Max buffer
                    500,   // Buffer para reanudar tras pausa
                    1000   // Buffer tras rebuferización
                )
                .setPrioritizeTimeOverSizeThresholds(true)
                .build()

            val audioAttributes = AudioAttributes.Builder()
                .setContentType(C.AUDIO_CONTENT_TYPE_MUSIC)
                .setUsage(C.USAGE_MEDIA)
                .build()

            // Creamos el reproductor en la memoria global de forma segura
            exoPlayerGlobal = ExoPlayer.Builder(this)
                .setLoadControl(controlDeCargaOptimizado)
                .setAudioAttributes(audioAttributes, true)
                .build().apply {
                    addListener(object : androidx.media3.common.Player.Listener {
                        override fun onAudioSessionIdChanged(audioSessionId: Int) {
                            GestorAudio.inicializarEfectos(audioSessionId, this@MainApp)
                        }
                    })
                }

            thread {
                try {
                    YoutubeDL.getInstance().updateYoutubeDL(this, YoutubeDL.UpdateChannel.STABLE)
                } catch (e: Exception) { e.printStackTrace() }
            }
        } catch (e: YoutubeDLException) {
            Log.e("YOUTUBEDL", "Error al inicializar", e)
        }
    }

    private fun crearCanalNotificaciones() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager: NotificationManager =
                getSystemService(NOTIFICATION_SERVICE) as NotificationManager

            // Canal para descargas
            val nameDesc = "Descargas"
            val descDesc = "Notificaciones de estado de descarga"
            val channelDesc = NotificationChannel(CHANNEL_ID, nameDesc, NotificationManager.IMPORTANCE_LOW).apply {
                description = descDesc
            }
            notificationManager.createNotificationChannel(channelDesc)

            // Canal para reproducción (Prioridad por defecto para que salgan controles en pantalla de bloqueo)
            val nameMedia = "Reproducción Multimedia"
            val descMedia = "Controles de reproducción en segundo plano"
            val channelMedia = NotificationChannel(MEDIA_CHANNEL_ID, nameMedia, NotificationManager.IMPORTANCE_DEFAULT).apply {
                description = descMedia
                setShowBadge(false)
            }
            notificationManager.createNotificationChannel(channelMedia)
        }
    }
}