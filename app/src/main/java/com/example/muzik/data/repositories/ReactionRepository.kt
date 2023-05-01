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
    fun uploadComment(comment: Comment){
        db.collection("comments")
            .add(comment)
            .addOnSuccessListener { documentReference ->
                Log.d("COMMENT", "DocumentSnapshot written with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w("COMMENT", "Error adding document", e)
            }
    }
    fun getComments(songId:String, onSuccess: (List<Comment>) -> Unit, onFailure: (Exception) -> Unit) {
        db.collection("comments")
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
}