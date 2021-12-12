package com.example.m_help.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.m_help.Helper
import com.example.m_help.Helper.completeAddress
import com.example.m_help.R
import com.example.m_help.dataClasses.Hospital
import com.example.m_help.database.DataBaseHelper
import com.example.m_help.databinding.FragmentHospitalsBinding
import com.example.m_help.databinding.HospitalDetailsDialogBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions

class HospitalsMapFragment : Fragment() {

    private var _binding: FragmentHospitalsBinding? = null
    private val binding: FragmentHospitalsBinding get() = _binding!!
    private lateinit var mMap: GoogleMap
    private var hospitals:MutableList<Hospital> = mutableListOf()
    private val markers:MutableList<Marker?> = mutableListOf()
    private lateinit var dialogBinding: HospitalDetailsDialogBinding
    private lateinit var dialog: AlertDialog
    private val dataBaseHelper = DataBaseHelper()

    private val callback = OnMapReadyCallback { mMap ->
        val latLng = LatLng(28.38, 77.12)

        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng))
        mMap.moveCamera(CameraUpdateFactory.zoomTo(12f))
        this.mMap = mMap

        mMap.setOnMarkerClickListener { marker: Marker? ->
            val index: Int = markers.indexOf(marker)
            if (index >= 0) {
                showDetailsDialog(index)
            }
            true
        }

        dataBaseHelper.fetchSuitableHospitals(
            Helper.patient?.address?.city,
            object:DataBaseHelper.Listener<MutableList<Hospital>>{
                override fun onSuccess(t: MutableList<Hospital>?) {
                    if(t==null || t.isEmpty()){
                        Toast.makeText(requireContext(),"No suitable hospital found",Toast.LENGTH_SHORT).show()
                    }else{
                        hospitals = t
                        addMarkers(hospitals)
                    }
                }

                override fun onFailure(message: String?) {
                    Toast.makeText(requireContext(),message,Toast.LENGTH_SHORT).show()
                }
            }
        )
    }

    private fun showDetailsDialog(index: Int) {
        val hospital = hospitals[index]
        val binding  = dialogBinding
        binding.address.text = hospital.address?.completeAddress()
        binding.email.text = Helper.email
        binding.mobile.text = hospital.contact
        binding.name.text = hospital.name

        binding.avialIcuBeds.text = hospital.availableIcuBed.toString()
        binding.avialOxyCilender.text = hospital.availableOxygenCylinder.toString()
        binding.avialOxygen.text = hospital.availableOxygen.toString()

        dialog.show()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHospitalsBinding.inflate(inflater, container, false)

        dialogBinding = HospitalDetailsDialogBinding.inflate(inflater)
        dialog = AlertDialog.Builder(requireContext())
            .setView(dialogBinding.root)
            .setPositiveButton("Ok", null)
            .create()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }

    fun addMarkers(customerDetails: List<Hospital>) {
        for (i in customerDetails.indices) {
            val address = customerDetails[i].address
            val latLng = LatLng(address?.latitude!!, address.longitude!!)

            markers.add(
                mMap.addMarker(
                    MarkerOptions()
                        .position(latLng)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)))
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}