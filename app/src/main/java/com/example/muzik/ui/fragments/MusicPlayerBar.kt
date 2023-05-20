package com.example.muzik.ui.fragments

import android.content.Context
import android.os.Bundle
import android.text.TextUtils
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

class MusicPlayerBar : Fragment(), ActionPlayerListener {
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
        setBtnClickListener();
        binding.viewmodel = viewModel
        binding.playerBarSongName.requestFocus();
        viewModel.setActionPlayerListener(this);
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.setOnClickListener {
            val musicPlayerFragment = MusicPlayer();
            val fragmentManager = (context as MainActivity).supportFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.setCustomAnimations(R.anim.slide_up,R.anim.slide_down,R.anim.slide_up, R.anim.slide_down)
                .addToBackStack("Player")
                .replace(R.id.music_player_fragment, musicPlayerFragment).commit()
        }
    }
    private fun setBtnClickListener(){

        binding.nextBtnBottomBar.setOnClickListener {
            viewModel.player.seekToNext();
            mainActivity.showNotification(R.drawable.ic_pause)
        }
        binding.prevBtnBottomBar.setOnClickListener {
            viewModel.player.seekToPrevious();
            mainActivity.showNotification(R.drawable.ic_pause)
        }
        binding.playBtnBottomBar.setOnClickListener {
            if(viewModel.player.isPlaying){
                viewModel.player.stop();
                mainActivity.showNotification(R.drawable.ic_play)
                (it as ImageButton).setImageResource(R.drawable.ic_play);
                viewModel.ellipsizeType.set(TextUtils.TruncateAt.END)
            }
            else{
                mainActivity.showNotification(R.drawable.ic_pause)
                viewModel.player.playWhenReady = true;
                viewModel.player.prepare();
                viewModel.player.play();
                (it as ImageButton).setImageResource(R.drawable.ic_pause);
                viewModel.ellipsizeType.set(TextUtils.TruncateAt.MARQUEE)
            }
        }
    }
    override fun nextClicked() {

    }

    override fun playCLicked() {
        if(viewModel.player.isPlaying){
            binding.playBtnBottomBar.setImageResource(R.drawable.ic_pause)
        }
        else{
            binding.playBtnBottomBar.setImageResource(R.drawable.ic_play)
        }
    }

    override fun prevClicked() {

    }
}