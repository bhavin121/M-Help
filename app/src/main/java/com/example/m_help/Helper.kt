package com.example.m_help

import android.content.Context
import android.location.Geocoder
import com.example.m_help.dataClasses.Address
import java.util.*

object Helper {

    const val LATITUDE = "lat"
    const val LONGITUDE = "lng"

    var email:String? = null
    var name: String? = null

    fun decodeAddress(context: Context,lat: Double?, lng: Double?): Address?{
        if(lat == null || lng == null) return null
        lateinit var addressRes: Address
        val geocoder = Geocoder(context, Locale.getDefault())
        return try {
            val addresses =
                geocoder.getFromLocation(lat, lng, 1)
            val address = addresses[0]
            addressRes = Address(
                city = address.locality,
                state = address.adminArea,
                country = address.countryName,
                street = address.subLocality,
                latitude = lat,
                longitude = lng
            )
            addressRes
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
