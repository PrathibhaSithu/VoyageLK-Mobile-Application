package com.example.madminiproject

import android.content.Intent
import android.graphics.Paint
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.madminiproject.models.Feedback
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class create_feedback_form : AppCompatActivity() {


    // Initialize variables for Firebase authentication and storage reference
    private lateinit var firebaseAuth : FirebaseAuth
    private lateinit var storageRef : StorageReference

    // Initialize a tag for logging purposes
    private val TAG = "AddFeedback"

    // Initialize a URI variable for the selected image
    private var uri: Uri? = null

    // Register an activity result launcher for getting the selected image URI
    private val pickImage = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        // Do something with the selected image URI
        val imageView = findViewById<ImageView>(R.id.addFeedbackImg)
        imageView.setImageURI(uri)
        this.uri = uri
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_feedback_form)

        // Initialize Firebase authentication
        firebaseAuth = FirebaseAuth.getInstance()

        // Set a click listener for the image picker button
        val imagePickerButton = findViewById<ImageView>(R.id.addFeedbackImg)
        imagePickerButton.setOnClickListener {
            pickImage.launch("image/*")
        }

        val submitButton = findViewById<TextView>(R.id.addFeedbackBtn)
        val topBackButton = findViewById<ImageView>(R.id.backBtn)

        //back navigation button function
        topBackButton.setOnClickListener{
            finish()
        }

        // Set a click listener for the submit button
        submitButton.setOnClickListener {

            // Get the text from the input fields
            val email = findViewById<EditText>(R.id.feedbackEmail).text.toString()
            val rating = findViewById<EditText>(R.id.feedbackRating).text.toString()
            val feedback = findViewById<EditText>(R.id.feedbackBody).text.toString()

            // Check if the required fields are not empty and an image has been selected
            if (email.isNotEmpty() && rating.isNotEmpty() && feedback.isNotEmpty()  && uri != null) {

                // Get the current user from Firebase authentication
                val currentUser = firebaseAuth.currentUser

                // Create a new feedback reference in the Firebase database under the current user ID
                val feedbackRef = FirebaseDatabase.getInstance().reference.child("feedbacks")
                    .child(currentUser?.uid!!).push()

                // Create a reference to the "name" field of the current user in the "users" node of the database
                val nameRef = FirebaseDatabase.getInstance().reference.child("users")
                    .child(currentUser?.uid!!).child("name")

                var name: String? = ""

                // Read the value of the "name" field using a listener
                nameRef.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {

                        // Get the value of the "name" field and save it to the "name" variable
                        name = dataSnapshot.getValue(String::class.java)

                        // Check if an image has been selected
                        if (uri != null) {

                            // Get the file extension from the selected image URI
                            val extension = contentResolver.getType(uri!!)?.substringAfterLast("/")

                            // Create a reference to the file in Firebase Storage with the current timestamp and file extension
                            val filename = System.currentTimeMillis().toString() + "." + extension
                            // val filename = postRef.key + "." + extension

                            // Set the storage reference to the Firebase Storage with the filename
                            storageRef = FirebaseStorage.getInstance().reference.child("feedbacks")
                                .child(filename)

                            // Upload the image to Firebase Storage
                            storageRef.putFile(uri!!)
                                .addOnSuccessListener {
                                    // Image upload successful, get the download URL
                                    storageRef.downloadUrl.addOnSuccessListener { downloadUrl ->
                                        // Set the image URL in the Post object and save it to the database
                                        val feedback = Feedback(
                                            feedbackRef.key ?: "",
                                            downloadUrl.toString(),
                                            email,
                                            rating.toDouble(),
                                            feedback,
                                            name ?: "",
                                            currentUser.uid
                                        )
                                        feedbackRef.setValue(feedback)
                                        Toast.makeText(applicationContext, "Feedback added", Toast.LENGTH_SHORT).show()
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
                            // No image selected, save the Post object to the database without an image URL
                            val feedback = Feedback(feedbackRef.key ?: "", "", email, rating.toDouble(), feedback,name ?: "", currentUser.uid)
                            feedbackRef.setValue(feedback)
                            Toast.makeText(applicationContext, "Feedback added", Toast.LENGTH_SHORT).show()
                            finish()
                        }
                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        // Handle errors here if the read operation is cancelled or fails
                        Log.e(TAG, "onCancelled", databaseError.toException())
                    }
                })
            } else {
                // If any of the required fields are empty, show a toast message to the user
                Toast.makeText(this, "Empty fields are not allowed !", Toast.LENGTH_SHORT).show()
            }

        }

    }
}