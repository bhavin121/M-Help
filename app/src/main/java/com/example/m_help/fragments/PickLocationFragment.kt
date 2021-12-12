package com.example.m_help.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.m_help.AFI
import com.example.m_help.Helper
import com.example.m_help.R
import com.example.m_help.databinding.FragmentPickLocationBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.tasks.OnSuccessListener

class PickLocationFragment : Fragment() {

    companion object{
        const val FOR = "for"
        const val DONOR = 0
        const val PATIENT = 1
        const val HOSPITAL = 2
        const val BLOOD_BANK = 3
    }

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var _binding: FragmentPickLocationBinding? = null
    private lateinit var afi: AFI
    private val binding: FragmentPickLocationBinding get() = _binding!!
    private var location: LatLng = LatLng(28.38, 77.12)

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is AFI) {
            afi = context
        } else {
            throw RuntimeException(context.javaClass.toString() + " must implement AFI")
        }
    }

    @SuppressLint("MissingPermission")
    private val callback = OnMapReadyCallback { mMap ->
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            mMap.isMyLocationEnabled = true
            fusedLocationClient.lastLocation
                .addOnSuccessListener(requireActivity(), object : OnSuccessListener<Location?> {
                    override fun onSuccess(location: Location) {
                        val latLng = LatLng(location.latitude, location.longitude)
                        mMap.addMarker(MarkerOptions().position(latLng))
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng))
                    }
                })
        } else {
            val latLng = LatLng(28.38, 77.12)
            mMap.addMarker(MarkerOptions().position(latLng))
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng))
        }
        mMap.moveCamera(CameraUpdateFactory.zoomTo(15f))

        mMap.setOnMapClickListener { latLng1: LatLng? ->
            mMap.clear()
            mMap.addMarker(MarkerOptions().position(latLng1!!))
            location = latLng1
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_pick_location, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentPickLocationBinding.bind(view)

        val b = Bundle()
        b.putDouble(Helper.LATITUDE, location.latitude)
        b.putDouble(Helper.LONGITUDE, location.longitude)

        binding.pick.setOnClickListener {
            val for_val = arguments?.getInt(FOR)
            when(for_val){
                DONOR ->{
                    val f = RegisterDonorFragment()
                    f.arguments = b
                    afi.changeFragmentTo(f, false)
                }
                PATIENT ->{
                    val f = RegisterPatientFragment()
                    f.arguments = b
                    afi.changeFragmentTo(f, false)
                }
                HOSPITAL->{
                    val f = RegisterHospitalFragment()
                    f.arguments = b
                    afi.changeFragmentTo(f, false)
                }
                BLOOD_BANK->{
                    val f = RegisterBloodBankFragment()
                    f.arguments = b
                    afi.changeFragmentTo(f, false)
                }
            }
        }

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}