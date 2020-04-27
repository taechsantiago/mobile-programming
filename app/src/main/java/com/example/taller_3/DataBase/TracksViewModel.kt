package com.example.taller_3.DataBase

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TracksViewModel (application: Application) : AndroidViewModel(application) {
    private val repository: TracksRepository
    val tracksList: LiveData<List<Tracks>>

    val amTrackList:LiveData<List<Tracks>>
    val coloresTrackList:LiveData<List<Tracks>>
    val yhlqmdlgTrackList:LiveData<List<Tracks>>
    val quepasaTrackList:LiveData<List<Tracks>>

    init {
        //se inicializan las variables correspondientes para encadenar el repository y el dao
        //con el viewmodel
        val tracksDao = TracksRoomDatabase.getDatabase(application).tracksDao()
        repository = TracksRepository(tracksDao)
        tracksList=repository.tracksList

        amTrackList=repository.amTrackList
        coloresTrackList=repository.coloresTrackList
        yhlqmdlgTrackList=repository.yhlqmdlgTrackList
        quepasaTrackList=repository.quepasaTrackList
    }
    //funcion con la cual se guardan productos desde la interfaz
    fun saveTrack(track: Tracks) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(track)
    }
    //se usa para buscar un producto desde la interfaz por medio de un codigo
    fun findByCode(code: Int): Tracks {
        return repository.findByCode(code)
    }
    //se usa para buscar por medio de nombre una cancion de la tabla tracks
    fun findByName(name: String): Tracks {
        return repository.findByName(name)
    }
    //se usa para buscar por medio de album una cancion de la tabla tracks
    fun findByAlbum(album: String): LiveData<List<Tracks>> {
        return repository.findByAlbum(album)
    }
}