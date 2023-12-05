package com.example.madminiproject

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.madminiproject.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.squareup.picasso.Picasso

class ViewProfileActivity : AppCompatActivity() {

    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private lateinit var authId: String
    private val TAG = "ViewProfileActivity"

    private lateinit var viewImage: ImageView
    private lateinit var viewName: TextView
    private lateinit var viewEmail: TextView
    private lateinit var viewPhone: TextView
    private lateinit var viewCountry: TextView

    // Declare the user variable
    private lateinit var user: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_profile)

        // Initialize Firebase
        init()

        viewImage = findViewById(R.id.view_profile_profileImg)
        viewName = findViewById(R.id.view_profile_name)
        viewEmail = findViewById(R.id.view_profile_email)
        viewPhone = findViewById(R.id.view_profile_phone)
        viewCountry = findViewById(R.id.view_profile_country)

        // Retrieve and display user profile data
        retrieveUserProfile()

        val editProfile = findViewById<ImageView>(R.id.btn_edit_profile)
        editProfile.setOnClickListener {
            val intent = Intent(this, UpdateProfileActivity::class.java)
            intent.putExtra("name", user.name)
            intent.putExtra("phone", user.phone)
            intent.putExtra("country", user.country)
            intent.putExtra("image", user.image)
            startActivity(intent)
        }

        // Set up the "Back" button to finish the activity when clicked
        val viewProfileBack = findViewById<ImageView>(R.id.view_profile_back)
        viewProfileBack.setOnClickListener {
            finish()
        }
    }

    private fun init() {
        auth = FirebaseAuth.getInstance()
        authId = auth.currentUser!!.uid
        database = FirebaseDatabase.getInstance().reference.child("users").child(authId)
    }

    private fun retrieveUserProfile() {
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                user = dataSnapshot.getValue(User::class.java)!!

                viewName.text = user.name
                viewEmail.text = user.email
                viewPhone.text = user.phone
                viewCountry.text = user.country

                // Load and display profile image using Picasso or any other library
                if (user.image.isNotEmpty()) {
                    Picasso.get().load(user.image).into(viewImage)
                } else {
                    viewImage.setImageResource(R.drawable.acc_profile_pic)
                }

            }

            override fun onCancelled(error: DatabaseError) {
                Log.e(TAG, "Failed to retrieve user profile: ${error.message}")
            }
        })
    }
}
