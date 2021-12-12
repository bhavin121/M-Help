package com.example.m_help.dataClasses

data class Donor(
    val name: String = "",
    val contact: String = "",
    val address: Address? = null,
    val bloodGroup: String = "",
    val donorType: String = "",
    var receiveForBlood: Boolean? = true,
    var receiveForPlasma: Boolean? = true
)
