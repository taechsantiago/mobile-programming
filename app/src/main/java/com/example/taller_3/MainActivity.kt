package com.example.taller_3

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.taller_3.AppConstants.Companion.ACTION_NEXT
import com.example.taller_3.AppConstants.Companion.ACTION_PLAY
import com.example.taller_3.AppConstants.Companion.ACTION_PREVIOUS
import com.example.taller_3.DataBase.Tracks
import com.example.taller_3.DataBase.TracksViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    companion object {
        const val NOTIFICATION_CHANNEL_HIGH = "NOTIFICATION_CHANNEL_HIGH"
        const val NOTIFICATION_ID_BASIC = 100

    }


    private lateinit var tracksViewModel: TracksViewModel
    private lateinit var tracks: Tracks

    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)


        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(setOf(
                 R.id.navigation_albums, R.id.navigation_home, R.id.navigation_tracks))
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        tracksViewModel= ViewModelProvider(this).get(TracksViewModel::class.java)
        addTracks()
    }

    private fun addTracks() {
        tracksViewModel.saveTrack(Tracks(0,"25/8","Bad Bunny","2:43","YHLQMDLG",R.raw.a25_8_yhlqmdlg,R.drawable.album_yhlqmdlg))
        tracksViewModel.saveTrack(Tracks(1,"Amarillo","J Balvin","2:43","Colores",R.raw.amarillo_colores,R.drawable.album_colores))
        tracksViewModel.saveTrack(Tracks(2,"Arabella","Arctic Monkeys","3:04","AM",R.raw.arabella_am,R.drawable.album_am))
        tracksViewModel.saveTrack(Tracks(3,"Arcoiris","J Balvin","2:43","Colores",R.raw.arcoiris_colores,R.drawable.album_colores))
        tracksViewModel.saveTrack(Tracks(4,"Azul","J Balvin","2:43","Colores",R.raw.azul_colores,R.drawable.album_colores))
        tracksViewModel.saveTrack(Tracks(5,"Bichiyal","Bad Bunny","2:43","YHLQMDLG",R.raw.bichiyal_yhlqmdlg,R.drawable.album_yhlqmdlg))
        tracksViewModel.saveTrack(Tracks(6,"Blanco","J Balvin","2:43","Colores",R.raw.blanco_colores,R.drawable.album_colores))
        tracksViewModel.saveTrack(Tracks(7,"Canción Rosa","La Toma","4:11","¿Que pasa en casa?",R.raw.cancion_rosa,R.drawable.album_la_toma))
        tracksViewModel.saveTrack(Tracks(8,"Do i wanna know?","Arctic Monkeys","3:04","AM",R.raw.do_i_wanna_know_am,R.drawable.album_am))
        tracksViewModel.saveTrack(Tracks(9,"Fireside","Arctic Monkeys","3:04","AM",R.raw.fireside_am,R.drawable.album_am))
        tracksViewModel.saveTrack(Tracks(10,"Gris","J Balvin","2:43","Colores",R.raw.gris_colores,R.drawable.album_colores))
        tracksViewModel.saveTrack(Tracks(11,"I wanna be yours","Arctic Monkeys","3:04","AM",R.raw.i_wanna_be_yours_am,R.drawable.album_am))
        tracksViewModel.saveTrack(Tracks(12,"Hablamos mañana","Bad Bunny","2:43","YHLQMDLG",R.raw.bichiyal_yhlqmdlg,R.drawable.album_yhlqmdlg))
        tracksViewModel.saveTrack(Tracks(13,"I want it all","Arctic Monkeys","3:04","AM",R.raw.i_want_it_all_am,R.drawable.album_am))
        tracksViewModel.saveTrack(Tracks(14,"Ignorantes","Bad Bunny","2:43","YHLQMDLG",R.raw.ignorantes_yhlqmdlg,R.drawable.album_yhlqmdlg))
        tracksViewModel.saveTrack(Tracks(15,"Knee socks","Arctic Monkeys","3:04","AM",R.raw.knee_socks_am,R.drawable.album_am))
        tracksViewModel.saveTrack(Tracks(16,"La Luna","La Toma","2:43","¿Que pasa en casa?",R.raw.la_luna,R.drawable.album_la_toma))
        tracksViewModel.saveTrack(Tracks(17,"La dificil","Bad Bunny","2:43","YHLQMDLG",R.raw.bichiyal_yhlqmdlg,R.drawable.album_yhlqmdlg))
        tracksViewModel.saveTrack(Tracks(18,"Mad sounds","Arctic Monkeys","3:04","AM",R.raw.mad_sounds_am,R.drawable.album_am))
        tracksViewModel.saveTrack(Tracks(19,"Morado","J Balvin","2:43","Colores",R.raw.morado_colores,R.drawable.album_colores))
        tracksViewModel.saveTrack(Tracks(20,"Negro","J Balvin","2:43","Colores",R.raw.negro_colores,R.drawable.album_colores))
        tracksViewModel.saveTrack(Tracks(21,"No.1 party anthem","Arctic Monkeys","3:04","AM",R.raw.no_1_party_anthem_am,R.drawable.album_am))
        tracksViewModel.saveTrack(Tracks(22,"One for the road","Arctic Monkeys","3:04","AM",R.raw.one_for_the_road_am,R.drawable.album_am))
        tracksViewModel.saveTrack(Tracks(23,"Pero ya no","Bad Bunny","2:43","YHLQMDLG",R.raw.pero_ya_no_yhlqmdlg,R.drawable.album_yhlqmdlg))
        tracksViewModel.saveTrack(Tracks(24,"RU mine","Arctic Monkeys","3:04","AM",R.raw.r_u_mine_am,R.drawable.album_am))
        tracksViewModel.saveTrack(Tracks(25,"Rojo","J Balvin","2:43","Colores",R.raw.rojo_colores,R.drawable.album_colores))
        tracksViewModel.saveTrack(Tracks(26,"Rosado","J Balvin","2:43","Colores",R.raw.rosado_colores,R.drawable.album_colores))
        tracksViewModel.saveTrack(Tracks(27,"Safaera","Bad Bunny","2:43","YHLQMDLG",R.raw.safaera_yhlqmdlg,R.drawable.album_yhlqmdlg))
        tracksViewModel.saveTrack(Tracks(28,"Si veo a tu mama","Bad Bunny","2:43","YHLQMDLG",R.raw.si_veo_a_tu_mama_yhlqmdlg,R.drawable.album_yhlqmdlg))
        tracksViewModel.saveTrack(Tracks(29,"Snap out of it","Arctic Monkeys","3:04","AM",R.raw.snap_out_of_it_am,R.drawable.album_am))
        tracksViewModel.saveTrack(Tracks(30,"Solia","Bad Bunny","2:43","YHLQMDLG",R.raw.solia_yhlqmdlg,R.drawable.album_yhlqmdlg))
        tracksViewModel.saveTrack(Tracks(31,"Verde","J Balvin","2:43","Colores",R.raw.verde_colores,R.drawable.album_colores))
        tracksViewModel.saveTrack(Tracks(32,"Why'd you only call me when you're high","Arctic Monkeys","2:43","AM",R.raw.whyd_you_only_call_me_when_youre_high_am,R.drawable.album_am))
        tracksViewModel.saveTrack(Tracks(33,"Yo perreo sola","Bad Bunny","2:43","YHLQMDLG",R.raw.yo_perreo_sola_yhlqmdlg,R.drawable.album_yhlqmdlg))
    }





}


