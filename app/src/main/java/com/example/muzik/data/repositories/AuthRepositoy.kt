package com.example.muzik.data.repositories

import android.util.Log
import com.example.muzik.data.models.User
import com.example.muzik.listeners.AuthListener
import com.example.muzik.utils.FirebaseAuthManager
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser


class AuthFirebaseRepository private constructor() {
    private var firebaseAuth: FirebaseAuth = FirebaseAuthManager.getInstance();
    companion object {
        var instance: AuthFirebaseRepository? = null
            get() {
                if (field == null) {
                    field = AuthFirebaseRepository()
                }
                return field
            }
            private set
    }
    fun signInWithEmail(email: String?, password: String?, authListener: AuthListener) {
        firebaseAuth.signInWithEmailAndPassword(email!!, password!!)
            .addOnCompleteListener(OnCompleteListener<AuthResult?> { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("AUTHENTICATION", "signInWithEmail:success")
                    val user: FirebaseUser? = firebaseAuth.getCurrentUser();
                    authListener.onSuccess();
                } else {
                    // If sign in fails, display a message to the user.
                    authListener.onFailure("Sign in failed!!!");
                    Log.w("AUTHENTICATION", "signInWithEmail:failure", task.exception)
                }
            })
    }

    fun signUpWithEmail(email: String?, password: String?, user: User, authListener: AuthListener) {
        firebaseAuth.createUserWithEmailAndPassword(email!!, password!!)
            .addOnCompleteListener(OnCompleteListener<AuthResult?> { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("LOG SIGN UP", "createUserWithEmail:success")
                    authListener.onSuccess();
                    val z: FirebaseUser? = firebaseAuth.getCurrentUser()

                    Log.i("z",z.toString());
                    UserRepository.instance!!.createUser(user);

                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("LOG SIGN UP", "createUserWithEmail:failure", task.exception)
                    authListener.onFailure("Sign up failed!!!");
                }
            })
    }


}