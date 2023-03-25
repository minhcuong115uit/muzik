package com.example.muzik.viewmodels.authentication

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.muzik.utils.FirebaseAuthManager
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class SignUpViewModel : ViewModel() {
    private var firebaseAuth: FirebaseAuth = FirebaseAuthManager.getInstance();
    var email: MutableLiveData<String> = MutableLiveData();
    var password : MutableLiveData<String> = MutableLiveData();

    private fun handleSignUp (email: String?, password:String?){
        if(email.isNullOrEmpty() || password.isNullOrEmpty())
            return;
        firebaseAuth.createUserWithEmailAndPassword(email!!, password!!)
            .addOnCompleteListener(OnCompleteListener<AuthResult?> { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("LOG SIGN UP", "createUserWithEmail:success")
                    val user: FirebaseUser? = firebaseAuth.getCurrentUser()
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("LOG SIGN UP", "createUserWithEmail:failure", task.exception)
                }
            })
    }
    fun onSignUpBtnClicked () {
        handleSignUp(email.value,password.value);
    }
}