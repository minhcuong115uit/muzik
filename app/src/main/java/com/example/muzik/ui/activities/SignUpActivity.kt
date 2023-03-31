package com.example.muzik.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import com.example.muzik.R
import com.example.muzik.databinding.ActivitySignUpBinding
import com.example.muzik.listeners.AuthListener
import com.example.muzik.ui.fragments.SignUpPhase1
import com.example.muzik.utils.ViewUtils
import com.example.muzik.viewmodels.authentication.SignUpViewModel

class SignUpActivity : AppCompatActivity(), AuthListener {
    private lateinit var  signUpViewModel: SignUpViewModel;
    private lateinit var  binding: ActivitySignUpBinding;
    private lateinit var navController: NavController;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        signUpViewModel =  ViewModelProvider(this)[SignUpViewModel::class.java]
        binding = DataBindingUtil.setContentView(this,R.layout.activity_sign_up)
        signUpViewModel.setAuthListener(this)
        binding.viewmodel = signUpViewModel

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        signUpViewModel.setNavController((navController));
        setContentView(binding.root)

//        val fragmentManager: FragmentManager = supportFragmentManager
//        val transaction = fragmentManager.beginTransaction()
//        val fragment = SignUpPhase1()
//        transaction.add(R.id.nav_host_fragment, fragment)
//        transaction.commit()
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