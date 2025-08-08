package com.example.blackstone.home

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class HeaderPagerAdapter(
    private val views: List<View>
) : RecyclerView.Adapter<HeaderPagerAdapter.ViewHolder>() {

    inner class ViewHolder(val itemViewContainer: View) : RecyclerView.ViewHolder(itemViewContainer)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(views[viewType])
    }

    override fun getItemCount(): Int = views.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) { }

    override fun getItemViewType(position: Int): Int = position
}