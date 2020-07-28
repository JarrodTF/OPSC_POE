package com.example.leadinghealthfitnessapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlin.math.roundToInt

class HomeFragment : Fragment() {
    lateinit var userBMI: TextView
    lateinit var userCals: TextView
    lateinit var userWeight: TextView
    lateinit var userP: TextView
    lateinit var userC: TextView
    lateinit var userF: TextView
    lateinit var activeGoals: TextView
    lateinit var measureSystem: TextView
    lateinit var ref: DatabaseReference
    private val currentUserID = FirebaseAuth.getInstance().currentUser?.uid.toString()
    private var numActiveGoals: Int = 0


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ref = FirebaseDatabase.getInstance().getReference("users")

        userBMI = textViewBMI
        userCals = textViewCals
        userWeight = textViewWeight
        userP = textViewP
        userC = textViewC
        userF = textViewF
        activeGoals = textViewActiveGoals
        measureSystem = textViewSystem

        measureSystem.text = "Weight"


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

                            if (u.child("Diet Goals").exists())
                            {

                                val goals = u.child("Diet Goals").getValue(UserDiet::class.java)

                                if (u.child("Workout").exists()){

                                    if(user!!.currentUserID == currentUserID)
                                    {
                                        numActiveGoals++
                                        numActiveGoals++
                                        userBMI.setText(user!!.bmi.toString())
                                        userCals.setText(goals!!.targetCalorie.toString())
                                        userWeight.setText(user!!.weight.roundToInt().toString())
                                        userP.setText(goals!!.protein.toString())
                                        userC.setText(goals!!.carb.toString())
                                        userF.setText(goals!!.fat.toString())
                                        activeGoals.setText(numActiveGoals.toString())
                                    }
                                }
                            }
                        }
                    }
                }

            }
        })
    }
}
