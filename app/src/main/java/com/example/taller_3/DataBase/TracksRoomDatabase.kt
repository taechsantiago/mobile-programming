package com.example.taller_3.DataBase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Tracks::class],version = 1,exportSchema = false)
abstract class TracksRoomDatabase : RoomDatabase()
{
    abstract fun tracksDao(): TracksDao

    companion object
    {
        const val DATABASE_NAME="TracksList.db"
        private lateinit var instance:TracksRoomDatabase
        fun getDatabase(context: Context?): TracksRoomDatabase
        {
            return if (::instance.isInitialized)
            {
                instance
            }
            else
            {
                Room.databaseBuilder(
                    context!!.applicationContext,
                    TracksRoomDatabase::class.java,
                    DATABASE_NAME
                )
                    .allowMainThreadQueries()
                    .build()
            }
        }
    }
}