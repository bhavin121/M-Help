package com.example.m_help

import android.app.Activity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.m_help.auth.GoogleSignInHelper
import com.example.m_help.databinding.ActivityMainBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import java.lang.Exception

class MainActivity : AppCompatActivity(), AFI {

    private lateinit var binding: ActivityMainBinding
    private lateinit var googleSignInHelper: GoogleSignInHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        googleSignInHelper = GoogleSignInHelper(this)

        val launcher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult(),
            ActivityResultCallback {
                if (it.resultCode != RESULT_OK) return@ActivityResultCallback
                googleSignInHelper.resolveSignInResult(it.data, object :GoogleSignInHelper.Listener{
                    override fun onSuccess(name: String?, email: String?) {
                        Helper.name = name
                        Helper.email = email
                    }

                    override fun onFailure(message: String?) {
                        Toast.makeText(this@MainActivity, message, Toast.LENGTH_SHORT)
                            .show()
                    }

                })
            }
        )


    }

    override fun changeFragmentTo(fragment: Fragment?, clearStack: Boolean) {
        if (clearStack) clearBackStack()
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.frame, fragment!!)
            .addToBackStack(null)
            .commit()
    }

    private fun clearBackStack() {
        val manager = supportFragmentManager
        val n = manager.backStackEntryCount
        for (i in 0 until n) {
            manager.popBackStack()
        }
    }
}