package com.example.m_help.fcm

import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging

object FcmHelper {

    fun generateTopic(city:String?, data:String?):String{
        return "$city-$data".replace('+','p').replace('-','n')
    }

    fun subscribeTopic(topic:String, listener: Listener){
        Firebase.messaging.subscribeToTopic(topic)
            .addOnCompleteListener {
                if(it.isSuccessful){
                    listener.onSuccess()
                }else{
                    listener.onFailure(it.exception?.message)
                }
            }
    }

    fun unsubscribeTopic(topic:String, listener: Listener){
        Firebase.messaging.unsubscribeFromTopic(topic)
            .addOnCompleteListener {
                if(it.isSuccessful){
                    listener.onSuccess()
                }else{
                    listener.onFailure(it.exception?.message)
                }
            }
    }

    interface Listener{
        fun onSuccess()
        fun onFailure(message: String?)
    }
}