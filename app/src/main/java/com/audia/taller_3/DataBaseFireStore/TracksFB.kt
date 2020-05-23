package com.audia.taller_3.DataBaseFireStore

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class TracksFB(
    @PrimaryKey val code: Int,
    @ColumnInfo(name = "mediaId") var mediaId: String,
    @ColumnInfo(name = "title") var title: String,
    @ColumnInfo(name= "artist") var artist: String,
    @ColumnInfo(name = "album") var album: String,
    @ColumnInfo(name = "genre") var genre: String,
    @ColumnInfo(name = "duration") var duration: Long,
    @ColumnInfo(name = "musicFilename") var musicFilename: String,
    @ColumnInfo(name = "albumArtResName") var albumArtResName: String
) {
    constructor() : this(0, "", "","", "", "",0,"","")
}