package com.example.muzik.viewmodels.musicplayer

import android.content.Context
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.widget.CheckBox
import androidx.compose.ui.window.Dialog
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.muzik.R
import com.example.muzik.data.models.Comment
import com.example.muzik.data.models.Playlist
import com.example.muzik.data.models.Song
import com.example.muzik.data.models.User
import com.example.muzik.data.repositories.ReactionRepository
import com.example.muzik.data.repositories.SongRepository
import com.example.muzik.listeners.ActionPlayerListener
import com.example.muzik.listeners.PlaySongListener
import com.example.muzik.ui.activities.MainActivity
import com.example.muzik.ui.fragments.CreatePlaylist
import com.example.muzik.utils.Formater
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.time.LocalDate
import java.util.UUID

//class PlayerViewModel(private val repo :ReactionRepository): ViewModel() {
class PlayerViewModel(): ViewModel() {
    private var actionPlayerListener: ActionPlayerListener? = null
    private var _playSongListener: PlaySongListener? = null;
    private var _repeatState = MutableLiveData<Int>(2);
    private var _shuffleMode = MutableLiveData<Boolean>(false);
    private var _listSong = mutableListOf<Song>()

    lateinit var player:ExoPlayer;
    var currentSongIndex = -1;
    var ellipsizeType  =  ObservableField<TextUtils.TruncateAt>(TextUtils.TruncateAt.MARQUEE)
    var currentSong = MutableLiveData<Song?>(null);


    fun getPlaySongListener(): PlaySongListener? {
        return _playSongListener;
    }
    fun setPlaySongListener(listener: PlaySongListener){
        this._playSongListener = listener
    }
    fun getListSong(): List<Song>{
        return _listSong;
    }

    val repeatState:LiveData<Int>
        get() {
            return _repeatState
        }
    val shuffleMode:LiveData<Boolean>
        get() {
            return _shuffleMode
        }
    fun setRepeatState(value:Int){
        _repeatState.value = value;
        player.repeatMode = value
    }
    fun setShuffleMode(value:Boolean){
        _shuffleMode.value = value;
        player.shuffleModeEnabled = value
    }
    fun initPlayer(context: Context) {
        player = ExoPlayer.Builder(context).build()
        player.repeatMode = _repeatState.value!!;
        player.shuffleModeEnabled = _shuffleMode.value!!;
        player.addListener(object: Player.Listener {
            override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                Log.i("PlayerEvents", "onMediaItemTransition ${reason.toString()}")
                super.onMediaItemTransition(mediaItem, reason)
                val newIndex = player.currentMediaItemIndex
                currentSongIndex = newIndex
                currentSong.value = _listSong[currentSongIndex]
            }
        })
    }
    override fun onCleared() {
        player.release()
        super.onCleared()
    }


    fun playSong(song: Song){
        currentSong.value = song;
        val mediaItem = MediaItem.fromUri(song.songUri);
        player.setMediaItem(mediaItem);
        player.prepare();
        player.play();
    }
    fun playSong(songIndex: Int){
        player.seekTo(songIndex,0);
        player.prepare();
        player.play();
        currentSong.value = _listSong[songIndex];
        currentSongIndex = songIndex
    }
    fun setMediaPlaylist(list: List<Song>) {
        _listSong.clear()
        _listSong.addAll(list)
        val listMediaItems = _listSong.map { it.convertToMediaItem() }
        player.clearMediaItems()
        player.setMediaItems(listMediaItems)
        currentSongIndex = player.currentMediaItemIndex
    }
    fun setActionPlayerListener(listener: ActionPlayerListener){
        this.actionPlayerListener = listener
    }
    fun getActionPlayerListener(): ActionPlayerListener?{
        return this.actionPlayerListener
    }
    fun playNext(){
        player.seekToNext()
        val index = player.currentMediaItemIndex
        currentSong.value = _listSong[index]
    }
    fun playPrev(){
        player.seekToPrevious();
        val index = player.currentMediaItemIndex
        currentSong.value = _listSong[index]
    }

}