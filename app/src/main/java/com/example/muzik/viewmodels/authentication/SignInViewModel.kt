package com.example.muzik.viewmodels.authentication

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.muzik.utils.FirebaseAuthManager
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

 class SignInViewModel: ViewModel() {
    private var firebaseAuth: FirebaseAuth = FirebaseAuthManager.getInstance();
    var email: MutableLiveData<String> = MutableLiveData();
    var password : MutableLiveData<String> = MutableLiveData();

    private fun handleSignIn(email: String?, password:String?){
        if(email.isNullOrEmpty() || password.isNullOrEmpty()){
            Log.d("AUTHENTICATION", "email or password is invalid")
        }
        firebaseAuth.signInWithEmailAndPassword(email!!, password!!)
            .addOnCompleteListener(OnCompleteListener<AuthResult?> { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("AUTHENTICATION", "signInWithEmail:success")
                    val user: FirebaseUser? = firebaseAuth.getCurrentUser();
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("AUTHENTICATION", "signInWithEmail:failure", task.exception)
                }
            })
    }
    fun onLoginButtonClicked () {
        handleSignIn(email.value,password.value);
    }

}