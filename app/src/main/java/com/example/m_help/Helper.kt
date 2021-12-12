package com.example.m_help

import android.content.Context
import android.location.Geocoder
import com.example.m_help.dataClasses.*
import com.example.m_help.databinding.BloodDetailsBinding
import com.example.m_help.databinding.UpdateMultipleBinding
import java.util.*

object Helper {

    const val LATITUDE = "lat"
    const val LONGITUDE = "lng"

    var email:String? = null
    var name: String? = null
    var donor: Donor? = null
    var hospital:Hospital? = null
    var patient:Patient? = null
    var bloodBank:BloodBank? = null

    fun Address.completeAddress():String{
        return "$street, $city, $state, $country"
    }

    fun BloodDetailsBinding.setBloodData(bloodGroup: BloodGroup?){
        aP.text = bloodGroup?.a_p.toString()
        aN.text = bloodGroup?.a_n.toString()
        bP.text = bloodGroup?.b_p.toString()
        bN.text = bloodGroup?.b_n.toString()
        oP.text = bloodGroup?.o_p.toString()
        oN.text = bloodGroup?.o_n.toString()
        abP.text = bloodGroup?.ab_p.toString()
        abN.text = bloodGroup?.ab_n.toString()
    }

    fun UpdateMultipleBinding.setData(bloodGroup: BloodGroup?){
        aP.setText( bloodGroup?.a_p.toString())
        aN.setText(bloodGroup?.a_n.toString())
        bP.setText(bloodGroup?.b_p.toString())
        bN.setText(bloodGroup?.b_n.toString())
        oP.setText( bloodGroup?.o_p.toString())
        oN.setText(bloodGroup?.o_n.toString())
        abP.setText(bloodGroup?.ab_p.toString())
        abN.setText(bloodGroup?.ab_n.toString())
    }

    fun UpdateMultipleBinding.readData():BloodGroup{
        return  BloodGroup(
            a_p = aP.text.toString().toInt(),
            a_n = aN.text.toString().toInt(),
            b_p = bP.text.toString().toInt(),
            b_n = bN.text.toString().toInt(),
            o_p = oP.text.toString().toInt(),
            o_n = oN.text.toString().toInt(),
            ab_p = abP.text.toString().toInt(),
            ab_n = abN.text.toString().toInt()
        )
    }

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
