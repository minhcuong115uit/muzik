package com.example.muzik.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.widget.ImageButton
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.example.muzik.R
import com.example.muzik.databinding.FragmentMusicPlayerBinding
import com.example.muzik.utils.Formatter
import com.example.muzik.viewmodels.musicplayer.ActionBarViewModel
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

private const val IS_LOCAL_SONG = "IS_LOCAL_SONG"

class MusicPlayer : Fragment() {
    lateinit var binding: FragmentMusicPlayerBinding
    private lateinit var viewModel: PlayerViewModel
    private val actionBarViewModel: ActionBarViewModel by activityViewModels()
    private lateinit var fragmentActionBar: BottomActionsBar
    private var isLocalSong = false;
    lateinit var shuffleBtn: ImageButton;
    lateinit var repeatBtn: ImageButton;
    lateinit var playNext: ImageButton;
    lateinit var playPrev: ImageButton;
    lateinit var disFragment: MusicDisc;

    //prevent activity response to onClick from fragment
    //Đoạn code này dùng để ngăn không cho click event trong Activtiy nằm dưới hoạt động
    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.setOnTouchListener { view, _ -> return@setOnTouchListener true }
        playNext = getView()?.findViewById(R.id.play_next_btn)!!
        shuffleBtn = getView()?.findViewById(R.id.shuffle_mode)!!
        repeatBtn = getView()?.findViewById(R.id.exo_repeat_mode)!!
        playPrev = getView()?.findViewById(R.id.play_prev_btn)!!
        setUpMusicPlayer();
        setBtnClickListener();
        setObservation();
        disFragment = MusicDisc.newInstance(viewModel.currentSong.value!!.imageUri);
        parentFragmentManager.beginTransaction().replace(R.id.music_disc,disFragment).commit()
        Log.i("PlayerEvents DURATION", "DURATION  ${viewModel.player.duration.toString()}")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =  DataBindingUtil.inflate(inflater,R.layout.fragment_music_player,container,false);
        viewModel = ViewModelProvider(requireActivity())[PlayerViewModel::class.java]
        binding.viewmodel= viewModel;
        viewModel.currentSong.value?.songId
        fragmentActionBar = BottomActionsBar.newInstance(viewModel.currentSong.value?.songId ?: "");
        val fragmentTransaction = parentFragmentManager.beginTransaction()
        fragmentTransaction.setCustomAnimations(R.anim.slide_up,R.anim.slide_down,R.anim.slide_up, R.anim.slide_down)
            .replace(R.id.actions_bar, fragmentActionBar).commit()

        if(isLocalSong){
            binding.actionsBar.visibility = View.INVISIBLE
        }
//        Không nên dùng viewModel.player.duration để set vì nó có thể chưa load xong
//        binding.durationTxt.text = Formater.formatDuration(viewModel.player.duration)
        binding.timeBar.setDuration(viewModel.currentSong.value?.duration ?: 0)
        binding.durationTxt.text = Formatter.formatDuration(viewModel.currentSong.value?.duration
                    ?: 0)
        binding.timeBar.setPosition(viewModel.player.currentPosition);

        return binding.root
    }
    private fun setUpMusicPlayer() {
        // Thiết lập playerControlView với player từ ViewModel
        binding.playerControlView.player = viewModel.player

        binding.playerControlView.player?.addListener(object : Player.Listener {
            // Xử lý sự kiện khi chuyển media item trong player
            override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                Log.i("PlayerEvents", "onMediaItemTransition Song Duration ${viewModel.player.duration}")
                super.onMediaItemTransition(mediaItem, reason)
                binding.durationTxt.text = Formatter.formatDuration(viewModel.currentSong.value?.duration ?: 0)
                viewModel.currentSong.value?.let { disFragment.setImage(it.imageUri) };
            }
            // Xử lý sự kiện khi trạng thái isPlaying thay đổi
            override fun onIsPlayingChanged(isPlaying: Boolean) {
                Log.i("PlayerEvents", "OnIsPlaying ${isPlaying.toString()}")
                if (isPlaying) {
                    disFragment.resumeAnimation();
                } else {
                    disFragment.stopAnimation()
                }
                viewModel.getActionPlayerListener()?.playCLicked()
            }

            // Xử lý sự kiện khi repeatMode thay đổi
            override fun onRepeatModeChanged(repeatMode: Int) {
                super.onRepeatModeChanged(repeatMode)
            }

            // Xử lý sự kiện khi trạng thái playWhenReady thay đổi
            override fun onPlayWhenReadyChanged(playWhenReady: Boolean, reason: Int) {
                Log.i("PlayerEvents", "onPlayWhenReadyChanged ${playWhenReady.toString()}")

                super.onPlayWhenReadyChanged(playWhenReady, reason)
                if (playWhenReady) {
                    disFragment.resumeAnimation()
                    viewModel.player.play()
                } else {
                    disFragment.stopAnimation()
                }
            }
        })

        // Xử lý sự kiện khi người dùng chạm vào timeBar
        binding.timeBar.addListener(object : TimeBar.OnScrubListener {
            override fun onScrubMove(timeBar: TimeBar, position: Long) {
            }
            override fun onScrubStart(timeBar: TimeBar, position: Long) {
            }
            override fun onScrubStop(timeBar: TimeBar, position: Long, canceled: Boolean) {
                viewModel.player.seekTo(position)
                binding.timeBar.setPosition(position);
                binding.currentPositionTxt.text = Formatter.formatDuration(position);
            }
        })
        //set timebar value keep updated
        binding.playerControlView.setProgressUpdateListener { position, bufferedPosition ->
            if(viewModel.player.isPlaying){
                binding.timeBar.setPosition(position)
                binding.timeBar.setBufferedPosition(bufferedPosition)
                binding.currentPositionTxt.text = Formatter.formatDuration(position);
            }
        }
    }
    private fun setObservation(){
        viewModel.currentSong.observe(viewLifecycleOwner){
            binding.artistnameTextview.text = Formatter.convertArrArtistToString(it?.artist!!)
            binding.songName.text = it?.name
            binding.titleSong.text = it?.name
        }
        actionBarViewModel.isShowComments.observe(viewLifecycleOwner) { isShowComments ->
            val alphaFrom: Float
            val alphaTo: Float
            if (isShowComments) {
                alphaFrom = 1f
                alphaTo = 0.5f
            } else {
                alphaFrom = 0.5f
                alphaTo = 1f
            }

            val animation = AlphaAnimation(alphaFrom, alphaTo)
            animation.duration = 800
            animation.fillAfter = true
            binding.playerBody.startAnimation(animation)
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
    private fun setBtnClickListener(){
        // Thiết lập sự kiện cho nút playNext
        playNext.setOnClickListener {
            viewModel.playNext()

        }
        playPrev.setOnClickListener {
            viewModel.playPrev()
        }
        // Thiết lập sự kiện cho nút shuffleBtn
        shuffleBtn.setOnClickListener(View.OnClickListener {
            viewModel.setShuffleMode(!viewModel.shuffleMode.value!!)
        })

        // Thiết lập sự kiện cho nút repeatBtn
        repeatBtn.setOnClickListener(View.OnClickListener {
            // Xác định chế độ repeat mới dựa trên giá trị hiện tại của repeatState
            val newRepeatMode = if (viewModel.repeatState.value == 2) 0 else viewModel.repeatState.value!!.plus(1)
            viewModel.setRepeatState(newRepeatMode)
        })
        binding.backButton.setOnClickListener {
            parentFragmentManager.popBackStack();
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            isLocalSong = it.getBoolean(IS_LOCAL_SONG)
        }
    }
    companion object {
        @JvmStatic
        fun newInstance(isLocalSong: Boolean) =
            MusicPlayer().apply {
                arguments = Bundle().apply {
                    putBoolean(IS_LOCAL_SONG, isLocalSong)
                }
            }
    }
}