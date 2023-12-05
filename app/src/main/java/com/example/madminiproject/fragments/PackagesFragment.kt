package com.example.madminiproject.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.madminiproject.ExploreItemSpacingDecoration
import com.example.madminiproject.ExploreViewPostActivity
import com.example.madminiproject.PackageDescription
import com.example.madminiproject.R
import com.example.madminiproject.adapters.PackageAdapter
import com.example.madminiproject.models.PackageModel
import com.example.madminiproject.models.Post
import com.google.firebase.database.*


class PackagesFragment : Fragment() {

    private lateinit var packageRecyclerView: RecyclerView
    private lateinit var packageAdapter:PackageAdapter
    private lateinit var database: DatabaseReference
    private val PACKAGETAG = "PackageFragment"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_packages, container, false)
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_packages, container, false)

        // Get a reference to RecyclerView and set its layout manager
        packageRecyclerView = view.findViewById(R.id.package_cards_recycler)
        packageRecyclerView.layoutManager = LinearLayoutManager(context)

        // Add item decoration
        val spacingInPixels = resources.getDimensionPixelSize(R.dimen.spacing)
        packageRecyclerView.addItemDecoration(ExploreItemSpacingDecoration(spacingInPixels))

        // Initialize ExploreAdapter and set it as RecyclerView's adapter
        packageAdapter = PackageAdapter(ArrayList()) { pack -> onItemClick(pack) }
        packageRecyclerView.adapter = packageAdapter

        init()
        getTaskFromFirebase()

        return view
    }

    // Function to initialize Firebase Database reference
    private fun init() {
        database = FirebaseDatabase.getInstance().reference.child("packages")
    }

    // Function to fetch data from Firebase
    private fun getTaskFromFirebase() {
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val packageList = ArrayList<PackageModel>()
                for (userSnapshot in snapshot.children) {
                    for (packageSnapshot in userSnapshot.children) {
                        val pack = packageSnapshot.getValue(PackageModel::class.java)
                        pack?.let { packageList.add(it) }
                    }
                }

                // Update ExploreAdapter with fetched data
                packageAdapter.updateData(packageList)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e(PACKAGETAG, "onCancelled", error.toException())
            }
        })
    }

    // Function to handle item click
    private fun onItemClick(pack: PackageModel) {
        // Update number of views in Firebase Database and start ExploreViewPostActivity
        database.child(pack.userId).child(pack.packID)
        val intent = Intent(activity, PackageDescription::class.java)
        intent.putExtra("image", pack.packImage)
        intent.putExtra("hotelName", pack.hotelName)
        intent.putExtra("hotelLocation", pack.hotelLocation)
        intent.putExtra("author", pack.packAuthor)
        intent.putExtra("price", pack.hotelPrice)
        intent.putExtra("packDescription", pack.packDesc)
        intent.putExtra("hotelContact", pack.contactNo)
        startActivity(intent)
    }
}