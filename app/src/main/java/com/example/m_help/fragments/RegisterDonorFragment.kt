package com.example.m_help.fragments

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
import com.example.m_help.dataClasses.Donor
import com.example.m_help.database.DataBaseHelper
import com.example.m_help.databinding.FragmentRegisterDonorBinding
import com.example.m_help.fcm.FcmHelper

class RegisterDonorFragment : Fragment() {

    private var _binding: FragmentRegisterDonorBinding? = null
    private lateinit var afi: AFI
    private val binding: FragmentRegisterDonorBinding get() = _binding!!

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
        _binding = FragmentRegisterDonorBinding.inflate(inflater, container, false)
        val bloodGroups = arrayOf("A+", "A-","B+","B-","O+","O-","AB+","AB-")
        val donorTypes = arrayOf("Blood","Plasma","Both")
        val dataBaseHelper= DataBaseHelper()
        val bloodGroupAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_dropdown_item_1line,
            bloodGroups
        )
        binding.bloodGroup.setAdapter(bloodGroupAdapter)

        val donorTypeAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_dropdown_item_1line,
            donorTypes
        )
        binding.donorType.setAdapter(donorTypeAdapter)

        val address = Helper.decodeAddress(requireContext(), arguments?.getDouble(Helper.LATITUDE), arguments?.getDouble(Helper.LONGITUDE))

        binding.submit.setOnClickListener {
            val name = binding.name.text.toString()
            val contact = binding.mobile.text.toString()
            val bloodGroup = binding.bloodGroup.text.toString()
            val donorType = binding.donorType.text.toString()

            if(name.isEmpty() || contact.isEmpty() || bloodGroup.isEmpty() || donorType.isEmpty()){
                Toast.makeText(requireContext(), "Fill empty fields", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }

            val donor = Donor(
                name = name,
                contact = contact,
                bloodGroup = bloodGroup,
                donorType = donorType,
                address = address
            )
            when (donorType) {
                donorTypes[2] -> {
                    val topic = FcmHelper.generateTopic(address?.city,bloodGroup)
                    FcmHelper.subscribeTopic(topic, object:FcmHelper.Listener{
                        override fun onSuccess() {
                            val topic2 = FcmHelper.generateTopic(address?.city,"plasma")
                            FcmHelper.subscribeTopic(topic2, object:FcmHelper.Listener{
                                override fun onSuccess() {
                                    dataBaseHelper.registerDonor(Helper.email, donor, object:DataBaseHelper.Listener<Void>{
                                        override fun onSuccess(t: Void?) {
                                            Helper.donor = donor
                                            afi.changeFragmentTo(DonorFragment(), false)
                                        }

                                        override fun onFailure(message: String?) {
                                            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()                                        }

                                    })
                                }

                                override fun onFailure(message: String?) {
                                    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                                }

                            })
                        }

                        override fun onFailure(message: String?) {
                            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                        }

                    })
                }
                donorTypes[1] -> {
                    val topic = FcmHelper.generateTopic(address?.city,"plasma")
                    FcmHelper.subscribeTopic(topic, object:FcmHelper.Listener{
                        override fun onSuccess() {
                            dataBaseHelper.registerDonor(Helper.email, donor, object:DataBaseHelper.Listener<Void>{
                                override fun onSuccess(t: Void?) {
                                    Helper.donor = donor
                                    afi.changeFragmentTo(DonorFragment(), false)
                                }

                                override fun onFailure(message: String?) {
                                    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()                                        }

                            })
                        }

                        override fun onFailure(message: String?) {
                            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                        }

                    })
                }
                donorTypes[0] -> {
                    val topic = FcmHelper.generateTopic(address?.city,bloodGroup)
                    FcmHelper.subscribeTopic(topic, object:FcmHelper.Listener{
                        override fun onSuccess() {
                            dataBaseHelper.registerDonor(Helper.email, donor, object:DataBaseHelper.Listener<Void>{
                                override fun onSuccess(t: Void?) {
                                    Helper.donor = donor
                                    afi.changeFragmentTo(DonorFragment(), false)
                                }

                                override fun onFailure(message: String?) {
                                    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()                                        }

                            })
                        }

                        override fun onFailure(message: String?) {
                            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                        }

                    })
                }
            }
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}