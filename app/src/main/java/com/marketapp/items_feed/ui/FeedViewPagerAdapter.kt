package com.marketapp.items_feed.ui

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.marketapp.R

class FeedViewPagerAdapter(private val list: List<String>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun getItemCount(): Int = list.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_pager, parent, false)
        return ViewPagerVH(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ViewPagerVH -> holder.bind(list[position])
        }
    }

}

class ViewPagerVH(itemview: View) : RecyclerView.ViewHolder(itemview) {


    fun bind(str: String) {
        Log.d("TAG", str)
    }

}