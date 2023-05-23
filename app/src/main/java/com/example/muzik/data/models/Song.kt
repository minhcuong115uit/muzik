package com.example.muzik.data.models

import com.google.android.exoplayer2.MediaItem


class Song (
    val songId: String = "",
    val imgUri:String ="",
    val songUri: String ="",
    val name: String = "",
    val artistName: String = "",
    val duration: Long = 0,
)
 {
     fun convertToMediaItem(): MediaItem {
         val mediaItem = com.google.android.exoplayer2.MediaItem.fromUri(this.songUri);
         return mediaItem
     }
 }