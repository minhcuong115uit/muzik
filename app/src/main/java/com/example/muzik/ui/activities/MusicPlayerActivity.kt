package com.example.muzik.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import com.example.muzik.R
import com.example.muzik.databinding.ActivityMusicPlayerBinding
import com.example.muzik.ui.fragments.MusicDisc
import com.example.muzik.utils.Formater
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ui.TimeBar



class MusicPlayerActivity : AppCompatActivity() {
    lateinit var binding: ActivityMusicPlayerBinding
    lateinit var player: ExoPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_music_player);
        player = ExoPlayer.Builder(this).build()
        var fisrtItem = MediaItem.fromUri("https://www.soundhelix.com/examples/mp3/SoundHelix-Song-1.mp3")
        var secItem = MediaItem.fromUri(" https://www.learningcontainer.com/wp-content/uploads/2020/02/Kalimba.mp3")
        player.addMediaItem(fisrtItem)
        player.addMediaItem(secItem)

        setUpMusicPlayer();

        player.prepare();
        player.play();
    }
    fun setUpMusicPlayer(){
        binding.playerControlView.player = player;
        binding.timeBar.addListener(object : TimeBar.OnScrubListener {
            override fun onScrubMove(timeBar: TimeBar, position: Long) {
                // Điều chỉnh thời gian của Player
                player.seekTo(position)
            }

            override fun onScrubStart(timeBar: TimeBar, position: Long) {
                // Tạm dừng Player khi người dùng bắt đầu tua
                player.playWhenReady = false

            }
            override fun onScrubStop(timeBar: TimeBar, position: Long, canceled: Boolean) {
                // Bắt đầu lại Player khi người dùng kết thúc tua
                player.playWhenReady = true
            }
        })
        player.addListener(object : Player.Listener {

            override fun onIsPlayingChanged(isPlaying: Boolean) {
                if (isPlaying) {
                    binding.timeBar.setDuration(player.duration)
                    Log.i("Media-Change",player.duration.toString())
                    val durationText = findViewById<TextView>(R.id.duration_txt);
                    durationText.text = Formater.formatDuration(player.duration);
                }
            }

            override fun onRepeatModeChanged(repeatMode: Int) {
                super.onRepeatModeChanged(repeatMode)
                val listModeBtnId =  listOf(
                    R.id.exo_repeat_mode_off,
                    R.id.exo_repeat_mode_all,
                    R.id.exo_repeat_mode_one);
                handleChangeRepeatMode(listModeBtnId,repeatMode);
                Log.i("RepeatModeChanged", repeatMode.toString())
            }

            override fun onPlayWhenReadyChanged(playWhenReady: Boolean, reason: Int) {
                super.onPlayWhenReadyChanged(playWhenReady, reason)
                val fragment = supportFragmentManager.findFragmentById(R.id.fragment_container_music_disc) as? MusicDisc

                if (playWhenReady) {
                    fragment?.resumeAnimation()

                } else {
                    fragment?.stopAnimation()
                }
            }
        })
        binding.playerControlView.setProgressUpdateListener { position, bufferedPosition ->
            binding.timeBar.setPosition(position)
            binding.timeBar.setBufferedPosition(bufferedPosition)
            val currentTxt = findViewById<TextView>(R.id.current_position_txt)
            currentTxt.text = Formater.formatDuration(position);

        }
        player.repeatMode = Player.REPEAT_MODE_ALL
        val btn = findViewById<ImageButton>(R.id.play_next_btn)
        btn.setOnClickListener(View.OnClickListener {
            player.seekToNext();
        })
        setOnChangeShuffleMode();
    }
    fun handleChangeRepeatMode(ListModeButton :List<Int>, currentRepeatMode :Int ){
        val selectedBtnId = when(currentRepeatMode){
            0->{
                R.id.exo_repeat_mode_off
            }
            1->{
                R.id.exo_repeat_mode_one
            }
            2->{
                R.id.exo_repeat_mode_all
            }
            else -> R.id.exo_repeat_mode_off
        }
        ListModeButton.forEach {
            if (it != selectedBtnId) {
                val btn = findViewById<ImageButton>(it);
                btn.visibility = View.GONE;
            }
        }
        val selectedBtn = findViewById<ImageButton>(selectedBtnId);
        selectedBtn.visibility = View.VISIBLE;
        selectedBtn.setOnClickListener(View.OnClickListener {
            player.repeatMode = when (currentRepeatMode) {
                Player.REPEAT_MODE_OFF -> Player.REPEAT_MODE_ONE
                Player.REPEAT_MODE_ONE -> Player.REPEAT_MODE_ALL
                Player.REPEAT_MODE_ALL -> Player.REPEAT_MODE_OFF
                else -> Player.REPEAT_MODE_OFF
            }
        })
    }

    private fun setOnChangeShuffleMode(){
        val shuffleOffBtn = findViewById<ImageButton>(R.id.shuffle_off_btn)
        val shuffleOnBtn = findViewById<ImageButton>(R.id.shuffle_on_btn)
        shuffleOnBtn.setOnClickListener(View.OnClickListener {
            player.shuffleModeEnabled = false;
            it.visibility = View.GONE;
            shuffleOffBtn.visibility = View.VISIBLE;
        })
        shuffleOffBtn.setOnClickListener(View.OnClickListener {
            player.shuffleModeEnabled = true;
            it.visibility = View.GONE;
            shuffleOnBtn.visibility = View.VISIBLE;

        })
    }
}