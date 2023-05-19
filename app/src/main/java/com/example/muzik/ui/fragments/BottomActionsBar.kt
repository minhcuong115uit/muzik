package com.example.muzik.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.NavHostFragment
import com.example.muzik.R
import com.example.muzik.databinding.FragmentBottomActionsBarBinding
import com.example.muzik.viewmodels.musicplayer.PlayerViewModel


class BottomActionsBar : Fragment() {
    private lateinit var binding: FragmentBottomActionsBarBinding
    private val viewModel: PlayerViewModel by activityViewModels()
    private lateinit var comments: Comments
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentBottomActionsBarBinding.inflate(inflater,container,false);
        binding.viewmodel = viewModel;
        comments = Comments()
        setObservations();
        //open Comments Modal
        binding.actionBarComment.setOnClickListener(View.OnClickListener {
            val fragmentTransaction = parentFragmentManager.beginTransaction()
            fragmentTransaction.setCustomAnimations(R.anim.slide_up,R.anim.slide_down,R.anim.slide_up, R.anim.slide_down)
                .addToBackStack("Comments")
                .replace(R.id.actions_bar, comments).commit()
        })
        return binding.root;
    }
    private fun setObservations(){
        viewModel.isFavourite.observe(viewLifecycleOwner) { isFavourite ->
            if (isFavourite) {
                binding.actionBarHeart.setImageResource(R.drawable.ic_heart);
            } else {
                binding.actionBarHeart.setImageResource(R.drawable.ic_empty_heart);

            }
        }
    }
}