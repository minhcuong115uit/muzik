package com.example.muzik.data.repositories

import android.content.Context
import android.provider.MediaStore
import android.util.Log
import com.example.muzik.data.models.Song

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

    fun getDeviceMp3Files(appContext: Context) : List<Song>{
        val listMp3Uri = arrayListOf<Song>()
        val uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(MediaStore.Audio.Media.DATA, MediaStore.Audio.Media.DISPLAY_NAME)
        val selection = MediaStore.Audio.Media.IS_MUSIC + "!= 0"
        val sortOrder = MediaStore.Audio.Media.DISPLAY_NAME + " ASC"

        val cursor = appContext.contentResolver.query(uri, projection, selection, null, sortOrder)

        cursor?.use {
            val dataColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)
            val nameColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME)

            while (it.moveToNext()) {
                val filePath = it.getString(dataColumn)
                val fileName = it.getString(nameColumn)
                listMp3Uri.add(Song("","",filePath,fileName,"",""));
                Log.e("LocalAudioFile", filePath);
                // Do something with the file path or name (e.g., display it, store it in a list, etc.)
            }
        }
        return listMp3Uri
    }

}