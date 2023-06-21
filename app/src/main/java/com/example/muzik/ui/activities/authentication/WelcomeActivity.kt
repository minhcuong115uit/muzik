package com.example.muzik.ui.activities.authentication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.example.muzik.databinding.ActivityWelcomeBinding
import com.example.muzik.ui.activities.MainActivity
import com.example.muzik.viewmodels.authentication.AuthViewModel
import com.example.muzik.viewmodels.authentication.SignUpViewModel

class WelcomeActivity : AppCompatActivity()  {
    private lateinit var binding: ActivityWelcomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityWelcomeBinding.inflate(layoutInflater);
        setContentView(binding.root);
        AuthViewModel.checkUserLoggedIn();
        AuthViewModel.isAuthenticated.observe(this){
            if(it == true){
                val intent = Intent(this, MainActivity::class.java);
                startActivity(intent);
            }
            else{
                Log.e("AUTHENTICATION", "Is not authenticated")
            }
        }

        val createAccountBtn = binding.btnCreateAccount;
        val haveAnAccountBtn = binding.btnAlreadyHaveAccount

        createAccountBtn.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, SignUpActivity::class.java);
            startActivity(intent);
        })
        haveAnAccountBtn.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, MainActivity::class.java);
            startActivity(intent);
        })

    }
}