package com.example.m_help.api

import com.example.m_help.api.classes.PushNotification
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object FcmServices {
    private val a = Retrofit.Builder()
        .baseUrl("https://fcm.googleapis.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(FcmApi::class.java)

    fun sendMessageToTopic(notification: PushNotification, listener: Listener){
        notification.to = "/topics/${notification.to}"
        a.sendMessage(notification)
            .enqueue(object: Callback<Void>{
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    println(notification)
                    listener.onSuccess()
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    listener.onFailure(t.message)
                }
            })
    }

    interface Listener{
        fun onSuccess()
        fun onFailure(message:String?)
    }
}