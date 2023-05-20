package com.example.muzik.ui.fragments

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.widget.ImageButton
import androidx.core.content.ContextCompat.getColor
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.muzik.R
import com.example.muzik.databinding.FragmentMusicPlayerBinding
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
    lateinit var binding: FragmentMusicPlayerBinding
    private lateinit var viewModel: PlayerViewModel
    private lateinit var fragmentActionBar: BottomActionsBar
    lateinit var shuffleBtn: ImageButton;
    lateinit var repeatBtn: ImageButton;
    lateinit var playNext: ImageButton;
    lateinit var playPrev: ImageButton;
    lateinit var disFragment: MusicDisc;

    //prevent activity response to onClick
    //Đoạn code này dùng để ngăn không cho click event trong Activtiy nằm dưới hoạt động
    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.setOnTouchListener { view, motionEvent -> return@setOnTouchListener true }
        playNext = getView()?.findViewById(R.id.play_next_btn)!!
        shuffleBtn = getView()?.findViewById(R.id.shuffle_mode)!!
        repeatBtn = getView()?.findViewById(R.id.exo_repeat_mode)!!
        playPrev = getView()?.findViewById(R.id.play_prev_btn)!!
        setUpMusicPlayer();
        setBtnClickListener();
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
        binding.viewmodel= viewModel;

        return binding.root
    }
    private fun setUpMusicPlayer() {

        // Thiết lập playerControlView với player từ ViewModel
        binding.playerControlView.player = viewModel.player

        // Đăng ký nghe sự kiện cho player
        binding.playerControlView.player?.addListener(object : Player.Listener {
            val durationText = binding.durationTxt

            // Xử lý sự kiện khi chuyển media item trong player
            override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                super.onMediaItemTransition(mediaItem, reason)
                val currentIndex = viewModel.player.currentMediaItemIndex
                viewModel.currentSong.value = viewModel.getListSong()[currentIndex]
            }

            // Xử lý sự kiện khi trạng thái isPlaying thay đổi
            override fun onIsPlayingChanged(isPlaying: Boolean) {
                if (isPlaying) {
                    viewModel.player.duration.let {
                        durationText.text =  Formater.formatDuration(it)
                        binding.timeBar.setDuration(it)
                    }
                } else {
                    disFragment.stopAnimation()
                }
                viewModel.getActionPlayerListener()?.playCLicked()
            }

            // Xử lý sự kiện khi repeatMode thay đổi
            override fun onRepeatModeChanged(repeatMode: Int) {
                super.onRepeatModeChanged(repeatMode)
                Log.i("RepeatModeChanged", repeatMode.toString())
            }

            // Xử lý sự kiện khi trạng thái playWhenReady thay đổi
            override fun onPlayWhenReadyChanged(playWhenReady: Boolean, reason: Int) {
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
                // Xử lý khi người dùng di chuyển thanh tua
            }

            override fun onScrubStart(timeBar: TimeBar, position: Long) {
                // Tạm dừng player khi người dùng bắt đầu tua
                viewModel.player.playWhenReady = false
            }

            override fun onScrubStop(timeBar: TimeBar, position: Long, canceled: Boolean) {
                // Bắt đầu lại Player khi người dùng kết thúc tua
                viewModel.player.seekTo(position)
                viewModel.player.playWhenReady = true
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
        viewModel.currentSong.observe(viewLifecycleOwner){
            binding.artistnameTextview.text = it?.artistName
            binding.songName.text = it?.name
            binding.titleSong.text = it?.name
        }
        viewModel.isShowComments.observe(viewLifecycleOwner) { isShowComments ->
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
}