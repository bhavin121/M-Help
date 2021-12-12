package com.example.m_help.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.m_help.AFI
import com.example.m_help.Helper
import com.example.m_help.Helper.completeAddress
import com.example.m_help.database.DataBaseHelper
import com.example.m_help.databinding.FragmentDonorBinding
import com.example.m_help.fcm.FcmHelper
import java.lang.RuntimeException

class DonorFragment : Fragment() {
    private var _binding: FragmentDonorBinding? = null
    private lateinit var afi: AFI
    private val binding: FragmentDonorBinding get() = _binding!!
    private val dataBaseHelper = DataBaseHelper()
    val donor = Helper.donor

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is AFI) {
            afi = context
        } else {
            throw RuntimeException(context.javaClass.toString() + " must implement AFI")
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        /* Inflate the layout for this fragment */
        _binding = FragmentDonorBinding.inflate(layoutInflater, container, false)

        binding.header.address.text = donor?.address?.completeAddress()
        binding.header.email.text = Helper.email
        binding.header.mobile.text = donor?.contact
        binding.header.name.text = donor?.name

        val donorTypes = arrayOf("Blood","Plasma","Both")
        when(donor?.donorType){
            donorTypes[0]->{
                setBloodData()
                binding.plasmaReq.visibility = View.GONE
            }
            donorTypes[1]->{
                setPlasmaData()
                binding.bloodReq.visibility = View.GONE
            }
            donorTypes[2]->{
                setBloodData()
                setPlasmaData()
            }
        }

        return binding.root
    }

    @SuppressLint("SetTextI18n")
    private fun setBloodData() {
        if(donor?.receiveForBlood == true){
            binding.bloodReq.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#f44336"))
            binding.bloodReq.text = "Stop receiving requests for blood"
            binding.bloodReq.setOnClickListener {

                FcmHelper.unsubscribeTopic(FcmHelper.generateTopic(donor.address?.city, donor.bloodGroup),
                    object:FcmHelper.Listener{
                        override fun onSuccess() {
                            dataBaseHelper.updateReceiveBloodReq(Helper.email, false, object :DataBaseHelper.Listener<Boolean>{
                                override fun onSuccess(t: Boolean?) {
                                    donor.receiveForBlood = t
                                    setBloodData()
                                    Toast.makeText(requireContext(),"Change saved", Toast.LENGTH_SHORT).show()
                                }

                                override fun onFailure(message: String?) {
                                    Toast.makeText(requireContext(),message, Toast.LENGTH_SHORT).show()
                                }
                            })
                        }

                        override fun onFailure(message: String?) {
                            Toast.makeText(requireContext(),message, Toast.LENGTH_SHORT).show()
                        }

                    })
            }
        }else{
            binding.bloodReq.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#00B060"))
            binding.bloodReq.text = "Start receiving requests for blood"
            binding.bloodReq.setOnClickListener {

                FcmHelper.subscribeTopic(FcmHelper.generateTopic(donor?.address?.city, donor?.bloodGroup),
                    object:FcmHelper.Listener{
                        override fun onSuccess() {
                            dataBaseHelper.updateReceiveBloodReq(Helper.email, true, object :DataBaseHelper.Listener<Boolean>{
                                override fun onSuccess(t: Boolean?) {
                                    donor?.receiveForBlood = t
                                    setBloodData()
                                    Toast.makeText(requireContext(),"Change saved", Toast.LENGTH_SHORT).show()
                                }

                                override fun onFailure(message: String?) {
                                    Toast.makeText(requireContext(),message, Toast.LENGTH_SHORT).show()
                                }
                            })
                        }

                        override fun onFailure(message: String?) {
                            Toast.makeText(requireContext(),message, Toast.LENGTH_SHORT).show()
                        }

                    })
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setPlasmaData() {
        if(donor?.receiveForPlasma == true){
            binding.plasmaReq.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#f44336"))
            binding.plasmaReq.text = "Stop receiving requests for plasma"
            binding.plasmaReq.setOnClickListener {
                FcmHelper.unsubscribeTopic(FcmHelper.generateTopic(donor.address?.city, "plasma"),
                    object:FcmHelper.Listener{
                        override fun onSuccess() {
                            dataBaseHelper.updateReceivePlasmaReq(Helper.email, false, object :DataBaseHelper.Listener<Boolean>{
                                override fun onSuccess(t: Boolean?) {
                                    donor.receiveForPlasma = t
                                    setPlasmaData()
                                    Toast.makeText(requireContext(),"Change saved", Toast.LENGTH_SHORT).show()
                                }

                                override fun onFailure(message: String?) {
                                    Toast.makeText(requireContext(),message, Toast.LENGTH_SHORT).show()
                                }
                            })
                        }

                        override fun onFailure(message: String?) {
                            Toast.makeText(requireContext(),message, Toast.LENGTH_SHORT).show()
                        }

                    })
            }
        }else{
            binding.plasmaReq.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#00B060"))
            binding.plasmaReq.text = "Start receiving requests for plasma"
            binding.plasmaReq.setOnClickListener {
                FcmHelper.subscribeTopic(FcmHelper.generateTopic(donor?.address?.city, "plasma"),
                object:FcmHelper.Listener{
                    override fun onSuccess() {
                        dataBaseHelper.updateReceivePlasmaReq(Helper.email, true, object :DataBaseHelper.Listener<Boolean>{
                            override fun onSuccess(t: Boolean?) {
                                donor?.receiveForPlasma = t
                                setPlasmaData()
                                Toast.makeText(requireContext(),"Change saved", Toast.LENGTH_SHORT).show()
                            }

                            override fun onFailure(message: String?) {
                                Toast.makeText(requireContext(),message, Toast.LENGTH_SHORT).show()
                            }
                        })
                    }

                    override fun onFailure(message: String?) {
                        Toast.makeText(requireContext(),message, Toast.LENGTH_SHORT).show()
                    }

                })
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}