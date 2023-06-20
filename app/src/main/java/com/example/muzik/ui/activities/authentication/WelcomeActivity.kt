package com.example.muzik.ui.activities.authentication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.example.muzik.databinding.ActivityWelcomeBinding
import com.example.muzik.ui.activities.MainActivity
import com.example.muzik.viewmodels.authentication.AuthViewModel
import com.example.muzik.viewmodels.authentication.SignUpViewModel

class WelcomeActivity : AppCompatActivity()  {
    private lateinit var binding: ActivityWelcomeBinding
    private lateinit var authViewModel: AuthViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityWelcomeBinding.inflate(layoutInflater);
        setContentView(binding.root);

        val createAccountBtn = binding.btnCreateAccount;
        val haveAnAccountBtn = binding.btnAlreadyHaveAccount

        authViewModel =  ViewModelProvider(this)[AuthViewModel::class.java]

        createAccountBtn.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, SignUpActivity::class.java);
            startActivity(intent);
        })
        haveAnAccountBtn.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, SignInActivity::class.java);
            startActivity(intent);
        })
        AuthViewModel.checkUserLoggedIn()
        AuthViewModel.isAuthenticated.observe(this){
            if(it){
                val intent = Intent(this, MainActivity::class.java);
                startActivity(intent);
            }
        }
    }
}