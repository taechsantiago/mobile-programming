package com.example.taller_3.DataBase

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Tracks(
    @PrimaryKey val code: Int,
    @ColumnInfo var name: String,
    @ColumnInfo var artist: String,
    @ColumnInfo var duration: String,
    @ColumnInfo var album: String,
    @ColumnInfo var image: Int
)