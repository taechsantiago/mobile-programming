package com.audia.taller_3.DataBaseFireStore

import androidx.lifecycle.LiveData
import com.audia.taller_3.DataBase.Tracks

class TracksRepositoryFB(private val tracksDaoFB: TracksDaoFB){
    val tracksList: LiveData<List<TracksFB>> =tracksDaoFB.getAll()
    val amTrackList:LiveData<List<TracksFB>> =tracksDaoFB.getAll()
    val coloresTrackList:LiveData<List<TracksFB>> =tracksDaoFB.getAll()
    val yhlqmdlgTrackList:LiveData<List<TracksFB>> =tracksDaoFB.getAll()
    val quepasaTrackList:LiveData<List<TracksFB>> =tracksDaoFB.getAll()

    //se usa para agregar las canciones a la tabla tracks
    suspend fun insert(vararg tracks: TracksFB) {
        tracksDaoFB.insert(*tracks)
    }
    //se usa para buscar por medio de codigo una cancion de la tabla tracks
    fun findByCode(code: Int): TracksFB {
        return tracksDaoFB.findByCode(code)
    }

    fun deleteAll(){
        tracksDaoFB.deleteAll()
    }
    //se usa para buscar por medio de nombre una cancion de la tabla tracks
    /*fun findByName(name: String): TracksFB {
        return tracksDaoFB.findByName(name)
    }*/
    //se usa para buscar por medio de album una cancion de la tabla tracks
    /*fun findByAlbum(album: String): LiveData<List<TracksFB>> {
        return tracksDaoFB.findByAlbum(album)
    }*/
}