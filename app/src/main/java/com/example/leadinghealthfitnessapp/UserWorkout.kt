package com.example.leadinghealthfitnessapp

class UserWorkout(val workoutName: String, val exercise1: String, val exercise2: String, val exercise3: String, val time: Double, var targetweight: Double) {
    constructor(): this("","","","", 0.0, 0.0){

    }
}