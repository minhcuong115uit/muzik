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
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.support.v4.media.session.MediaSessionCompat
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.muzik.R
import com.example.muzik.data.models.Song
import com.example.muzik.databinding.ActivityMainBinding
import com.example.muzik.listeners.ActionPlayerListener
import com.example.muzik.listeners.PlaySongListener
import com.example.muzik.receiver.NotificationReceiver
import com.example.muzik.services.MusicService
import com.example.muzik.ui.fragments.Home
import com.example.muzik.ui.fragments.Library
import com.example.muzik.ui.fragments.MusicPlayer
import com.example.muzik.ui.fragments.MusicPlayerBar
import com.example.muzik.utils.ImageHelper
import com.example.muzik.viewmodels.musicplayer.PlayerViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.runBlocking
import java.util.Formatter

class MainActivity : AppCompatActivity(), ServiceConnection, ActionPlayerListener,
    PlaySongListener {
    private lateinit var mediaSession: MediaSessionCompat
    private lateinit var navView: BottomNavigationView
    private lateinit var musicService: MusicService
    private lateinit var viewModel: PlayerViewModel
    private lateinit var musicPlayerBarFragment: MusicPlayerBar
    private lateinit var binding: ActivityMainBinding;
    private lateinit var libFragment: Fragment
    private lateinit var homeFragment: Fragment
    private var notificationVisibility = NotificationCompat.VISIBILITY_PUBLIC
    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->

        when (item.itemId) {
            R.id.menu_home -> {
                supportFragmentManager.beginTransaction().replace(R.id.main_fragment, homeFragment).commit()
                // Xử lý khi người dùng chọn item "Home"
                return@OnNavigationItemSelectedListener true
            }
            R.id.menu_search -> {
                // Xử lý khi người dùng chọn item "Search"
                return@OnNavigationItemSelectedListener true
            }
            R.id.menu_library -> {
                supportFragmentManager.beginTransaction().replace(R.id.main_fragment, libFragment).commit()

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

        viewModel =  ViewModelProvider(this)[PlayerViewModel::class.java]
        viewModel.initialize(this);
        viewModel.initPlayer(this);
        viewModel.setPlaySongListener(this);

        initFragments();
        navView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)
        //service
        val intent = Intent(this,MusicService::class.java);
        mediaSession = MediaSessionCompat(this,"PlayerAudio");
        bindService(intent,this, BIND_AUTO_CREATE);
        askNotificationPermission();

        setObservation();
    }
    private fun initFragments() {
        libFragment = Library();
        homeFragment = Home();
    }
    // Declare the launcher at the top of your Activity/Fragment:
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission(),
    ) { isGranted: Boolean ->
        if (isGranted) {
           Log.d("Notification","Allowed");
        } else {
            Log.d("Notification","Denied");
        }
    }

    private fun askNotificationPermission() {
        // This is only necessary for API level >= 33 (TIRAMISU)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Log.d("Notification","Asking");
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) ==
                PackageManager.PERMISSION_GRANTED
            ) {
                // FCM SDK (and your app) can post notifications.
            } else if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {

            } else {
                // Directly ask for the permission
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }
    fun showNotification( playPauseBtn:Int, imageUrl: String? = null){
        val intent = Intent(this,MainActivity::class.java);
        Log.d("Notification Visibility",notificationVisibility.toString());
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
        var picture:Bitmap? = if(!imageUrl.isNullOrEmpty()){
            runBlocking {
               ImageHelper.getBitmapFromURL(imageUrl)
            }
        } else{
            BitmapFactory.decodeResource(resources,R.drawable.girl_listening_to_music);
        }
        val notification: Notification = NotificationCompat.Builder(this,"CHANNEL_2")
            .setSmallIcon(R.drawable.girl_listening_to_music)
            .setLargeIcon(picture)
            .setVisibility(notificationVisibility)
            .setContentTitle(viewModel.currentSong.value?.name)
            .setContentText(com.example.muzik.utils.Formatter.convertArrArtistToString(viewModel.currentSong.value?.artist!!))
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
    override fun nextClicked( ) {
        showNotification(R.drawable.ic_pause, viewModel.currentSong.value?.imageUri)
    }

    override fun onPause() {
        super.onPause()
        if(viewModel.currentSong.value != null){
            notificationVisibility = NotificationCompat.VISIBILITY_PUBLIC
            Log.d("PlayerIsPlaying" , viewModel.player.isPlaying.toString())
            showNotification(if( viewModel.player.isPlaying) R.drawable.ic_pause else R.drawable.ic_play, viewModel.currentSong.value?.imageUri)
        }
    }

    override fun onResume() {
        super.onResume()
//        if(viewModel.currentSong.value != null){
//            notificationVisibility = NotificationCompat.VISIBILITY_SECRET
//            Log.d("PlayerIsPlaying" , viewModel.player.isPlaying.toString())
//            showNotification(R.drawable.ic_pause, viewModel.currentSong.value?.imageUri)
//        }
        val notificationManger = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManger.cancel(0)
    }

    override fun playCLicked() {
        if(viewModel.player.isPlaying){
            showNotification(R.drawable.ic_play,viewModel.currentSong.value?.imageUri);
            musicPlayerBarFragment.binding.playBtnBottomBar.setImageResource(R.drawable.ic_play);
        }
        else{
            showNotification(R.drawable.ic_pause,viewModel.currentSong.value?.imageUri);
            musicPlayerBarFragment.binding.playBtnBottomBar.setImageResource(R.drawable.ic_pause);
        }
    }
    override fun prevClicked() {
        showNotification(R.drawable.ic_pause, viewModel.currentSong.value?.imageUri)
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
    override fun playSong(songIndex: Int, song:Song) {
        //Nếu đúng thì phát bài hát mới nếu không thì tiếp tục phát bài hát cũ
        if(songIndex != viewModel.currentSongIndex){
            viewModel.playSong(songIndex, song);
        }
//        if(viewModel.currentSong.value.isLocalSong)
        showPlayerScreen();
    }
    override fun playSong(song: Song) {
        if(song.songId != viewModel.currentSong.value!!.songId){
            viewModel.playSong(song);
        }
        showPlayerScreen();

    }
    private fun showPlayerScreen () {
        val musicPlayerFragment = MusicPlayer.newInstance(viewModel.currentSong.value!!.isLocalSong)
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
                musicPlayerBarFragment = MusicPlayerBar.newInstance(viewModel.currentSongIndex, viewModel.currentSong.value!!)
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_music_player_bar, musicPlayerBarFragment)
                    .commit()
//                showNotification(R.drawable.ic_play, viewModel.currentSong.value!!.imageUri);
            }
        }
    }
}