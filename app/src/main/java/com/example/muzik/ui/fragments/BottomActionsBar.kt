package com.example.muzik.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import com.example.muzik.R
import com.example.muzik.data.models.Song
import com.example.muzik.databinding.FragmentBottomActionsBarBinding
import com.example.muzik.viewmodels.authentication.AuthViewModel
import com.example.muzik.viewmodels.authentication.SignInViewModel
import com.example.muzik.viewmodels.musicplayer.ActionBarViewModel
import com.example.muzik.viewmodels.musicplayer.PlayerViewModel


class BottomActionsBar : Fragment() {
    private lateinit var binding: FragmentBottomActionsBarBinding
//    private val viewModel: PlayerViewModel by activityViewModels()
    private lateinit var comments: Comments
    private lateinit var viewModel: ActionBarViewModel
    private lateinit var songId:String
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentBottomActionsBarBinding.inflate(inflater,container,false);
        viewModel =  ViewModelProvider(requireActivity())[ActionBarViewModel::class.java];
        binding.viewmodel = viewModel;
        viewModel.setCurrentSongId(songId)
        comments = Comments()
        AuthViewModel.getUser()?.let { viewModel.getDefaultFavouriteValue(songId, it.userId) };
        setObservations();
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
        viewModel.isShowComments.observe(viewLifecycleOwner){isShowComments->
            val fragmentManager = parentFragmentManager
            if (isShowComments) {
                val fragmentTransaction = fragmentManager.beginTransaction()
                fragmentTransaction.setCustomAnimations(R.anim.slide_up,R.anim.slide_down,R.anim.slide_up, R.anim.slide_down)
                    .addToBackStack("Comments")
                    .replace(R.id.comments_container, comments).commit()
            }
            else {
            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            songId = it.getString(SONG_ID).toString()

        }
    }
    companion object {
        const val SONG_ID = "SongId"

        @JvmStatic
        fun newInstance(songId: String): BottomActionsBar {
            val fragment = BottomActionsBar()
            val args = Bundle().apply {
                putString(SONG_ID, songId)
            }
            fragment.arguments = args
            return fragment
        }

    }
}