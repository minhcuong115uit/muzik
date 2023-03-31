package com.example.muzik.viewmodels.authentication

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.muzik.R
import com.example.muzik.data.models.User
import com.example.muzik.data.repositories.AuthFirebaseRepository
import com.example.muzik.data.repositories.UserRepository
import com.example.muzik.listeners.AuthListener
import com.example.muzik.ui.fragments.SignUpPhase1
import com.example.muzik.utils.FirebaseAuthManager
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class SignUpViewModel (savedStateHandle: SavedStateHandle): ViewModel() {
    private var firebaseAuth: FirebaseAuth = FirebaseAuthManager.getInstance();
    private lateinit var navController: NavController
    private var authListener: AuthListener? = null
    //user data
    var email: MutableLiveData<String> = MutableLiveData();
    var password : MutableLiveData<String> = MutableLiveData();
    var firstName: MutableLiveData<String> = MutableLiveData();
    var lastName : MutableLiveData<String> = MutableLiveData();
    var age: MutableLiveData<String> = MutableLiveData();
    var gender : MutableLiveData<String> = MutableLiveData();
    var displayName : MutableLiveData<String> = MutableLiveData();

    //
    val ages = listOf("18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40")
    fun setNavController(controller: NavController) {
        navController = controller
    }
    private fun handleSignUp (email: String?, password:String?){
        if(email.isNullOrEmpty() || password.isNullOrEmpty())
            return;
        AuthFirebaseRepository.instance?.signUpWithEmail(email,password,this.authListener!!);
        handleCreateUser();

    }



    fun handleCreateUser () {

//        val user:User = User(
//            firstName.value,lastName.value, displayName.value,
//            gender.value,age.value, official = false)
//
//        UserRepository.instance.createUser();

    }

    fun setAuthListener(authListener: AuthListener) {
        this.authListener = authListener
    }
    fun handleNavigateToSignUpPhase2(){
        navController.navigate(R.id.action_signUpPhase1_to_signUpPhase2);
    }
    fun handleNavigateToSignUpPhase3(){
        navController.navigate(R.id.action_signUpPhase2_to_signUpPhase3);
    }
    fun onAgeChanged() {
        Log.d("ViewModel - AgeChange","Age Changed")
    }


}