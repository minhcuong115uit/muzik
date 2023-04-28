package com.example.muzik.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.muzik.R
import com.example.muzik.databinding.ActivityWelcomeBinding
import com.example.muzik.databinding.FragmentBottomActionsBarBinding
import com.example.muzik.viewmodels.musicplayer.BottomActionsBarViewModel

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [BottomActionsBar.newInstance] factory method to
 * create an instance of this fragment.
 */
class BottomActionsBar : Fragment() {
    private lateinit var binding: FragmentBottomActionsBarBinding
    private lateinit var viewModel: BottomActionsBarViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        viewModel =  BottomActionsBarViewModel();
        binding = FragmentBottomActionsBarBinding.inflate(inflater,container,false);
        binding.viewmodel = viewModel;

        setViewModelObservation();

        return binding.root;
    }
    fun setViewModelObservation(){
        viewModel.isFavourite.observe(viewLifecycleOwner) { isFavourite ->
            if (isFavourite) {
                binding.actionBarHeart.setImageResource(R.drawable.ic_heart);
            } else {
                binding.actionBarHeart.setImageResource(R.drawable.ic_empty_heart);

            }
        }
    }
}