package com.example.muzik.ui.fragments

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.example.muzik.R
import com.example.muzik.databinding.FragmentSignUpPhase1Binding
import com.example.muzik.databinding.FragmentSignUpPhase3Binding
import com.example.muzik.listeners.AuthListener
import com.example.muzik.utils.Validator
import com.example.muzik.viewmodels.authentication.SignUpViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SignUpPhase3.newInstance] factory method to
 * create an instance of this fragment.
 */
class SignUpPhase3 : Fragment() {

    private val signUpViewModel: SignUpViewModel by activityViewModels()
    private lateinit var binding: FragmentSignUpPhase3Binding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

         binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_sign_up_phase3, container, false)
        binding.viewmodel = signUpViewModel
        binding.lifecycleOwner = viewLifecycleOwner

        binding.btnContinue.setOnClickListener {
            if(isValidForm()){
                signUpViewModel.handleSignUp();
            }
        }
        val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            // Callback is invoked after the user selects a media item or closes the
            // photo picker.
            if (uri != null) {
                signUpViewModel.avatarUrl.value = uri.toString();
                Log.d("PhotoPicker", "Selected URI: $uri")
                Glide.with(this)
                    .load(uri)
                    .into(binding.imageButton)
            } else {
                Log.d("PhotoPicker", "No media selected")
            }
        }
        binding.imageButton.setOnClickListener(View.OnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        })
        return binding.root
    }



    private fun isValidForm(): Boolean{
        binding.edtDisplayName.error = Validator.validateName(binding.edtDisplayName.text.toString());
        return binding.edtDisplayName.error == null
    }

}