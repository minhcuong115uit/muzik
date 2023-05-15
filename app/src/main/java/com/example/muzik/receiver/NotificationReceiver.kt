package com.example.muzik.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.example.muzik.listeners.ActionPlayerListener
import com.example.muzik.services.MusicService
import com.example.muzik.viewmodels.musicplayer.PlayerViewModel

//notification player
class NotificationReceiver(): BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        val action = intent?.action;
        val intent1 = Intent(context,MusicService::class.java);
        if(action !=null){
            when(action){
                "NEXT" ->{
                    Toast.makeText(context,"Next",Toast.LENGTH_SHORT).show();
                    intent1.putExtra("myAction",action);
                    context?.startService(intent1);
                }
                "PLAY" ->{
                    Toast.makeText(context, "STOP", Toast.LENGTH_SHORT).show();
                   intent1.putExtra("myAction",action);
                    context?.startService(intent1);
                }
                "PREVIOUS" ->{
//                    viewModel.player.value?.seekToPrevious();
                    Toast.makeText(context,"PREV",Toast.LENGTH_SHORT).show();
                    intent1.putExtra("myAction",action);
                    context?.startService(intent1);
                }
            }
        }
    }
}