package com.example.muzik.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.muzik.R
import com.example.muzik.databinding.ActivityWelcomeBinding

class WelcomeActivity : AppCompatActivity()  {
    private lateinit var binding: ActivityWelcomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityWelcomeBinding.inflate(layoutInflater);
        setContentView(binding.root);

        val createAccountBtn = binding.btnCreateAccount;
        val haveAnAccountBtn = binding.btnAlreadyHaveAccount

        createAccountBtn.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, SignUpActivity::class.java);
            startActivity(intent);
        })
        haveAnAccountBtn.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, SignInActivity::class.java);
            startActivity(intent);
        })
    }
}