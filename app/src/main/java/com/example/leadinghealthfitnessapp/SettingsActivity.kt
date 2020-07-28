package com.example.leadinghealthfitnessapp

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_settings.*
import kotlin.math.roundToInt

class SettingsActivity : AppCompatActivity() {
    private val auth = FirebaseAuth.getInstance()
    lateinit var userNameText: EditText
    lateinit var userAgeText: EditText
    lateinit var userWeightText: EditText
    lateinit var userBMIText: EditText
    lateinit var userHeightText: EditText
    lateinit var userListView: ListView
    lateinit var textViewSystem: TextView
    lateinit var textViewSystem2: TextView
    lateinit var userList: MutableList<User>
    lateinit var ref: DatabaseReference
    private val currentUserID = FirebaseAuth.getInstance().currentUser?.uid.toString()
    private var metric: Boolean = true
    private var isExecuted: Boolean = false
    private var hasConvertedImperial: Boolean = false
    private var hasConvertedMetric: Boolean = false
    private var isChecked: Boolean = true
    private var firstTime: Boolean = true
    private var firstTime2: Boolean = true
    var poundConv = 0.4535
    var kiloConv = 0.4535
    var mConv = 0.3048
    var fConv = 0.3048


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        userList = mutableListOf()
        ref = FirebaseDatabase.getInstance().getReference("users")

        userNameText = findViewById(R.id.nameEditText)
        userAgeText = findViewById(R.id.ageEditText)
        userWeightText = findViewById(R.id.weightEditText)
        userBMIText = findViewById(R.id.bmiEditText)
        userHeightText = findViewById(R.id.heightEditText)
        userListView = findViewById<ListView>(R.id.userDataListView)
        textViewSystem = findViewById(R.id.textViewSystem)
        textViewSystem2 = findViewById(R.id.textViewSystem2)

        //measuringSystemText.text = "Metric System"
        //textViewSystem.text = "kg"
        //textViewSystem2.text = "m"

        LoadPreference()

        if (isExecuted == false){
            createProfile()
            isExecuted = true
        }

        saveButton.setOnClickListener {
            saveInfo()
            savePreference()
            userList.clear()
        }

        switch1.setOnCheckedChangeListener{ _, isChecked ->
            if (isChecked){
                metric = false
                measuringSystemText.text = "Imperial System"
                textViewSystem.text = "lb"
                textViewSystem2.text = "ft"
            }else{
                metric = true
                measuringSystemText.text = "Metric System"
                textViewSystem.text = "kg"
                textViewSystem2.text = "m"
            }
        }

        ref.addValueEventListener(object: ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(p0: DataSnapshot) {
                if (p0!!.exists()){
                    userList.clear()

                    for (i in p0.children){

                        if (i.child("Profile").exists()){
                            val user = i.child("Profile").getValue(User::class.java)

                            if (user!!.currentUserID == currentUserID){
                                userNameText.setText(user!!.name.toString())
                                userAgeText.setText(user!!.age.toString())
                                userBMIText.setText(user!!.bmi.toString())

                                if (user!!.mSystem.toString() == "FALSE" && textViewSystem.text == "lb"){

                                    if (textViewSystem.text == "lb" && !hasConvertedImperial){
                                        user!!.weight = user!!.weight/poundConv
                                        userWeightText.setText(user!!.weight.roundToInt().toString())

                                        user!!.height = user!!.height/fConv

                                        hasConvertedImperial = true
                                        hasConvertedMetric = false
                                    }

                                    userList.add(user!!)

                                }else if (user!!.mSystem.toString() == "TRUE" && textViewSystem.text == "kg"){

                                    if (textViewSystem.text == "kg" && !hasConvertedMetric && hasConvertedImperial){
                                        user!!.weight = user!!.weight*kiloConv
                                        userWeightText.setText(user!!.weight.roundToInt().toString())

                                        user!!.height = user!!.height*mConv

                                        hasConvertedMetric = true
                                        hasConvertedImperial = false
                                    }

                                    userList.add(user!!)
                                }

                                userWeightText.setText(user!!.weight.roundToInt().toString())
                                userHeightText.setText("%.2f".format(user!!.height))

                                val mSystem1 = User(currentUserID ,user.name, user.age.toInt(), user.weight.toDouble(), user.bmi.toDouble(), user.height.toDouble(),user.mSystem)
                                ref.child(currentUserID).child("Profile").setValue(mSystem1)
                            }
                        }
                    }

                    val adapter = UserAdapter(applicationContext, R.layout.users, userList)
                    userListView.adapter = adapter
                }
            }
        })

        val logoutBut = findViewById<Button>(R.id.signoutButton)
        logoutBut.setOnClickListener{
            signOut()
            val i = Intent(this, LoginActivity::class.java)
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            i.putExtra("EXIT", true)
            startActivity(i)
            finish()
        }

        auth.addAuthStateListener {
            if (auth.currentUser == null){
                this.finish()
            }
        }
    }

    private fun savePreference(){
        val sharedPreferences = getSharedPreferences("userPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.apply{putBoolean("BOOLEAN_KEY", switch1.isChecked)}.apply()

        Toast.makeText(this, "Preference Saved", Toast.LENGTH_SHORT).show()
    }

    private fun LoadPreference(){
        val sharedPreferences = getSharedPreferences("userPrefs", Context.MODE_PRIVATE)
        val savedBoolean = sharedPreferences.getBoolean("BOOLEAN_KEY", false)

        switch1.isChecked = savedBoolean

        if(switch1.isChecked){
            measuringSystemText.text = "Imperial System"
            textViewSystem.text = "lb"
            textViewSystem2.text = "f"
        }
        else{
            measuringSystemText.text = "Metric System"
            textViewSystem.text = "kg"
            textViewSystem2.text = "m"
        }
    }

   private fun signOut(){
        auth.signOut()
    }

    private fun saveInfo(){
        val name = userNameText.text.toString().trim()
        val age = userAgeText.text.toString().trim()
        var weight = userWeightText.text.toString().trim()
        val bmi = userBMIText.text.toString().trim()
        var height = userHeightText.text.toString().trim()

        var mSystem = if (metric){
            "TRUE"
        }
        else{
            "FALSE"
        }


        if (name.isEmpty()){
            userNameText.error = "Please enter a name!"
            return
        }

        if (age.isEmpty()){
            userAgeText.error = "Please enter your age!"
            return
        }

        if (weight.isEmpty()){
            userWeightText.error = "Please enter your weight!"
            return
        }

        if (bmi.isEmpty()){
            userBMIText.error = "Please enter your bmi"
            return
        }

        if (height.isEmpty()){
            userHeightText.error = "Please enter your height!"
            return
        }
        //val userid = ref.push().key!!
        val user = User(currentUserID ,name, age.toInt(), weight.toDouble(), bmi.toDouble(), height.toDouble(),mSystem)
        val profile = "Profile"


        ref.child(currentUserID).child(profile).setValue(user).addOnCompleteListener {
            Toast.makeText(applicationContext, "User saved successfully", Toast.LENGTH_LONG).show()
        }
    }

    private fun createProfile(){
        val name = userNameText.text.toString().trim()
        val age = userAgeText.text.toString().trim()
        var weight = userWeightText.text.toString().trim()
        val bmi = userBMIText.text.toString().trim()
        var height = userHeightText.text.toString().trim()

        var mSystem = if (metric){
            "TRUE"
        }
        else{
            "FALSE"
        }

        if (name.isEmpty()){
            userNameText.text = null
            return
        }

        if (age.isEmpty()){
            userAgeText.text = null
            return
        }

        if (weight.isEmpty()){
            userWeightText.text = null
            return
        }

        if (bmi.isEmpty()){
            userBMIText.text = null
            return
        }

        if (height.isEmpty()){
            userHeightText.text = null
            return
        }

        val user = User(currentUserID ,name, age.toInt(), weight.toDouble(), bmi.toDouble(), height.toDouble(),mSystem)
        val profile = "Profile"

        ref.child(currentUserID).child(profile).setValue(user)
    }
}
