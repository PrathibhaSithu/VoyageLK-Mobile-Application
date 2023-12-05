package com.example.madminiproject.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.madminiproject.R
import com.example.madminiproject.models.Post
import com.google.android.material.imageview.ShapeableImageView
import com.squareup.picasso.Picasso

class ExploreAdapter(private var exploreList : ArrayList<Post>, private val onItemClick: (Post) -> Unit) : RecyclerView.Adapter<ExploreAdapter.ExploreViewHolder>(){

    // Inflate layout and create a new ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExploreViewHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.explore_list_item, parent, false)

        return ExploreViewHolder(itemView)
    }

    // Bind data to the views for a particular post
    override fun onBindViewHolder(holder: ExploreViewHolder, position: Int) {
        val currentItem = exploreList[position]
        Picasso.get().load(currentItem.image).into(holder.imageView)
        holder.title.text = currentItem.title
        holder.location.text = currentItem.location

        // OnItemClickListener for a particular post
        holder.itemView.setOnClickListener {
            onItemClick(currentItem)
        }
    }

    // Return the size of exploreList ArrayList
    override fun getItemCount(): Int {

        return exploreList.size
    }

    // Inner class to hold the views which are used to display a single post
    class ExploreViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {

        val imageView : ShapeableImageView = itemView.findViewById(R.id.explore_item_image)
        val title : TextView = itemView.findViewById(R.id.explore_item_title)
        val location : TextView = itemView.findViewById(R.id.explore_item_location)
    }

    // Update the data in the adapter and notify about the data change
    fun updateData(newPostList: ArrayList<Post>) {
        exploreList = newPostList
        notifyDataSetChanged()
    }

}