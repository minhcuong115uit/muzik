package com.example.muzik.ui.activities

import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
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

        //
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
//            // Use WindowInsetsController to hide the status bar.
//            window.insetsController?.hide(WindowInsets.Type.statusBars())
//        } else {
//            // Use the deprecated method to hide the status bar.
//            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
//        }
           setContentView(binding.root)
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