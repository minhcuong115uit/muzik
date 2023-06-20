package com.example.muzik.viewmodels

import android.content.Context
import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.muzik.data.models.Playlist
import com.example.muzik.data.models.Song
import com.example.muzik.data.repositories.PlaylistRepository
import com.example.muzik.data.repositories.SongRepository
import com.example.muzik.listeners.PlaySongListener
import com.example.muzik.ui.activities.MainActivity
import com.example.muzik.ui.fragments.CreatePlaylist
import com.example.muzik.viewmodels.authentication.AuthViewModel
import com.google.firebase.Timestamp
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class LibraryViewModel: ViewModel() {
    private var _playSongListener: PlaySongListener? = null;
    private val _playlist = MutableLiveData<MutableList<Playlist>>(mutableListOf())
    private val _playlistItems = MutableLiveData<MutableList<Song>>(mutableListOf())
    private val user = AuthViewModel.getUser()
    private val _localListSong = mutableListOf<Song>()
    //bottom sheet fragment for creating a new playlist
    private var createPlaylistFragment = CreatePlaylist();
    var playlistName = ObservableField<String>("");
    var isPrivatedPlaylist = ObservableField<Boolean>(false) ;
    var notifyChange = MutableLiveData(false);
    var isLoading = MutableLiveData(false);
    init {
        runBlocking  {
            getPlaylists()
        }
    }
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


    fun addSongToPlaylist(playlistId: String, songId: String) {
        PlaylistRepository.instance?.addSongToPlaylist(playlistId, songId) {
            // Handle success
        }
    }

    fun removeSongFromPlaylist(playlistId: String, songId: String) {
        PlaylistRepository.instance?.deleteSongFromPlaylist(playlistId, songId) {
            // Handle success
        }
    }

    fun createPlaylist() {
        isLoading.value = true;
        val newPlaylist = Playlist("","",playlistName.get().toString(),isPrivatedPlaylist.get()?: false,
            mutableListOf(), userId = user!!.userId, createdAt = Timestamp.now()
        )
        PlaylistRepository.instance?.createPlaylist(newPlaylist,{ newPlist ->
            Log.d("PlaylistName",playlistName.get().toString())
            _playlist.value?.add(newPlist);
            notifyChange.value = !notifyChange.value!!;
            refreshCreatePlaylistForm();
            createPlaylistFragment.dismiss()
            isLoading.value = false;

        }){
            isLoading.value = false;
        }
    }
    fun updatePlaylist(playlistId: String, fieldName: String, value: Any) {
        PlaylistRepository.instance?.updatePlaylist(playlistId, fieldName, value) {
            val updatedPlaylist = _playlist.value?.map {
                if (it.playlistId == playlistId) {
                    when (fieldName) {
                        "name" -> it.copy(name = value as String)
                        "isPrivate" -> it.copy(isPrivate = value as Boolean)
                        else -> it
                    }
                } else {
                    it
                }
            }
            _playlist.value?.clear()
            _playlist.value?.addAll(updatedPlaylist!!)
            notifyChange.value = !notifyChange.value!!
        }
    }

    fun deletePlaylist(playlistId: String) {
        PlaylistRepository.instance?.deletePlaylist(playlistId) {
            val newPlaylist = _playlist.value?.filter {
                it.playlistId != playlistId
            }
            _playlist.value?.clear()
            _playlist.value?.addAll(newPlaylist!!)
            notifyChange.value = !notifyChange.value!!;
        }
    }


    suspend fun getPlaylists() {
        isLoading.value = true;
        try {
            val playlists = PlaylistRepository.instance?.getPlaylists()
            _playlist.value = playlists?.toMutableList()
            Log.d("Playlist", "get playlists successfully")
            isLoading.value = false;
        } catch (e: Exception) {
            Log.e("Playlist", "cannot get playlists")
            isLoading.value = false;
        }
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