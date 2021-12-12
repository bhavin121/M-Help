package com.example.m_help.auth

import android.content.Context
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions

class GoogleSignInHelper(private val context: Context) {
    private val googleSignInOpt = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestEmail()
        .build()

    private var googleSignInClient: GoogleSignInClient = GoogleSignIn.getClient(context, googleSignInOpt)

    fun getSignedInUser():GoogleSignInAccount?{
        return GoogleSignIn.getLastSignedInAccount(context)
    }

    fun resolveSignInResult(intent: Intent?, l:Listener){
        if (intent == null){
            l.onFailure("Something went wrong")
            return
        }
        val task = GoogleSignIn.getSignedInAccountFromIntent(intent)
        task.addOnSuccessListener { googleSignInAccount: GoogleSignInAccount ->
            // Sign in success
            val email = googleSignInAccount.email
            val name = googleSignInAccount.displayName
            l.onSuccess(name, email)
        }
        task.addOnFailureListener { e: Exception? ->
            // Sign in failed
            l.onFailure(e?.message)
        }
    }

    fun signOut(listener: Listener){
        googleSignInClient.signOut()
            .addOnCompleteListener {
                if(it.isSuccessful){
                    listener.onSuccess("","")
                }
            }
    }

    fun signIn(launcher: ActivityResultLauncher<Intent>){
        val intent = googleSignInClient.signInIntent
        launcher.launch(intent)
    }

    interface Listener{
        fun onSuccess(name:String?, email:String?)
        fun onFailure(message: String?)
    }
}