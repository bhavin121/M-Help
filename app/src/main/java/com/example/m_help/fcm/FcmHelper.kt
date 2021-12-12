package com.example.m_help.fcm

import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging

object FcmHelper {

    fun generateTopic(city:String?, data:String?):String{
        return "$city-$data"
    }

    fun subscribeTopic(topic:String, listener: Listener){
        Firebase.messaging.subscribeToTopic(topic)
            .addOnSuccessListener {
                listener.onSuccess()
            }.addOnFailureListener {
                listener.onFailure(it.message)
            }
    }

    fun unsubscribeTopic(topic:String, listener: Listener){
        Firebase.messaging.unsubscribeFromTopic(topic)
            .addOnSuccessListener {
                listener.onSuccess()
            }.addOnFailureListener {
                listener.onFailure(it.message)
            }
    }

    interface Listener{
        fun onSuccess()
        fun onFailure(message: String?)
    }
}