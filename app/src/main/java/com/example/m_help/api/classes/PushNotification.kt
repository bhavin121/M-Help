package com.example.m_help.api.classes

data class PushNotification(
    val data: NotificationData,
    val notification: Notification,
    var to: String
)
