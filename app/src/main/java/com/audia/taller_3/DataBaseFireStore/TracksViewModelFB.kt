package com.audia.taller_3.DataBaseFireStore

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TracksViewModelFB (application: Application) : AndroidViewModel(application) {
    private val TAG = "TracksDaoFireStore"
    private val repository: TracksRepositoryFB
    val tracksList: LiveData<List<TracksFB>>

    private  var firestore: FirebaseFirestore
    private  var tracksDaoFB: TracksDaoFB


    init {
        //se inicializan las variables correspondientes para encadenar el repository y el dao
        //con el viewmodel
        firestore= FirebaseFirestore.getInstance()
        tracksDaoFB= TracksDaoFB(firestore, application.baseContext)
        repository= TracksRepositoryFB(tracksDaoFB)
        tracksList=repository.tracksList
    }
    //funcion con la cual se guardan productos desde la interfaz
    fun saveTrack(vararg tracks: TracksFB) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(*tracks)
    }
    fun deleteAll(){
        repository.deleteAll()
    }
}