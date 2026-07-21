package com.greninja.frogdownloader

import android.content.Context
import android.media.audiofx.BassBoost
import android.media.audiofx.LoudnessEnhancer
import android.util.Log

object GestorAudio {

    private var loudnessEnhancer: LoudnessEnhancer? = null
    private var bassBoost: BassBoost? = null
    private var audioSessionIdActual: Int = -1

    fun inicializarEfectos(sessionId: Int, contexto: Context) {
        if (sessionId == audioSessionIdActual) return
        liberarEfectos()
        
        audioSessionIdActual = sessionId
        val prefs = PreferenciasApp(contexto)
        val activado = prefs.obtenerBoolean(PreferenciasApp.KEY_AUDIO_PREMIUM, false)

        try {
            loudnessEnhancer = LoudnessEnhancer(sessionId).apply {
                enabled = activado
                setTargetGain(1000) // 10dB de ganancia para "pegada"
            }

            bassBoost = BassBoost(0, sessionId).apply {
                enabled = activado
                val nivel = prefs.obtenerString(PreferenciasApp.KEY_NIVEL_BAJOS, "Medio")
                setStrength(mapearNivelBajos(nivel).toShort())
            }
            
            Log.d("FROG_AUDIO", "Efectos inicializados en sesion: $sessionId (Activado: $activado)")
        } catch (e: Exception) {
            Log.e("FROG_AUDIO", "Error al inicializar efectos: ${e.message}")
        }
    }

    fun actualizarEstado(activado: Boolean, contexto: Context) {
        val prefs = PreferenciasApp(contexto)
        val nivel = prefs.obtenerString(PreferenciasApp.KEY_NIVEL_BAJOS, "Medio")
        
        loudnessEnhancer?.enabled = activado
        bassBoost?.enabled = activado
        bassBoost?.setStrength(mapearNivelBajos(nivel).toShort())
        
        Log.d("FROG_AUDIO", "Estado de audio actualizado: $activado, Nivel bajos: $nivel")
    }

    private fun mapearNivelBajos(nivel: String): Int {
        return when (nivel) {
            "Bajo" -> 300
            "Medio" -> 600
            "Alto" -> 1000
            else -> 600
        }
    }

    fun liberarEfectos() {
        loudnessEnhancer?.release()
        bassBoost?.release()
        loudnessEnhancer = null
        bassBoost = null
        audioSessionIdActual = -1
    }
}
