package com.example.muzik.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.muzik.R
import com.example.muzik.databinding.FragmentLibraryBinding
import com.example.muzik.ui.activities.MainActivity
import com.example.muzik.ui.adapters.MusicItemAdapter
import com.example.muzik.ui.adapters.PlaylistItemAdapter
import com.example.muzik.viewmodels.musicplayer.PlayerViewModel

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class Library : Fragment() {
    private lateinit var musicListAdapter: MusicItemAdapter;
    private lateinit var playlistAdapter: PlaylistItemAdapter;
//    private val viewModel: PlayerViewModel by lazy {
//        ViewModelProvider(requireActivity())[PlayerViewModel::class.java]
//    }
    private lateinit var viewModel: PlayerViewModel
    private lateinit var binding: FragmentLibraryBinding;
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding =  DataBindingUtil.inflate(inflater,R.layout.fragment_library, container,false);
        viewModel = ViewModelProvider(requireActivity())[PlayerViewModel::class.java]
        //set local song playlist
        viewModel.setPlayList(viewModel.getLocalListSong())
        playlistAdapter = PlaylistItemAdapter(requireActivity(),viewModel);
        musicListAdapter = MusicItemAdapter(requireActivity(),viewModel);
        binding.viewmodel = viewModel;
        binding.context = requireActivity() as MainActivity?
        binding.recDeviceSongs.adapter = musicListAdapter;
        binding.recPlayList.adapter = playlistAdapter;


        viewModel.notifyChange.observe(viewLifecycleOwner){
            playlistAdapter.notifyDataSetChanged()
        }
//        viewModel.getPlaylist().observe(viewLifecycleOwner){
//            playlistAdapter.notifyDataSetChanged()
//        }
        return binding.root
    }

}