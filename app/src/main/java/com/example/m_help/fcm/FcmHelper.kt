package com.example.m_help.fcm

import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging

object FcmHelper {
    fun subscribeTopic(topic:String){
        Firebase.messaging.subscribeToTopic(topic)
            .addOnSuccessListener {
                println("Subscribed for topic $topic")
            }.addOnFailureListener {
                println("Failed to subscribed for topic $topic")
            }
    }


}