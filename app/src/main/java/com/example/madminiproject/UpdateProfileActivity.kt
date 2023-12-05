package com.example.madminiproject

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso

class UpdateProfileActivity : AppCompatActivity() {

    // Initialize variables for Firebase authentication and storage reference
    private lateinit var firebaseAuth : FirebaseAuth
    private lateinit var storageRef : StorageReference

    // Initialize a tag for logging purposes
    private val TAG = "UpdateProfileActivity"

    // Initialize a URI variable for the selected image
    private var uri: Uri? = null

    // Register an activity result launcher for getting the selected image URI
    private val pickImage = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        // Do something with the selected image URI
        val imageView = findViewById<ImageView>(R.id.updateProfileImg)
        imageView.setImageURI(uri)
        this.uri = uri
    }

    // Create the activity
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Set the layout for the activity
        setContentView(R.layout.activity_update_profile)

        // Set a click listener for the image picker button
        val imagePickerButton = findViewById<ImageView>(R.id.updateProfileImg)
        imagePickerButton.setOnClickListener {
            pickImage.launch("image/*")
        }

        // Get the user information from the previous activity
        val name = intent.getStringExtra("name")
        val phone = intent.getStringExtra("phone")
        val country = intent.getStringExtra("country")
        val image = intent.getStringExtra("image")

        val edtName = findViewById<EditText>(R.id.editTextName)
        val edtPhone = findViewById<EditText>(R.id.editTextPhone)
        val edtCountry = findViewById<EditText>(R.id.editTextCountry)

        // Set the text and image views with the profile information
        if (image != null) {
            if (image.isNotEmpty()) {
                Picasso.get().load(image).into(imagePickerButton)
            } else {
                imagePickerButton.setImageResource(R.drawable.image_upload)
            }
        }
        edtName.setText(name)
        edtPhone.setText(phone)
        edtCountry.setText(country)

        // Initialize Firebase authentication
        firebaseAuth = FirebaseAuth.getInstance()

        val updateButton = findViewById<TextView>(R.id.updateProfileBtn)

        // Set a click listener for the update button
        updateButton.setOnClickListener {

            // Check if the required fields are not empty
            if (edtName.text.toString().isNotEmpty()) {

                // Get the current user from Firebase authentication
                val currentUser = firebaseAuth.currentUser

                // Get the user reference in the Firebase database for the current user
                val userRef = FirebaseDatabase.getInstance().reference.child("users")
                    .child(currentUser?.uid!!)


                // Check if an image has been selected
                if (uri != null) {

                    // Get the file extension from the selected image URI
                    val extension = contentResolver.getType(uri!!)?.substringAfterLast("/")

                    // Create a reference to the file in Firebase Storage with the current timestamp and file extension
                    val filename = System.currentTimeMillis().toString() + "." + extension
                    // val filename = userRef.key + "." + extension

                    // Set the storage reference to the Firebase Storage with the filename
                    storageRef = FirebaseStorage.getInstance().reference.child("users")
                        .child(filename)

                    // Upload the image to Firebase Storage
                    storageRef.putFile(uri!!)
                        .addOnSuccessListener {
                            // Image upload successful, get the download URL
                            storageRef.downloadUrl.addOnSuccessListener { downloadUrl ->
                                // Set the image URL in the User object and update it in the database
                                userRef.child("image").setValue(downloadUrl.toString())
                                userRef.child("name").setValue(edtName.text.toString())
                                userRef.child("phone").setValue(edtPhone.text.toString())
                                userRef.child("country").setValue(edtCountry.text.toString())
                                Toast.makeText(applicationContext, "Profile updated", Toast.LENGTH_SHORT).show()
                                finish()
                            }
                        }
                        .addOnFailureListener { e ->
                            // Image upload failed, handle the error and show a toast message
                            Toast.makeText(applicationContext, "Image upload failed", Toast.LENGTH_SHORT).show()
                            Log.e(TAG, "Image upload failed", e)
                        }
                        .addOnProgressListener { taskSnapshot ->
                            // Image upload is in progress, show a toast message with the progress
                            val progress = (100.0 * taskSnapshot.bytesTransferred / taskSnapshot.totalByteCount).toInt()
                            Toast.makeText(applicationContext, "Image uploading progress: $progress%", Toast.LENGTH_SHORT).show()
                        }
                } else {
                    // No image selected, update the User object in the database without an image URL
                    userRef.child("name").setValue(edtName.text.toString())
                    userRef.child("phone").setValue(edtPhone.text.toString())
                    userRef.child("country").setValue(edtCountry.text.toString())
                    Toast.makeText(applicationContext, "Profile updated", Toast.LENGTH_SHORT).show()
                    finish()
                }

            } else {
                // If any of the required fields are empty, show a toast message to the user
                Toast.makeText(this, "Name cannot be empty !!", Toast.LENGTH_SHORT).show()
            }

        }

        // Set up the "Back" button to finish the activity when clicked
        val updateProfileBack = findViewById<ImageView>(R.id.update_profile_back)
        updateProfileBack.setOnClickListener {
            finish()
        }
    }
}