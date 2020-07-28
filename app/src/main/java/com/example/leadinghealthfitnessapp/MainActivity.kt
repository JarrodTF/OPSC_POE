package com.example.leadinghealthfitnessapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_home.*

class MainActivity : AppCompatActivity() {

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener {item ->  
        when(item.itemId){
            R.id.nav_home -> {
                replaceFragment(HomeFragment())
                return@OnNavigationItemSelectedListener true
            }
            R.id.nav_workout -> {
                replaceFragment(WorkoutFragment())
                return@OnNavigationItemSelectedListener true
            }
            R.id.nav_diet -> {
                replaceFragment(DietFragment())
                return@OnNavigationItemSelectedListener true
            }
            R.id.nav_selfcare -> {
                replaceFragment(SelfcareFragment())
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottom_navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        replaceFragment(HomeFragment())

        val settingsBut = findViewById<ImageButton>(R.id.settingsButton)
        settingsBut.setOnClickListener{
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }
    }

    private fun replaceFragment(fragment: Fragment){
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragmentContainer, fragment)
        fragmentTransaction.commit()
    }

    override fun onBackPressed() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Exit Leading Health")
        builder.setMessage("Do you want exit the app")
        builder.setPositiveButton("Yes", {dialog, which -> super.onBackPressed()})
        builder.setNegativeButton("No",{dialog, which ->  })
        builder.show()

    }
}
