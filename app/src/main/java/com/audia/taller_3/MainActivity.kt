package com.audia.taller_3

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.audia.taller_3.DataBase.Tracks
import com.audia.taller_3.DataBase.TracksViewModel
import com.audia.taller_3.DataBaseFireStore.TracksDaoFB
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {
    companion object {
        private val TAG="MainActivity"

    }
    private lateinit var tracksViewModel: TracksViewModel
    private lateinit var tracksDaoFB: TracksDaoFB
    private lateinit var firestore: FirebaseFirestore


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

        if(intent.hasExtra(AppConstants.EXTRA_FRAGMENT)){
            val bundle = bundleOf("access_notification" to "true")
            navController.navigate(R.id.track_description,bundle)
        }

        tracksViewModel= ViewModelProvider(this).get(TracksViewModel::class.java)

        firestore= FirebaseFirestore.getInstance()
        tracksDaoFB= TracksDaoFB(firestore)

        //addTracks()
    }
    private fun addTracks() {
        tracksViewModel.saveTrack(Tracks(0,"25/8","Bad Bunny","2:43","YHLQMDLG",R.drawable.album_yhlqmdlg))
        tracksViewModel.saveTrack(Tracks(1,"Amarillo","J Balvin","2:43","Colores",R.drawable.album_colores))
        tracksViewModel.saveTrack(Tracks(2,"Arabella","Arctic Monkeys","3:04","AM",R.drawable.album_am))
        tracksViewModel.saveTrack(Tracks(3,"Arcoiris","J Balvin","2:43","Colores",R.drawable.album_colores))
        tracksViewModel.saveTrack(Tracks(4,"Azul","J Balvin","2:43","Colores",R.drawable.album_colores))
        tracksViewModel.saveTrack(Tracks(5,"Bichiyal","Bad Bunny","2:43","YHLQMDLG",R.drawable.album_yhlqmdlg))
        tracksViewModel.saveTrack(Tracks(6,"Blanco","J Balvin","2:43","Colores",R.drawable.album_colores))
        tracksViewModel.saveTrack(Tracks(7,"Canción Rosa","La Toma","4:11","¿Que pasa en casa?",R.drawable.album_la_toma))
        tracksViewModel.saveTrack(Tracks(8,"Do i wanna know?","Arctic Monkeys","3:04","AM",R.drawable.album_am))
        tracksViewModel.saveTrack(Tracks(9,"Fireside","Arctic Monkeys","3:04","AM",R.drawable.album_am))
        tracksViewModel.saveTrack(Tracks(10,"Gris","J Balvin","2:43","Colores",R.drawable.album_colores))
        tracksViewModel.saveTrack(Tracks(11,"I wanna be yours","Arctic Monkeys","3:04","AM",R.drawable.album_am))
        tracksViewModel.saveTrack(Tracks(12,"Hablamos mañana","Bad Bunny","2:43","YHLQMDLG",R.drawable.album_yhlqmdlg))
        tracksViewModel.saveTrack(Tracks(13,"I want it all","Arctic Monkeys","3:04","AM",R.drawable.album_am))
        tracksViewModel.saveTrack(Tracks(14,"Ignorantes","Bad Bunny","2:43","YHLQMDLG",R.drawable.album_yhlqmdlg))
        tracksViewModel.saveTrack(Tracks(15,"Knee socks","Arctic Monkeys","3:04","AM",R.drawable.album_am))
        tracksViewModel.saveTrack(Tracks(16,"La Luna","La Toma","2:43","¿Que pasa en casa?",R.drawable.album_la_toma))
        tracksViewModel.saveTrack(Tracks(17,"La dificil","Bad Bunny","2:43","YHLQMDLG",R.drawable.album_yhlqmdlg))
        tracksViewModel.saveTrack(Tracks(18,"Mad sounds","Arctic Monkeys","3:04","AM",R.drawable.album_am))
        tracksViewModel.saveTrack(Tracks(19,"Morado","J Balvin","2:43","Colores",R.drawable.album_colores))
        tracksViewModel.saveTrack(Tracks(20,"Negro","J Balvin","2:43","Colores",R.drawable.album_colores))
        tracksViewModel.saveTrack(Tracks(21,"No.1 party anthem","Arctic Monkeys","3:04","AM",R.drawable.album_am))
        tracksViewModel.saveTrack(Tracks(22,"One for the road","Arctic Monkeys","3:04","AM",R.drawable.album_am))
        tracksViewModel.saveTrack(Tracks(23,"Pero ya no","Bad Bunny","2:43","YHLQMDLG",R.drawable.album_yhlqmdlg))
        tracksViewModel.saveTrack(Tracks(24,"RU mine","Arctic Monkeys","3:04","AM",R.drawable.album_am))
        tracksViewModel.saveTrack(Tracks(25,"Rojo","J Balvin","2:43","Colores",R.drawable.album_colores))
        tracksViewModel.saveTrack(Tracks(26,"Rosado","J Balvin","2:43","Colores",R.drawable.album_colores))
        tracksViewModel.saveTrack(Tracks(27,"Safaera","Bad Bunny","2:43","YHLQMDLG",R.drawable.album_yhlqmdlg))
        tracksViewModel.saveTrack(Tracks(28,"Si veo a tu mama","Bad Bunny","2:43","YHLQMDLG",R.drawable.album_yhlqmdlg))
        tracksViewModel.saveTrack(Tracks(29,"Snap out of it","Arctic Monkeys","3:04","AM",R.drawable.album_am))
        tracksViewModel.saveTrack(Tracks(30,"Solia","Bad Bunny","2:43","YHLQMDLG",R.drawable.album_yhlqmdlg))
        tracksViewModel.saveTrack(Tracks(31,"Verde","J Balvin","2:43","Colores",R.drawable.album_colores))
        tracksViewModel.saveTrack(Tracks(32,"Why'd you only call me when you're high","Arctic Monkeys","2:43","AM",R.drawable.album_am))
        tracksViewModel.saveTrack(Tracks(33,"Yo perreo sola","Bad Bunny","2:43","YHLQMDLG",R.drawable.album_yhlqmdlg))
    }
}


