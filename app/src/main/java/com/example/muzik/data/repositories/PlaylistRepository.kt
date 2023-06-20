package com.example.muzik.data.repositories

import android.util.Log
import com.example.muzik.data.models.Playlist
import com.example.muzik.data.models.Song
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.tasks.await

class PlaylistRepository private constructor() {
    private val db = FirebaseFirestore.getInstance()

    companion object {
        var instance: PlaylistRepository? = null
            get() {
                if (field == null) {
                    field = PlaylistRepository()
                }
                return field
            }
            private set
    }

    suspend fun getPlaylists(): List<Playlist> {
        val result = db.collection("playlists")
            .whereEqualTo("deletedAt", null)
            .get()
            .await()
        val playlists = mutableListOf<Playlist>()
        for (document in result) {
            val playlist = document.toObject<Playlist>()
            playlists.add(playlist)
        }
        return playlists
    }

    fun addSongToPlaylist(playlistId: String, songId: String, onSuccess: (() -> Unit)? = null) {
        db.collection("playlists")
            .document(playlistId)
            .get()
            .addOnSuccessListener { document ->
                val playlist = document.toObject<Playlist>()
                if (playlist != null && !playlist.songIds.contains(songId)) {
                    db.collection("playlists")
                        .document(playlistId)
                        .update("songs", FieldValue.arrayUnion(songId))
                        .addOnSuccessListener {
                            Log.d("PLAYLIST", "Song added to playlist")
                            onSuccess?.invoke()
                        }
                        .addOnFailureListener { e ->
                            Log.w("PLAYLIST", "Error adding song to playlist", e)
                        }
                } else {
                    Log.d("PLAYLIST", "Song already in playlist")
                }
            }
            .addOnFailureListener { e ->
                Log.w("PLAYLIST", "Error getting playlist", e)
            }
    }

    fun deleteSongFromPlaylist(playlistId: String, songId: String, onSuccess: (() -> Unit)? = null) {
        db.collection("playlists")
            .document(playlistId)
            .update("songIds", FieldValue.arrayRemove(songId))
            .addOnSuccessListener {
                Log.d("PLAYLIST", "Song removed from playlist")
                onSuccess?.invoke()
            }
            .addOnFailureListener { e ->
                Log.w("PLAYLIST", "Error removing song from playlist", e)
            }
    }


    fun createPlaylist(playlist: Playlist, onSuccess: ((newPlaylist: Playlist) -> Unit)? = null,  onFailure: (() -> Unit)? = null) {
        playlist.playlistId = db.collection("playlists").document().id
        db.collection("playlists")
            .add(playlist)
            .addOnSuccessListener { documentReference ->
                Log.d("PLAYLIST", "DocumentSnapshot written with ID: ${documentReference.id}")
                onSuccess?.invoke(playlist)
            }
            .addOnFailureListener { e ->
                Log.w("PLAYLIST", "Error adding document", e)
                onFailure?.invoke()
            }
    }
    fun updatePlaylist(playlistId: String, fieldName: String, value: Any, onSuccess: (() -> Unit)?) {
        db.collection("playlists")
            .whereEqualTo("playlistId", playlistId)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    document.reference.update(fieldName, value)
                }
                onSuccess?.invoke()
                Log.w("PLAYLIST", "Update successfully new value = $value")
            }
            .addOnFailureListener { e ->
                Log.w("PLAYLIST", "Update playlist failed", e)
            }
    }

    fun deletePlaylist(playlistId: String, onSuccess: (() -> Unit)? = null) {
        db.collection("playlists")
            .whereEqualTo("playlistId", playlistId)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    document.reference.update("deletedAt", FieldValue.serverTimestamp())
                }
                onSuccess?.invoke()
            }
            .addOnFailureListener { e ->
                Log.w("PLAYLIST", "Error deleting playlist", e)
            }
    }
}
