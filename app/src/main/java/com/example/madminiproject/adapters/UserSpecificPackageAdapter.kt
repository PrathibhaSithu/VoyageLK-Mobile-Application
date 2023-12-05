package com.example.madminiproject.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.madminiproject.R
import com.example.madminiproject.UpdatePackageDetails
import com.example.madminiproject.models.PackageModel
import com.google.android.material.imageview.ShapeableImageView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso

class UserSpecificPackageAdapter(private val userPackages : ArrayList<PackageModel>, private val onItemClick: (PackageModel) -> Unit) : RecyclerView.Adapter<UserSpecificPackageAdapter.UserPackagesHolder>() {

    // Declare Firebase variables for authentication and database access
    private lateinit var databaseRef: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private lateinit var authId: String

    // Create a ViewHolder for each item in the RecyclerView
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserPackagesHolder {

        // Inflate the layout for each item in the RecyclerView
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.activity_package_modification_card, parent, false)
        return UserPackagesHolder(itemView)
    }

    // Bind the data to each item in the RecyclerView
    override fun onBindViewHolder(holder: UserPackagesHolder, position: Int) {

        // Get the post at the current position in the list
        val currentItem = userPackages[position]

        // Load the post image using Picasso library
//        Picasso.get().load(currentItem.packImage).into(holder.modifyingImage)
        if (!currentItem.packImage.isNullOrEmpty()) {
            // Load the post image using Picasso library
            Picasso.get().load(currentItem.packImage).into(holder.modifyingImage)
        }
//        if (!currentItem.image.isNullOrEmpty()) {
//            Picasso.get().load(currentItem.image).into(holder.imageView)
//        }
        // Set the post title and location
        holder.lodgeName.text = currentItem.hotelName
        holder.location.text = currentItem.hotelLocation
        holder.price.text = currentItem.hotelPrice.toString()

        // Get the current user's ID for database reference
        auth = FirebaseAuth.getInstance()
        authId = auth.currentUser!!.uid

        // Get the database reference to the current post
        databaseRef = FirebaseDatabase.getInstance().getReference("packages").child(authId).child(currentItem.packID)

        //handling the update request when button is clicked
        holder.updateBtn.setOnClickListener {
            val intent = Intent(holder.itemView.context, UpdatePackageDetails::class.java)
            intent.putExtra("packID", currentItem.packID)
            intent.putExtra("hotelName", currentItem.hotelName)
            intent.putExtra("hotelLocation", currentItem.hotelLocation)
            intent.putExtra("hotelPrice", currentItem.hotelPrice).toString()
            intent.putExtra("packDescription", currentItem.packDesc)
            intent.putExtra("image", currentItem.packImage)
            intent.putExtra("contactNumber", currentItem.contactNo)
            intent.putExtra("packAuthor", currentItem.packAuthor)
            holder.itemView.context.startActivity(intent)
        }

        //handling the delete request when button is clicked
        holder.deleteBtn.setOnClickListener {
            databaseRef.removeValue()
            Toast.makeText(holder.itemView.context, "Package deleted", Toast.LENGTH_SHORT).show()
        }

        // OnItemClickListener for a particular post
        holder.itemView.setOnClickListener {
            onItemClick(currentItem)
        }
    }

    // Return the size of user packages ArrayList
    override fun getItemCount(): Int {
        return userPackages.size
    }

    // Inner class to hold the views which are used to display a single post
    class UserPackagesHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val updateBtn : Button = itemView.findViewById(R.id.mod_card_btn_update)
        val deleteBtn : Button = itemView.findViewById(R.id.mod_card_btn_delete)
        val modifyingImage : ShapeableImageView = itemView.findViewById(R.id.package_image_mod_card)
        val location : TextView = itemView.findViewById(R.id.package_item_lodge_location_mod_card)
        val price : TextView = itemView.findViewById(R.id.package_item_lodge_price_mod_card)
        val lodgeName : TextView = itemView.findViewById(R.id.package_item_lodge_name_mod_card)
    }



}