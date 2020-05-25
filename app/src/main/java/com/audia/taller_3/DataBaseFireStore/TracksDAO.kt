package com.audia.taller_3.DataBaseFireStore

import androidx.lifecycle.LiveData

interface TracksDAO {
    fun getAll(): LiveData<List<TracksFB>>

    fun findByCode(code: Int): TracksFB

    fun findByAlbum(album: String): LiveData<List<TracksFB>>

    fun insert(vararg tracks: TracksFB)

    fun update(track: TracksFB)

    fun delete(track: TracksFB)

    fun deleteAll()
}