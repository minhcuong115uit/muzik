package com.example.muzik.services

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import com.example.muzik.listeners.ActionPlayerListener
import com.google.android.exoplayer2.ExoPlayer

class MusicService : Service() {
    private val mBinder:IBinder = MusicBinder();
    private var player:ExoPlayer? =null;
    private var actionListener:ActionPlayerListener? = null;
    override fun onBind(intent: Intent?): IBinder? {
        return mBinder
    }
    fun setMusicPlayer (player: ExoPlayer?, listener: ActionPlayerListener){
        this.player = player;
        actionListener =listener
    }
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val action = intent?.getStringExtra("myAction");
        if(action!= null){
            when (action){
                "PLAY" ->{
                    actionListener?.playCLicked();
                    if(player?.isPlaying == true){
                        player?.stop();
                    }
                    else{
                        player?.prepare();
                        player?.play();
                    }
                }
                "PREVIOUS" ->{
                    player?.seekToPrevious();
                    actionListener?.prevClicked();
                }
                "NEXT" ->{
                    player?.seekToNext();
                    actionListener?.nextClicked();
                }
            }
        }
        return START_STICKY
    }

    inner class MusicBinder : Binder() {
        fun getService(): MusicService = this@MusicService
    }

}