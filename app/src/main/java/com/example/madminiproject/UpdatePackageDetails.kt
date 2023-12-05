package com.example.madminiproject

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso

class UpdatePackageDetails : AppCompatActivity() {

    // Initialize variables for Firebase authentication and storage reference
    private lateinit var firebaseAuth : FirebaseAuth
    private lateinit var storageRef : StorageReference

    // Initialize a tag for logging purposes
    private val PACKUPDATETAG = "UpdatePackageActivity"

    // Initialize a URI variable for the selected image
    private var uri: Uri? = null

    // Register an activity result launcher for getting the selected image URI
    private val pickImage = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        val updatingImgView = findViewById<ImageView>(R.id.package_pic_upload)
        updatingImgView.setImageURI(uri)
        this.uri = uri
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_package_details)

        // Set a click listener for the image picker button
        val imagePickerButton = findViewById<ImageView>(R.id.package_pic_upload)
        imagePickerButton.setOnClickListener {
            pickImage.launch("image/*")
        }

        // Get the package information from the previous activity
        val packageID = intent.getStringExtra("packID")
        val packageImage = intent.getStringExtra("image")
        val hotelName = intent.getStringExtra("hotelName")
        val hotelLocation = intent.getStringExtra("hotelLocation")
        val hotelPrice = intent.getDoubleExtra("hotelPrice", 0.0)
        val packDescription = intent.getStringExtra("packDescription")
        val packPhoneNo = intent.getStringExtra("contactNumber")
        val packAuthor = intent.getStringArrayExtra("packAuthor")

        val edtHotelName = findViewById<EditText>(R.id.update_lodge_name)
        val edtHotelLocation = findViewById<EditText>(R.id.update_lodge_location)
        val edtHotelDescription = findViewById<MultiAutoCompleteTextView>(R.id.update_package_description)
        val edtPackPrice = findViewById<EditText>(R.id.update_package_price)
        val edtHotelContact = findViewById<EditText>(R.id.update_package_contact_number)

        // assign relevant values to the edit text views
        Picasso.get()
            .load(packageImage)
            .into(imagePickerButton)
        edtHotelName.setText(hotelName)
        edtHotelLocation.setText(hotelLocation)
        edtPackPrice.setText(hotelPrice.toString())
        edtHotelDescription.setText(packDescription)
        edtHotelContact.setText(packPhoneNo)

        // Initialize Firebase authentication
        firebaseAuth = FirebaseAuth.getInstance()

        val topBarBackButton = findViewById<ImageView>(R.id.backBtn)

        //back navigation button function
        topBarBackButton.setOnClickListener{
            finish()
        }

        val updatePackageButton = findViewById<TextView>(R.id.update_package_btn)

        updatePackageButton.setOnClickListener{


            if (edtHotelName.text.toString().isNotEmpty() && edtHotelLocation.text.toString().isNotEmpty() && edtPackPrice.text.toString().isNotEmpty() && edtHotelDescription.text.toString().isNotEmpty() && edtHotelContact.text.isNotEmpty()){

                // Get the current user from Firebase authentication
                val currentUser = firebaseAuth.currentUser

                // Get the package reference in the Firebase database for the current package
                val packageRef = FirebaseDatabase.getInstance().reference.child("packages")
                    .child(currentUser?.uid!!).child(packageID!!)

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

                            // Upload the image to Firebase Storage
                            storageRef.putFile(uri!!)
                                .addOnSuccessListener {
                                    // Image upload successful, get the download URL
                                    storageRef.downloadUrl.addOnSuccessListener { downloadUrl ->
                                        // Set the image URL in the Post object and update it in the database
                                        packageRef.child("packImage").setValue(downloadUrl.toString())
                                        packageRef.child("hotelName").setValue(edtHotelName.text.toString())
                                        packageRef.child("hotelLocation").setValue(edtHotelLocation.text.toString())
                                        packageRef.child("hotelPrice").setValue(edtPackPrice.text.toString())
                                        packageRef.child("packAuthor").setValue(name)
                                        packageRef.child("packDesc").setValue(edtHotelDescription.text.toString())
                                        packageRef.child("contactNo").setValue(edtHotelContact.text.toString())
                                        Toast.makeText(applicationContext, "Post updated", Toast.LENGTH_SHORT).show()
                                        finish()
                                    }
                                }
                                .addOnFailureListener { e ->
                                    // Image upload failed, handle the error and show a toast message
                                    Toast.makeText(applicationContext, "Image upload failed", Toast.LENGTH_SHORT).show()
                                    Log.e(PACKUPDATETAG, "Image upload failed", e)
                                }
                                .addOnProgressListener { taskSnapshot ->
                                    // Image upload is in progress, show a toast message with the progress
                                    val progress = (100.0 * taskSnapshot.bytesTransferred / taskSnapshot.totalByteCount).toInt()
                                    Toast.makeText(applicationContext, "Image uploading progress: $progress%", Toast.LENGTH_SHORT).show()
                                }
                        } else {
                            // No image selected, update the Package object in the database without an image URL
                            packageRef.child("hotelName").setValue(edtHotelName.text.toString())
                            packageRef.child("hotelLocation").setValue(edtHotelLocation.text.toString())
                            packageRef.child("author").setValue(name)
                            packageRef.child("hotelPrice").setValue(edtPackPrice.text.toString().toDouble())
                            packageRef.child("packDesc").setValue(edtHotelDescription.text.toString())
                            packageRef.child("contactNo").setValue(edtHotelContact.text.toString())
                            Toast.makeText(applicationContext, "Package Updated", Toast.LENGTH_SHORT).show()
                            finish()
                        }

                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        // Handle errors here if the read operation is cancelled or fails
                        Log.e(PACKUPDATETAG, "onCancelled", databaseError.toException())
                    }
                })
            }

            else {
                // If any of the required fields are empty, show a toast message to the user
                Toast.makeText(this, "Empty fields are not allowed !!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}