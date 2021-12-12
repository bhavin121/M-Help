package com.example.m_help.dataClasses

data class Patient(
    val name: String = "",
    val contact: String ="",
    val address: Address?=null,
    val age: String="",
    val gender: String=""
)
