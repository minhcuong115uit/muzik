package com.example.muzik.viewmodels.authentication

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.muzik.utils.FirebaseAuthManager

class AuthViewModel : ViewModel() {
    private var _isAuthenticated = MutableLiveData<Boolean>(false);
    val isAuthenticated:LiveData<Boolean>
        get() {
            return _isAuthenticated
        }
    fun checkSignedIn() {
        val user = FirebaseAuthManager.getInstance().currentUser;
        Log.e("User",user.toString());
        _isAuthenticated.value = user != null
    }
    fun logOut(){
        FirebaseAuthManager.getInstance().signOut();
    }
}