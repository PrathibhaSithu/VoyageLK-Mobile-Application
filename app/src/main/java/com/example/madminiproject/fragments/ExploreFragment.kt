package com.example.madminiproject.fragments

import android.content.Intent
import android.graphics.PorterDuff
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.madminiproject.*
import com.example.madminiproject.R
import com.example.madminiproject.adapters.ExploreAdapter
import com.example.madminiproject.models.Post
import com.google.firebase.database.*
import java.util.*
import kotlin.collections.ArrayList

class ExploreFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var exploreAdapter: ExploreAdapter
    private lateinit var database: DatabaseReference
    private val TAG = "ExploreFragment"

    private lateinit var customActionBar: LinearLayout
    private lateinit var searchIcon: ImageView
    private lateinit var searchView: SearchView
    private var postList: ArrayList<Post> = ArrayList()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_explore, container, false)

        // Get a reference to RecyclerView and set its layout manager
        recyclerView = view.findViewById(R.id.exploreRecyclerView)
        recyclerView.layoutManager = GridLayoutManager(context, 2)

        // Add item decoration
        val spacingInPixels = resources.getDimensionPixelSize(R.dimen.spacing)
        recyclerView.addItemDecoration(ExploreItemSpacingDecoration(spacingInPixels))

        // Initialize ExploreAdapter and set it as RecyclerView's adapter
        exploreAdapter = ExploreAdapter(ArrayList()) { post -> onItemClick(post) }
        recyclerView.adapter = exploreAdapter


        // Find the action bar view
        customActionBar = view.findViewById(R.id.linearLayout)

        // Create a SearchView programmatically
        searchView = SearchView(requireActivity())
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                // Hide the search view and show other elements in the action bar
                customActionBar.isVisible = true
                searchView.clearFocus()

                // Filter the data based on the search query
                val filteredList = postList.filter { post ->
                    post.title.lowercase(Locale.ROOT).contains(query!!.lowercase(Locale.ROOT)) ||
                            post.location.lowercase(Locale.ROOT).contains(query.lowercase(Locale.ROOT)) ||
                            post.author.lowercase(Locale.ROOT).contains(query.lowercase(Locale.ROOT)) ||
                            post.content.lowercase(Locale.ROOT).contains(query.lowercase(Locale.ROOT))
                }

                // Cast filteredList to ArrayList
                val filteredArrayList = ArrayList(filteredList)

                // Update ExploreAdapter with filtered data
                exploreAdapter.updateData(filteredArrayList)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // Filter the data based on the search query
                val filteredList = postList.filter { post ->
                    post.title.lowercase(Locale.getDefault()).contains(newText!!.lowercase(Locale.ROOT)) ||
                            post.location.lowercase(Locale.ROOT).contains(newText.lowercase(Locale.ROOT)) ||
                            post.author.lowercase(Locale.ROOT).contains(newText.lowercase(Locale.ROOT)) ||
                            post.content.lowercase(Locale.ROOT).contains(newText.lowercase(Locale.ROOT))
                }

                // Cast filteredList to ArrayList
                val filteredArrayList = ArrayList(filteredList)

                // Update ExploreAdapter with filtered data
                exploreAdapter.updateData(filteredArrayList)
                return true
            }
        })

        // Get the search icon ImageView
        val searchIcon = searchView.findViewById<ImageView>(androidx.appcompat.R.id.search_button)
        // Set the color of the search icon
        searchIcon.setColorFilter(ContextCompat.getColor(requireContext(), R.color.background), PorterDuff.Mode.SRC_IN)

        // Set padding for the searchView close button
        searchView.findViewById<ImageView>(androidx.appcompat.R.id.search_close_btn)?.apply {
            setPadding(0, 0, 120, 0)
        }

        //Set the background of the searchView
        searchView.setOnSearchClickListener {
            val searchPlate = searchView.findViewById(androidx.appcompat.R.id.search_plate) as View
            searchPlate.setBackgroundResource(R.color.background)
        }

        // Add the SearchView to the custom action bar
        customActionBar.addView(searchView)


        init()
        getTaskFromFirebase()

        return view
    }

    // Function to initialize Firebase Database reference
    private fun init() {
        database = FirebaseDatabase.getInstance().reference.child("posts")
    }

    // Function to fetch data from Firebase
    private fun getTaskFromFirebase() {
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                postList.clear()
                for (userSnapshot in snapshot.children) {
                    for (postSnapshot in userSnapshot.children) {
                        val post = postSnapshot.getValue(Post::class.java)
                        post?.let { postList.add(it) }
                    }
                }

                // Update ExploreAdapter with fetched data
                exploreAdapter.updateData(postList)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e(TAG, "onCancelled", error.toException())
            }
        })
    }

    // Function to handle item click
    private fun onItemClick(post: Post) {
        // Update number of views in Firebase Database and start ExploreViewPostActivity
        database.child(post.userId).child(post.postId).child("views").setValue(post.views + 1)
        val intent = Intent(activity, ExploreViewPostActivity::class.java)
        intent.putExtra("image", post.image)
        intent.putExtra("title", post.title)
        intent.putExtra("location", post.location)
        intent.putExtra("author", post.author)
        intent.putExtra("views", post.views)
        intent.putExtra("content", post.content)
        startActivity(intent)
    }
}