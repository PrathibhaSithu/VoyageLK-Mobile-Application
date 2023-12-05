package com.example.madminiproject

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

// Class for setting item spacing decoration in RecyclerView
class ExploreItemSpacingDecoration(private val spacing: Int) : RecyclerView.ItemDecoration() {

    // Overriding the method for setting item spacing offsets
    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        outRect.top = spacing
        outRect.bottom = spacing
        outRect.left = spacing
        outRect.right = spacing
    }
}