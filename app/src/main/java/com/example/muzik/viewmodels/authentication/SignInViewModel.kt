package com.example.muzik.viewmodels.authentication

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.muzik.data.repositories.AuthFirebaseRepository
import com.example.muzik.listeners.AuthListener
import com.example.muzik.utils.FirebaseAuthManager
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

//chưa sử dụng Dependency Injection
 class SignInViewModel: ViewModel() {
     var email: MutableLiveData<String> = MutableLiveData();
     var password : MutableLiveData<String> = MutableLiveData();

     private var authListener: AuthListener? = null

    private fun handleSignIn(email: String?, password:String?){
        if(email.isNullOrEmpty() || password.isNullOrEmpty()){
            Log.d("AUTHENTICATION", "email or password is invalid")
            return;
        }
        // đoann này nên sửa thành truyền repo vô viewmodel
        AuthFirebaseRepository.instance?.signInWithEmail(email,password,this.authListener!!)
    }
    fun onLoginButtonClicked () {
        handleSignIn(email.value,password.value);
    }
     fun onLoginWithGoogleBtnClicked(){
         AuthFirebaseRepository.instance?.SignInWithGoogle();
     }
     fun setAuthListener(authListener: AuthListener) {
         this.authListener = authListener
     }

}