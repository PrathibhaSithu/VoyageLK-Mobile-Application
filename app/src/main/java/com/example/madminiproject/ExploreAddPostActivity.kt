package com.example.madminiproject

import android.graphics.Paint
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.madminiproject.models.Post
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference



class ExploreAddPostActivity : AppCompatActivity() {

    // Initialize variables for Firebase authentication and storage reference
    private lateinit var firebaseAuth : FirebaseAuth
    private lateinit var storageRef : StorageReference

    // Initialize a tag for logging purposes
    private val TAG = "ExploreAddPostActivity"

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
        setContentView(R.layout.activity_explore_add_post)

        // Initialize Firebase authentication
        firebaseAuth = FirebaseAuth.getInstance()

        // Set a click listener for the image picker button
        val imagePickerButton = findViewById<ImageView>(R.id.explorePostImg)
        imagePickerButton.setOnClickListener {
            pickImage.launch("image/*")
        }

        // Remove the underline from the text view
        val myTextView = findViewById<TextView>(R.id.editTextExplorePostContent)
        myTextView.paintFlags = myTextView.paintFlags and Paint.UNDERLINE_TEXT_FLAG.inv()


        val submitButton = findViewById<TextView>(R.id.addPostBtn)

        // Set a click listener for the submit button
        submitButton.setOnClickListener {

            // Get the text from the input fields
            val title = findViewById<EditText>(R.id.editTextExplorePostTitle).text.toString()
            val location = findViewById<EditText>(R.id.editTextExplorePostLocation).text.toString()
            val content = findViewById<EditText>(R.id.editTextExplorePostContent).text.toString()

            // Check if the required fields are not empty and an image has been selected
            if (title.isNotEmpty() && location.isNotEmpty() && content.isNotEmpty() && uri != null) {

                // Get the current user from Firebase authentication
                val currentUser = firebaseAuth.currentUser

                // Create a new post reference in the Firebase database under the current user ID
                val postRef = FirebaseDatabase.getInstance().reference.child("posts")
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
                            storageRef = FirebaseStorage.getInstance().reference.child("posts")
                                .child(filename)

                            // Upload the image to Firebase Storage
                            storageRef.putFile(uri!!)
                                .addOnSuccessListener {
                                    // Image upload successful, get the download URL
                                    storageRef.downloadUrl.addOnSuccessListener { downloadUrl ->
                                        // Set the image URL in the Post object and save it to the database
                                        val post = Post(
                                            postRef.key ?: "",
                                            downloadUrl.toString(),
                                            title,
                                            location,
                                            name ?: "",
                                            0,
                                            content,
                                            currentUser.uid
                                        )
                                        postRef.setValue(post)
                                        Toast.makeText(applicationContext, "Post added", Toast.LENGTH_SHORT).show()
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
                            val post = Post(postRef.key ?: "", "", title, location, name ?: "", 0, content, currentUser.uid)
                            postRef.setValue(post)
                            Toast.makeText(applicationContext, "Post added", Toast.LENGTH_SHORT).show()
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
        val addPostBack = findViewById<ImageView>(R.id.add_post_back)
        addPostBack.setOnClickListener {
            finish()
        }
    }
}
