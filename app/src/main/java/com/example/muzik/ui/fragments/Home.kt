package com.example.muzik.ui.fragments

import android.os.Bundle
import android.transition.ChangeBounds
import android.transition.TransitionManager
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.example.muzik.R
import com.example.muzik.data.models.SearchBarState
import com.example.muzik.databinding.FragmentHomeBinding
import com.example.muzik.databinding.FragmentLibraryBinding
import com.example.muzik.ui.adapters.MusicItemAdapter
import com.example.muzik.viewmodels.HomeViewModel
import com.example.muzik.viewmodels.LibraryViewModel
import com.example.muzik.viewmodels.musicplayer.PlayerViewModel


class Home : Fragment() {
    private lateinit var homeViewModel: HomeViewModel
    private val playerViewModel: PlayerViewModel by activityViewModels()
    private lateinit var binding: FragmentHomeBinding;
    private lateinit var musicListAdapter: MusicItemAdapter;
    val initialSearchBarState = SearchBarState(
        width = 200,
        height = 50,
        leftMargin = 16,
        topMargin = 16,
        rightMargin = 16,
        bottomMargin = 16
    )

    val finalSearchBarState = SearchBarState(
        width = ViewGroup.LayoutParams.MATCH_PARENT,
        height = ViewGroup.LayoutParams.MATCH_PARENT,
        leftMargin = 0,
        topMargin = 0,
        rightMargin = 0,
        bottomMargin = 0
    )



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        homeViewModel = ViewModelProvider(requireActivity())[HomeViewModel::class.java]
        setObservation();
        binding =  DataBindingUtil.inflate(inflater,R.layout.fragment_home, container,false);
        //set local song playlist
        playerViewModel.setMediaPlaylist(playerViewModel.getListSong().filter { song -> !song.isLocalSong })
        musicListAdapter = MusicItemAdapter(requireActivity(),playerViewModel, playerViewModel.getListSong().filter { song -> !song.isLocalSong })
        binding.recSongs.adapter = musicListAdapter
        binding.searchBar.setPlayerViewModel(playerViewModel)

        return binding.root
    }
//    private fun setUpSearchBar(){
//        // Set up the transition
//        val transition = ChangeBounds()
//        transition.duration = 300 // Set the duration of the animation
//
////      Set up a click listener for the search bar
//        binding.searchBar.setOnClickListener {
//            // Begin the transition
//            TransitionManager.beginDelayedTransition(binding.container, transition)
//
//            // Update the layout parameters of the search bar to match the final state
//            val layoutParams = binding.searchBar.layoutParams as ViewGroup.MarginLayoutParams
//            layoutParams.width = finalSearchBarState.width
//            layoutParams.height = finalSearchBarState.height
//            layoutParams.setMargins(
//                finalSearchBarState.leftMargin,
//                finalSearchBarState.topMargin,
//                finalSearchBarState.rightMargin,
//                finalSearchBarState.bottomMargin
//            )
//            binding.searchBar.layoutParams = layoutParams
//        }
//    }
    private fun setObservation(){
        playerViewModel.isGettingSongs.observe(viewLifecycleOwner){isGettingSongs->
            if(!isGettingSongs){
//                binding.container.visibility = View.VISIBLE
                binding.progressBar.visibility = View.GONE
                musicListAdapter.notifyDataSetChanged()
            }
            else {
//                binding.container.visibility = View.GONE
                binding.progressBar.visibility = View.VISIBLE
            }
        }
    }
}