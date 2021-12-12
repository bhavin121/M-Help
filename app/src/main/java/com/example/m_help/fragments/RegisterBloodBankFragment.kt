package com.example.m_help.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.m_help.AFI
import com.example.m_help.Helper
import com.example.m_help.dataClasses.BloodBank
import com.example.m_help.dataClasses.Patient
import com.example.m_help.database.DataBaseHelper
import com.example.m_help.databinding.FragmentRegisterBloodBankBinding

class RegisterBloodBankFragment : Fragment() {

    private var _binding: FragmentRegisterBloodBankBinding? = null
    private lateinit var afi: AFI
    private val binding: FragmentRegisterBloodBankBinding get() = _binding!!

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
        _binding = FragmentRegisterBloodBankBinding.inflate(layoutInflater, container, false)

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

            val bloodBank = BloodBank(
                name = name,
                contact = contact,
                email = Helper.email,
                address = address
            )
            dataBaseHelper.registerBloodBank(Helper.email, bloodBank, object: DataBaseHelper.Listener<Void>{
                override fun onSuccess(t: Void?) {
                    Helper.bloodBank = bloodBank
                    afi.changeFragmentTo(BloodBankPanelFragment(), false)
                }

                override fun onFailure(message: String?) {
                    Toast.makeText(requireContext(),message,Toast.LENGTH_SHORT).show()
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