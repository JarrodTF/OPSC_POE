package com.example.leadinghealthfitnessapp

import com.google.firebase.auth.FirebaseAuth

class User(val currentUserID: String, val name: String, val age: Int, var weight: Double, val bmi: Double, var height: Double, var mSystem: String){

    constructor(): this("","",0,0.0,0.0,0.0, ""){

    }
}