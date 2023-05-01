package com.example.muzik.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import com.example.muzik.R
import com.example.muzik.databinding.FragmentSignUpPhase1Binding
import com.example.muzik.databinding.FragmentSignUpPhase2Binding
import com.example.muzik.ui.adapters.SpinnerGenderAdapter
import com.example.muzik.viewmodels.authentication.SignUpViewModel
import kotlin.math.sign

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SignUpPhase2.newInstance] factory method to
 * create an instance of this fragment.
 */
class SignUpPhase2 : Fragment() {

    private val signUpViewModel: SignUpViewModel by activityViewModels()
    private lateinit var spinnerGenderAdapter: SpinnerGenderAdapter;
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentSignUpPhase2Binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_sign_up_phase2, container, false)
        binding.viewmodel = signUpViewModel
        spinnerGenderAdapter = SpinnerGenderAdapter(requireContext(),signUpViewModel.genderList);
        binding.lifecycleOwner = viewLifecycleOwner

        binding.spinnerGender.adapter = spinnerGenderAdapter;
        // Set the observer for the selectedGender variable
        signUpViewModel.selectedGender.observe(viewLifecycleOwner) { gender ->
            val position = spinnerGenderAdapter.getPosition(gender)
            binding.spinnerGender.setSelection(position)
        }

        // Set the onItemSelectedListener for the spinner
        binding.spinnerGender.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val gender = parent?.getItemAtPosition(position) as String
                signUpViewModel.setSelectedGender(gender)
                Log.d("SelectedItem", signUpViewModel.selectedGender.value.toString());
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
        binding.btnContinue.setOnClickListener(View.OnClickListener {
            signUpViewModel.getNavController().navigate(R.id.action_signUpPhase2_to_signUpPhase3);
        })
        return binding.root
    }
}