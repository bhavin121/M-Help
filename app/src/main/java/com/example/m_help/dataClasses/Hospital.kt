package com.example.m_help.dataClasses

data class Hospital(
    val name: String,
    val email: String?,
    val contact: String,
    val address: Address?,
    val bloodGroup: BloodGroup = BloodGroup(),
    val availableIcuBed: Int = 0,
    val availableOxygenCylinder: Int = 0,
    val availableOxygen: Double = 0.0
)
