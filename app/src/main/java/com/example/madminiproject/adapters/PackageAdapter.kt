package com.example.madminiproject.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.madminiproject.R
import com.example.madminiproject.models.PackageModel
import com.google.android.material.imageview.ShapeableImageView
import com.squareup.picasso.Picasso



class PackageAdapter(private var packageList : ArrayList<PackageModel>, private val onItemClick: (PackageModel) -> Unit) : RecyclerView.Adapter<PackageAdapter.PackageViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PackageViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.package_card, parent, false)

        return PackageAdapter.PackageViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return packageList.size
    }

    override fun onBindViewHolder(holder: PackageViewHolder, position: Int) {
        val currentItem = packageList[position]
        Picasso.get().load(currentItem.packImage).into(holder.packageImage)
        holder.lodgeName.text = currentItem.hotelName
        holder.lodgeLocation.text = currentItem.hotelLocation
        holder.lodgePrice.text = "LKR"+currentItem.hotelPrice.toString()

        // OnItemClickListener for a particular post
        holder.itemView.setOnClickListener {
            onItemClick(currentItem)
        }
    }

    class PackageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val packageImage: ShapeableImageView = itemView.findViewById(R.id.package_image)
        val lodgeName: TextView = itemView.findViewById(R.id.package_item_lodge_name)
        val lodgeLocation: TextView = itemView.findViewById(R.id.package_item_lodge_location)
        val lodgePrice: TextView = itemView.findViewById(R.id.package_item_lodge_price)
    }

    // Update the data in the adapter and notify about the data change
    fun updateData(newPackageList: ArrayList<PackageModel>) {
        packageList = newPackageList
        notifyDataSetChanged()
    }

}