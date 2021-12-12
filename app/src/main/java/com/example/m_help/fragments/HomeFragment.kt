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
import com.example.m_help.dataClasses.Donor
import com.example.m_help.dataClasses.Hospital
import com.example.m_help.dataClasses.Patient
import com.example.m_help.database.DataBaseHelper
import com.example.m_help.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private lateinit var afi: AFI
    private val binding: FragmentHomeBinding get() = _binding!!

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
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        binding.name.text = Helper.name
        val dataBaseHelper = DataBaseHelper()

        val pickLocationFragment = PickLocationFragment()
        val b = Bundle()
        binding.donor.setOnClickListener {
            dataBaseHelper.getDonor(Helper.email, object :DataBaseHelper.Listener<Donor>{
                override fun onSuccess(t: Donor?) {
                    if(t==null){
                        b.putInt(PickLocationFragment.FOR, PickLocationFragment.DONOR)
                        pickLocationFragment.arguments = b
                        afi.changeFragmentTo(pickLocationFragment, false)
                    }else{
                        Helper.donor = t
                        afi.changeFragmentTo(DonorFragment(), false)
                    }
                }

                override fun onFailure(message: String?) {
                    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                }

            })
        }

        binding.hospital.setOnClickListener {
            dataBaseHelper.getHospital(Helper.email, object :DataBaseHelper.Listener<Hospital>{
                override fun onSuccess(t: Hospital?) {
                    if(t==null){
                        b.putInt(PickLocationFragment.FOR, PickLocationFragment.HOSPITAL)
                        pickLocationFragment.arguments = b
                        afi.changeFragmentTo(pickLocationFragment, false)
                    }else{
                        Helper.hospital = t
                        afi.changeFragmentTo(HospitalFragment(), false)
                    }
                }

                override fun onFailure(message: String?) {
                    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                }

            })
        }

        binding.patient.setOnClickListener {
            dataBaseHelper.getPatient(Helper.email, object :DataBaseHelper.Listener<Patient>{
                override fun onSuccess(t: Patient?) {
                    if(t==null){
                        b.putInt(PickLocationFragment.FOR, PickLocationFragment.PATIENT)
                        pickLocationFragment.arguments = b
                        afi.changeFragmentTo(pickLocationFragment, false)
                    }else{
                        Helper.patient = t
                        afi.changeFragmentTo(PatientFragment(), false)
                    }
                }

                override fun onFailure(message: String?) {
                    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                }

            })
        }

        binding.bloodBank.setOnClickListener {
            dataBaseHelper.getBloodBank(Helper.email, object :DataBaseHelper.Listener<BloodBank>{
                override fun onSuccess(t: BloodBank?) {
                    if(t==null){
                        b.putInt(PickLocationFragment.FOR, PickLocationFragment.BLOOD_BANK)
                        pickLocationFragment.arguments = b
                        afi.changeFragmentTo(pickLocationFragment, false)
                    }else{
                        Helper.bloodBank = t
                        afi.changeFragmentTo(BloodBankPanelFragment(), false)
                    }
                }

                override fun onFailure(message: String?) {
                    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                }

            })
        }

        binding.signOut.setOnClickListener {
            afi.signOut()
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}