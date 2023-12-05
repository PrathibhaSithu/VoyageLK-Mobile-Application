package com.example.madminiproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.madminiproject.adapters.UserSpecificPackageAdapter
import com.example.madminiproject.models.PackageModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class activity_package_configurations : AppCompatActivity() {

    // Declare variables for RecyclerView, adapter, Firebase database, authentication, authentication ID, and tag for debugging
    private lateinit var recyclerView: RecyclerView
    private lateinit var PackageAdapter: UserSpecificPackageAdapter
    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private lateinit var authId: String
    private val TAG = "UserSpecificPackagesActivity"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_package_configurations)

        // Initialize Firebase objects
        init()

        val topBarBackButton = findViewById<ImageView>(R.id.pkgBackBtn)

        //back navigation button function
        topBarBackButton.setOnClickListener{
            finish()
        }

        val addPackNavigationBtn = findViewById<Button>(R.id.pack_config_add_btn)

        addPackNavigationBtn.setOnClickListener {
            val intent = Intent(this,create_package_form::class.java)
            startActivity(intent)
        }


        // Call function to retrieve data from Firebase
        getTaskFromFirebase()

//        // Set up the "Back" button to finish the activity when clicked
//        val postBack = findViewById<ImageView>(R.id.my_posts_back)
//        postBack.setOnClickListener {
//            finish()
//        }
    }

//    function for initialize the firebase objects
    private fun init() {
        auth = FirebaseAuth.getInstance()
        authId = auth.currentUser!!.uid
        database = FirebaseDatabase.getInstance().reference.child("packages").child(authId)
    }

    private fun getTaskFromFirebase() {
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val packageList = ArrayList<PackageModel>()
                for (postSnapshot in snapshot.children) {
                    val pack = postSnapshot.getValue(PackageModel::class.java)
                    pack?.let { packageList.add(it) }
                }

                recyclerView = findViewById(R.id.user_specific_packages_recycler)
                PackageAdapter = UserSpecificPackageAdapter(packageList) { pack -> onItemClick(pack) }
                recyclerView.adapter = PackageAdapter
                recyclerView.layoutManager = LinearLayoutManager(this@activity_package_configurations)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e(TAG, "onCancelled", error.toException())
            }
        })
    }

    // Define a function to handle click events on a Post object and start the ExploreViewPostActivity with relevant data
    private fun onItemClick(pack: PackageModel) {
        val intent = Intent(this, PackageDescription::class.java)
        intent.putExtra("packID", pack.packID)
        intent.putExtra("hotelName", pack.hotelName)
        intent.putExtra("hotelLocation", pack.hotelLocation)
        intent.putExtra("packAuthor", pack.packAuthor)
        intent.putExtra("hotelPrice", pack.hotelPrice)
        intent.putExtra("packDescription", pack.packDesc)
        intent.putExtra("hotelContact", pack.contactNo)
        intent.putExtra("image", pack.packImage)
        startActivity(intent)
    }
}