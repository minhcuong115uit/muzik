package com.example.muzik.data.repositories

import android.content.Context
import android.provider.MediaStore
import android.util.Log
import com.example.muzik.data.models.Song
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject

interface ISongRepository {

}
class SongRepository {
    private val db = FirebaseFirestore.getInstance();

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
        // định nghĩa các cột dữ liệu muốn truy vấn
        val projection =
            arrayOf(MediaStore.Audio.Media.DATA, MediaStore.Audio.Media.DISPLAY_NAME, MediaStore.Audio.Media.DURATION)
        val selection = MediaStore.Audio.Media.IS_MUSIC + "!= 0"
        val sortOrder = MediaStore.Audio.Media.DISPLAY_NAME + " ASC"

        val cursor = appContext.contentResolver.query(uri, projection, selection, null, sortOrder)

        cursor?.use {
            val dataColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)
            var id = 0
            val nameColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME)
            val durationColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)
            while (it.moveToNext()) {
                val filePath = it.getString(dataColumn)
                val fileName = it.getString(nameColumn)
                val duration = it.getLong(durationColumn)
                listMp3Uri.add(Song("DeviceSong${id}","",filePath, name = fileName,
                    arrayListOf(), isLocalSong = true, duration= duration , deletedAt = null));
                Log.e("LocalAudioFile", filePath);
                id++;
                // Do something with the file path or name (e.g., display it, store it in a list, etc.)
            }
        }
        return listMp3Uri
    }
    fun getRemoteSongs(onSuccess: (List<Song>) -> Unit, onFailure: (Exception) -> Unit) {
        db.collection("songs")
            .whereEqualTo("deletedAt",null)
            .get()
            .addOnSuccessListener { result ->
                val songs = mutableListOf<Song>()
                for (document in result) {
                    val song = document.toObject<Song>()
                    songs.add(song)
                }
                onSuccess(songs)
            }
            .addOnFailureListener { e ->
                onFailure(e)
            }
    }
}