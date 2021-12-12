package com.example.m_help.fragments

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.m_help.AFI
import com.example.m_help.Helper
import com.example.m_help.Helper.completeAddress
import com.example.m_help.api.FcmServices
import com.example.m_help.api.classes.Notification
import com.example.m_help.api.classes.PushNotification
import com.example.m_help.databinding.ChooseBloodGroupBinding
import com.example.m_help.databinding.FragmentPatientBinding
import com.example.m_help.fcm.FcmHelper

class PatientFragment : Fragment() {

    private var _binding: FragmentPatientBinding? = null
    private lateinit var afi: AFI
    private val binding: FragmentPatientBinding get() = _binding!!
    val patient = Helper.patient
    private lateinit var chooseBloodGroupBinding: ChooseBloodGroupBinding
    private lateinit var chooseBloodGroupDialog: AlertDialog
    private val bloodGroups = arrayOf("A+", "A-","B+","B-","O+","O-","AB+","AB-")

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is AFI) {
            afi = context
        } else {
            throw RuntimeException(context.javaClass.toString() + " must implement AFI")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentPatientBinding.inflate(layoutInflater, container, false)

        binding.header.address.text = patient?.address?.completeAddress()
        binding.header.email.text = Helper.email
        binding.header.mobile.text = patient?.contact
        binding.header.name.text = patient?.name

        binding.messagePlasmaDonor.setOnClickListener {
            val message = "${patient?.name} is a ${patient?.age} years old ${patient?.gender} patient and need plasma.\nPlease contact if you can help ${patient?.contact}"
            val not = Notification("Need Plasma Donor", message)
            val notification = PushNotification(not, FcmHelper.generateTopic(patient?.address?.city, "plasma"))
            FcmServices.sendMessageToTopic(notification, object:FcmServices.Listener{
                override fun onSuccess() {
                    Toast.makeText(requireContext(), "Message Sent", Toast.LENGTH_SHORT).show()
                }

                override fun onFailure(message: String?) {
                    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                }
            })
        }

        binding.messageBloodDonor.setOnClickListener {
            chooseBloodGroupDialog.show()
        }

        binding.findHospital.setOnClickListener {
            afi.changeFragmentTo(HospitalsMapFragment(), false)
        }

        buildDialogs()
        return binding.root
    }

    private fun buildDialogs(){
        chooseBloodGroupBinding = ChooseBloodGroupBinding.inflate(LayoutInflater.from(requireContext()))
        chooseBloodGroupDialog = AlertDialog.Builder(requireContext())
            .setView(chooseBloodGroupBinding.root)
            .create()

        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_dropdown_item_1line,
            bloodGroups
        )
        chooseBloodGroupBinding.bloodGroup.setAdapter(adapter)

        chooseBloodGroupBinding.send.setOnClickListener {
            chooseBloodGroupDialog.dismiss()
            val bloodGroup = chooseBloodGroupBinding.bloodGroup.text.toString()
            val message = "${patient?.name} is a ${patient?.age} years old ${patient?.gender} patient and need $bloodGroup blood.\nPlease contact if you can help ${patient?.contact}"
            val not = Notification("Need $bloodGroup blood", message)
            val notification = PushNotification(not, FcmHelper.generateTopic(patient?.address?.city, bloodGroup))
            FcmServices.sendMessageToTopic(notification, object:FcmServices.Listener{
                override fun onSuccess() {
                    Toast.makeText(requireContext(), "Message Sent", Toast.LENGTH_SHORT).show()
                }

                override fun onFailure(message: String?) {
                    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}