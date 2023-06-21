package com.example.muzik.data.models

import android.os.Parcel
import android.os.Parcelable
import com.google.firebase.Timestamp
import com.google.type.Date
import com.google.type.DateTime
import java.util.ArrayList

class Artist(
    var id: String = "",
    var name: String = "",
    var dateOfBirth: Timestamp? = null,
    var deletedAt: Timestamp? = null,
    var avtUri: String = "",
    var followers: ArrayList<String> = ArrayList<String>()
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readParcelable(Timestamp::class.java.classLoader),
        parcel.readParcelable(Timestamp::class.java.classLoader),
        parcel.readString() ?: "",
        arrayListOf<String>().apply {
            parcel.readList(this, String::class.java.classLoader)
        }
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(name)
        parcel.writeParcelable(dateOfBirth, flags)
        parcel.writeParcelable(deletedAt, flags)
        parcel.writeString(avtUri)
        parcel.writeList(followers)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Artist> {
        override fun createFromParcel(parcel: Parcel): Artist {
            return Artist(parcel)
        }

        override fun newArray(size: Int): Array<Artist?> {
            return arrayOfNulls(size)
        }
    }
}
