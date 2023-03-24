package com.example.muzik.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import com.example.muzik.R
import com.example.muzik.databinding.ActivitySignInBinding
import com.example.muzik.viewmodels.AuthViewModel

class SignInActivity : AppCompatActivity() {


    private lateinit var  authViewModel: AuthViewModel;
    private lateinit var  binding: ActivitySignInBinding;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        authViewModel =  ViewModelProvider(this)[AuthViewModel::class.java];
        binding = DataBindingUtil.setContentView(this,R.layout.activity_sign_in);
        binding.viewmodel = authViewModel;
        setContentView(binding.root)
    }
}