package com.example.muzik.utils

import com.google.firebase.auth.FirebaseAuth

//Tạo ra 1 instance duy nhất của firebaseAuth trong toàn bộ app
class FirebaseAuthManager private constructor() {
    companion object {
        @Volatile
        private var instance: FirebaseAuth = FirebaseAuth.getInstance();

        fun getInstance(): FirebaseAuth {
            return instance;
        }
    }
}