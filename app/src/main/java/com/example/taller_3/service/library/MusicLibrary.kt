package com.example.taller_3.service.library

import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaMetadataCompat
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import com.example.taller_3.BuildConfig
import com.example.taller_3.DataBase.TracksViewModel
import com.example.taller_3.R
import java.util.*
import java.util.concurrent.TimeUnit

object MusicLibrary {
    private val music = TreeMap<String, MediaMetadataCompat>()
    private val albumRes = HashMap<String, Int?>()
    private val musicFileName = HashMap<String, String?>()

    fun getRoot():String{
        return "root"
    }

    private fun getAlbumArtUri(albumArtResName: String): String {
        return ContentResolver.SCHEME_ANDROID_RESOURCE + "://" +
                BuildConfig.APPLICATION_ID + "/drawable/" + albumArtResName
    }

    fun getMusicFilename(mediaId: String?): String? {
        return if (musicFileName.containsKey(mediaId)) musicFileName[mediaId] else null
    }

    private fun getAlbumRes(mediaId: String): Int {
        return if (albumRes.containsKey(mediaId)) albumRes[mediaId]!! else 0
    }

    fun getAlbumBitmap(context: Context, mediaId: String): Bitmap {
        return BitmapFactory.decodeResource(
            context.resources,
            getAlbumRes(
                mediaId
            )
        )
    }

    fun getMediaItems(): List<MediaBrowserCompat.MediaItem> {
        val result: MutableList<MediaBrowserCompat.MediaItem> =
            ArrayList()
        for (metadata in music.values) {
            result.add(
                MediaBrowserCompat.MediaItem(
                    metadata.description, MediaBrowserCompat.MediaItem.FLAG_PLAYABLE
                )
            )
        }
        return result
    }

    fun getMetadata(context: Context, mediaId: String): MediaMetadataCompat {
        val metadataWithoutBitmap = music[mediaId]
        val albumArt =
            getAlbumBitmap(
                context,
                mediaId
            )
        // Since MediaMetadataCompat is immutable, we need to create a copy to set the album art.
        // We don't set it initially on all items so that they don't take unnecessary memory.
        val builder = MediaMetadataCompat.Builder()
        for (key in arrayOf(
            MediaMetadataCompat.METADATA_KEY_MEDIA_ID,
            MediaMetadataCompat.METADATA_KEY_ALBUM,
            MediaMetadataCompat.METADATA_KEY_ARTIST,
            MediaMetadataCompat.METADATA_KEY_GENRE,
            MediaMetadataCompat.METADATA_KEY_TITLE
        )) {
            builder.putString(key, metadataWithoutBitmap!!.getString(key))
        }
        builder.putLong(
            MediaMetadataCompat.METADATA_KEY_DURATION,
            metadataWithoutBitmap!!.getLong(MediaMetadataCompat.METADATA_KEY_DURATION)
        )
        builder.putBitmap(MediaMetadataCompat.METADATA_KEY_ALBUM_ART, albumArt)
        return builder.build()
    }

    private fun createMediaMetadataCompat(
        mediaId: String,
        title: String,
        artist: String,
        album: String,
        genre: String,
        duration: Long,
        durationUnit: TimeUnit,
        musicFilename: String,
        albumArtResId: Int,
        albumArtResName: String
    ) {
        music[mediaId] = MediaMetadataCompat.Builder()
            .putString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID, mediaId)
            .putString(MediaMetadataCompat.METADATA_KEY_ALBUM, album)
            .putString(MediaMetadataCompat.METADATA_KEY_ARTIST, artist)
            .putLong(
                MediaMetadataCompat.METADATA_KEY_DURATION,
                TimeUnit.MILLISECONDS.convert(duration, durationUnit)
            )
            .putString(MediaMetadataCompat.METADATA_KEY_GENRE, genre)
            .putString(
                MediaMetadataCompat.METADATA_KEY_ALBUM_ART_URI,
                getAlbumArtUri(
                    albumArtResName
                )
            )
            .putString(
                MediaMetadataCompat.METADATA_KEY_DISPLAY_ICON_URI,
                getAlbumArtUri(
                    albumArtResName
                )
            )
            .putString(MediaMetadataCompat.METADATA_KEY_TITLE, title)
            .build()
        albumRes[mediaId] = albumArtResId
        musicFileName[mediaId] = musicFilename
    }

    init {
        if(music.isEmpty()){
            Log.d("MediaPlaybackService","musica vacia,library")
        }
        createMediaMetadataCompat(
            "a25_8_yhlqmdlg",
            "25/8",
            "Bad Bunny",
            "YHLQMDLG",
            "TRAP",
            240,
            TimeUnit.SECONDS,
            "a25_8_yhlqmdlg.mp3",
            R.drawable.album_yhlqmdlg,
            "album_yhlqmdlg")

        createMediaMetadataCompat(
            "amarillo_colores",
            "Amarillo",
            "J Balvin",
            "Colores",
            "reggaeton",
            13,
            TimeUnit.SECONDS,
            "amarillo_colores.mp3",
            R.drawable.album_colores,
            "album_colores")

        createMediaMetadataCompat(
            "arabella_am",
            "Arabella",
            "Arctic Monkeys",
            "AM",
            "Alternative Rock",
            251,
            TimeUnit.SECONDS,
            "arabella_am.mp3",
            R.drawable.album_am,
            "album_am")

        createMediaMetadataCompat(
            "arcoiris_colores",
            "Arcoiris",
            "J Balvin",
            "Colores",
            "reggaeton",
            13,
            TimeUnit.SECONDS,
            "arcoiris_colores.mp3",
            R.drawable.album_colores,
            "album_colores")

        createMediaMetadataCompat(
            "azul_colores",
            "Azul",
            "J Balvin",
            "Colores",
            "reggaeton",
            13,
            TimeUnit.SECONDS,
            "azul_colores.mp3",
            R.drawable.album_colores,
            "album_colores")

        createMediaMetadataCompat(
            "bichiyal_yhlqmdlg",
            "Bichiyal",
            "Bad Bunny",
            "YHLQMDLG",
            "TRAP",
            266,
            TimeUnit.SECONDS,
            "bichiyal_yhlqmdlg.mp3",
            R.drawable.album_yhlqmdlg,
            "album_yhlqmdlg")

        createMediaMetadataCompat(
            "blanco_colores",
            "Blanco",
            "J Balvin",
            "Colores",
            "reggaeton",
            13,
            TimeUnit.SECONDS,
            "blanco_colores.mp3",
            R.drawable.album_colores,
            "album_colores")

        createMediaMetadataCompat(
            "cancion_rosa",
            "Canción Rosa",
            "La Toma",
            "¿Que pasa en casa?",
            "Alternative Rock",
            251,
            TimeUnit.SECONDS,
            "cancion_rosa.mp3",
            R.drawable.album_la_toma,
            "album_la_toma"
        )

        createMediaMetadataCompat(
            "do_i_wanna_know_am",
            "Do i wanna know?",
            "Arctic Monkeys",
            "AM",
            "Alternative Rock",
            265,
            TimeUnit.SECONDS,
            "do_i_wanna_know_am.mp3",
            R.drawable.album_am,
            "album_am"
        )

        createMediaMetadataCompat(
            "fireside_am",
            "Fireside",
            "Arctic Monkeys",
            "AM",
            "Alternative Rock",
            181,
            TimeUnit.SECONDS,
            "fireside_am.mp3",
            R.drawable.album_am,
            "album_am"
        )

        createMediaMetadataCompat(
            "gris_colores",
            "Gris",
            "J Balvin",
            "Colores",
            "reggaeton",
            13,
            TimeUnit.SECONDS,
            "gris_colores.mp3",
            R.drawable.album_colores,
            "album_colores")

        createMediaMetadataCompat(
            "i_wanna_be_yours_am",
            "I wanna be yours",
            "Arctic Monkeys",
            "AM",
            "Alternative Rock",
            184,
            TimeUnit.SECONDS,
            "i_wanna_be_yours_am.mp3",
            R.drawable.album_am,
            "album_am"
        )

        createMediaMetadataCompat(
            "bichiyal_yhlqmdlg",
            "Hablamos mañana",
            "Bad Bunny",
            "YHLQMDLG",
            "TRAP",
            266,
            TimeUnit.SECONDS,
            "bichiyal_yhlqmdlg.mp3",
            R.drawable.album_yhlqmdlg,
            "album_yhlqmdlg"
        )

        createMediaMetadataCompat(
            "i_want_it_all_am",
            "I want it all",
            "Arctic Monkeys",
            "AM",
            "Alternative Rock",
            185,
            TimeUnit.SECONDS,
            "i_want_it_all_am.mp3",
            R.drawable.album_am,
            "album_am"
        )

        createMediaMetadataCompat(
            "ignorantes_yhlqmdlg",
            "Ignorantes",
            "Bad Bunny",
            "YHLQMDLG",
            "TRAP",
            247,
            TimeUnit.SECONDS,
            "ignorantes_yhlqmdlg.mp3",
            R.drawable.album_yhlqmdlg,
            "album_yhlqmdlg"
        )

        createMediaMetadataCompat(
            "knee_socks_am",
            "Knee socks",
            "Arctic Monkeys",
            "AM",
            "Alternative Rock",
            257,
            TimeUnit.SECONDS,
            "knee_socks_am.mp3",
            R.drawable.album_am,
            "album_am"
        )

        createMediaMetadataCompat(
            "la_luna",
            "La Luna",
            "La Toma",
            "¿Que pasa en casa?",
            "Alternative Rock",
            218,
            TimeUnit.SECONDS,
            "la_luna.mp3",
            R.drawable.album_la_toma,
            "album_la_toma"
        )

        createMediaMetadataCompat(
            "ignorantes_yhlqmdlg",
            "La dificil",
            "Bad Bunny",
            "YHLQMDLG",
            "TRAP",
            192,
            TimeUnit.SECONDS,
            "ignorantes_yhlqmdlg.mp3",
            R.drawable.album_yhlqmdlg,
            "album_yhlqmdlg"
        )

        createMediaMetadataCompat(
            "mad_sounds_am",
            "Mad sounds",
            "Arctic Monkeys",
            "AM",
            "Alternative Rock",
            215,
            TimeUnit.SECONDS,
            "mad_sounds_am.mp3",
            R.drawable.album_am,
            "album_am"
        )

        createMediaMetadataCompat(
            "morado_colores",
            "Morado",
            "J Balvin",
            "Colores",
            "reggaeton",
            13,
            TimeUnit.SECONDS,
            "morado_colores.mp3",
            R.drawable.album_colores,
            "album_colores"
        )

        createMediaMetadataCompat(
            "negro_colores",
            "Negro",
            "J Balvin",
            "Colores",
            "reggaeton",
            13,
            TimeUnit.SECONDS,
            "negro_colores.mp3",
            R.drawable.album_colores,
            "album_colores"
        )

        createMediaMetadataCompat(
            "no_1_party_anthem_am",
            "No.1 party anthem",
            "Arctic Monkeys",
            "AM",
            "Alternative Rock",
            243,
            TimeUnit.SECONDS,
            "no_1_party_anthem_am.mp3",
            R.drawable.album_am,
            "album_am"
        )

    }
}