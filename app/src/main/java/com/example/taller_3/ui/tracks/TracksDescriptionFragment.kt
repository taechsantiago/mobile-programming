package com.example.taller_3.ui.tracks

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.media.MediaPlayer
import android.os.Bundle
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.taller_3.AppConstants
import com.example.taller_3.DataBase.TracksViewModel
import com.example.taller_3.DynamicBroadcastReceiver
import com.example.taller_3.R
import com.example.taller_3.SongChangeBroadcastReceiver
import com.example.taller_3.service.MediaPlaybackService
import com.example.taller_3.service.MyMediaPlaybackHelper
import com.example.taller_3.service.adapter.IndexMedia
import com.example.taller_3.service.adapter.SongChangeDuringPlay
import com.example.taller_3.service.library.MusicLibrary
import kotlin.properties.Delegates

class TracksDescriptionFragment: Fragment(),SongChangeDuringPlay {

    private lateinit var mView: View

    private lateinit var tracksViewModel: TracksViewModel
    private var track_code=0


    private var mMediaBrowserHelper: MyMediaPlaybackHelper? = null
    private var isPlaying = false

    private lateinit var play_btn: ImageButton
    private lateinit var past_btn: ImageButton
    private lateinit var next_btn: ImageButton
    private lateinit var repeat_btn: ImageButton
    private lateinit var random_btn: ImageButton

    private lateinit var album_img: ImageView
    private lateinit var album_song: TextView
    private lateinit var tittle_song: TextView
    private lateinit var artist_song: TextView
    private lateinit var duration_song: TextView


    private var repeat_btn_press=false
    private var song_change_playing=false
    private lateinit var action: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mView = inflater.inflate(R.layout.fragment_tracks_description, container, false)

        context?.applicationContext?.registerReceiver(SongChangeBroadcastReceiver(this),IntentFilter(AppConstants.ACTION_SERVICE_CHANGE_SONG))

        tracksViewModel = activity?.run {
            ViewModelProvider(this).get(TracksViewModel::class.java)
        } ?: throw Exception("Invalid Activity")

        arguments?.apply {
            track_code=getInt("track_code")
            song_change_playing=getBoolean("song_change_playing")
        }

        album_img=mView.findViewById(R.id.track_album_img_description)
        album_song=mView.findViewById(R.id.track_album_text_description)
        tittle_song=mView.findViewById(R.id.track_title_text_description)
        artist_song=mView.findViewById(R.id.track_artist_text_description)

        play_btn=mView.findViewById(R.id.play_song_btn)
        play_btn.setOnClickListener { playSong() }

        past_btn=mView.findViewById(R.id.past_song_btn)
        past_btn.setOnClickListener { pastSong() }

        next_btn=mView.findViewById(R.id.next_song_btn)
        next_btn.setOnClickListener { nextSong() }

        repeat_btn=mView.findViewById(R.id.repeat_song_btn)
        repeat_btn.setOnClickListener { repeatSong() }

        random_btn=mView.findViewById(R.id.random_song_btn)

        mMediaBrowserHelper = context?.let { MediaBrowserConnection(it) }
        (mMediaBrowserHelper as MediaBrowserConnection).registerCallback(MediaBrowserListener())

        tracksViewModel= ViewModelProvider(this).get(TracksViewModel::class.java)

        return mView
    }

    override fun onStart() {
        super.onStart()
        mMediaBrowserHelper?.onStart()
    }
    override fun onStop() {
        super.onStop()
        mMediaBrowserHelper?.onStop()
    }

    private fun pastSong() {
        mMediaBrowserHelper?.getTransportControls()?.skipToPrevious()
        play_btn.setBackgroundResource(R.drawable.song_pause_notification)
        song_change_playing=false
    }
    private fun playSong() {
        if (isPlaying) {
            if(song_change_playing){
                mMediaBrowserHelper?.getTransportControls()?.play()
                play_btn.setBackgroundResource(R.drawable.song_pause_notification)
            }
            else{
                mMediaBrowserHelper?.getTransportControls()?.pause()
                play_btn.setBackgroundResource(R.drawable.song_play_notification)
                song_change_playing=false
            }
        } else {
            mMediaBrowserHelper?.getTransportControls()?.play()
            play_btn.setBackgroundResource(R.drawable.song_pause_notification)
            song_change_playing=false
        }
    }
    private fun nextSong() {
        mMediaBrowserHelper?.getTransportControls()?.skipToNext()
        play_btn.setBackgroundResource(R.drawable.song_pause_notification)
        song_change_playing=false
    }
    private fun repeatSong() {
        if(repeat_btn_press==false){
            mMediaBrowserHelper?.getTransportControls()?.sendCustomAction("REPEAT",null)
            repeat_btn.setBackgroundResource(R.drawable.repeat_click_song)
            repeat_btn_press=true
            Log.d("Repetir_musica","Se desea repetir la musica.Main")
        }
        else{
            mMediaBrowserHelper?.getTransportControls()?.sendCustomAction("NOT_REPEAT",null)
            repeat_btn.setBackgroundResource(R.drawable.repeat_song)
            repeat_btn_press=false
            Log.d("Repetir_musica","No se desea repetir la musica.Main")
        }
    }

    inner class MediaBrowserConnection(context: Context): MyMediaPlaybackHelper(context,
        MediaPlaybackService::class.java) {
        override fun onChildrenLoaded(
            parentId: String,
            children: MutableList<MediaBrowserCompat.MediaItem>
        ) {
            Log.d("MediaPlaybackService","Enviando indice inicial, desde el fragmento")
            val intent=Intent(AppConstants.ACTION_SERVICE_INDEX).apply {
                putExtra("INDEX_MEDIA", track_code)
            }
            context?.applicationContext?.sendBroadcast(intent)

            if(children.isEmpty()){
                Log.d("MediaPlaybackService","musica vacia, fragmento")
            }

            val mediaController = getMediaController()
            for (mediaItem in children) {
                mediaController.addQueueItem(mediaItem.description)
            }
            mediaController.transportControls.prepare()
        }
    }
    inner class MediaBrowserListener: MediaControllerCompat.Callback() {
        override fun onPlaybackStateChanged(state: PlaybackStateCompat?) {
            isPlaying = state != null && state.getState() == PlaybackStateCompat.STATE_PLAYING
            album_img.isPressed = isPlaying
        }

        override fun onMetadataChanged(metadata: MediaMetadataCompat?) {
            if (metadata == null) {
                return
            }
            tittle_song.text = metadata.getString(MediaMetadataCompat.METADATA_KEY_TITLE)
            artist_song.text = metadata.getString(MediaMetadataCompat.METADATA_KEY_ARTIST)
            album_song.text= metadata.getString(MediaMetadataCompat.METADATA_KEY_ALBUM)
            album_img.setImageBitmap(
                context?.let {
                    MusicLibrary.getAlbumBitmap(
                        it,
                        metadata.getString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID)
                    )
                }
            )
        }
    }

    override fun changeSong(intent: Intent) {
        song_change_playing=intent.getBooleanExtra("CHANGE_SONG",true)
    }

}