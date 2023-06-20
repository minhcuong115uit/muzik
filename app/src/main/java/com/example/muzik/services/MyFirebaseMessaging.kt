package com.example.muzik.services

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.util.Log
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.example.muzik.R
import com.example.muzik.ui.activities.MainActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

const val CHANNEL_ID = "notification_channel"
const val CHANNEL_NAME = "channel_name"

class MyFirebaseMessaging :FirebaseMessagingService(){

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        if(message.notification != null){
            generateNotification(message.notification!!.title!!,message.notification!!.body!!)
        }
        Log.d("FCM_MESSAGE", "Received notification: ${message.notification?.title} - ${message.notification?.body}")
    }
    @SuppressLint("RemoteViewLayout")
    fun getRemoteView(title:String, message:String): RemoteViews{
        val remoteView = RemoteViews("com.example.muzik",R.layout.notification)
        remoteView.setTextViewText(R.id.notification_title,title)
        remoteView.setTextViewText(R.id.notification_des,message)
        remoteView.setImageViewResource(R.id.notification_img,R.drawable.girl_listening_to_music)
        return remoteView
    }
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("FCM_TOKEN", token)
        // Do further processing if needed, such as sending the token to your server
    }
    fun generateNotification(title:String, message:String){
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(
            this,
            1,
            intent,
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
        );
        var builder: NotificationCompat.Builder = NotificationCompat.Builder(applicationContext,
            CHANNEL_ID)
            .setSmallIcon(R.drawable.girl_listening_to_music)
            .setAutoCancel(true)
            .setVibrate(longArrayOf(1000,1000,1000,1000))
            .setOnlyAlertOnce(true)
            .setContentIntent(pendingIntent)
        builder = builder.setContent(getRemoteView(title,message))
        val notificationManager = getSystemService(Service.NOTIFICATION_SERVICE) as NotificationManager
        val notificationChannel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME,NotificationManager.IMPORTANCE_HIGH)
        notificationManager.createNotificationChannel(notificationChannel);
        notificationManager.notify(1,builder.build());
    }
}