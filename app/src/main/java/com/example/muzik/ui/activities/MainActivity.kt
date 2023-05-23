package com.example.muzik.ui.activities

import android.Manifest
import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.support.v4.media.session.MediaSessionCompat
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.muzik.R
import com.example.muzik.databinding.ActivityMainBinding
import com.example.muzik.listeners.ActionPlayerListener
import com.example.muzik.listeners.PlaySongListener
import com.example.muzik.receiver.NotificationReceiver
import com.example.muzik.services.MusicService
import com.example.muzik.ui.fragments.Library
import com.example.muzik.ui.fragments.MusicPlayer
import com.example.muzik.ui.fragments.MusicPlayerBar
import com.example.muzik.viewmodels.musicplayer.PlayerViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity(), ServiceConnection, ActionPlayerListener,
    PlaySongListener {
    private lateinit var mediaSession: MediaSessionCompat
    private lateinit var navView: BottomNavigationView
    private lateinit var musicService: MusicService
    private lateinit var viewModel: PlayerViewModel
    private lateinit var musicPlayerBarFragment: MusicPlayerBar
    private lateinit var binding: ActivityMainBinding;
    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        var fragment: Fragment = Library();
        when (item.itemId) {
            R.id.menu_home -> {
                // Xử lý khi người dùng chọn item "Home"
                return@OnNavigationItemSelectedListener true
            }
            R.id.menu_search -> {
                // Xử lý khi người dùng chọn item "Search"
                return@OnNavigationItemSelectedListener true
            }
            R.id.menu_library -> {
                fragment = Library();
                supportFragmentManager.beginTransaction().replace(R.id.main_fragment, fragment).commit()

                return@OnNavigationItemSelectedListener true
            }
            R.id.menu_upload_music -> {
                // Xử lý khi người dùng chọn item "Upload Music"
                return@OnNavigationItemSelectedListener true
            }
        }
    false
    }
    override fun onCreate(savedInstanceState: Bundle?)  {
        super.onCreate(savedInstanceState)
        binding =  DataBindingUtil.setContentView(this,R.layout.activity_main);
        navView = findViewById(R.id.bottom_nav_view)
        navView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)
        viewModel =  ViewModelProvider(this)[PlayerViewModel::class.java]
        viewModel.initPlayer(this);
        viewModel.setPlaySongListener(this);
        //service
        val intent = Intent(this,MusicService::class.java);
        mediaSession = MediaSessionCompat(this,"PlayerAudio");
        bindService(intent,this, BIND_AUTO_CREATE);

        setObservation();
    }

    fun showNotification( playPauseBtn:Int){
        val intent = Intent(this,MainActivity::class.java);
        val contentIntent:PendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )
        val preIntent = Intent(this,NotificationReceiver::class.java).setAction("PREVIOUS")
        val prePendingIntent:PendingIntent =
            PendingIntent.getBroadcast(this,0,preIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        val nextIntent = Intent(this,NotificationReceiver::class.java).setAction("NEXT")
        val nextPendingIntent:PendingIntent =
            PendingIntent.getBroadcast(this,0,nextIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        val playIntent = Intent(this,NotificationReceiver::class.java).setAction("PLAY")
        val playPendingIntent:PendingIntent =
            PendingIntent.getBroadcast(this,0,playIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        val picture: Bitmap = BitmapFactory.decodeResource(resources,R.drawable.girl_listening_to_music);
        val notification: Notification = NotificationCompat.Builder(this,"CHANNEL_2")
            .setSmallIcon(R.drawable.girl_listening_to_music)
            .setLargeIcon(picture)
            .setContentTitle(viewModel.currentSong.value?.name)
            .setContentText(viewModel.currentSong.value?.artistName)
            .addAction(R.drawable.ic_play_previous,"Previous",prePendingIntent)
            .addAction(playPauseBtn,"Play",playPendingIntent)
            .addAction(R.drawable.ic_play_next,"Next",nextPendingIntent)
            .setStyle(androidx.media.app.NotificationCompat.MediaStyle().setMediaSession(
                mediaSession.sessionToken
            ))
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setContentIntent(contentIntent)
            .setOnlyAlertOnce(true)
            .build()
        val notificationManger = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManger.notify(0,notification);
    }
    override fun nextClicked() {
        showNotification(R.drawable.ic_pause)
    }
    override fun playCLicked() {
        if(viewModel.player.isPlaying){
            showNotification(R.drawable.ic_pause);
            musicPlayerBarFragment.binding.playBtnBottomBar.setImageResource(R.drawable.ic_pause);
        }
        else{
            showNotification(R.drawable.ic_play);
            musicPlayerBarFragment.binding.playBtnBottomBar.setImageResource(R.drawable.ic_pause);
        }
    }
    override fun prevClicked() {
        showNotification(R.drawable.ic_pause)
    }
    override fun onDestroy() {
        super.onDestroy()
        unbindService(this);
    }
    override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
        val binder: MusicService.MusicBinder = service as MusicService.MusicBinder;
        musicService = binder.getService()
        musicService.setMusicPlayer(viewModel.player,this);
        Log.e("ServiceConnected", musicService.toString());
    }
    override fun onServiceDisconnected(name: ComponentName?) {
        Log.e("ServiceDisconnected", musicService.toString());
    }
    override fun playSong(songId: Int) {
        //Nếu đúng thì phát bài hát mới nếu không thì tiếp tục phát bài hát cũ
        if(songId != viewModel.currentSongIndex){
            viewModel.playSong(songId);
        }
        val musicPlayerFragment = MusicPlayer()
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.setCustomAnimations(
            R.anim.slide_up,
            R.anim.slide_down,
            R.anim.slide_up, R.anim.slide_down)
            .addToBackStack("Player")
            .add(R.id.main_bottom_fragment, musicPlayerFragment).commit()
    }
    private fun setObservation(){
        //kiểm tra nếu có bài hát thì sẽ hiển thị notification bar và music player bar
        viewModel.currentSong.observe(this){
            if(viewModel.currentSong.value != null)
            {
                musicPlayerBarFragment = MusicPlayerBar.newInstance(viewModel.currentSongIndex)
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_music_player_bar, musicPlayerBarFragment)
                    .commit()
                showNotification(R.drawable.ic_play);
            }
        }
    }
}