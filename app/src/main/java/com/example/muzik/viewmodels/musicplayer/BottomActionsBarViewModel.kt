package com.example.muzik.viewmodels.musicplayer

import android.widget.Button
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class BottomActionsBarViewModel: ViewModel() {

    private val _isFavorite = MutableLiveData(false)
    val isFavourite: LiveData<Boolean>
        get(){
            return _isFavorite
        }


    fun handleToggleHeart() {
        _isFavorite.value = !_isFavorite.value!!;
    }
}