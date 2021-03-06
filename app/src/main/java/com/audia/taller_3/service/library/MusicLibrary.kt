package com.audia.taller_3.service.library

import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaMetadataCompat
import android.util.Log
import com.audia.taller_3.BuildConfig
import com.audia.taller_3.DataBaseFireStore.TracksFB
import com.google.firebase.storage.FirebaseStorage
import java.io.File
import java.util.*
import java.util.concurrent.TimeUnit

object MusicLibrary {
    private val TAG = "TracksDaoFireStore"
    private var music = TreeMap<String, MediaMetadataCompat>()
    private val albumRes = HashMap<String, String>()
    private val musicFileName = HashMap<String, String?>()

    private val storage= FirebaseStorage.getInstance()

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

    /*private fun getAlbumRes(mediaId: String): File {
        return if (albumRes.containsKey(mediaId)) albumRes[mediaId]!! else 0
    }*/

    fun getAlbumBitmap(context: Context, mediaId: String): Bitmap {
        val filename="${albumRes[mediaId]}.jpg"
        val imageFile= File(context.cacheDir,filename)
        return BitmapFactory.decodeFile(imageFile.toString())
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
        musicFilename: String,
        albumArtResName: String
    ) {
        music[mediaId] = MediaMetadataCompat.Builder()
            .putString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID, mediaId)
            .putString(MediaMetadataCompat.METADATA_KEY_ALBUM, album)
            .putString(MediaMetadataCompat.METADATA_KEY_ARTIST, artist)
            .putLong(
                MediaMetadataCompat.METADATA_KEY_DURATION,
                TimeUnit.MILLISECONDS.convert(duration, TimeUnit.SECONDS)
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
        albumRes[mediaId] = albumArtResName
        musicFileName[mediaId] = musicFilename
    }

    fun setMusic(listMusic: List<TracksFB>, context: Context){
        for(track in listMusic){
            loadImageWithCache(track.albumArtResName, context)
            createMediaMetadataCompat(
                track.mediaId,
                track.title,
                track.artist,
                track.album,
                track.genre,
                track.duration,
                track.musicFilename,
                track.albumArtResName
            )
        }
    }

    private fun loadImageWithCache(pictureFile: String?, context: Context){
        val filename="${pictureFile}.jpg"
        val imageFile= File(context.cacheDir,filename)
        if(!imageFile.exists()){
            storage.reference.child("images").child(filename).getFile(imageFile)
                .addOnFailureListener{
                    Log.d(TAG,"Error to keep the image")
                }
        }
    }

}