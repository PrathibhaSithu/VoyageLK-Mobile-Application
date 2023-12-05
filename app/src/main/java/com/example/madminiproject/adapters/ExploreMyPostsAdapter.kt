package com.example.madminiproject.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.madminiproject.ExploreUpdatePostActivity
import com.example.madminiproject.R
import com.example.madminiproject.models.Post
import com.google.android.material.imageview.ShapeableImageView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso

class ExploreMyPostsAdapter(private val myPostsList : ArrayList<Post>, private val onItemClick: (Post) -> Unit) : RecyclerView.Adapter<ExploreMyPostsAdapter.MyPostsHolder>(){

    // Declare Firebase variables for authentication and database access
    private lateinit var databaseRef: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private lateinit var authId: String

    // Create a ViewHolder for each item in the RecyclerView
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyPostsHolder {

        // Inflate the layout for each item in the RecyclerView
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.my_post_list_item, parent, false)

        return MyPostsHolder(itemView)
    }

    // Bind the data to each item in the RecyclerView
    override fun onBindViewHolder(holder: MyPostsHolder, position: Int) {

        // Get the post at the current position in the list
        val currentItem = myPostsList[position]

        // Load the post image using Picasso library
        Picasso.get().load(currentItem.image).into(holder.imageView)
//        if (!currentItem.image.isNullOrEmpty()) {
//            Picasso.get().load(currentItem.image).into(holder.imageView)
//        }
        // Set the post title and location
        holder.title.text = currentItem.title
        holder.location.text = currentItem.location

        // Get the current user's ID for database reference
        auth = FirebaseAuth.getInstance()
        authId = auth.currentUser!!.uid

        // Get the database reference to the current post
        databaseRef = FirebaseDatabase.getInstance().getReference("posts").child(authId).child(currentItem.postId)

        // Inflate the menu on postOptions button click
        holder.postOptions.setOnClickListener { view ->
            val popupMenu = PopupMenu(view.context, view)
            popupMenu.inflate(R.menu.recycler_view_post_menu)
            popupMenu.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.editPost -> {
                        // If the user clicks the "Edit Post" option, start the ExploreUpdatePostActivity and pass the post properties as extras
                        val intent = Intent(view.context, ExploreUpdatePostActivity::class.java)
                        intent.putExtra("postId", currentItem.postId)
                        intent.putExtra("image", currentItem.image)
                        intent.putExtra("title", currentItem.title)
                        intent.putExtra("location", currentItem.location)
                        intent.putExtra("content", currentItem.content)
                        view.context.startActivity(intent)
                        true
                    }
                    R.id.deletePost -> {
                        // If the user clicks the "Delete Post" option, remove the post from the database and display a toast message
                        databaseRef.removeValue()
                        Toast.makeText(view.context, "Post deleted", Toast.LENGTH_SHORT).show()
                        true
                    }
                    else -> false
                }
            }

            popupMenu.show()
        }

        // OnItemClickListener for a particular post
        holder.itemView.setOnClickListener {
            onItemClick(currentItem)
        }
    }

    // Return the size of myPostsList ArrayList
    override fun getItemCount(): Int {
        return myPostsList.size
    }

    // Inner class to hold the views which are used to display a single post
    class MyPostsHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {

        val imageView : ShapeableImageView = itemView.findViewById(R.id.post_item_image)
        val title : TextView = itemView.findViewById(R.id.post_item_title)
        val location : TextView = itemView.findViewById(R.id.post_item_location)
        val postOptions: ImageView = itemView.findViewById(R.id.postOptions)
    }

}