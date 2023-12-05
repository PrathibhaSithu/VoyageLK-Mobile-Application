package com.example.madminiproject

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso

class PackageDescription : AppCompatActivity() {
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_package_description)

        val packImage: ImageView = findViewById<ImageView>(R.id.pack_desc_image)
        val lodgeName = findViewById<TextView>(R.id.pack_desc_lodgeName)
        val lodgeLocation = findViewById<TextView>(R.id.pack_desc_location)
        val lodgePrice = findViewById<TextView>(R.id.pack_desc_price)
        val description = findViewById<TextView>(R.id.pack_desc_description)
        val author = findViewById<TextView>(R.id.pack_lodge_author)
        val phoneNumber = findViewById<TextView>(R.id.pack_lodge_phone)

        val bundle : Bundle?=intent.extras

        val lname = intent.getStringExtra("hotelName")
        val imgID = intent.getStringExtra("image")
        val location = intent.getStringExtra("hotelLocation")
        val price = intent.getDoubleExtra("price", 0.0)
        val ldescription = intent.getStringExtra("packDescription")
        val writer = intent.getStringExtra("author")
        val contact = intent.getStringExtra("hotelContact")

        val topBarBackButton = findViewById<ImageView>(R.id.backBtn_to_packages)

        //back navigation button function
        topBarBackButton.setOnClickListener{
            finish()
        }

        // Use the Picasso library to load the image from the URL into the ImageView
        Picasso.get()
            .load(imgID)
            .into(packImage)

        lodgeName.text = lname
        lodgeLocation.text = location
        lodgePrice.text = "LKR $price per night"
        description.text = ldescription
        author.text = "Published by $writer"
        phoneNumber.text = contact
    }
}