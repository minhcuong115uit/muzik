package com.example.muzik.data.repositories

import android.util.Log
import com.example.muzik.data.models.User
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.FirebaseFirestore


class UserRepository private constructor(){
    private val  db :FirebaseFirestore = FirebaseFirestore.getInstance()
    companion object {
        var instance: UserRepository? = null
            get() {
                if (field == null) {
                    field = UserRepository()
                }
                return field
            }
            private set
    }
    fun createUser ( user:User)
    {
        db.collection("users")
            .add(user)
            .addOnSuccessListener { documentReference ->
                Log.d("USER", "DocumentSnapshot written with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w("USER", "Error adding document", e)
            }
    }
}