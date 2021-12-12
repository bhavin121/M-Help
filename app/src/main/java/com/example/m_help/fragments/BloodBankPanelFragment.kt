package com.example.m_help.fragments

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.m_help.AFI
import com.example.m_help.Helper
import com.example.m_help.Helper.completeAddress
import com.example.m_help.Helper.readData
import com.example.m_help.Helper.setBloodData
import com.example.m_help.Helper.setData
import com.example.m_help.dataClasses.BloodBank
import com.example.m_help.dataClasses.BloodGroup
import com.example.m_help.database.DataBaseHelper
import com.example.m_help.databinding.FragmentBloodBankPanelBinding
import com.example.m_help.databinding.UpdateMultipleBinding
import java.lang.RuntimeException


class BloodBankPanelFragment : Fragment() {

    private var _binding: FragmentBloodBankPanelBinding? = null
    private lateinit var afi: AFI
    private val binding: FragmentBloodBankPanelBinding get() = _binding!!
    private lateinit var updateMultipleBinding: UpdateMultipleBinding
    private lateinit var updateMultipleDialog: AlertDialog

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
        _binding = FragmentBloodBankPanelBinding.inflate(layoutInflater, container, false)

        val dataBaseHelper = DataBaseHelper()
        val bloodBank = Helper.bloodBank
        binding.header.address.text = bloodBank?.address?.completeAddress()
        binding.header.email.text = Helper.email
        binding.header.mobile.text = bloodBank?.contact
        binding.header.name.text = bloodBank?.name

        binding.bloodDetails.setBloodData(bloodBank?.bloodGroup)

        binding.bloodDetails.edit.setOnClickListener {
            updateMultipleBinding.update.setOnClickListener {
                val data = updateMultipleBinding.readData()
                updateMultipleDialog.dismiss()
                dataBaseHelper.updateBloodBankBloodData(Helper.email, data, object:
                    DataBaseHelper.Listener<BloodGroup>{
                    override fun onSuccess(t: BloodGroup?) {
                        bloodBank?.bloodGroup = t
                        binding.bloodDetails.setBloodData(t)
                        Toast.makeText(requireContext(), "Updated", Toast.LENGTH_SHORT).show()
                    }

                    override fun onFailure(message: String?) {
                        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                    }
                })
            }
            showUpdateMultipleDialog(bloodBank?.bloodGroup)
        }

        buildUpdateDialogs()

        return binding.root
    }

    private fun buildUpdateDialogs() {
        updateMultipleBinding = UpdateMultipleBinding.inflate(LayoutInflater.from(requireContext()))
        updateMultipleDialog = AlertDialog.Builder(requireContext())
            .setView(updateMultipleBinding.root)
            .create()
    }

    private fun showUpdateMultipleDialog(data:BloodGroup?){
        updateMultipleBinding.setData(data)
        updateMultipleDialog.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}