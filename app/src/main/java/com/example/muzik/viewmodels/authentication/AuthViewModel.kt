package com.example.muzik.viewmodels.authentication

import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.muzik.data.models.User
import com.example.muzik.data.repositories.AuthFirebaseRepository
import com.example.muzik.data.repositories.UserRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.UUID

class AuthViewModel : ViewModel() {
    companion object{
        var isAuthenticated = MutableLiveData(false)
        private var user: User? = null
        fun checkUserLoggedIn() {
            if (FirebaseAuth.getInstance().currentUser != null) {
                // The user is signed in
                isAuthenticated.value = (true)

                runBlocking {
                    FirebaseAuth.getInstance().currentUser?.uid?.let { getUserFromDB(it) };
                }
                Log.d("AUTHENTICATION", "User logged in")
            } else {
                // The user is not signed in
                isAuthenticated.value = (false)
                Log.d("AUTHENTICATION", "User didn't logged in")
            }
        }

        suspend fun getUserFromDB(userId: String) {
            UserRepository.instance?.getUserFromDb(userId){u->
                user = u
            }
        }
        fun getUser(): User?{
            return user
        }
    }
