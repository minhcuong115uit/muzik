package com.example.muzik.viewmodels

import android.content.Context
import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.muzik.data.models.Song
import com.example.muzik.data.repositories.SongRepository
import com.example.muzik.listeners.PlaySongListener
import com.example.muzik.ui.fragments.CreatePlaylist

class HomeViewModel: ViewModel() {
    var playlistName = ObservableField<String>("");
    var notifyChange = MutableLiveData(false);
    private var _playSongListener: PlaySongListener? = null;
    private val _listRemoteSongs = mutableListOf<Song>()
    var isGettingSongs = MutableLiveData(false);
    init {
        getRemoteSongsFromDb();
    }
    fun getRemoteSongsFromDb () {
        isGettingSongs.value = true;
        SongRepository.instance?.getRemoteSongs(onSuccess = {list ->
            _listRemoteSongs.clear();
            _listRemoteSongs.addAll(list);
            Log.d("SONG","Get songs successfully ${list.size}")
            isGettingSongs.value = false;
        }, onFailure = {
            Log.e("SONG","Failed to get songs ${it.message}")
            isGettingSongs.value = false;
        })
    }
    fun getPlaySongListener(): PlaySongListener? {
        return _playSongListener
    }
    fun setPlaySongListener(listener: PlaySongListener){
        this._playSongListener = listener
    }
    private fun refreshCreatePlaylistForm(){
        playlistName.set("")
    }
    fun getListRemoteSongs(): List<Song>{
        return _listRemoteSongs;
    }
    fun getRemoteSongsFromDb (context: Context) {
        _listRemoteSongs.clear()
        val songList = SongRepository.instance?.getDeviceMp3Files(context);
        songList?.forEach {
            _listRemoteSongs.add(it);
        }
    }
}