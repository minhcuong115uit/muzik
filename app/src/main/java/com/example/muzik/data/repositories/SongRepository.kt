package com.example.muzik.data.repositories

import android.content.Context
import android.provider.MediaStore

interface ISongRepository {

}
class SongRepository {
    companion object{
        var instance: SongRepository? = null
            get() {
                if (field == null) {
                    field = SongRepository()
                }
                return field
            }
            private set
    }

    fun getDeviceMp3Files(appContext: Context) {
        val selection = "${MediaStore.Files.FileColumns.MEDIA_TYPE}=${MediaStore.Files.FileColumns.MEDIA_TYPE_AUDIO} AND ${MediaStore.Files.FileColumns.DISPLAY_NAME} LIKE ?"
        val selectionArgs = arrayOf("%mp3")

        val projection = arrayOf(
            MediaStore.Files.FileColumns._ID,
            MediaStore.Files.FileColumns.DATA,
            MediaStore.Files.FileColumns.DISPLAY_NAME,
            MediaStore.Files.FileColumns.SIZE
        )

        val sortOrder = "${MediaStore.Files.FileColumns.DISPLAY_NAME} ASC"

        appContext.contentResolver.query(
            MediaStore.Files.getContentUri("external"),
            projection,
            selection,
            selectionArgs,
            sortOrder
        )?.use { cursor ->
            while (cursor.moveToNext()) {

//                val name = cursor.getString(cursor.getColumnIndex(MediaStore.Files.FileColumns.DISPLAY_NAME))
//                val path = cursor.getString(cursor.getColumnIndex(MediaStore.Files.FileColumns.DATA))
//                val size = cursor.getLong(cursor.getColumnIndex(MediaStore.Files.FileColumns.SIZE))
            
            }
        }

    }
}