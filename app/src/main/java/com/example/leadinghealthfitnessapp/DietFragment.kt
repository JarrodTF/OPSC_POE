package com.example.leadinghealthfitnessapp

import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker.checkSelfPermission
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_diet.*

class DietFragment : Fragment() {

    lateinit var userMealText: EditText
    lateinit var userProteinText: EditText
    lateinit var userFatText: EditText
    lateinit var userCarbText: EditText
    lateinit var userTotalCalorieText: EditText
    lateinit var userTargetCalorie: EditText
    lateinit var ref: DatabaseReference
    private val currentUserID = FirebaseAuth.getInstance().currentUser?.uid.toString()
    lateinit var image_view:ImageView

    private val PERMISSION_CODE = 1000;
    private val IMAGE_CAPTURE_CODE = 1001
    var image_uri: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_diet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val food1 = imageViewMeal
        val food2 = imageViewMeal2
        val food3 = imageViewMeal3
        val food4 = imageViewMeal4

        ref = FirebaseDatabase.getInstance().getReference("users")

        userMealText = editTextMealName
        userProteinText = editTextProtein
        userFatText = editTextFat
        userCarbText = editTextCarbs
        userTotalCalorieText = editTextAmountCalories
        userTargetCalorie = editTextTargetCalorie


        saveMealButton.setOnClickListener {
            saveMealInfo()
        }

        imageViewMeal.setOnClickListener {
            image_view = food1

            val permissionCheck: Int = ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.CAMERA)
            val permissionCheck2: Int = ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)

            //if system os is Marshmallow or Above, we need to request runtime permission
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                if (permissionCheck == PackageManager.PERMISSION_DENIED || permissionCheck2 == PackageManager.PERMISSION_DENIED){
                    //permission was not enabled
                    val permission = arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    //show popup to request permission
                    requestPermissions(permission, PERMISSION_CODE)
                }
                else{
                    //permission already granted
                    openCamera()
                }
            }
            else{
                //system os is < marshmallow
                openCamera()
            }
        }
        imageViewMeal2.setOnClickListener {
            image_view = food2

            val permissionCheck: Int = ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.CAMERA)
            val permissionCheck2: Int = ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)

            //if system os is Marshmallow or Above, we need to request runtime permission
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                if (permissionCheck == PackageManager.PERMISSION_DENIED || permissionCheck2 == PackageManager.PERMISSION_DENIED){
                    //permission was not enabled
                    val permission = arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    //show popup to request permission
                    requestPermissions(permission, PERMISSION_CODE)
                }
                else{
                    //permission already granted
                    openCamera()
                }
            }
            else{
                //system os is < marshmallow
                openCamera()
            }
        }
        imageViewMeal3.setOnClickListener {
            image_view = food3

            val permissionCheck: Int = ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.CAMERA)
            val permissionCheck2: Int = ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)

            //if system os is Marshmallow or Above, we need to request runtime permission
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                if (permissionCheck == PackageManager.PERMISSION_DENIED || permissionCheck2 == PackageManager.PERMISSION_DENIED){
                    //permission was not enabled
                    val permission = arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    //show popup to request permission
                    requestPermissions(permission, PERMISSION_CODE)
                }
                else{
                    //permission already granted
                    openCamera()
                }
            }
            else{
                //system os is < marshmallow
                openCamera()
            }
        }
        imageViewMeal4.setOnClickListener {
            image_view = food4

            val permissionCheck: Int = ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.CAMERA)
            val permissionCheck2: Int = ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)

            //if system os is Marshmallow or Above, we need to request runtime permission
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                if (permissionCheck == PackageManager.PERMISSION_DENIED || permissionCheck2 == PackageManager.PERMISSION_DENIED){
                    //permission was not enabled
                    val permission = arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    //show popup to request permission
                    requestPermissions(permission, PERMISSION_CODE)
                }
                else{
                    //permission already granted
                    openCamera()
                }
            }
            else{
                //system os is < marshmallow
                openCamera()
            }
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

                            if (u.child("Diet Goals").exists())
                            {
                                val goals = u.child("Diet Goals").getValue(UserDiet::class.java)

                                if(user!!.currentUserID == currentUserID)
                                {
                                    userMealText.setText(goals!!.mealName.toString())
                                    userProteinText.setText(goals!!.protein.toString())
                                    userFatText.setText(goals!!.fat.toString())
                                    userCarbText.setText(goals!!.carb.toString())
                                    userTotalCalorieText.setText(goals!!.mealCalorieCount.toString())
                                    userTargetCalorie.setText(goals!!.targetCalorie.toString())
                                }
                                }
                            }
                        else
                        {
                            val goals = u.child("Diet Goals").getValue(UserDiet::class.java)

                            userMealText.setText(goals!!.mealName.toString())
                            userProteinText.setText(goals!!.protein.toString())
                            userFatText.setText(goals!!.fat.toString())
                            userCarbText.setText(goals!!.carb.toString())
                            userTotalCalorieText.setText(goals!!.mealCalorieCount.toString())
                            userTargetCalorie.setText(goals!!.targetCalorie.toString())
                        }
                        }
                    }

                }
        })
    }

    private fun saveMealInfo(){
        val mealName = userMealText.text.toString().trim()
        val protein = userProteinText.text.toString().trim()
        val fat = userFatText.text.toString().trim()
        val carb = userCarbText.text.toString().trim()
        val mealCalorieCount = userTotalCalorieText.text.toString().trim()
        val targetCalorie = userTargetCalorie.text.toString().trim()

        if (mealName.isEmpty()){
            userMealText.error = "Please enter a meal name!"
            return
        }

        if (protein.isEmpty()){
            userProteinText.error = "Please enter the amount of protein!"
            return
        }

        if (fat.isEmpty()){
            userFatText.error = "Please enter the amount of fat!"
            return
        }

        if (carb.isEmpty()){
            userCarbText.error = "Please enter the amount of carbs!"
            return
        }

        if (mealCalorieCount.isEmpty()){
            userTotalCalorieText.error = "Please enter the meals total calories"
            return
        }

        if (targetCalorie.isEmpty()){
            userTargetCalorie.error = "Please enter your daily target calorie count"
            return
        }

        val userDiet = UserDiet(mealName, protein.toDouble(), fat.toDouble(), carb.toDouble(), mealCalorieCount.toInt(), targetCalorie.toInt())
        val dietGoals = "Diet Goals"

        ref.child(currentUserID).child(dietGoals).setValue(userDiet).addOnCompleteListener {
            Toast.makeText(activity, "User meal and goal saved successfully", Toast.LENGTH_LONG).show()
        }
    }

    private fun openCamera() {
        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, "New Picture")
        values.put(MediaStore.Images.Media.DESCRIPTION, "From the Camera")
        image_uri = requireActivity().contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        //camera intent
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri)
        startActivityForResult(cameraIntent, IMAGE_CAPTURE_CODE)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        //called when user presses ALLOW or DENY from Permission Request Popup
        when(requestCode){
            PERMISSION_CODE -> {
                if (grantResults.size > 0 && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED){
                    //permission from popup was granted
                    openCamera()
                }
                else{
                    //permission from popup was denied
                    Toast.makeText(activity, "Permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        //called when image was captured from camera intent
        if (resultCode == Activity.RESULT_OK){
            //set image captured to image view

            image_view.setImageURI(image_uri)
        }
    }
}
