package com.audia.taller_3.broadcast

import android.content.Intent

interface SongChangeDuringPlay {
    fun changeSong(intent: Intent)
}