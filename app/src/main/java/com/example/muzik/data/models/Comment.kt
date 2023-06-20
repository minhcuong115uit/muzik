package com.example.muzik.data.models

import com.google.firebase.Timestamp
import java.util.UUID

data class Comment (
    var commentId:String = "",
    val songId: String = "",
    var createdAt: Timestamp? = null,
    var deletedAt: Timestamp? = null,
    var modifiedAt: Timestamp? = null,
    val content: String = "",
//    var user: User = User(),
//    var user: User = User(),
    var userName: String = "",
    var userId: String = "",
    var userAvatar: String = "",
    val replyToCommentId: String? = null,
)
