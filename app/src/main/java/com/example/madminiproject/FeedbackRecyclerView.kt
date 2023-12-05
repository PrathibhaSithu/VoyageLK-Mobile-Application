package com.example.madminiproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.madminiproject.adapters.FeedbackAdapter
import com.example.madminiproject.models.Feedback
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.*

class FeedbackRecyclerView : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var feedbackAdapter: FeedbackAdapter
    private lateinit var database: DatabaseReference
    private val TAG = "FeedbackRecycler"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feedback_recycler_view)

        val giveFeedbackbtn = findViewById<FloatingActionButton>(R.id.floatingFeedbackBtn)

        giveFeedbackbtn.setOnClickListener{
            val intent = Intent(this, create_feedback_form::class.java)
            startActivity(intent)
        }

        val topBackButton = findViewById<ImageView>(R.id.backBtnFeedbackList)

        //back navigation button function
        topBackButton.setOnClickListener{
            finish()
        }

        // Get a reference to RecyclerView and set its layout manager
        recyclerView = findViewById(R.id.feedback_cards_recycler)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Initialize the adapter
        feedbackAdapter = FeedbackAdapter(ArrayList())

        // Attach the adapter to the RecyclerView
        recyclerView.adapter = feedbackAdapter

        init()
        getTaskFromFirebase()
    }

    // Function to initialize Firebase Database reference
    private fun init() {
        database = FirebaseDatabase.getInstance().reference.child("feedbacks")
    }

    private fun getTaskFromFirebase() {
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val feedbackList = ArrayList<Feedback>()
                for (userSnapshot in snapshot.children) {
                    for (feedbackSnapshot in userSnapshot.children) {
                        val feedback = feedbackSnapshot.getValue(Feedback::class.java)
                        feedback?.let { feedbackList.add(it) }
                    }
                }

                // Update the adapter with the fetched data
                feedbackAdapter.updateData(feedbackList)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e(TAG, "onCancelled", error.toException())
            }
        })
    }
}