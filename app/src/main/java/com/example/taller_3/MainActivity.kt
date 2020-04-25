package com.example.taller_3

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.taller_3.DataBase.Tracks
import com.example.taller_3.DataBase.TracksViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var tracksViewModel: TracksViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(setOf(
                 R.id.navigation_dashboard, R.id.navigation_home, R.id.navigation_notifications))
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        tracksViewModel= ViewModelProvider(this).get(TracksViewModel::class.java)
        addTracks()
    }

    private fun addTracks() {
        tracksViewModel.saveTrack(Tracks(1,"Canción Rosa","La Toma","4:11","¿Que pasa en casa?",R.raw.cancion_rosa,R.drawable.album_la_toma))
        tracksViewModel.saveTrack(Tracks(2,"I wanna be yours","Arctic Monkeys","3:04","AM",R.raw.i_wanna_be_yours,R.drawable.album_am))
        tracksViewModel.saveTrack(Tracks(3,"La Luna","La Toma","2:43","¿Que pasa en casa?",R.raw.la_luna,R.drawable.album_la_toma))
        tracksViewModel.saveTrack(Tracks(4,"Why'd you only call me when you're high","Arctic Monkeys","2:43","AM",R.raw.whyd_you_only_call_me_when_youre_high,R.drawable.album_am))
    }
}
