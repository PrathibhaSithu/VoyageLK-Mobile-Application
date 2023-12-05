package com.example.madminiproject.PackageRecylcer

import android.annotation.SuppressLint
import android.provider.ContactsContract.Data
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.view.menu.ActionMenuItemView
import androidx.recyclerview.widget.RecyclerView
import com.example.madminiproject.R
import com.google.android.material.imageview.ShapeableImageView

class DataAdapter(private val packageList:ArrayList<PackageData>) : RecyclerView.Adapter<DataAdapter.PackageViewHolder>() {

    private lateinit var mListener:onItemClickListenter

    interface onItemClickListenter{
        fun onPackageClick(position:Int)
    }

    fun setOnItemClickListener(listener:onItemClickListenter){
        mListener = listener
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PackageViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.package_card,parent,false)
        return PackageViewHolder(itemView,mListener)
    }

    override fun getItemCount(): Int {
        return packageList.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: PackageViewHolder, position: Int) {
        val currentItem = packageList[position]
        holder.packageImage.setImageResource(currentItem.packageImage)
        holder.lodgeName.text = currentItem.lodgeName
        holder.lodgeLocation.text = currentItem.lodgeLocation
        holder.lodgePrice.text = "LKR"+currentItem.lodgePrice.toString()
    }

    class PackageViewHolder(itemView: View , listener: onItemClickListenter) : RecyclerView.ViewHolder(itemView){
        val packageImage: ShapeableImageView = itemView.findViewById(R.id.package_image)
        val lodgeName: TextView = itemView.findViewById(R.id.package_item_lodge_name)
        val lodgeLocation: TextView = itemView.findViewById(R.id.package_item_lodge_location)
        val lodgePrice: TextView = itemView.findViewById(R.id.package_item_lodge_price)

        init{
            itemView.setOnClickListener {
//                listener.onPackageClick(bindingAdapterPosition)
            }
        }

    }

}