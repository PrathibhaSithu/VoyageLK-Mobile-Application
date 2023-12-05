package com.example.madminiproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso

class ExploreViewPostActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Set the layout file for this activity
        setContentView(R.layout.activity_explore_view_post)

        // Get the data passed in from the previous activity
        val image = intent.getStringExtra("image")
        val title = intent.getStringExtra("title")
        val location = intent.getStringExtra("location")
        val author = intent.getStringExtra("author")
        val views = intent.getIntExtra("views", 0)
        val content = intent.getStringExtra("content")

        // Find the relevant views in the layout file
        val imageView: ImageView = findViewById<ImageView>(R.id.exploreImage)
        val headingView = findViewById<TextView>(R.id.exploreTitle)
        val locationView = findViewById<TextView>(R.id.exploreLocation)
        val authorView = findViewById<TextView>(R.id.exploreAuthor)
        val viewsCountView = findViewById<TextView>(R.id.exploreViews)
        val contentView = findViewById<TextView>(R.id.exploreDes)

        // Use the Picasso library to load the image from the URL into the ImageView
        Picasso.get()
            .load(image)
            .into(imageView)

        // Set the text for the various TextViews
        headingView.text = title
        locationView.text = location
        authorView.text = "By $author"
//        val viewsCount = String.format("%.1fK", views.toFloat() / 1000)
//        viewsCountView.text = viewsCount
        viewsCountView.text = "$views"
        contentView.text = content

        // Set up the "Back" button to finish the activity when clicked
        val exploreBack = findViewById<ImageView>(R.id.explore_back)
        exploreBack.setOnClickListener {
            finish()
        }
    }
}