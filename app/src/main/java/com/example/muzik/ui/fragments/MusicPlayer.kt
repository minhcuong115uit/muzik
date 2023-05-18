package com.example.muzik.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import com.example.muzik.R
import com.example.muzik.databinding.ActivityMusicPlayerBinding
import com.example.muzik.databinding.FragmentMusicPlayerBinding
import com.example.muzik.viewmodels.musicplayer.PlayerViewModel

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
/**
 * A simple [Fragment] subclass.
 * Use the [MusicPlayer.newInstance] factory method to
 * create an instance of this fragment.
 */
class MusicPlayer : Fragment() {
    private var songId: String? = null
    lateinit var binding: FragmentMusicPlayerBinding
    private lateinit var viewModel: PlayerViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            songId = it.getString(ARG_PARAM1)
        }
    }


    //prevent activity response to onClick
    //Đoạn code này dùng để ngăn không cho click event trong Activtiy hoạt động
    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view!!.setOnTouchListener { view, motionEvent -> return@setOnTouchListener true }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding =  DataBindingUtil.inflate(inflater,R.layout.fragment_music_player,container,false);
        viewModel = ViewModelProvider(requireActivity())[PlayerViewModel::class.java]
//        val navHostFragment = parentFragmentManager.findFragmentById(R.id.actions_bar) as NavHostFragment
//        val navController = navHostFragment.navController
//        viewModel.setNavController(navController);
        return binding.root
    }
    companion object {
        @JvmStatic
        fun newInstance(songId: String) =
            MusicPlayer().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, songId)
                }
            }
    }
}