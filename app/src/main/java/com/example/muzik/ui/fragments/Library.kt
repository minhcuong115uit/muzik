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
import com.example.muzik.ui.adapters.MusicItemAdapter
import com.example.muzik.viewmodels.musicplayer.PlayerViewModel

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Library.newInstance] factory method to
 * create an instance of this fragment.
 */
class Library : Fragment() {
    private lateinit var adapter: MusicItemAdapter;
    private lateinit var viewModel: PlayerViewModel
    private lateinit var binding: FragmentLibraryBinding;

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding =  DataBindingUtil.inflate(inflater,R.layout.fragment_library, container,false);
        viewModel = ViewModelProvider(requireActivity())[PlayerViewModel::class.java]
        adapter = MusicItemAdapter(requireActivity(),viewModel.getLocalListSong());
        binding.recViewHistory.adapter = adapter;
        return binding.root
    }

}