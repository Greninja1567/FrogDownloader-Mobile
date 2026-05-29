package com.greninja.frogdownloader

import android.content.Context
import android.content.SharedPreferences

class PreferenciasApp(contexto: Context) {
    private val prefs: SharedPreferences = contexto.getSharedPreferences("frog_prefs", Context.MODE_PRIVATE)

    fun guardarString(clave: String, valor: String) {
        prefs.edit().putString(clave, valor).apply()
    }

    fun obtenerString(clave: String, defecto: String): String {
        return prefs.getString(clave, defecto) ?: defecto
    }

    fun guardarBoolean(clave: String, valor: Boolean) {
        prefs.edit().putBoolean(clave, valor).apply()
    }

    fun obtenerBoolean(clave: String, defecto: Boolean): Boolean {
        return prefs.getBoolean(clave, defecto)
    }

    companion object {
        const val KEY_RESOLUCION_REPRO = "res_repro"
        const val KEY_FORMATO_AUDIO = "fmt_audio"
        const val KEY_CALIDAD_AUDIO = "cal_audio"
        const val KEY_RUTA_AUDIO = "ruta_audio"
        const val KEY_FORMATO_VIDEO = "fmt_video"
        const val KEY_CALIDAD_VIDEO = "cal_video"
        const val KEY_RUTA_VIDEO = "ruta_video"
        const val KEY_ANDROID_AUTO = "android_auto"
        const val KEY_TEMA_APP = "tema_app"
    }
}
