package com.example.leadinghealthfitnessapp

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.ErrorCodes
import com.firebase.ui.auth.IdpResponse
import com.firebase.ui.auth.viewmodel.RequestCodes
import com.google.firebase.auth.FirebaseAuth
import java.lang.Error

class LoginActivity : AppCompatActivity() {

    private val RC_SIGN_IN = 1111
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val auth = FirebaseAuth.getInstance()
        if(auth.currentUser != null){
            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
        } else{
            startActivityForResult(
                AuthUI.getInstance()
                    .createSignInIntentBuilder()
                    .setAvailableProviders(
                        listOf(
                            AuthUI.IdpConfig.EmailBuilder().build(),
                            AuthUI.IdpConfig.GoogleBuilder().build()
                        )
                    )
                    .setTheme(R.style.AppTheme)
                    .setIsSmartLockEnabled(false)
                    .build(),
                RC_SIGN_IN
            )
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data:Intent?){
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN){
            val response = IdpResponse.fromResultIntent(data)
            if (resultCode == Activity.RESULT_OK) {
                startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                finish()
                return
            } else{
                    if(response == null){
                        Log.e("Login", "Login cancelled by user")
                        Toast.makeText(this, "Cancelled", Toast.LENGTH_SHORT)
                            .show()
                        return
                    }
                    if (response.error!!.errorCode == ErrorCodes.NO_NETWORK){
                        Log.e("Login", "No Internet Connection")
                        Toast.makeText(this, "There is no internet connection", Toast.LENGTH_SHORT)
                            .show()
                        return
                    }
                    if (response.error!!.errorCode == ErrorCodes.UNKNOWN_ERROR){
                        Log.e("Login", "Unknown Error")
                        Toast.makeText(this, "Unknown Error", Toast.LENGTH_SHORT)
                            .show()
                        return
                    }

                Log.e("Login", "Unknown sign in response")
                Toast.makeText(this, "Unknown Sign in Error", Toast.LENGTH_SHORT)
                    .show()
                }
            }
        }
    }
