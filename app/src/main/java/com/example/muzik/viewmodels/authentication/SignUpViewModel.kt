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
    private lateinit var authListener: AuthListener;
    //user data
    var email: MutableLiveData<String> = MutableLiveData();
    var password : MutableLiveData<String> = MutableLiveData();
    var firstName: MutableLiveData<String> = MutableLiveData();
    var lastName : MutableLiveData<String> = MutableLiveData();
    var age: MutableLiveData<Int> = MutableLiveData();
    var gender : MutableLiveData<String> = MutableLiveData();
    var displayName : MutableLiveData<String> = MutableLiveData();

    //
    val ages = listOf("18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40")
    fun setNavController(controller: NavController) {
        navController = controller
    }
    fun handleSignUp (){
       //check data
        //        ....
        age.value = 18;
        val user = User(
            firstName.value!!,lastName.value!!, displayName.value!!,
            gender.value!!,age.value!!, false)

        AuthFirebaseRepository.instance!!.
            signUpWithEmail(email.value!!, password.value!!, user ,this.authListener);
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



}