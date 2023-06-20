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
import com.example.muzik.data.models.Song
import com.example.muzik.databinding.FragmentMusicPlayerBarBinding
import com.example.muzik.listeners.ActionPlayerListener
import com.example.muzik.ui.activities.MainActivity
import com.example.muzik.viewmodels.musicplayer.PlayerViewModel
import com.squareup.picasso.Picasso
import java.io.Serializable


class MusicPlayerBar : Fragment(), ActionPlayerListener {
    lateinit var binding:FragmentMusicPlayerBarBinding;
    lateinit var viewModel: PlayerViewModel
    private var SongIndex: Int? = -1
    private var song: Song? = null;
    lateinit var mainActivity: MainActivity
    override fun onAttach(context: Context) {
        super.onAttach(context)
        viewModel = ViewModelProvider(requireActivity())[PlayerViewModel::class.java]
        mainActivity = requireActivity() as MainActivity;
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            SongIndex = it.getInt(SONG_INDEX)
            song = it.getParcelable<Song>(SONG_KEY);
        }
    }
    companion object {
        const val SONG_INDEX = "SongIndex"
        const val SONG_KEY = "SongKey"
        @JvmStatic
        fun newInstance(songId: Int, song: Song): MusicPlayerBar {
            val fragment = MusicPlayerBar()
            val args = Bundle().apply {
                putInt(SONG_INDEX, songId)
                putParcelable(SONG_KEY, song)
            }
            fragment.arguments = args
            return fragment
        }

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
        if(song?.imageUri?.isNotEmpty() == true){
            Picasso.get().load(song?.imageUri).into(binding.playerBarImage)
        }
        viewModel.setActionPlayerListener(this);
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.setOnClickListener {
            SongIndex?.let { it1 -> song?.let { it2 ->
                viewModel.getPlaySongListener()?.playSong(it1,
                    it2
                )
            } }
        }
    }
    private fun setBtnClickListener(){
        binding.nextBtnBottomBar.setOnClickListener {
            viewModel.playNext()
//            mainActivity.showNotification(R.drawable.ic_pause, viewModel.currentSong.value?.imageUri)
        }
        binding.prevBtnBottomBar.setOnClickListener {
//            viewModel.player.seekToPrevious();
            viewModel.playPrev()
//            mainActivity.showNotification(R.drawable.ic_pause, viewModel.currentSong.value?.imageUri)
        }
        binding.playBtnBottomBar.setOnClickListener {
            if(viewModel.player.isPlaying){
                viewModel.player.stop();
//                mainActivity.showNotification(R.drawable.ic_play , viewModel.currentSong.value?.imageUri)
                (it as ImageButton).setImageResource(R.drawable.ic_play);
                viewModel.ellipsizeType.set(TextUtils.TruncateAt.END)
            }
            else{
//                mainActivity.showNotification(R.drawable.ic_pause, viewModel.currentSong.value?.imageUri)
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