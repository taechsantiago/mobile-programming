package com.example.taller_3.DataBase

import androidx.lifecycle.LiveData

class TracksRepository (private val tracksDao: TracksDao){
    val cartProducts: LiveData<List<Tracks>> =tracksDao.getAll()

    //se usa para agregar las canciones a la tabla tracks
    suspend fun insert(track: Tracks) {
        tracksDao.insert(track)
    }
    //se usa para buscar por medio de codigo una cancion de la tabla tracks
    fun findByCode(code: Int): Tracks {
        return tracksDao.findByCode(code)
    }
    //se usa para buscar por medio de nombre una cancion de la tabla tracks
    fun findByName(name: String): Tracks {
        return tracksDao.findByName(name)
    }
    //se usa para buscar por medio de album una cancion de la tabla tracks
    fun findByAlbum(album: String): LiveData<List<Tracks>> {
        return tracksDao.findByAlbum(album)
    }

}