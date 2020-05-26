package com.audia.taller_3.DataBaseFireStore

import androidx.lifecycle.LiveData

class TracksRepositoryFB(private val tracksDaoFB: TracksDaoFB){
    val tracksList: LiveData<List<TracksFB>> =tracksDaoFB.getAll()

    //se usa para agregar las canciones a la tabla tracks
    fun insert(vararg tracks: TracksFB) {
        tracksDaoFB.insert(*tracks)
    }

    fun deleteAll(){
        tracksDaoFB.deleteAll()
    }
}