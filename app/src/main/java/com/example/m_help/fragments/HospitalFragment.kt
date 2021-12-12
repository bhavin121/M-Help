package com.example.m_help.fragments

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.m_help.AFI
import com.example.m_help.Helper
import com.example.m_help.Helper.completeAddress
import com.example.m_help.Helper.readData
import com.example.m_help.Helper.setBloodData
import com.example.m_help.Helper.setData
import com.example.m_help.dataClasses.BloodGroup
import com.example.m_help.database.DataBaseHelper
import com.example.m_help.databinding.FragmentHospitalBinding
import com.example.m_help.databinding.UpdateDialogBinding
import com.example.m_help.databinding.UpdateMultipleBinding

class HospitalFragment : Fragment() {

    private var _binding: FragmentHospitalBinding? = null
    private lateinit var afi: AFI
    private val binding: FragmentHospitalBinding get() = _binding!!
    private lateinit var updateDialogBinding: UpdateDialogBinding
    private lateinit var updateDialog: AlertDialog
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
        _binding = FragmentHospitalBinding.inflate(layoutInflater, container, false)

        val dataBaseHelper = DataBaseHelper()
        val hospital = Helper.hospital
        binding.header.address.text = hospital?.address?.completeAddress()
        binding.header.email.text = Helper.email
        binding.header.mobile.text = hospital?.contact
        binding.header.name.text = hospital?.name

        binding.avialIcuBeds.text = hospital?.availableIcuBed.toString()
        binding.avialOxyCilender.text = hospital?.availableOxygenCylinder.toString()
        binding.avialOxygen.text = hospital?.availableOxygen.toString()

        binding.bloodDetails.setBloodData(hospital?.bloodGroup)

        binding.icuEdit.setOnClickListener {
            updateDialogBinding.update.setOnClickListener {
                val data = updateDialogBinding.data.text.toString().toInt()
                updateDialog.dismiss()
                dataBaseHelper.updateIcuBeds(Helper.email, data, object:DataBaseHelper.Listener<Int>{
                    override fun onSuccess(t: Int?) {
                        hospital?.availableIcuBed = t
                        binding.avialIcuBeds.text = t.toString()
                        Toast.makeText(requireContext(), "Updated", Toast.LENGTH_SHORT).show()
                    }

                    override fun onFailure(message: String?) {
                        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                    }
                })
            }
            showUpdateDialog(hospital?.availableIcuBed)
        }

        binding.oxyCilenderEdit.setOnClickListener {
            updateDialogBinding.update.setOnClickListener {
                val data = updateDialogBinding.data.text.toString().toInt()
                updateDialog.dismiss()
                dataBaseHelper.updateOxygenCylinder(Helper.email, data, object:DataBaseHelper.Listener<Int>{
                    override fun onSuccess(t: Int?) {
                        hospital?.availableOxygenCylinder = t
                        binding.avialOxyCilender.text = t.toString()
                        Toast.makeText(requireContext(), "Updated", Toast.LENGTH_SHORT).show()
                    }

                    override fun onFailure(message: String?) {
                        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                    }
                })
            }
            showUpdateDialog(hospital?.availableOxygenCylinder)
        }

        binding.oxygenEdit.setOnClickListener {
            updateDialogBinding.update.setOnClickListener {
                val data = updateDialogBinding.data.text.toString().toDouble()
                updateDialog.dismiss()
                dataBaseHelper.updateOxygen(Helper.email, data, object:DataBaseHelper.Listener<Double>{
                    override fun onSuccess(t: Double?) {
                        hospital?.availableOxygen = t
                        binding.avialOxygen.text = t.toString()
                        Toast.makeText(requireContext(), "Updated", Toast.LENGTH_SHORT).show()
                    }

                    override fun onFailure(message: String?) {
                        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                    }
                })
            }
            showUpdateDialog(hospital?.availableOxygen)
        }

        binding.bloodDetails.edit.setOnClickListener {
            updateMultipleBinding.update.setOnClickListener {
                val data = updateMultipleBinding.readData()
                updateMultipleDialog.dismiss()
                dataBaseHelper.updateHospitalBloodData(Helper.email, data, object:DataBaseHelper.Listener<BloodGroup>{
                    override fun onSuccess(t: BloodGroup?) {
                        hospital?.bloodGroup = t
                        binding.bloodDetails.setBloodData(t)
                        Toast.makeText(requireContext(), "Updated", Toast.LENGTH_SHORT).show()
                    }

                    override fun onFailure(message: String?) {
                        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                    }
                })
            }
            showUpdateMultipleDialog(hospital?.bloodGroup)
        }

        buildUpdateDialogs()
        return binding.root
    }

    private fun buildUpdateDialogs() {
        updateDialogBinding = UpdateDialogBinding.inflate(LayoutInflater.from(requireContext()))
        updateDialog = AlertDialog.Builder(requireContext())
            .setView(updateDialogBinding.root)
            .create()

        updateMultipleBinding = UpdateMultipleBinding.inflate(LayoutInflater.from(requireContext()))
        updateMultipleDialog = AlertDialog.Builder(requireContext())
            .setView(updateMultipleBinding.root)
            .create()
    }

    private fun showUpdateDialog(data:Any?){
        updateDialogBinding.data.setText(data.toString())
        updateDialog.show()
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