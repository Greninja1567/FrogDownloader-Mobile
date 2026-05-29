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
import android.os.Build

val CHANNEL_ID = "descargas_channel"
@OptIn(UnstableApi::class)
class MainApp : Application() {

    companion object {
        var exoPlayerGlobal: ExoPlayer? = null
    }

    override fun onCreate() {
        super.onCreate()
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
                .build()

            thread {
                try {
                    YoutubeDL.getInstance().updateYoutubeDL(this, YoutubeDL.UpdateChannel.STABLE)
                } catch (e: Exception) { e.printStackTrace() }
            }
        } catch (e: YoutubeDLException) {
            Log.e("YOUTUBEDL", "Error al inicializar", e)
        }
    }
}