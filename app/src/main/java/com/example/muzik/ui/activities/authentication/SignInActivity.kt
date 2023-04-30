package com.example.muzik.ui.activities.authentication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.muzik.R
import com.example.muzik.databinding.ActivitySignInBinding
import com.example.muzik.listeners.AuthListener
import com.example.muzik.utils.ViewUtils
import com.example.muzik.viewmodels.authentication.SignInViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class SignInActivity : AppCompatActivity() , AuthListener  {


    private lateinit var  signInViewModel: SignInViewModel;
    private lateinit var  binding: ActivitySignInBinding;
    lateinit var gso: GoogleSignInOptions;
    lateinit var gsc: GoogleSignInClient;
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        signInViewModel =  ViewModelProvider(this)[SignInViewModel::class.java];
        binding = DataBindingUtil.setContentView(this,R.layout.activity_sign_in);
        signInViewModel.setAuthListener(this);
        binding.viewmodel = signInViewModel;

        gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                    .build();
        gsc = GoogleSignIn.getClient(this,gso);

        binding.btnSignInGoogle.setOnClickListener(View.OnClickListener {
            signInWithGoogle();
        })
        setContentView(binding.root)

    }
    private fun signInWithGoogle(){
        var intent = gsc.signInIntent;
        startActivityForResult(intent,100);

    }

    protected override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 100){
            val task  = GoogleSignIn.getSignedInAccountFromIntent(data);
            try{
                val account :GoogleSignInAccount  = task.getResult(ApiException::class.java)
                Log.i("ID TOKEN", account.idToken.toString())
                val credential = GoogleAuthProvider.getCredential(account.idToken, null)

                FirebaseAuth.getInstance().signInWithCredential(credential)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            Log.i("LOGIN WITH EMAIL",FirebaseAuth.getInstance().currentUser!!.uid);
                            Toast.makeText(this,"Succeeded",Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(this,"Error",Toast.LENGTH_LONG).show();
                        }
                    }
            }
            catch ( e : ApiException){
                Log.e("SIGN IN WITH GOOGLE", "Error signing in with Google ${e.message}", e)
                Toast.makeText(this,"Error",Toast.LENGTH_LONG).show();
            }
        }
    }
    override fun onStarted() {

    }

    override fun onSuccess() {
        ViewUtils.showToast(this, "Sign in succeeded");
    }

    override fun onFailure(message: String?) {
        ViewUtils.showToast(this, message);
    }
}