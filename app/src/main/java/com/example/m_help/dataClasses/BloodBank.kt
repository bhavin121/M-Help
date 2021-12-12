package com.example.m_help.dataClasses

data class BloodBank(
    val name: String = "",
    val email: String? ="",
    val contact: String ="",
    val address: Address?=null,
    var bloodGroup: BloodGroup? = BloodGroup()
)
