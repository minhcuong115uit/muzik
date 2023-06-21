package com.example.muzik.viewmodels.musicplayer

import android.content.Context
import android.os.CountDownTimer
import android.text.TextUtils
import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.muzik.data.models.Song
import com.example.muzik.data.repositories.ReactionRepository
import com.example.muzik.data.repositories.SongRepository
import com.example.muzik.listeners.ActionPlayerListener
import com.example.muzik.listeners.PlaySongListener
import com.example.muzik.listeners.ShowBottomSheetListener
import com.example.muzik.utils.Formatter
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

//class PlayerViewModel(private val repo :ReactionRepository): ViewModel() {
class PlayerViewModel(): ViewModel() {
    private var actionPlayerListener: ActionPlayerListener? = null
    private var _playSongListener: PlaySongListener? = null;
    private var _showBottomSheetMusic: ShowBottomSheetListener? = null;
    private var _repeatState = MutableLiveData<Int>(2);
    private var _shuffleMode = MutableLiveData<Boolean>(false);
    private var _listSong = mutableListOf<Song>()  // local + remote songs
    private var _currentPlaylistSong = mutableListOf<Song>() // current songs set for player
    private var _listRemoteSongs = mutableListOf<Song>()
    private var _listLocalSongs = mutableListOf<Song>()
    lateinit var player:ExoPlayer;
    private var timer: CountDownTimer? = null
    var isGettingSongs = MutableLiveData(false);
    var currentTimerValue =  ObservableField<String>("")
    var currentSongIndex = -1;
    var ellipsizeType  =  ObservableField(TextUtils.TruncateAt.MARQUEE)
    var currentSong = MutableLiveData<Song?>(null);
    fun initialize(context: Context) {
        viewModelScope.launch {
            _listSong.clear() // localsongs + remotesongs
            _listRemoteSongs.clear()
            _listLocalSongs.clear();
            isGettingSongs.value = true
            //get device songs
            getDeviceSongs(context)
            //get remote songs
            getRemoteSongsFromDb();
            combineLists()
        }
    }
    private fun combineLists() {
        _listSong.clear()
        val list = mutableListOf<Song>()
        list.addAll(_listRemoteSongs)
        list.addAll(_listLocalSongs)
        isGettingSongs.value = false
        _listSong = list
    }
    suspend fun getRemoteSongsFromDb() = suspendCoroutine<Unit> { continuation ->
        SongRepository.instance?.getRemoteSongs(onSuccess = { list ->
            _listRemoteSongs.clear()
            _listRemoteSongs.addAll(list)
            Log.d("SONG", "Get songs successfully ${list.size}")
            continuation.resume(Unit)
        }, onFailure = {
            Log.e("SONG", "Failed to get songs ${it.message}")
            continuation.resumeWithException(it)
        })
    }
    private fun getDeviceSongs (context: Context) {
        _listLocalSongs.clear()
        val songList = SongRepository.instance?.getDeviceMp3Files(context);
        songList?.forEach {
            _listLocalSongs.add(it);
        }
    }
    fun getListLocalSong(): MutableList<Song> {
        return _listLocalSongs
    }
    fun getPlaySongListener(): PlaySongListener? {
        return _playSongListener;
    }
    fun setPlaySongListener(listener: PlaySongListener){
        this._playSongListener = listener
    }
    fun getShowBottomSheetMusic(): ShowBottomSheetListener? {
        return _showBottomSheetMusic;
    }

    fun setTimer(time: String) {
        // Hủy bỏ timer cũ (nếu có)
        timer?.cancel()

        // Chuyển đổi giá trị time sang miliseconds
        val timeInMillis = time.toLongOrNull()?.times(60000) ?: 0
        Log.d("Timer","$timeInMillis")
        // Tạo timer mới với giá trị mới
        timer = object : CountDownTimer(timeInMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val minutes = (millisUntilFinished / 1000) / 60
                val seconds = (millisUntilFinished / 1000) % 60
                currentTimerValue.set(String.format("%02d:%02d", minutes, seconds))

            }

            override fun onFinish() {
                // Dừng Player khi hết thời gian
                Log.d("Timer","Finish")
                currentTimerValue.set("")
                if(player.isPlaying)
                    player.pause()
            }
        }
        timer?.start()
    }

    fun showBottomDialog(){
        currentSong.value?.let { this.getShowBottomSheetMusic()?.showBottomSheet(it) };
    }

    fun setShowBottomSheetMusic(listener: ShowBottomSheetListener){
        this._showBottomSheetMusic = listener
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
                super.onMediaItemTransition(mediaItem, reason)
                try{
                    val newIndex = player.currentMediaItemIndex
                    currentSongIndex = newIndex
                    currentSong.value = _currentPlaylistSong[currentSongIndex]
                }
                catch (e:Exception){
                    Log.e("Exception", "onMediaItemTransition er ${e.message}")
                }
            }
        })
    }
    override fun onCleared() {
        player.release()
        super.onCleared()
    }
    fun getCurrentArtistName(): String{
        return Formatter.convertArrArtistToString(currentSong.value!!.artist);
    }

    fun playSong(song: Song){
        currentSong.value = song;
        val mediaItem = MediaItem.fromUri(song.uri);
        player.setMediaItem(mediaItem);
        actionPlayerListener?.playCLicked()
        player.prepare();
        player.play();
    }
    fun playSong(index: Int, song: Song){
        player.seekTo(index,0);
        player.prepare();
        player.play();
        currentSong.value = song;
        currentSongIndex = index
    }
    fun setMediaPlaylist(list: List<Song>) {
        _currentPlaylistSong.clear()
        _currentPlaylistSong.addAll(list)
        Log.d("MediaPlaylist", "New media playlist is set ${list.size}")
        val listMediaItems = list.map { it.convertToMediaItem() }
        player.clearMediaItems()
        player.setMediaItems(listMediaItems)
//        currentSongIndex = -1
//        currentSong.value = null
    }
    fun getSongByIds(listIds :List<String>): List<Song>{
        return  _listSong.filter { it.songId in listIds }
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
        currentSong.value = _currentPlaylistSong[index]
        actionPlayerListener?.nextClicked()
    }
    fun playPrev(){
        player.seekToPrevious();
        val index = player.currentMediaItemIndex
        currentSong.value = _currentPlaylistSong[index]
        actionPlayerListener?.prevClicked()
    }
}