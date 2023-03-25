package com.example.muzik.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.muzik.R
import com.example.muzik.databinding.ActivitySignInBinding
import com.example.muzik.databinding.ActivitySignUpBinding
import com.example.muzik.viewmodels.authentication.SignInViewModel
import com.example.muzik.viewmodels.authentication.SignUpViewModel

class SignUpActivity : AppCompatActivity() {
    private lateinit var  signUpViewModel: SignUpViewModel;
    private lateinit var  binding: ActivitySignUpBinding;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        signUpViewModel =  ViewModelProvider(this)[SignUpViewModel::class.java];
        binding = DataBindingUtil.setContentView(this,R.layout.activity_sign_up);
        binding.viewmodel = signUpViewModel;
        setContentView(binding.root)
    }
}