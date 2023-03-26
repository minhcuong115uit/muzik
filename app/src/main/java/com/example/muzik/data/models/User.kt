package com.example.muzik.data.models

data class User(
    val firstName: String,
    val lastName: String,
    val displayName: String,
    val gender: Byte,
    val avatar: String,
    val official: Boolean,
    )