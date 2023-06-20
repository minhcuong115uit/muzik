package com.example.muzik.ui.activities.authentication

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.muzik.R
import com.example.muzik.databinding.ActivitySignUpBinding
import com.example.muzik.listeners.AuthListener
import com.example.muzik.utils.ViewUtils
import com.example.muzik.viewmodels.authentication.SignUpViewModel
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class SignUpActivity : AppCompatActivity(), AuthListener {
    private lateinit var  signUpViewModel: SignUpViewModel;
    private lateinit var  binding: ActivitySignUpBinding;
    private lateinit var navController: NavController;
    private lateinit var store : StorageReference;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //for uploading user avatar
        val formatter = SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.KOREAN)
        val now = Date()
        val fileName = formatter.format(now);
        store =  FirebaseStorage.getInstance().getReference("images/$fileName")


        signUpViewModel =  ViewModelProvider(this)[SignUpViewModel::class.java]
        binding = DataBindingUtil.setContentView(this,R.layout.activity_sign_up)
        signUpViewModel.setAuthListener(this)
        binding.viewmodel = signUpViewModel
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        signUpViewModel.setNavController((navController));
        setContentView(binding.root)
    }

    override fun onStarted() {

    }

    override fun onSuccess() {
        if(!signUpViewModel.avatarUrl.value.isNullOrEmpty()){
            val avt = Uri.parse(signUpViewModel.avatarUrl.value)
            store.putFile(avt)
                .addOnSuccessListener {
                    Log.d("UploadImage","Succeeded");
                }
                .addOnFailureListener { e ->
                    Log.d("UploadImage", "Failed")
                }
        }
        ViewUtils.showToast(this, "Sign up succeeded");
        val intent =  Intent(this,SignInActivity::class.java);
        startActivity(intent);
    }

    override fun onFailure(message: String?) {
        ViewUtils.showToast(this,message);
    }

}