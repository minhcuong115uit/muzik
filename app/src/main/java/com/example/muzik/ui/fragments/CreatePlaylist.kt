package com.example.muzik.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import com.example.muzik.R
import com.example.muzik.databinding.FragmentCreatePlaylistBinding
import com.example.muzik.viewmodels.LibraryViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment



class CreatePlaylist : BottomSheetDialogFragment() {

    private val viewmodel: LibraryViewModel by activityViewModels()
    private lateinit var binding: FragmentCreatePlaylistBinding;
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//        dialog?.window?.attributes?.windowAnimations = R.style.BottomSheetAnimation

        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_create_playlist,container,false);
        binding.viewmodel = viewmodel
        return binding.root
    }
}