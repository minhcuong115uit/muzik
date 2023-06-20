package com.example.muzik.listeners

import com.example.muzik.data.models.Song

interface PlaySongListener {
    fun playSong(songId: Int, song:Song);
    fun playSong(song: Song);
}