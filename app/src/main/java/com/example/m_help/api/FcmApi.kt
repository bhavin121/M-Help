package com.example.m_help.api

import com.example.m_help.api.classes.PushNotification
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface FcmApi {

    companion object{
        const val CONTENT_TYPE = "application/json"
        const val serverKey = "AAAAY4KA8Sc:APA91bEYex73pBmtnVoSF2QY2IoLmUy0jslAUMSUBqq425WMKUc4SNHSk1nuwJqIT2GHXTK5dEc7XC7pgPKWkZM8EIveGDuWmtDU7SdcYAtMlVhchjhN9sVapcoWzuV8ERX4QNHSaTo4"
    }

    @Headers("Authorization: key=$serverKey", "Content-Type:$CONTENT_TYPE")
    @POST("fcm/send")
    fun sendMessage(@Body notification: PushNotification):Call<Void>
}