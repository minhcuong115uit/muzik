package com.example.muzik.viewmodels.authentication

import android.util.Log
import android.view.View
import android.widget.AdapterView
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
    private lateinit var navController: NavController
    private lateinit var authListener: AuthListener;
    //user data
    var email: MutableLiveData<String> = MutableLiveData();
    var password : MutableLiveData<String> = MutableLiveData();
    var firstName: MutableLiveData<String> = MutableLiveData();
    var lastName : MutableLiveData<String> = MutableLiveData();
    var age: MutableLiveData<String> = MutableLiveData();
    var selectedGender : MutableLiveData<String> = MutableLiveData();
    var displayName : MutableLiveData<String> = MutableLiveData();
    val genderList =  ArrayList<String>().apply {
        add("Female");
        add("Male");
        add("Custom");
        add("Refer not to say");
    }

    fun setNavController(controller: NavController) {
        navController = controller
    }
    fun handleSignUp (){
       //check data
        //        ....
        age.value = "18";
        val user = User("",
            firstName.value!!,lastName.value!!, displayName.value!!,
            selectedGender.value!!,age.value!!, false, avatarUrl = "")
        // đoann này nên sửa thành truyền repo vô viewmodel
        AuthFirebaseRepository.instance!!.
            signUpWithEmail(email.value!!, password.value!!, user ,this.authListener);
    }

    fun setAuthListener(authListener: AuthListener) {
        this.authListener = authListener
    }
    fun getNavController(): NavController {
        return navController
    }
    fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
        // Xử lý sự kiện onItemSelected ở đây
    }
    fun setSelectedGender(gender:String){
        selectedGender.value = gender
    }


}