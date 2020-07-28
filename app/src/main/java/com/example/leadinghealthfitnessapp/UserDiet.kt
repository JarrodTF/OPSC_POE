package com.example.leadinghealthfitnessapp

class UserDiet(val mealName: String, val protein: Double, val fat: Double, val carb: Double, val mealCalorieCount: Int, val targetCalorie: Int){

    constructor(): this("",0.0,0.0,0.0, 0,0){

    }
}