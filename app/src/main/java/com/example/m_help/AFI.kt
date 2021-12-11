package com.example.m_help

import androidx.fragment.app.Fragment

// Activity Fragment Interface

interface AFI {
    fun changeFragmentTo(fragment: Fragment?, clearStack: Boolean)
    fun signIn()
    fun signOut()
}