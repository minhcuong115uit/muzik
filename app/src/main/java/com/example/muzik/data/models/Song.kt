package com.example.muzik.data.models

import android.os.Parcel
import android.os.Parcelable
import com.google.android.exoplayer2.MediaItem
import com.google.firebase.Timestamp
import com.google.type.Date
import com.google.type.DateTime


class Song (
    var songId: String = "",
    val imageUri:String ="",
    val uri: String ="",
    val name: String = "",
    val artist: ArrayList<Artist> = arrayListOf(),
    val likes:ArrayList<String> = arrayListOf(),
    val duration: Long = 0,
    //song in user's device
    var isLocalSong: Boolean = false,
    val publishDate: Timestamp? = null,
    val deletedAt: Timestamp? = null,
    val listens: Int = 0,

    //not used
    val expoNotifyToken:String? = null,
)
    : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        arrayListOf<Artist>().apply {
            parcel.readList(this, Artist::class.java.classLoader)
        },
        arrayListOf<String>().apply {
            parcel.readList(this, String::class.java.classLoader)
        },
        parcel.readLong(),
        parcel.readByte() != 0.toByte(),
        parcel.readParcelable(Timestamp::class.java.classLoader),
        parcel.readParcelable(Timestamp::class.java.classLoader),
        parcel.readInt(),
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(songId)
        parcel.writeString(imageUri)
        parcel.writeString(uri)
        parcel.writeString(name)
        parcel.writeList(artist)
        parcel.writeList(likes)
        parcel.writeLong(duration)
        parcel.writeByte(if (isLocalSong) 1 else 0)
        parcel.writeParcelable(publishDate, flags)
        parcel.writeParcelable(deletedAt, flags)
        parcel.writeInt(listens)
        parcel.writeString(expoNotifyToken)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Song> {
        override fun createFromParcel(parcel: Parcel): Song {
            return Song(parcel)
        }

        override fun newArray(size: Int): Array<Song?> {
            return arrayOfNulls(size)
        }
    }
     fun convertToMediaItem(): MediaItem {
         val mediaItem = com.google.android.exoplayer2.MediaItem.fromUri(this.uri);
         return mediaItem
     }
 }