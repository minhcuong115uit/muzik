package com.example.muzik.data.models

import java.util.UUID

data class Playlist (
    //    val commentId = String = ""
    val playListId: String = "",
    val playListImageUri: String = "",
    val name: String = "",
    val isPrivate: Boolean = false,
    val listId: MutableList<UUID> = mutableListOf(),
    val ownerName:String = ""
)