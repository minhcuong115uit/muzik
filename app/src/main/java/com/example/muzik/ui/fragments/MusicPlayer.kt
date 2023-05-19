package com.example.muzik.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import com.example.muzik.R
import com.example.muzik.databinding.FragmentMusicPlayerBinding
import com.example.muzik.ui.activities.MainActivity
import com.example.muzik.utils.Formater
import com.example.muzik.viewmodels.musicplayer.PlayerViewModel
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ui.TimeBar

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
    private lateinit var fragmentActionBar: BottomActionsBar
    lateinit var shuffleBtn: ImageButton;
    lateinit var repeatBtn: ImageButton;
    lateinit var playNext: ImageButton;
    lateinit var disFragment: MusicDisc;
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
        playNext = getView()?.findViewById<ImageButton>(R.id.play_next_btn)!!
        shuffleBtn = getView()?.findViewById(R.id.shuffle_mode)!!
        repeatBtn = getView()?.findViewById(R.id.exo_repeat_mode)!!
        setUpMusicPlayer();
        setObservations();
        disFragment = MusicDisc()
        parentFragmentManager.beginTransaction().replace(R.id.music_disc,disFragment).commit()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentActionBar = BottomActionsBar();
        val fragmentTransaction = parentFragmentManager.beginTransaction()
        fragmentTransaction.setCustomAnimations(R.anim.slide_up,R.anim.slide_down,R.anim.slide_up, R.anim.slide_down)
            .replace(R.id.actions_bar, fragmentActionBar).commit()
        binding =  DataBindingUtil.inflate(inflater,R.layout.fragment_music_player,container,false);

        viewModel = ViewModelProvider(requireActivity())[PlayerViewModel::class.java]
        binding.backButton.setOnClickListener {
            parentFragmentManager.popBackStack();
        }

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
    private fun setUpMusicPlayer(){
        playNext.setOnClickListener {
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
            val durationText = binding.durationTxt

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

        //handle user click on timeBar
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
        //set timebar value keep updated
        binding.playerControlView.setProgressUpdateListener { position, bufferedPosition ->
            binding.timeBar.setPosition(position)
            binding.timeBar.setBufferedPosition(bufferedPosition)
            binding.currentPositionTxt.text = Formater.formatDuration(position);

        }
    }
    private fun setObservations(){
        viewModel.isShowComments.observe(viewLifecycleOwner) { isShowComments ->
            if (isShowComments) {
                binding.playerBody.alpha = 0.2F
            } else {
                binding.playerBody.alpha = 1F
            }
        }
        viewModel.repeatState.observe(viewLifecycleOwner){
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
        viewModel.shuffleMode.observe(viewLifecycleOwner){
            if(it == true){
                shuffleBtn.setImageResource(R.drawable.ic_play_random)
            }
            else{
                shuffleBtn.setImageResource(R.drawable.ic_play_random_off)
            }
        }
    }
}