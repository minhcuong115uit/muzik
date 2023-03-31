package com.example.muzik.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import com.example.muzik.R
import com.example.muzik.databinding.ActivitySignInBinding
import com.example.muzik.listeners.AuthListener
import com.example.muzik.utils.ViewUtils
import com.example.muzik.viewmodels.authentication.SignInViewModel

class SignInActivity : AppCompatActivity() , AuthListener  {

    override fun onStarted() {

    }

    override fun onSuccess() {
        ViewUtils.showToast(this, "Sign in succeeded");
    }

    override fun onFailure(message: String?) {
        ViewUtils.showToast(this, message);
    }

    private lateinit var  signInViewModel: SignInViewModel;
    private lateinit var  binding: ActivitySignInBinding;
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        signInViewModel =  ViewModelProvider(this)[SignInViewModel::class.java];
        binding = DataBindingUtil.setContentView(this,R.layout.activity_sign_in);
        signInViewModel.setAuthListener(this);
        binding.viewmodel = signInViewModel;
        setContentView(binding.root)

    }
}