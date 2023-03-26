package com.example.muzik.viewmodels.authentication

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.muzik.data.repositories.AuthFirebaseRepository
import com.example.muzik.listeners.AuthListener
import com.example.muzik.utils.FirebaseAuthManager
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class SignUpViewModel (savedStateHandle: SavedStateHandle): ViewModel() {
    private var firebaseAuth: FirebaseAuth = FirebaseAuthManager.getInstance();

    var email: MutableLiveData<String> = MutableLiveData();
    var password : MutableLiveData<String> = MutableLiveData();
    private var authListener: AuthListener? = null

    private fun handleSignUp (email: String?, password:String?){
        if(email.isNullOrEmpty() || password.isNullOrEmpty())
            return;
        AuthFirebaseRepository.instance?.signUpWithEmail(email,password,this.authListener!!);
    }

    fun onSignUpWithEmailClicked () {
        handleSignUp(email.value,password.value);
    }

    fun setAuthListener(authListener: AuthListener) {
        this.authListener = authListener
    }
}