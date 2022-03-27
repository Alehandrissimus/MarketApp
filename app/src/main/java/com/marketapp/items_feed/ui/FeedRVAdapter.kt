package com.marketapp.items_feed.ui

import android.graphics.drawable.TransitionDrawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory
import com.marketapp.R
import com.marketapp.databinding.FeedItemBinding
import com.marketapp.items_feed.data.models.FeedItemModel

class FeedRVAdapter(private val callback: (FeedItemModel) -> Unit) :
    ListAdapter<FeedItemModel, FeedRVViewHolder>(FeedDiffs()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedRVViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.feed_item, parent, false)

        return FeedRVViewHolder(view, callback)
    }

    override fun onBindViewHolder(holder: FeedRVViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

}

class FeedRVViewHolder(itemview: View, private val callback: (FeedItemModel) -> Unit) :
    RecyclerView.ViewHolder(itemview) {

    private val binding = FeedItemBinding.bind(itemView)

    fun bind(feedItemModel: FeedItemModel) {
        binding.apply {
            tvFeedName.text = feedItemModel.name
            Glide.with(ivFeed)
                .load(feedItemModel.icon)
                //.transition(withCrossFade(DrawableCrossFadeFactory.Builder().setCrossFadeEnabled(true).build()))
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(ivFeed)
            tvFeedValue.text = feedItemModel.avg24hPrice.toString()
        }
        binding.containerFeedItem.setOnClickListener {
            callback(feedItemModel)
        }
    }
}

class FeedDiffs : DiffUtil.ItemCallback<FeedItemModel>() {
    override fun areItemsTheSame(oldItem: FeedItemModel, newItem: FeedItemModel): Boolean {
        return oldItem.uid == newItem.uid
    }

    override fun areContentsTheSame(oldItem: FeedItemModel, newItem: FeedItemModel): Boolean {
        return oldItem.uid == newItem.uid
    }

}