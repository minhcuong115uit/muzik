package com.example.muzik.data.models

import java.util.UUID

data class Comment (
    val songId: String,
    val createdAt: String,
    val modifiedAt: String,
    val content: String,
    val user:User,
    val hearts:List<UUID>,
)