package com.example.muzik.data.repositories

import android.util.Log
import com.example.muzik.data.models.Comment
import com.example.muzik.data.models.Song
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.tasks.await

class ReactionRepository private constructor() {
    private val db = FirebaseFirestore.getInstance();
    companion object{
        var instance: ReactionRepository? = null
            get() {
                if (field == null) {
                    field = ReactionRepository()
                }
                return field
            }
            private set
    }
    fun uploadComment(comment: Comment, onSuccess: ((newComment:Comment) -> Unit)? = null){
        comment.commentId = db.collection("comments").document().id;
        db.collection("comments")
            .add(comment)
            .addOnSuccessListener { documentReference ->
                Log.d("COMMENT", "DocumentSnapshot written with ID: ${documentReference.id}")
                if (onSuccess != null) {
                    onSuccess(comment)
                };
            }
            .addOnFailureListener { e ->
                Log.w("COMMENT", "Error adding document", e)
            }
    }
    fun getComments(songId:String, onSuccess: (List<Comment>) -> Unit, onFailure: (Exception) -> Unit) {
        db.collection("comments")
            .whereEqualTo("replyToCommentId", null)
            .get()
            .addOnSuccessListener { result ->
                val comments = mutableListOf<Comment>()
                for (document in result) {
                    val comment = document.toObject<Comment>()
                    if(comment.songId == songId)
                    {
                        comments.add(comment)
                    }
                }
                onSuccess(comments)
            }
            .addOnFailureListener { e ->
                onFailure(e)
            }
    }
    fun getReplyComments(commentId: String, onSuccess: (List<Comment>) -> Unit){
        db.collection("comments")
            .whereEqualTo("replyToCommentId", commentId)
            .get()
            .addOnSuccessListener { result ->
                val comments = mutableListOf<Comment>()
                for (document in result) {
                    val comment = document.toObject<Comment>()
                    comments.add(comment)
                }
                onSuccess(comments);
                Log.d("COMMENT", " get reply comments successfully ${comments.size}")
            }
            .addOnFailureListener { e ->
                Log.e("COMMENT", "Error when get reply comments ${e.message}")
            }
    }
    fun sendReactToSong(songId: String, userId: String, onSuccess: (() -> Unit)? = null) {
        db.collection("songs")
            .document(songId)
            .get()
            .addOnSuccessListener { document ->
                val song = document.toObject<Song>()
                if (song != null) {
                    if (song.likes.contains(userId)) {
                        // Nếu userId đã có trong mảng likes, xóa nó khỏi mảng
                        document.reference.update("likes", FieldValue.arrayRemove(userId))
                            .addOnSuccessListener {
                                Log.d("SONG", "User unliked song")
                                onSuccess?.invoke()
                            }
                            .addOnFailureListener { e ->
                                Log.w("SONG", "Error unliking song", e)
                            }
                    } else {
                        // Nếu userId chưa có trong mảng likes, thêm nó vào mảng
                        document.reference.update("likes", FieldValue.arrayUnion(userId))
                            .addOnSuccessListener {
                                Log.d("SONG", "User liked song")
                                onSuccess?.invoke()
                            }
                            .addOnFailureListener { e ->
                                Log.w("SONG", "Error liking song", e)
                            }
                    }
                }
            }
            .addOnFailureListener { e ->
                Log.w("SONG", "Error getting song", e)
            }
    }
    suspend fun getFavouriteValue(songId: String, userId: String): Boolean {
        return try {
            val document = db.collection("songs")
                .document(songId)
                .get()
                .await()
            val song = document.toObject<Song>()
            song?.likes?.contains(userId) ?: false
        } catch (e: Exception) {
            Log.w("SONG", "Error getting song", e)
            false
        }
    }



}