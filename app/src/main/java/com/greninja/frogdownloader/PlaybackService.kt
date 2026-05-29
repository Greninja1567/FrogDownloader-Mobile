package com.greninja.frogdownloader

import android.net.Uri
import android.os.Environment
import androidx.annotation.OptIn
import androidx.media3.common.ForwardingPlayer
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.session.LibraryResult
import androidx.media3.session.MediaLibraryService
import androidx.media3.session.MediaSession
import androidx.media3.session.SessionError
import com.google.common.collect.ImmutableList
import com.google.common.util.concurrent.Futures
import com.google.common.util.concurrent.ListenableFuture
import java.io.File

@OptIn(UnstableApi::class)
class PlaybackService : MediaLibraryService() {

    private var mediaLibrarySession: MediaLibrarySession? = null

    companion object {
        private var instance: PlaybackService? = null
        fun actualizarListaEnAuto() {
            instance?.mediaLibrarySession?.notifyChildrenChanged("FROG_ROOT", 0, null)
        }
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        
        val iconUri = Uri.parse("android.resource://${packageName}/drawable/icono")
        val player = MainApp.exoPlayerGlobal ?: androidx.media3.exoplayer.ExoPlayer.Builder(this).build().also { MainApp.exoPlayerGlobal = it }

        val forwardingPlayer = object : ForwardingPlayer(player) {
            override fun isCommandAvailable(command: Int): Boolean {
                if (command == Player.COMMAND_SEEK_TO_NEXT || command == Player.COMMAND_SEEK_TO_NEXT_MEDIA_ITEM ||
                    command == Player.COMMAND_SEEK_TO_PREVIOUS || command == Player.COMMAND_SEEK_TO_PREVIOUS_MEDIA_ITEM ||
                    command == Player.COMMAND_SET_SHUFFLE_MODE || command == Player.COMMAND_SET_REPEAT_MODE) {
                    return true
                }
                return super.isCommandAvailable(command)
            }

            override fun getAvailableCommands(): Player.Commands {
                return super.getAvailableCommands().buildUpon()
                    .add(Player.COMMAND_SEEK_TO_NEXT)
                    .add(Player.COMMAND_SEEK_TO_NEXT_MEDIA_ITEM)
                    .add(Player.COMMAND_SEEK_TO_PREVIOUS)
                    .add(Player.COMMAND_SEEK_TO_PREVIOUS_MEDIA_ITEM)
                    .add(Player.COMMAND_SET_SHUFFLE_MODE)
                    .add(Player.COMMAND_SET_REPEAT_MODE)
                    .build()
            }

            override fun hasNextMediaItem(): Boolean = indiceActualVideoGlobal + 1 < videosPlaylistGlobal.size
            override fun hasPreviousMediaItem(): Boolean = indiceActualVideoGlobal - 1 >= 0

            override fun seekToNext() {
                if (hasNextMediaItem()) {
                    GestorReproduccion.cargarYReproducirIndice(indiceActualVideoGlobal + 1, this@PlaybackService)
                }
            }

            override fun seekToNextMediaItem() {
                seekToNext()
            }

            override fun seekToPrevious() {
                if (hasPreviousMediaItem()) {
                    GestorReproduccion.cargarYReproducirIndice(indiceActualVideoGlobal - 1, this@PlaybackService)
                }
            }

            override fun seekToPreviousMediaItem() {
                seekToPrevious()
            }
        }

        val callback = object : MediaLibrarySession.Callback {
            override fun onConnect(
                session: MediaSession,
                controller: MediaSession.ControllerInfo
            ): MediaSession.ConnectionResult {
                val sessionCommands = MediaSession.ConnectionResult.DEFAULT_SESSION_AND_LIBRARY_COMMANDS.buildUpon()
                    .build()
                val playerCommands = session.player.availableCommands.buildUpon()
                    .build()
                return MediaSession.ConnectionResult.accept(sessionCommands, playerCommands)
            }

            override fun onGetLibraryRoot(
                session: MediaLibrarySession,
                browser: MediaSession.ControllerInfo,
                params: LibraryParams?
            ): ListenableFuture<LibraryResult<MediaItem>> {
                android.util.Log.d("FROG_MEDIA", "onGetLibraryRoot called")
                val rootItem = MediaItem.Builder()
                    .setMediaId("FROG_ROOT")
                    .setMediaMetadata(MediaMetadata.Builder()
                        .setIsBrowsable(true)
                        .setIsPlayable(false)
                        .setMediaType(MediaMetadata.MEDIA_TYPE_FOLDER_MIXED)
                        .setTitle("FrogDownloader")
                        .setArtworkUri(iconUri)
                        .build())
                    .build()
                return Futures.immediateFuture(LibraryResult.ofItem(rootItem, params))
            }

            override fun onGetChildren(
                session: MediaLibrarySession,
                browser: MediaSession.ControllerInfo,
                parentId: String,
                page: Int,
                pageSize: Int,
                params: LibraryParams?
            ): ListenableFuture<LibraryResult<ImmutableList<MediaItem>>> {
                android.util.Log.d("FROG_MEDIA", "onGetChildren parentId: $parentId")
                val items = mutableListOf<MediaItem>()
                
                if (parentId == "FROG_ROOT") {
                    items.add(
                        MediaItem.Builder()
                            .setMediaId("PLAYLIST_SECTION")
                            .setMediaMetadata(MediaMetadata.Builder()
                                .setTitle("Cola de YouTube")
                                .setSubtitle("Tus videos actuales")
                                .setIsBrowsable(true)
                                .setIsPlayable(false)
                                .setMediaType(MediaMetadata.MEDIA_TYPE_FOLDER_MIXED)
                                .setArtworkUri(iconUri)
                                .build())
                            .build()
                    )
                    items.add(
                        MediaItem.Builder()
                            .setMediaId("DOWNLOADS_SECTION")
                            .setMediaMetadata(MediaMetadata.Builder()
                                .setTitle("Mis Descargas")
                                .setSubtitle("Archivos locales")
                                .setIsBrowsable(true)
                                .setIsPlayable(false)
                                .setMediaType(MediaMetadata.MEDIA_TYPE_FOLDER_MIXED)
                                .setArtworkUri(iconUri)
                                .build())
                            .build()
                    )
                    return Futures.immediateFuture(LibraryResult.ofItemList(ImmutableList.copyOf(items), params))
                }
                
                if (parentId == "PLAYLIST_SECTION") {
                    if (videosPlaylistGlobal.isEmpty()) {
                        items.add(
                            MediaItem.Builder()
                                .setMediaId("empty")
                                .setMediaMetadata(MediaMetadata.Builder()
                                    .setTitle("Sin canciones en la lista")
                                    .setIsBrowsable(false)
                                    .setIsPlayable(false)
                                    .setMediaType(MediaMetadata.MEDIA_TYPE_FOLDER_MIXED)
                                    .setArtworkUri(iconUri)
                                    .build())
                                .build()
                        )
                    } else {
                        videosPlaylistGlobal.forEachIndexed { index, video ->
                            items.add(
                                MediaItem.Builder()
                                    .setMediaId("video_$index")
                                    .setMediaMetadata(MediaMetadata.Builder()
                                        .setTitle(video.titulo)
                                        .setArtist("FrogDownloader")
                                        .setIsBrowsable(false)
                                        .setIsPlayable(true)
                                        .setMediaType(MediaMetadata.MEDIA_TYPE_VIDEO)
                                        .setArtworkUri(iconUri)
                                        .build())
                                    .build()
                            )
                        }
                    }
                }

                if (parentId == "DOWNLOADS_SECTION") {
                    val prefs = PreferenciasApp(this@PlaybackService)
                    val rutaAudio = prefs.obtenerString(PreferenciasApp.KEY_RUTA_AUDIO, "MisDescargas/Musica")
                    val carpetaDescargas = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                    val rutaDestino = File(carpetaDescargas, rutaAudio)
                    
                    if (rutaDestino.exists() && rutaDestino.isDirectory) {
                        rutaDestino.listFiles()?.forEach { file ->
                            if (file.isFile && !file.name.startsWith(".")) {
                                items.add(
                                    MediaItem.Builder()
                                        .setMediaId("file_${file.absolutePath}")
                                        .setRequestMetadata(androidx.media3.common.MediaItem.RequestMetadata.Builder().setMediaUri(Uri.fromFile(file)).build())
                                        .setMediaMetadata(MediaMetadata.Builder()
                                            .setTitle(file.nameWithoutExtension)
                                            .setArtist("Descargado")
                                            .setIsBrowsable(false)
                                            .setIsPlayable(true)
                                            .setMediaType(MediaMetadata.MEDIA_TYPE_MUSIC)
                                            .setArtworkUri(iconUri)
                                            .build())
                                        .build()
                                )
                            }
                        }
                    }
                    if (items.isEmpty()) {
                        items.add(
                            MediaItem.Builder()
                                .setMediaId("empty_downloads")
                                .setMediaMetadata(MediaMetadata.Builder()
                                    .setTitle("No hay descargas aún")
                                    .setIsBrowsable(false)
                                    .setIsPlayable(false)
                                    .setArtworkUri(iconUri)
                                    .build())
                                .build()
                        )
                    }
                }
                
                return Futures.immediateFuture(LibraryResult.ofItemList(ImmutableList.copyOf(items), params))
            }
            
            override fun onGetItem(
                session: MediaLibrarySession,
                browser: MediaSession.ControllerInfo,
                mediaId: String
            ): ListenableFuture<LibraryResult<MediaItem>> {
                if (mediaId == "FROG_ROOT") {
                    val rootItem = MediaItem.Builder()
                        .setMediaId("FROG_ROOT")
                        .setMediaMetadata(MediaMetadata.Builder()
                            .setIsBrowsable(true)
                            .setIsPlayable(false)
                            .setMediaType(MediaMetadata.MEDIA_TYPE_FOLDER_MIXED)
                            .setTitle("FrogDownloader")
                            .build())
                        .build()
                    return Futures.immediateFuture(LibraryResult.ofItem(rootItem, null))
                }
                if (mediaId == "PLAYLIST_SECTION" || mediaId == "DOWNLOADS_SECTION") {
                    val item = MediaItem.Builder()
                        .setMediaId(mediaId)
                        .setMediaMetadata(MediaMetadata.Builder()
                            .setTitle(if (mediaId == "PLAYLIST_SECTION") "Cola de YouTube" else "Mis Descargas")
                            .setIsBrowsable(true)
                            .setIsPlayable(false)
                            .setMediaType(MediaMetadata.MEDIA_TYPE_FOLDER_MIXED)
                            .setArtworkUri(iconUri)
                            .build())
                        .build()
                    return Futures.immediateFuture(LibraryResult.ofItem(item, null))
                }
                if (mediaId.startsWith("video_")) {
                    val index = mediaId.removePrefix("video_").toIntOrNull() ?: -1
                    if (index >= 0 && index < videosPlaylistGlobal.size) {
                        val video = videosPlaylistGlobal[index]
                        val iconUriLocal = Uri.parse("android.resource://${packageName}/drawable/icono")
                        val item = MediaItem.Builder()
                            .setMediaId(mediaId)
                            .setMediaMetadata(MediaMetadata.Builder()
                                .setTitle(video.titulo)
                                .setArtist("FrogDownloader")
                                .setIsBrowsable(false)
                                .setIsPlayable(true)
                                .setMediaType(MediaMetadata.MEDIA_TYPE_VIDEO)
                                .setArtworkUri(iconUriLocal)
                                .build())
                            .build()
                        return Futures.immediateFuture(LibraryResult.ofItem(item, null))
                    }
                }
                return Futures.immediateFuture(LibraryResult.ofError(SessionError.ERROR_BAD_VALUE))
            }

            override fun onAddMediaItems(
                mediaSession: MediaSession,
                controller: MediaSession.ControllerInfo,
                mediaItems: MutableList<MediaItem>
            ): ListenableFuture<MutableList<MediaItem>> {
                val updatedItems = mediaItems.map { item ->
                    if (item.mediaId.startsWith("video_")) {
                        val index = item.mediaId.removePrefix("video_").toIntOrNull() ?: -1
                        if (index != -1) {
                            androidx.core.content.ContextCompat.getMainExecutor(this@PlaybackService).execute {
                                GestorReproduccion.cargarYReproducirIndice(index, this@PlaybackService)
                            }
                        }
                    }
                    item
                }.toMutableList()
                return Futures.immediateFuture(updatedItems)
            }
        }

        mediaLibrarySession = MediaLibrarySession.Builder(this, forwardingPlayer, callback).build()
    }

    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaLibrarySession? {
        val prefs = PreferenciasApp(this)
        val activado = prefs.obtenerBoolean(PreferenciasApp.KEY_ANDROID_AUTO, true)
        return if (activado) mediaLibrarySession else null
    }

    override fun onDestroy() {
        instance = null
        mediaLibrarySession?.run {
            release()
            mediaLibrarySession = null
        }
        super.onDestroy()
    }
}