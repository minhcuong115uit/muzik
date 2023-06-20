package com.example.muzik.data.repositories

import android.util.Log
import com.example.muzik.data.models.User
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import java.util.UUID


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
    suspend fun getUserFromDb(userId: String, onSuccess: (User) -> Unit) {
        val query = db.collection("users").whereEqualTo("userId", userId)
        try {
            val snapshot = query.get().await()
            if (snapshot.documents.isNotEmpty()) {
                val user = snapshot.documents.first().toObject(User::class.java)
                if (user != null) {
                    onSuccess(user)
                } else {
                    Log.w("USER", "Error getting user with userId: $userId")
                }
            } else {
                Log.w("USER", "No user found with userId: $userId")
            }
        } catch (e: Exception) {
            Log.w("USER", "Error getting user with userId: $userId", e)
        }
    }


}