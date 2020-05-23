package com.audia.taller_3.DataBase

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface TracksDao {
    @Query("SELECT * FROM tracks")
    fun getAll(): LiveData<List<Tracks>>

    @Query("SELECT * FROM tracks WHERE code = :code")
    fun findByCode(code: Int): Tracks

    @Query("SELECT * FROM tracks WHERE name = :name")
    fun findByName(name: String): Tracks

    @Query("SELECT * FROM tracks WHERE album = :album")
    fun findByAlbum(album: String): LiveData<List<Tracks>>

    @Insert
    fun insertAll(vararg tracks: Tracks)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(tracks: Tracks)

    @Update
    fun update(tracks: Tracks)

    @Delete
    fun delete(tracks: Tracks)

    @Query("DELETE FROM tracks")
    fun deleteAll()

}