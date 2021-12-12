package com.example.m_help

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.m_help.auth.GoogleSignInHelper
import com.example.m_help.databinding.ActivityMainBinding
import com.example.m_help.fragments.HomeFragment
import com.example.m_help.fragments.SignInFragment
import com.example.m_help.fragments.SplashFragment

class MainActivity : AppCompatActivity(), AFI {

    private lateinit var launcher: ActivityResultLauncher<Intent>
    private lateinit var binding: ActivityMainBinding
    private lateinit var googleSignInHelper: GoogleSignInHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        googleSignInHelper = GoogleSignInHelper(this)

        launcher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult(),
            ActivityResultCallback {
                if (it.resultCode != RESULT_OK) return@ActivityResultCallback
                googleSignInHelper.resolveSignInResult(it.data, object :GoogleSignInHelper.Listener{
                    override fun onSuccess(name: String?, email: String?) {
                        Helper.name = name
                        Helper.email = email
                        changeFragmentTo(HomeFragment(), true)
                    }

                    override fun onFailure(message: String?) {
                        Toast.makeText(this@MainActivity, message, Toast.LENGTH_SHORT)
                            .show()
                    }

                })
            }
        )

        changeFragmentTo(SplashFragment(), false)


    }

    override fun onBackPressed() {
        super.onBackPressed()
        if (supportFragmentManager.backStackEntryCount == 0) {
            super.onBackPressed()
        }
    }

    override fun onStart() {
        super.onStart()
        val account = googleSignInHelper.getSignedInUser()
        if(account==null){
            // sends to sign in fragment
            Handler().postDelayed({ changeFragmentTo(SignInFragment(), true) }, 1000)
        }else{
            Helper.name = account.displayName
            Helper.email = account.email
            Handler().postDelayed({
                changeFragmentTo(HomeFragment(), true)
            }, 1000)
        }
    }

    override fun changeFragmentTo(fragment: Fragment?, clearStack: Boolean) {
        if (clearStack) clearBackStack()
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.frame, fragment!!)
            .addToBackStack(null)
            .commit()
    }

    override fun signIn() {
        googleSignInHelper.signIn(launcher)
    }

    override fun signOut() {
        googleSignInHelper.signOut {
            changeFragmentTo(SignInFragment(), true)
        }
    }

    private fun clearBackStack() {
        val manager = supportFragmentManager
        val n = manager.backStackEntryCount
        for (i in 0 until n) {
            manager.popBackStack()
        }
    }
}