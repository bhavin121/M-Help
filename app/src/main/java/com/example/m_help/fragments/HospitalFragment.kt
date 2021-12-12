package com.example.m_help.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.m_help.AFI
import com.example.m_help.R
import com.example.m_help.databinding.FragmentBloodBankPanelBinding
import com.example.m_help.databinding.FragmentHomeBinding
import java.lang.RuntimeException

class HospitalFragment : Fragment() {

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
        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}