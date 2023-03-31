package com.example.muzik.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.example.muzik.R
import com.example.muzik.databinding.FragmentSignUpPhase1Binding
import com.example.muzik.viewmodels.authentication.SignUpViewModel

/**
 * A simple [Fragment] subclass.
 * Use the [SignUpPhase1.newInstance] factory method to
 * create an instance of this fragment.
 */
class SignUpPhase1 () : Fragment() {
    private val signUpViewModel: SignUpViewModel by activityViewModels()
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentSignUpPhase1Binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_sign_up_phase1, container, false)
        binding.viewmodel = signUpViewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }
}