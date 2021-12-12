package com.example.m_help.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.m_help.AFI
import com.example.m_help.Helper
import com.example.m_help.R
import com.example.m_help.dataClasses.Hospital
import com.example.m_help.dataClasses.Patient
import com.example.m_help.database.DataBaseHelper
import com.example.m_help.databinding.FragmentRegisterBloodBankBinding
import com.example.m_help.databinding.FragmentRegisterHospitalBinding

class RegisterHospitalFragment : Fragment() {

    private var _binding: FragmentRegisterHospitalBinding? = null
    private lateinit var afi: AFI
    private val binding: FragmentRegisterHospitalBinding get() = _binding!!

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
        _binding = FragmentRegisterHospitalBinding.inflate(layoutInflater, container, false)

        val address = Helper.decodeAddress(requireContext(), arguments?.getDouble(Helper.LATITUDE), arguments?.getDouble(
            Helper.LONGITUDE))
        val dataBaseHelper = DataBaseHelper()

        binding.submit.setOnClickListener {
            val name = binding.name.text.toString()
            val contact = binding.mobile.text.toString()

            if(name.isEmpty() || contact.isEmpty()){
                Toast.makeText(requireContext(), "Fill empty fields", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }

            val hospital = Hospital(
                name = name,
                contact = contact,
                address = address,
                email = Helper.email,
            )
            dataBaseHelper.registerHospital(Helper.email, hospital, object: DataBaseHelper.Listener<Void>{
                override fun onSuccess(t: Void?) {
                    println("Success Donor inserted")
                }

                override fun onFailure(message: String?) {
                    println(message)
                }
            })
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}