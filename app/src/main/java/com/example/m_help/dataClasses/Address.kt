package com.example.m_help.dataClasses

data class Address (
    val city: String?=null,
    val state: String?=null,
    val street: String?=null,
    val country: String? =null,
    val latitude: Double? = 0.0,
    val longitude: Double? = 0.0
)
