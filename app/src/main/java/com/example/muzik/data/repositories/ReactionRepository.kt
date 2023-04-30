package com.example.muzik.data.repositories

import android.util.Log
import com.example.muzik.data.models.Comment
import com.google.firebase.firestore.FirebaseFirestore

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
}