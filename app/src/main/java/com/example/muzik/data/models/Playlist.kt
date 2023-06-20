package com.example.muzik.data.models

import com.google.firebase.Timestamp
import java.util.UUID

data class Playlist (
    var playlistId: String = "",
    val playListImageUri: String = "",
    val name: String = "",
    val isPrivate: Boolean = false,
    val songIds: MutableList<String> = mutableListOf(),
    val userId: String = "",
    var deletedAt: Timestamp? = null,
    var createdAt: Timestamp? = null,

)