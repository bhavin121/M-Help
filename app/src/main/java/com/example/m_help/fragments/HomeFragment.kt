package com.example.m_help.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.m_help.AFI
import com.example.m_help.Helper
import com.example.m_help.R
import com.example.m_help.databinding.FragmentHomeBinding
import com.example.m_help.databinding.FragmentSignInBinding
import java.lang.RuntimeException

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
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        binding.name.text = Helper.name

        val pickLocationFragment = PickLocationFragment()
        val b = Bundle()
        binding.donor.setOnClickListener {
            b.putInt(PickLocationFragment.FOR, PickLocationFragment.DONOR)
            pickLocationFragment.arguments = b
            afi.changeFragmentTo(pickLocationFragment, false)
        }

        binding.hospital.setOnClickListener {
            b.putInt(PickLocationFragment.FOR, PickLocationFragment.HOSPITAL)
            pickLocationFragment.arguments = b
            afi.changeFragmentTo(pickLocationFragment, false)
        }

        binding.patient.setOnClickListener {
            b.putInt(PickLocationFragment.FOR, PickLocationFragment.PATIENT)
            pickLocationFragment.arguments = b
            afi.changeFragmentTo(pickLocationFragment, false)
        }

        binding.bloodBank.setOnClickListener {
            b.putInt(PickLocationFragment.FOR, PickLocationFragment.BLOOD_BANK)
            pickLocationFragment.arguments = b
            afi.changeFragmentTo(pickLocationFragment, false)
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