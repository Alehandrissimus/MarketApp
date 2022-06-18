package com.marketapp.items_feed.ui

import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.marketapp.R
import com.marketapp.databinding.FeedItemBinding
import com.marketapp.models.FeedItemModel
import java.text.DecimalFormat

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
        val formatter = DecimalFormat("#,###,###")
        binding.apply {
            tvFeedName.text = feedItemModel.name
            Glide.with(ivFeed)
                .load(feedItemModel.icon)
                .dontAnimate()
                .into(ivFeed)
            tvFeedValue.text = itemView.resources.getString(R.string.details_price_currency, formatter.format(feedItemModel.avg24hPrice))

            val spanText24hDiff = SpannableString(itemView.resources.getString(R.string.details_price_change, formatter.format(feedItemModel.diff24h)))
            feedItemModel.diff24h?.let { diff24h ->
                if (diff24h > 0) {
                    spanText24hDiff.setSpan(ForegroundColorSpan(itemView.resources.getColor(R.color.details_text_green)), 0, spanText24hDiff.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                } else if (diff24h <= 0) {
                    spanText24hDiff.setSpan(ForegroundColorSpan(itemView.resources.getColor(R.color.white)), 0, spanText24hDiff.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                }
            }
            tvFeedAvg.text = spanText24hDiff
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