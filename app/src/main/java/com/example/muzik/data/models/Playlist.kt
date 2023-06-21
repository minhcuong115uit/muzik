package com.example.muzik.data.models

import com.google.firebase.Timestamp
import java.util.UUID

import android.os.Parcel
import android.os.Parcelable

data class Playlist(
    var playlistId: String = "",
    val playListImageUri: String = "",
    val name: String = "",
    val isPrivate: Boolean = false,
    val songIds: MutableList<String> = mutableListOf(),
    val userId: String = "",
    var deletedAt: Timestamp? = null,
    var createdAt: Timestamp? = null,
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readByte() != 0.toByte(),
        mutableListOf<String>().apply {
            parcel.readList(this, String::class.java.classLoader)
        },
        parcel.readString() ?: "",
        parcel.readParcelable(Timestamp::class.java.classLoader),
        parcel.readParcelable(Timestamp::class.java.classLoader)
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(playlistId)
        parcel.writeString(playListImageUri)
        parcel.writeString(name)
        parcel.writeByte(if (isPrivate) 1 else 0)
        parcel.writeList(songIds)
        parcel.writeString(userId)
        parcel.writeParcelable(deletedAt, flags)
        parcel.writeParcelable(createdAt, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Playlist> {
        override fun createFromParcel(parcel: Parcel): Playlist {
            return Playlist(parcel)
        }

        override fun newArray(size: Int): Array<Playlist?> {
            return arrayOfNulls(size)
        }
    }
}
