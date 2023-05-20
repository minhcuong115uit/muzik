package com.example.muzik.viewmodels.musicplayer

import android.content.Context
import android.text.TextUtils
import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.muzik.data.models.Comment
import com.example.muzik.data.models.Song
import com.example.muzik.data.models.User
import com.example.muzik.data.repositories.ReactionRepository
import com.example.muzik.data.repositories.SongRepository
import com.example.muzik.listeners.ActionPlayerListener
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import java.time.LocalDate
import java.util.UUID

//class PlayerViewModel(private val repo :ReactionRepository): ViewModel() {
class PlayerViewModel(): ViewModel() {
    private lateinit var navController: NavController
    //to observe pause or start state
    private var actionPlayerListener: ActionPlayerListener? = null
    private var _repeatState = MutableLiveData<Int>(2);
    private var _shuffleMode = MutableLiveData<Boolean>(false);
    private val _isFavorite = MutableLiveData(false)
    private val _isShowComments = MutableLiveData(false)
    private val _isLoading = MutableLiveData<Boolean>()
    private var _listSong = mutableListOf<Song>()
    private val _localListSong = mutableListOf<Song>()
    lateinit var player:ExoPlayer;
    var ellipsizeType  =  ObservableField<TextUtils.TruncateAt>(TextUtils.TruncateAt.MARQUEE)
    var currentSong = MutableLiveData<Song?>(null);

    val isPlaying: LiveData<Boolean>
        get() {
            return MutableLiveData<Boolean>(player.isPlaying?:false)
        }

    fun getListSong(): List<Song>{
        return _listSong;
    }
    fun getLocalListSong(): List<Song>{
        return _localListSong;
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
//    val player: LiveData<ExoPlayer>
//        get() = _player

    fun initPlayer(context: Context) {
        player = ExoPlayer.Builder(context).build()
        player.repeatMode = _repeatState.value!!;
        player.shuffleModeEnabled = _shuffleMode.value!!;

    }
    fun getCurrentPosition(): Long {
        return player.currentPosition ?: 0
    }
    override fun onCleared() {
        player.release()
        super.onCleared()
    }
    var commentContent = ObservableField<String>();
    private val user =  User("1212","first","last", "displayName","male","18",official = false, avatarUrl = "")
    private val _comments = MutableLiveData<MutableList<Comment>>(mutableListOf())

    val comments: LiveData<MutableList<Comment>>
        get() = _comments
    val isFavourite: LiveData<Boolean>
        get(){
            return _isFavorite
        }
    val isLoading: LiveData<Boolean>
        get() = _isLoading
    val isShowComments: LiveData<Boolean>
        get(){
            return _isShowComments
        }
    fun handleToggleHeart() {
        _isFavorite.value = !_isFavorite.value!!;
    }
    fun handleToggleShowComments(){
        _isShowComments.value = !_isShowComments.value!!
    }
    fun uploadComment(){
        _isLoading.value = true;
        val comment = Comment("3Wj9MsZv9nLwsmj75A7w", createdAt = LocalDate.now().toString(),
            content =  commentContent.get()!!, user = user,
            modifiedAt = "", hearts = listOf<UUID>()
        );
        try{
            ReactionRepository.instance!!.uploadComment(comment);
            commentContent.set("")
            _comments.value?.add(comment);
            _isLoading.value = false;

        }catch(err: Exception){
            Log.e("COMMENT",err.toString());
            _isLoading.value = false;

        }
    }
    fun loadComments(songId:String) {
        _isLoading.value = true;
        _comments.value?.clear();
        ReactionRepository.instance!!.getComments (songId,{
            cmts->
            cmts.forEach{comment ->
                _comments.value?.add(comment)
            }
            _isLoading.value = false;
        }){
            e->
            Log.e("COMMENT", "Error getting comments", e)
            _isLoading.value = false;
        }
    }
    fun getLocalMp3Files (context: Context) {
        _localListSong.clear()
        val songList = SongRepository.instance?.getDeviceMp3Files(context);
        songList?.forEach {
            val mediaItem = MediaItem.fromUri(it.songUri)
            player.addMediaItem(mediaItem);
            _localListSong.add(it);
        }
    }

    fun playSong(song: Song){
        currentSong.value = song;
        val mediaItem = MediaItem.fromUri(song.songUri);
        player.setMediaItem(mediaItem);
        player.prepare();
        player.play();
    }
    fun playSong(index: Int){
        player.seekTo(index,0);
        player.prepare();
        player.play();
        currentSong.value = _listSong[index];
    }
    fun setPlayList(list: List<Song>) {
        _listSong.clear()
        _listSong.addAll(list)
        val listMediaItems = _listSong.map { it.convertToMediaItem() }
        player.clearMediaItems()
        player.setMediaItems(listMediaItems)
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