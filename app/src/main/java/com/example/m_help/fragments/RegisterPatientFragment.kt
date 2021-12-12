package com.example.m_help.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import com.example.m_help.AFI
import com.example.m_help.Helper
import com.example.m_help.R
import com.example.m_help.dataClasses.Donor
import com.example.m_help.dataClasses.Patient
import com.example.m_help.database.DataBaseHelper
import com.example.m_help.databinding.FragmentRegisterDonorBinding
import com.example.m_help.databinding.FragmentRegisterPatientBinding
import java.lang.RuntimeException

class RegisterPatientFragment : Fragment() {
    private var _binding: FragmentRegisterPatientBinding? = null
    private lateinit var afi: AFI
    private val binding: FragmentRegisterPatientBinding get() = _binding!!

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
        _binding = FragmentRegisterPatientBinding.inflate(inflater, container, false)
        val genders = arrayOf("Male","Female")
        val genderAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_dropdown_item_1line,
            genders
        )
        binding.gender.setAdapter(genderAdapter)

        val address = Helper.decodeAddress(requireContext(), arguments?.getDouble(Helper.LATITUDE), arguments?.getDouble(
            Helper.LONGITUDE))
        val dataBaseHelper = DataBaseHelper()

        binding.submit.setOnClickListener {
            val name = binding.name.text.toString()
            val contact = binding.mobile.text.toString()
            val age = binding.age.text.toString()
            val gender = binding.gender.text.toString()

            if(name.isEmpty() || contact.isEmpty() || age.isEmpty() || gender.isEmpty()){
                Toast.makeText(requireContext(), "Fill empty fields", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }

            val patient = Patient(
                name = name,
                contact = contact,
                age = age,
                gender = gender,
                address = address
            )
            dataBaseHelper.registerPatient(Helper.email, patient, object: DataBaseHelper.Listener<Void>{
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