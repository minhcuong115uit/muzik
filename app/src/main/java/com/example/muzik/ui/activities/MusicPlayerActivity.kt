package com.example.muzik.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.muzik.R
import com.example.muzik.data.repositories.ReactionRepository
import com.example.muzik.databinding.ActivityMusicPlayerBinding
import com.example.muzik.ui.fragments.MusicDisc
import com.example.muzik.utils.Formater
import com.example.muzik.viewmodels.authentication.SignUpViewModel
import com.example.muzik.viewmodels.musicplayer.PlayerViewModel
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ui.TimeBar



class MusicPlayerActivity : AppCompatActivity() {
    lateinit var binding: ActivityMusicPlayerBinding
    lateinit var viewModel: PlayerViewModel
    lateinit var shuffleBtn:ImageButton;
    lateinit var repeatBtn: ImageButton;
    private lateinit var navController: NavController;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel =  ViewModelProvider(this)[PlayerViewModel::class.java]
//        viewModel.initPlayer(this);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_music_player);
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.actions_bottom_bar) as NavHostFragment
        navController = navHostFragment.navController
        viewModel.setNavController(navController);

        binding.viewmodel = viewModel;
        shuffleBtn = findViewById(R.id.shuffle_mode)
        repeatBtn = findViewById(R.id.exo_repeat_mode);

        setUpMusicPlayer();
        setObservations();
    }
    private fun setUpMusicPlayer(){
        val btn = findViewById<ImageButton>(R.id.play_next_btn)
        btn.setOnClickListener {
            viewModel.player.value?.seekToNext();
        }
        shuffleBtn.setOnClickListener(View.OnClickListener {
            viewModel.setShuffleMode(!viewModel.shuffleMode.value!!);
        })
        repeatBtn.setOnClickListener(View.OnClickListener {
            var newRepeatMode = if(viewModel.repeatState.value == 2) 0 else viewModel.repeatState.value!!.plus(
                1
            );
            viewModel.setRepeatState(newRepeatMode);
        })
        binding.playerControlView.player = viewModel.player.value;
        binding.playerControlView.player?.addListener(object : Player.Listener {
            val durationText = findViewById<TextView>(R.id.duration_txt);
            val disFragment = supportFragmentManager.findFragmentById(R.id.fragment_container_music_disc) as? MusicDisc

            override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                super.onMediaItemTransition(mediaItem, reason)
                viewModel.player.value?.duration?.let { binding.timeBar.setDuration(it) }
                durationText.text =
                    viewModel.player.value?.duration?.let { Formater.formatDuration(it) };
            }
            override fun onIsPlayingChanged(isPlaying: Boolean) {
                if (isPlaying) {
                    viewModel.player.value?.duration?.let { binding.timeBar.setDuration(it) }
                    durationText.text =
                        viewModel.player.value?.duration?.let { Formater.formatDuration(it) };
                }
                else{
                    disFragment?.stopAnimation()
                }
            }

            override fun onRepeatModeChanged(repeatMode: Int) {
                super.onRepeatModeChanged(repeatMode)
                Log.i("RepeatModeChanged", repeatMode.toString())
            }
            override fun onPlayWhenReadyChanged(playWhenReady: Boolean, reason: Int) {
                super.onPlayWhenReadyChanged(playWhenReady, reason)
                if (playWhenReady) {
                    disFragment?.resumeAnimation()
                    viewModel.player.value?.play();

                } else {
                    disFragment?.stopAnimation()
                }
            }
        })
        binding.timeBar.addListener(object : TimeBar.OnScrubListener {
            override fun onScrubMove(timeBar: TimeBar, position: Long) {
            }

            override fun onScrubStart(timeBar: TimeBar, position: Long) {
                // Tạm dừng Player khi người dùng bắt đầu tua
                viewModel.player.value?.playWhenReady = false
            }
            override fun onScrubStop(timeBar: TimeBar, position: Long, canceled: Boolean) {
                // Bắt đầu lại Player khi người dùng kết thúc tua
                viewModel.player.value?.seekTo(position)
                viewModel.player.value?.playWhenReady = true
            }
        })
        binding.playerControlView.setProgressUpdateListener { position, bufferedPosition ->
            binding.timeBar.setPosition(position)
            binding.timeBar.setBufferedPosition(bufferedPosition)
            val currentTxt = findViewById<TextView>(R.id.current_position_txt)
            currentTxt.text = Formater.formatDuration(position);

        }
    }
    private fun setObservations(){
        viewModel.isShowComments.observe(this) { isShowComments ->
            if (isShowComments) {
                binding.playerBody.alpha = 0.2F
            } else {
                binding.playerBody.alpha = 1F
            }
        }
        viewModel.repeatState.observe(this){
            when(it){
                0->{
                    repeatBtn.setImageResource(R.drawable.exo_styled_controls_repeat_off);

                }
                1->{
                    repeatBtn.setImageResource(R.drawable.exo_styled_controls_repeat_one);

                }
                2->{
                    repeatBtn.setImageResource(R.drawable.exo_styled_controls_repeat_all);
                }
                else -> repeatBtn.setBackgroundResource(R.drawable.exo_styled_controls_repeat_off);
            }
        }
        viewModel.shuffleMode.observe(this){
            if(it == true){
                shuffleBtn.setImageResource(R.drawable.ic_play_random)
            }
            else{
                shuffleBtn.setImageResource(R.drawable.ic_play_random_off)
            }
        }
    }
}