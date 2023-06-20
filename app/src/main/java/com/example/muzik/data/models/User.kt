package com.example.muzik.data.models

data class User(
    var userId: String = "",
    var firstName: String = "",
    var lastName: String = "",
    var displayName: String = "",
    var gender: String = "",
    var age: String = "",
    var official: Boolean = false,
    var avatar: String = ""
)
