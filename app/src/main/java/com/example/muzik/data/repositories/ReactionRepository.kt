package com.example.muzik.data.repositories

import android.util.Log
import com.example.muzik.data.models.Comment
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject

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
}