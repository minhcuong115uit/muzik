package com.example.muzik.utils

import android.app.Application
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build

class ApplicationClass : Application(){
    val CHANNEL_ID_1 = "CHANNEL_1";
    val CHANNEL_ID_2 = "CHANNEL_2";
    val ACTION_NEXT  = "NEXT";
    val ACTION_PREV = "PREVIOUS";
    val ACTION_PLAY = "PLAY"
    override fun onCreate() {
        super.onCreate()
        createNotificationChannel();
    }
    private fun createNotificationChannel(){
        val notificationChannel1 = NotificationChannel(CHANNEL_ID_1,"Channel(1)",NotificationManager.IMPORTANCE_HIGH);
        notificationChannel1.description = "Channel 1 description";
        val notificationChannel2 = NotificationChannel(CHANNEL_ID_2,"Channel(2)",NotificationManager.IMPORTANCE_HIGH);
        notificationChannel2.description = "Channel 2 description";
        val notifyManager = getSystemService(NotificationManager::class.java);
        notifyManager.createNotificationChannel(notificationChannel1);
        notifyManager.createNotificationChannel(notificationChannel2);
    }
}