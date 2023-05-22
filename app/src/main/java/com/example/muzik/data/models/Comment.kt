package com.example.muzik.data.models

import java.util.UUID

data class Comment (
//    val commentId = String = ""
    val songId: String = "",
    val createdAt: String = "",
    val modifiedAt: String = "",
    val content: String = "",
    val user: User = User(),
    val hearts: MutableList<UUID> = mutableListOf()
)
