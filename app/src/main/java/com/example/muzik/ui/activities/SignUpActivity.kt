package com.example.muzik.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import com.example.muzik.R
import com.example.muzik.databinding.ActivitySignInBinding
import com.example.muzik.databinding.ActivitySignUpBinding
import com.example.muzik.listeners.AuthListener
import com.example.muzik.ui.fragments.SignUpPhase1
import com.example.muzik.utils.ViewUtils
import com.example.muzik.viewmodels.authentication.SignInViewModel
import com.example.muzik.viewmodels.authentication.SignUpViewModel

class SignUpActivity : AppCompatActivity(), AuthListener {
    private lateinit var  signUpViewModel: SignUpViewModel;
    private lateinit var  binding: ActivitySignUpBinding;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        signUpViewModel =  ViewModelProvider(this)[SignUpViewModel::class.java];
        binding = DataBindingUtil.setContentView(this,R.layout.activity_sign_up);
        signUpViewModel.setAuthListener(this)
        binding.viewmodel = signUpViewModel;
        setContentView(binding.root)

        val fragmentManager: FragmentManager = supportFragmentManager
        val transaction = fragmentManager.beginTransaction()
        val bundle = Bundle()

        val fragment = SignUpPhase1();
        transaction.add(binding.signUpFragmentContainer.id, fragment)
        transaction.commit()
    }

    override fun onStarted() {

    }

    override fun onSuccess() {
        ViewUtils.showToast(this, "Sign up succeeded");
    }

    override fun onFailure(message: String?) {
        ViewUtils.showToast(this,message);
    }
}