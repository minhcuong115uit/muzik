package com.example.muzik.listeners

import com.example.muzik.data.models.Song

interface ShowBottomSheetListener {
    fun showBottomSheet(song: Song)
    fun showPlaylistSheet(song: Song)
    fun showTimeOptionsSheet()
    fun closePlaylistSheet()
}