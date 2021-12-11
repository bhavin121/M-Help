package com.example.m_help

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MessagingService:FirebaseMessagingService() {

    override fun onNewToken(p0: String) {
        super.onNewToken(p0)
        println("Registration token $p0")
    }

    override fun onMessageReceived(p0: RemoteMessage) {
        super.onMessageReceived(p0)
        println(p0.notification?.title)
    }
}