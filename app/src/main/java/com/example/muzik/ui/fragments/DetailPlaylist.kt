package com.example.muzik.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import com.example.muzik.R
import com.example.muzik.databinding.FragmentDetailPlaylistBinding
import com.example.muzik.ui.adapters.DetailPlaylistItemAdapter
import com.example.muzik.viewmodels.musicplayer.LibraryViewModel


class DetailPlaylist : Fragment() {

    private lateinit var binding: FragmentDetailPlaylistBinding
    private lateinit var adapter: DetailPlaylistItemAdapter
    private val libraryViewModel: LibraryViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_detail_playlist, container, false);
        adapter = DetailPlaylistItemAdapter(requireContext(),libraryViewModel);
        binding.recDetailPlaylist.adapter = adapter
        return binding.root
    }

}