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
import com.example.madminiproject.models.Post
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso


class ExploreUpdatePostActivity : AppCompatActivity() {

    // Initialize variables for Firebase authentication and storage reference
    private lateinit var firebaseAuth : FirebaseAuth
    private lateinit var storageRef : StorageReference

    // Initialize a tag for logging purposes
    private val TAG = "ExploreUpdatePostActivity"

    // Initialize a URI variable for the selected image
    private var uri: Uri? = null

    // Register an activity result launcher for getting the selected image URI
    private val pickImage = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        // Do something with the selected image URI
        val imageView = findViewById<ImageView>(R.id.explorePostImg)
        imageView.setImageURI(uri)
        this.uri = uri
    }

    // Create the activity
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Set the layout for the activity
        setContentView(R.layout.activity_explore_update_post)

        // Set a click listener for the image picker button
        val imagePickerButton = findViewById<ImageView>(R.id.explorePostImg)
        imagePickerButton.setOnClickListener {
            pickImage.launch("image/*")
        }

        // Get the post information from the previous activity
        val id = intent.getStringExtra("postId")
        val image = intent.getStringExtra("image")
        val title = intent.getStringExtra("title")
        val location = intent.getStringExtra("location")
        val content = intent.getStringExtra("content")

        val edtTitle = findViewById<EditText>(R.id.editTextExplorePostTitle)
        val edtLocation = findViewById<EditText>(R.id.editTextExplorePostLocation)
        val edtContent = findViewById<EditText>(R.id.editTextExplorePostContent)

        // Set the text and image views with the post information
        Picasso.get()
            .load(image)
            .into(imagePickerButton)
        edtTitle.setText(title)
        edtLocation.setText(location)
        edtContent.setText(content)

        // Initialize Firebase authentication
        firebaseAuth = FirebaseAuth.getInstance()

        val updateButton = findViewById<TextView>(R.id.updatePostBtn)

        // Set a click listener for the update button
        updateButton.setOnClickListener {

            // Check if the required fields are not empty
            if (edtTitle.text.toString().isNotEmpty() && edtLocation.text.toString().isNotEmpty() && edtContent.text.toString().isNotEmpty()) {

                // Get the current user from Firebase authentication
                val currentUser = firebaseAuth.currentUser

                // Get the post reference in the Firebase database for the current post
                val postRef = FirebaseDatabase.getInstance().reference.child("posts")
                    .child(currentUser?.uid!!).child(id!!)

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
                            storageRef = FirebaseStorage.getInstance().reference.child("posts")
                                .child(filename)

                            // Upload the image to Firebase Storage
                            storageRef.putFile(uri!!)
                                .addOnSuccessListener {
                                    // Image upload successful, get the download URL
                                    storageRef.downloadUrl.addOnSuccessListener { downloadUrl ->
                                        // Set the image URL in the Post object and update it in the database
                                        postRef.child("image").setValue(downloadUrl.toString())
                                        postRef.child("title").setValue(edtTitle.text.toString())
                                        postRef.child("location").setValue(edtLocation.text.toString())
                                        postRef.child("author").setValue(name)
                                        postRef.child("content").setValue(edtContent.text.toString())
                                        Toast.makeText(applicationContext, "Post updated", Toast.LENGTH_SHORT).show()
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
                            // No image selected, update the Post object in the database without an image URL
                            postRef.child("title").setValue(edtTitle.text.toString())
                            postRef.child("location").setValue(edtLocation.text.toString())
                            postRef.child("author").setValue(name)
                            postRef.child("content").setValue(edtContent.text.toString())
                            Toast.makeText(applicationContext, "Post updated", Toast.LENGTH_SHORT).show()
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
                Toast.makeText(this, "Empty fields are not allowed !!", Toast.LENGTH_SHORT).show()
            }

        }

        // Set up the "Back" button to finish the activity when clicked
        val updatePostBack = findViewById<ImageView>(R.id.update_post_back)
        updatePostBack.setOnClickListener {
            finish()
        }
    }
}