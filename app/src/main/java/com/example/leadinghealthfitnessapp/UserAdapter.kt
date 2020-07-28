package com.example.leadinghealthfitnessapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import kotlin.math.roundToInt

class UserAdapter(val mContext: Context, val layoutResId: Int, val userList: List<User>):ArrayAdapter<User>(mContext, layoutResId, userList) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val layoutInflater: LayoutInflater = LayoutInflater.from(mContext)
        val view: View = layoutInflater.inflate(layoutResId, null)

        val allUserInfo = view.findViewById<TextView>(R.id.allUserInfo)
        val user = userList[position]

        allUserInfo.text = "Name: " + user.name + "\n" + "Age: " + user.age.toString() + "\n" + "Weight: " + user.weight.roundToInt().toString() + "\n" + "BMI: " + user.bmi.toString() + "\n" + "Height: " + "%.2f".format(user!!.height)
        return  view
    }

}