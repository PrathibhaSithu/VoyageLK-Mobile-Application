package com.example.madminiproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.madminiproject.adapters.ExploreMyPostsAdapter
import com.example.madminiproject.models.Post
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class ExploreMyPostListActivity : AppCompatActivity() {

    // Declare variables for RecyclerView, adapter, Firebase database, authentication, authentication ID, and tag for debugging
    private lateinit var recyclerView: RecyclerView
    private lateinit var myPostsAdapter: ExploreMyPostsAdapter
    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private lateinit var authId: String
    private val TAG = "ExploreMyPostListActivity"

    // Define the function which is called when the activity is created
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_explore_my_post_list)

        // Initialize Firebase objects
        init()

        // Set an onClickListener for the "add_post" ImageView, which starts the ExploreAddPostActivity
        val addButton = findViewById<ImageView>(R.id.add_post)
        addButton.setOnClickListener {
            val intent = Intent(this, ExploreAddPostActivity::class.java)
            startActivity(intent)
        }

        // Call function to retrieve data from Firebase
        getTaskFromFirebase()

        // Set up the "Back" button to finish the activity when clicked
        val postBack = findViewById<ImageView>(R.id.my_posts_back)
        postBack.setOnClickListener {
            finish()
        }
    }

    // Define a function to initialize Firebase objects
    private fun init() {
        auth = FirebaseAuth.getInstance()
        authId = auth.currentUser!!.uid
        database = FirebaseDatabase.getInstance().reference.child("posts").child(authId)
    }

    // Define a function to retrieve data from Firebase and display it in the RecyclerView using an adapter
    private fun getTaskFromFirebase() {
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val postList = ArrayList<Post>()
                for (postSnapshot in snapshot.children) {
                    val post = postSnapshot.getValue(Post::class.java)
                    post?.let { postList.add(it) }
                }

                recyclerView = findViewById(R.id.myPostsRecyclerView)
                myPostsAdapter = ExploreMyPostsAdapter(postList) { post -> onItemClick(post) }
                recyclerView.adapter = myPostsAdapter
                recyclerView.layoutManager = LinearLayoutManager(this@ExploreMyPostListActivity)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e(TAG, "onCancelled", error.toException())
            }
        })
    }

    // Define a function to handle click events on a Post object and start the ExploreViewPostActivity with relevant data
    private fun onItemClick(post: Post) {
        val intent = Intent(this, ExploreViewPostActivity::class.java)
        intent.putExtra("image", post.image)
        intent.putExtra("title", post.title)
        intent.putExtra("location", post.location)
        intent.putExtra("author", post.author)
        intent.putExtra("views", post.views)
        intent.putExtra("content", post.content)
        startActivity(intent)
    }

}