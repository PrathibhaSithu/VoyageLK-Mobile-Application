package com.example.madminiproject

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.MultiAutoCompleteTextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.madminiproject.models.PackageModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class create_package_form : AppCompatActivity() {

    // setting variables for firebase authentication and image storage facility
    private lateinit var firebaseAuth : FirebaseAuth
    private lateinit var storageRef : StorageReference

    // Initialize a tag for logging purposes
    private val PACKTAG = "AddNewVacationPackage"

    // Initialize a URI variable for the selected image
    private var uri: Uri? = null

    // Register an activity result launcher for getting the selected package image URI
    private val selectPackageImage = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        // Do something with the selected image URI
        val packImageView = findViewById<ImageView>(R.id.package_pic_upload)
        packImageView.setImageURI(uri)
        this.uri = uri
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_package_form)

        // Initialize Firebase authentication
        firebaseAuth = FirebaseAuth.getInstance()

        val topBarBackButton = findViewById<ImageView>(R.id.addPkgbackBtn)

        //back navigation button function
        topBarBackButton.setOnClickListener{
            finish()
        }

        // Set a click listener for the image picker button
        val imgPickerBtn = findViewById<ImageView>(R.id.package_pic_upload)
        imgPickerBtn.setOnClickListener {
            selectPackageImage.launch("image/*")
        }

        // assigning the button to a variable
        val addPackageButton = findViewById<Button>(R.id.addPackageBtn)

        //eventListener for add pack button
        addPackageButton.setOnClickListener {

            //assigning the required text fields values to the variables
            val hotelName = findViewById<EditText>(R.id.add_package_form_lodgeName).text.toString()
            val hotelLocation = findViewById<EditText>(R.id.add_package_form_lodgeLocation).text.toString()
            val hotelPrice = findViewById<EditText>(R.id.add_package_form_lodgePrice).text.toString()
            val hotelContact = findViewById<EditText>(R.id.add_package_form_contact_number).text.toString()
            val packDescription = findViewById<MultiAutoCompleteTextView>(R.id.add_package_form_description).text.toString()

            if(hotelName.isNotEmpty() && hotelLocation.isNotEmpty() && hotelPrice.isNotEmpty() && packDescription.isNotEmpty() && hotelContact.isNotEmpty() && uri != null ){

                //assigning the current user using firebase auth
                val currentUser = firebaseAuth.currentUser

                // Create a new package reference in the Firebase database under the current user ID
                val packageRef = FirebaseDatabase.getInstance().reference.child("packages")
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
                            storageRef = FirebaseStorage.getInstance().reference.child("packages")
                                .child(filename)

                            // Upload the package image to Firebase Storage
                            storageRef.putFile(uri!!)
                                .addOnSuccessListener {
                                    // Image upload successful, get the download URL
                                    storageRef.downloadUrl.addOnSuccessListener { downloadUrl ->
                                        // Set the image URL in the Package object and save it to the database
                                        val pack = PackageModel(
                                            packageRef.key ?: "",
                                            downloadUrl.toString(),
                                            hotelName,
                                            hotelLocation,
                                            name ?: "",
                                            hotelPrice.toDouble(),
                                            packDescription,
                                            currentUser.uid,
                                            hotelContact
                                        )
                                        packageRef.setValue(pack)
                                        Toast.makeText(applicationContext, "Post added", Toast.LENGTH_SHORT).show()
                                        finish()
                                    }
                                }
                                .addOnFailureListener { e ->
                                    // handle if image uploading operation was a failure
                                    Toast.makeText(applicationContext, "Image upload failed", Toast.LENGTH_SHORT).show()
                                    Log.e(PACKTAG, "Image upload failed", e)
                                }
                                .addOnProgressListener { taskSnapshot ->
                                    // uploading image is in progress, show a toast message with the progress
                                    val progress = (100.0 * taskSnapshot.bytesTransferred / taskSnapshot.totalByteCount).toInt()
                                    Toast.makeText(applicationContext, "Image uploading progress: $progress%", Toast.LENGTH_SHORT).show()
                                }
                        } else {
                            // No image selected, save the Post object to the database without an image URL
                            val pack = PackageModel(packageRef.key ?: "", "", hotelName, hotelLocation, name ?: "", 0.0, packDescription, currentUser.uid, hotelContact)
                            packageRef.setValue(pack)
                            Toast.makeText(applicationContext, "Post added", Toast.LENGTH_SHORT).show()
                            finish()
                        }

                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        // Handle errors here if the read operation is cancelled or fails
                        Log.e(PACKTAG, "onCancelled", databaseError.toException())
                    }
                })


            }

        }

    }

}