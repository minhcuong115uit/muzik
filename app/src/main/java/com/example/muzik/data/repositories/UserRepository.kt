package com.example.muzik.data.repositories

import android.util.Log
import com.example.muzik.data.models.User
import com.google.firebase.firestore.FirebaseFirestore


class UserRepository {
    val  db :FirebaseFirestore = FirebaseFirestore.getInstance()
    fun createUser ( user:User)
    {
        db.collection("users")
            .add(user)
            .addOnSuccessListener { documentReference ->
                Log.d("CREATE USER", "DocumentSnapshot written with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w("CREATE USER", "Error adding document", e)
            }
    }
}