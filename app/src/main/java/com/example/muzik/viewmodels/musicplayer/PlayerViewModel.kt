package com.example.muzik.viewmodels.musicplayer

import android.content.Context
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
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.firebase.auth.FirebaseAuth
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Date
import java.util.Locale
import java.util.UUID

//class PlayerViewModel(private val repo :ReactionRepository): ViewModel() {
class PlayerViewModel(): ViewModel() {
    private lateinit var navController: NavController
    private var _repeatState = MutableLiveData<Int>(2);
    private var _shuffleMode = MutableLiveData<Boolean>(false);
    private val _currentMediaItem = MutableLiveData<MediaItem>()
    private val _player = MutableLiveData<ExoPlayer>()
    private val _isFavorite = MutableLiveData(false)
    private val _isShowComments = MutableLiveData(false)
    private val _isLoading = MutableLiveData<Boolean>()
    private val _listSong = mutableListOf<Song>().apply {
        this.add(Song("","","Đã lỡ yêu em nhiều","Justatee"))
        this.add(Song("","","Dù cho mai về sau","Bùi Trường Linh"))
        this.add(Song("","","Missing you","Phương Ly"))
    }
    private val _localListSong = mutableListOf<Song>()

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
        _player.value?.repeatMode = value
    }
    fun setShuffleMode(value:Boolean){
        _shuffleMode.value = value;
        _player.value?.shuffleModeEnabled = value
    }
    val player: LiveData<ExoPlayer>
        get() = _player

    val currentMediaItem: LiveData<MediaItem>
        get() = _currentMediaItem

    fun initPlayer(context: Context) {
        _player.value = ExoPlayer.Builder(context).build()
//        val firstItem = MediaItem.fromUri("https://p.scdn.co/mp3-preview/0496b1c18c7653d9124a2f39e148ec3babcae737?cid=cfe923b2d660439caf2b557b21f31221")
//        val secItem = MediaItem.fromUri(" https://www.learningcontainer.com/wp-content/uploads/2020/02/Kalimba.mp3")
//        _player.value?.addMediaItem(firstItem)
//        _player.value?.addMediaItem(secItem)
//        _player.value?.prepare()
//        _currentMediaItem.value = firstItem
        _player.value?.repeatMode = _repeatState.value!!;
        _player.value?.shuffleModeEnabled = _shuffleMode.value!!;

    }
    fun getCurrentPosition(): Long {
        return _player.value?.currentPosition ?: 0
    }

    fun setCurrentMediaItem(mediaItem: MediaItem) {
        _currentMediaItem.value = mediaItem
        _player.value?.setMediaItem(mediaItem)
        _player.value?.prepare()
        _player.value?.play()
    }
    override fun onCleared() {
        _player.value?.release()
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
    fun setNavController(navController: NavController) {
        this.navController = navController
    }
    fun getNavController(): NavController {
        return navController
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
        val songList = SongRepository.instance?.getDeviceMp3Files(context);
        songList?.forEach {
            val mediaItem = MediaItem.fromUri(it.songUri)
            _player.value?.addMediaItem(mediaItem);
            _localListSong.add(it);
        }
    }
}