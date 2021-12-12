package com.example.m_help

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.ContentResolver
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import android.media.AudioAttributes
import android.net.Uri


class MessagingService:FirebaseMessagingService() {

    companion object{
        const val CHANNEL_ID = "my-channel"
    }

    override fun onNewToken(p0: String) {
        super.onNewToken(p0)
        println("Registration token $p0")
    }

    override fun onMessageReceived(p0: RemoteMessage) {
        super.onMessageReceived(p0)

        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
        builder.setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(p0.notification?.title)
            .setContentText(p0.notification?.body)
            .setAutoCancel(true)

        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel =
                NotificationChannel(CHANNEL_ID, "myChannel", NotificationManager.IMPORTANCE_HIGH)

            manager.createNotificationChannel(channel)
        }

        manager.notify(0, builder.build())
        println(p0.notification?.body)
    }

}