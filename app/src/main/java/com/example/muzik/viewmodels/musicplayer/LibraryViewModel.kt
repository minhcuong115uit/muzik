package com.example.muzik.viewmodels.musicplayer

import android.content.Context
import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.muzik.data.models.Playlist
import com.example.muzik.data.models.Song
import com.example.muzik.data.repositories.SongRepository
import com.example.muzik.listeners.PlaySongListener
import com.example.muzik.ui.activities.MainActivity
import com.example.muzik.ui.fragments.CreatePlaylist
import com.google.android.exoplayer2.MediaItem

class LibraryViewModel: ViewModel() {
    var playlistName = ObservableField<String>("");
    var isPrivatedPlaylist = ObservableField<Boolean>(false) ;
    var notifyChange = MutableLiveData(false);
    private var _playSongListener: PlaySongListener? = null;
    private val _playlist = MutableLiveData<MutableList<Playlist>>(mutableListOf())
    private val _playlistItems = MutableLiveData<MutableList<Song>>(mutableListOf())
    private val _localListSong = mutableListOf<Song>()

    //bottom sheet fragment for creating a new playlist
    private var createPlaylistFragment = CreatePlaylist();


    fun getPlaySongListener(): PlaySongListener? {
        return _playSongListener
    }
    fun setPlaySongListener(listener: PlaySongListener){
        this._playSongListener = listener
    }
    fun showDialog(context: Context) {
        createPlaylistFragment.show((context as MainActivity).supportFragmentManager, "CreatePlaylist");
    }
    fun getPlaylist(): LiveData<MutableList<Playlist>> {
        return _playlist
    }
    fun getPlaylistItems():LiveData<MutableList<Song>> {
        return _playlistItems
    }
    fun setPlaylistItems(playlistId: String){

    }
    fun setPlaylistItems(listSong: List<Song>){
        _playlistItems.value?.clear()
        _playlistItems.value?.addAll(listSong);
    }

    fun createPlaylist(){
        Log.d("PlaylistName",playlistName.get().toString())
        val newPlaylist = Playlist("","",playlistName.get().toString(),isPrivatedPlaylist.get()?: false,
            mutableListOf(),""
        )
        _playlist.value?.add(newPlaylist);
        notifyChange.value = !notifyChange.value!!;
        refreshCreatePlaylistForm();
        createPlaylistFragment.dismiss()
    }
    private fun refreshCreatePlaylistForm(){
        playlistName.set("")
        isPrivatedPlaylist.set(false);
    }
    fun getLocalListSong(): List<Song>{
        return _localListSong;
    }
    fun getLocalMp3Files (context: Context) {
        _localListSong.clear()
        val songList = SongRepository.instance?.getDeviceMp3Files(context);
        songList?.forEach {
            _localListSong.add(it);
        }
    }
}