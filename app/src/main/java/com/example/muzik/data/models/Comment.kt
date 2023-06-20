package com.example.muzik.data.models

import java.util.UUID

data class Comment (
    var commentId:String = "",
    val songId: String = "",
    var createdAt: String = "",
    val modifiedAt: String = "",
    val content: String = "",
//    var user: User = User(),
//    var user: User = User(),
    var userName: String = "",
    var userId: String = "",
    var userAvatar: String = "",
    val replyToCommentId: String? = null,
    val hearts: MutableList<UUID> = mutableListOf()
)
