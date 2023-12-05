package com.example.madminiproject.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.madminiproject.R
import com.example.madminiproject.models.Feedback
import com.google.android.material.imageview.ShapeableImageView
import com.squareup.picasso.Picasso

class FeedbackAdapter (private var feedbackList : ArrayList<Feedback>): RecyclerView.Adapter<FeedbackAdapter.FeedbackViewHolder>(){
    // Inflate layout and create a new ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedbackViewHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.activity_feedback_card, parent, false)
        return FeedbackViewHolder(itemView)
    }

    // Bind data to the views for a particular post
    override fun onBindViewHolder(holder: FeedbackViewHolder, position: Int) {
        val currentItem = feedbackList[position]
        Picasso.get().load(currentItem.image).into(holder.imageView)
        holder.author.text = currentItem.author
        holder.email.text = currentItem.email
        holder.rating.text = currentItem.rating.toString()
        holder.feedback.text = currentItem.feedback

    }

    // Return the size of exploreList ArrayList
    override fun getItemCount(): Int {
        return feedbackList.size
    }

    // Inner class to hold the views which are used to display a single post
    class FeedbackViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val imageView : ShapeableImageView = itemView.findViewById(R.id.feedback_author_profile_photo)
        val author : TextView = itemView.findViewById(R.id.feedback_author_name)
        val email : TextView = itemView.findViewById(R.id.feedback_author_email)
        val rating : TextView = itemView.findViewById(R.id.feedback_author_rating)
        val feedback : TextView = itemView.findViewById(R.id.feedback_author_feedback)
    }

    // Update the data in the adapter and notify about the data change
    fun updateData(newFeedbackList: ArrayList<Feedback>) {
        feedbackList = newFeedbackList
        notifyDataSetChanged()
    }
}