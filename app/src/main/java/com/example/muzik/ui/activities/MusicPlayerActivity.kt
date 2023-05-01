package com.example.muzik.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
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
    lateinit var player: ExoPlayer
    lateinit var repository: ReactionRepository
    private lateinit var navController: NavController;

    var repeatState = Player.REPEAT_MODE_OFF;
    var isShuffled = false;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel =  ViewModelProvider(this)[PlayerViewModel::class.java]
        binding = DataBindingUtil.setContentView(this,R.layout.activity_music_player);
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.actions_bottom_bar) as NavHostFragment
        navController = navHostFragment.navController
        viewModel.setNavController(navController);
        binding.viewmodel = viewModel;


        player = ExoPlayer.Builder(this).build()
        val firstItem = MediaItem.fromUri("https://p.scdn.co/mp3-preview/0496b1c18c7653d9124a2f39e148ec3babcae737?cid=cfe923b2d660439caf2b557b21f31221")
        val secItem = MediaItem.fromUri(" https://www.learningcontainer.com/wp-content/uploads/2020/02/Kalimba.mp3")
        player.addMediaItem(firstItem)
        player.addMediaItem(secItem)
        setUpMusicPlayer();
        setObservations();
        player.prepare();
        player.play();
    }
    private fun setUpMusicPlayer(){
        val shuffleBtn = findViewById<ImageButton>(R.id.shuffle_mode)
        val repeatBtn = findViewById<ImageButton>(R.id.exo_repeat_mode);
        val btn = findViewById<ImageButton>(R.id.play_next_btn)

        btn.setOnClickListener(View.OnClickListener {
            player.seekToNext();
        })
        shuffleBtn.setOnClickListener(View.OnClickListener {
            handleShuffleModeChange(shuffleBtn)
        })
        repeatBtn.setOnClickListener(View.OnClickListener {
            handleRepeatModeChange(repeatBtn)
        })

        binding.playerControlView.player = player;
        player.addListener(object : Player.Listener {
            val durationText = findViewById<TextView>(R.id.duration_txt);
            val disFragment = supportFragmentManager.findFragmentById(R.id.fragment_container_music_disc) as? MusicDisc

            override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                super.onMediaItemTransition(mediaItem, reason)
                binding.timeBar.setDuration(player.duration)
                durationText.text = Formater.formatDuration(player.duration);
            }
            override fun onIsPlayingChanged(isPlaying: Boolean) {
                if (isPlaying) {
                    binding.timeBar.setDuration(player.duration)
                    durationText.text = Formater.formatDuration(player.duration);
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
                    player.play();

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
                player.playWhenReady = false
            }
            override fun onScrubStop(timeBar: TimeBar, position: Long, canceled: Boolean) {
                // Bắt đầu lại Player khi người dùng kết thúc tua
                player.seekTo(position)
                player.playWhenReady = true
            }
        })

        binding.playerControlView.setProgressUpdateListener { position, bufferedPosition ->
            binding.timeBar.setPosition(position)
            binding.timeBar.setBufferedPosition(bufferedPosition)
            val currentTxt = findViewById<TextView>(R.id.current_position_txt)
            currentTxt.text = Formater.formatDuration(position);

        }
        player.repeatMode = repeatState
    }
    private fun handleShuffleModeChange(btn: ImageButton){
        isShuffled = !isShuffled
        player.shuffleModeEnabled = isShuffled;
        if(isShuffled){
            btn.setImageResource(R.drawable.ic_play_random)
        }
        else{
            btn.setImageResource(R.drawable.ic_play_random_off)
        }
    }
    private fun handleRepeatModeChange(repeatBtn: ImageButton){
        when(repeatState){
            0->{
                repeatBtn.setImageResource(R.drawable.exo_styled_controls_repeat_one);
                // reassign repeatState for next repeatModeChanged event
                repeatState = 1;
            }
            1->{
                repeatBtn.setImageResource(R.drawable.exo_styled_controls_repeat_all);
                repeatState = 2;
            }
            2->{
                repeatBtn.setImageResource(R.drawable.exo_styled_controls_repeat_off);
                repeatState = 0;
            }
            else -> repeatBtn.setBackgroundResource(R.drawable.exo_styled_controls_repeat_off);
        }
        player.repeatMode = repeatState
    }
    private fun setObservations(){
        viewModel.isShowComments.observe(this) { isShowComments ->
            if (isShowComments) {
                binding.playerBody.alpha = 0.2F
            } else {
                binding.playerBody.alpha = 1F
            }
        }
    }
}