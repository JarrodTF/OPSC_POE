package com.example.leadinghealthfitnessapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_workout.*
import kotlinx.android.synthetic.main.fragment_workout.textViewBMI
import kotlinx.android.synthetic.main.fragment_workout.textViewWeight
import kotlin.math.roundToInt

class WorkoutFragment : Fragment() {

    lateinit var workoutText: EditText
    lateinit var E1: EditText
    lateinit var E2: EditText
    lateinit var E3: EditText
    lateinit var timeNeeded: EditText
    lateinit var targetWeight: EditText
    lateinit var currentWeight: TextView
    lateinit var currentBMI: TextView
    lateinit var textSystem: TextView
    lateinit var ref: DatabaseReference
    private val currentUserID = FirebaseAuth.getInstance().currentUser?.uid.toString()
    var poundConv = 0.4535
    var kiloConv = 0.4535

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_workout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ref = FirebaseDatabase.getInstance().getReference("users")

        workoutText = editTextWorkoutName
        E1 = editTextE1
        E2 = editTextE2
        E3 = editTextE3
        timeNeeded = editTextTime
        targetWeight = editTextTargetWeight
        currentBMI = textViewBMI
        currentWeight = textViewWeight
        textSystem = textViewSystemWorkout

        textSystem.text = "KG"

        addWorkoutButton.setOnClickListener {
            addWorkout()
        }

        addWorkoutButton2.setOnClickListener {
            addWorkout()
        }

        ref.addValueEventListener(object: ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(p0: DataSnapshot) {

                if (p0!!.exists()) {
                    for (u in p0.children)
                    {
                        if (u.child("Profile").exists())
                        {
                            val user = u.child("Profile").getValue(User::class.java)

                            if (u.child("Workout").exists())
                            {
                                val workout = u.child("Workout").getValue(UserWorkout::class.java)

                                if(user!!.currentUserID == currentUserID)
                                {
                                    workoutText.setText(workout!!.workoutName.toString())
                                    E1.setText(workout!!.exercise1.toString())
                                    E2.setText(workout!!.exercise2.toString())
                                    E3.setText(workout!!.exercise3.toString())
                                    timeNeeded.setText(workout!!.time.toString())
                                    currentBMI.setText(user!!.bmi.toString())
                                    currentWeight.setText(user!!.weight.roundToInt().toString())

                                    if (user!!.mSystem == "FALSE"){
                                        textSystem.text = "LB"
                                        workout!!.targetweight = workout!!.targetweight/poundConv

                                    }else if (user!!.mSystem == "TRUE"){
                                        textSystem.text = "KG"
                                        targetWeight.setText(workout!!.targetweight.toString())
                                    }

                                    targetWeight.setText(workout!!.targetweight.roundToInt().toString())
                                }
                            }
                        }
                        else
                        {
                            val workout = u.child("Workout").getValue(UserWorkout::class.java)
                            val user = u.child("Profile").getValue(User::class.java)

                            workoutText.setText(workout!!.workoutName.toString())
                            E1.setText(workout!!.exercise1.toString())
                            E2.setText(workout!!.exercise2.toString())
                            E3.setText(workout!!.exercise3.toString())
                            timeNeeded.setText(workout!!.time.toString())
                            currentBMI.setText(user!!.bmi.toString())
                            currentWeight.setText(user!!.weight.toString())

                            if (user!!.mSystem == "FALSE"){
                                textSystem.text = "LB"
                                workout!!.targetweight = workout!!.targetweight/poundConv

                            }else if (user!!.mSystem == "TRUE"){
                                textSystem.text = "KG"
                                targetWeight.setText(workout!!.targetweight.toString())
                            }

                            targetWeight.setText(workout!!.targetweight.roundToInt().toString())
                        }
                    }
                }

            }
        })

    }

    private fun addWorkout(){
        val workoutName = workoutText.text.toString().trim()
        val exercise1 = E1.text.toString().trim()
        val exercise2 = E2.text.toString().trim()
        val exercise3 = E3.text.toString().trim()
        val time = timeNeeded.text.toString().trim()
        var targetWeight = targetWeight.text.toString().trim()

        if (workoutName.isEmpty()){
            workoutText.error = "Please enter a workout name!"
            return
        }

        if (exercise1.isEmpty()){
            E1.error = "Please type the details of this exercise!"
            return
        }

        if (exercise2.isEmpty()){
            E2.error = "Please type the details of this exercise!"
            return
        }

        if (exercise3.isEmpty()){
            E3.error = "Please type the details of this exercise!"
            return
        }

        if (time.isEmpty()){
            timeNeeded.error = "Please type the details of this exercise!"
            return
        }

        if (targetWeight.isEmpty()){
            timeNeeded.error = "You have not set your target weight!"
            return
        }

        val userWorkout = UserWorkout(workoutName, exercise1, exercise2, exercise3, time.toDouble(), targetWeight.toDouble())
        val workout = "Workout"

        ref.child(currentUserID).child(workout).setValue(userWorkout).addOnCompleteListener {
            Toast.makeText(activity, "User workout saved successfully", Toast.LENGTH_LONG).show()
        }
    }
}
