package com.example.muzik.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.example.muzik.R
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
        return binding.root
    }
    private fun setObservation(){
        playerViewModel.isGettingSongs.observe(viewLifecycleOwner){isGettingSongs->
            if(!isGettingSongs){
                binding.container.visibility = View.VISIBLE
                binding.progressBar.visibility = View.GONE
                musicListAdapter.notifyDataSetChanged()
            }
            else {
                binding.container.visibility = View.GONE
                binding.progressBar.visibility = View.VISIBLE
            }
        }
    }
}