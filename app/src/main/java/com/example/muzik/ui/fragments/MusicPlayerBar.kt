package com.example.muzik.ui.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.muzik.R
import com.example.muzik.databinding.FragmentMusicPlayerBarBinding
import com.example.muzik.listeners.ActionPlayerListener
import com.example.muzik.ui.activities.MainActivity
import com.example.muzik.viewmodels.musicplayer.PlayerViewModel

val ARG_PARAM = "param1";
class MusicPlayerBar : Fragment() {
    lateinit var binding:FragmentMusicPlayerBarBinding;
    lateinit var viewModel: PlayerViewModel
    lateinit var mainActivity: MainActivity
    override fun onAttach(context: Context) {
        super.onAttach(context)
        viewModel = ViewModelProvider(requireActivity())[PlayerViewModel::class.java]
        mainActivity = requireActivity() as MainActivity;
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_music_player_bar, container, false)
//        viewModel.initPlayer(requireActivity());
        binding.nextBtnBottomBar.setOnClickListener {
            viewModel.player.value?.seekToNext();
            mainActivity.showNotification(R.drawable.ic_pause)

        }
        binding.prevBtnBottomBar.setOnClickListener {
            viewModel.player.value?.seekToPrevious();
            mainActivity.showNotification(R.drawable.ic_pause)

        }
        binding.playBtnBottomBar.setOnClickListener {
            if(viewModel.player.value?.isPlaying == true){
                viewModel.player.value?.stop();
                mainActivity.showNotification(R.drawable.ic_play)
                (it as ImageButton).setImageResource(R.drawable.ic_play);
            }
            else{
                mainActivity.showNotification(R.drawable.ic_pause)
                viewModel.player.value?.playWhenReady = true;
                viewModel.player.value?.prepare();
                viewModel.player.value?.play();
                (it as ImageButton).setImageResource(R.drawable.ic_pause);
            }
        }
        return binding.root
    }
}