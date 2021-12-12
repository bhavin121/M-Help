package com.example.m_help.dataClasses

data class Donor(
    val name: String = "",
    val contact: String = "",
    val address: Address? = null,
    val bloodGroup: String = "",
    val donorType: String = "",
    val receiveForBlood: Boolean = true,
    val receiveForPlasma: Boolean = true
)
