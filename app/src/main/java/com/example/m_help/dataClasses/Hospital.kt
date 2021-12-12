package com.example.m_help.dataClasses

data class Hospital(
    val name: String = "",
    val email: String? = "",
    val contact: String = "",
    val address: Address? = null,
    var bloodGroup: BloodGroup? = BloodGroup(),
    var availableIcuBed: Int? = 0,
    var availableOxygenCylinder: Int? = 0,
    var availableOxygen: Double? = 0.0
)
