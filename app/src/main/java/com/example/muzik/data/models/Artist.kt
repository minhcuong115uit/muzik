package com.example.muzik.data.models

import com.google.firebase.Timestamp
import com.google.type.Date
import com.google.type.DateTime

data class Artist(
    var id: String = "",
    var name: String = "",
    var lastName: String = "",
    var displayName: String = "",
    var gender: String = "",
    var age: String = "",
    var dateOfBirth: Timestamp? = null,
    var deletedAt: Timestamp? = null,
    var avatarUrl: String = ""
){
}
