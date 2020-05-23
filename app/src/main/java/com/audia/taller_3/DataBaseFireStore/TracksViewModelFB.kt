package com.audia.taller_3.DataBaseFireStore

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TracksViewModelFB (application: Application) : AndroidViewModel(application) {
    private val repository: TracksRepositoryFB
    val tracksList: LiveData<List<TracksFB>>
    val amTrackList:LiveData<List<TracksFB>>
    val coloresTrackList:LiveData<List<TracksFB>>
    val yhlqmdlgTrackList:LiveData<List<TracksFB>>
    val quepasaTrackList:LiveData<List<TracksFB>>

    private  var firestore: FirebaseFirestore
    private  var tracksDaoFB: TracksDaoFB


    init {
        //se inicializan las variables correspondientes para encadenar el repository y el dao
        //con el viewmodel
        firestore= FirebaseFirestore.getInstance()
        tracksDaoFB= TracksDaoFB(firestore)
        repository= TracksRepositoryFB(tracksDaoFB)
        tracksList=repository.tracksList

        amTrackList=repository.amTrackList
        coloresTrackList=repository.coloresTrackList
        yhlqmdlgTrackList=repository.yhlqmdlgTrackList
        quepasaTrackList=repository.quepasaTrackList
    }
    //funcion con la cual se guardan productos desde la interfaz
    fun saveTrack(vararg tracks: TracksFB) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(*tracks)
    }
    //se usa para buscar un producto desde la interfaz por medio de un codigo
    fun findByCode(code: Int): TracksFB {
        return repository.findByCode(code)
    }
    fun deleteAll(){
        repository.deleteAll()
    }
    //se usa para buscar por medio de nombre una cancion de la tabla tracks
    /*fun findByName(name: String): TracksFB {
        return repository.findByName(name)
    }
    //se usa para buscar por medio de album una cancion de la tabla tracks
    fun findByAlbum(album: String): LiveData<List<TracksFB>> {
        return repository.findByAlbum(album)
    }*/
}