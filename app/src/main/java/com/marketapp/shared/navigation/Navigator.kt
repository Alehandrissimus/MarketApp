package com.marketapp.shared.navigation

import androidx.annotation.IdRes
import androidx.fragment.app.FragmentManager
import com.marketapp.items_feed.data.models.FeedItemModel
import com.marketapp.items_feed.ui.DetailsFragment
import com.marketapp.items_feed.ui.FeedFragment

class Navigator(
    private val fragmentManager: FragmentManager,
    @IdRes private val containerId: Int,
) {

    fun openFeedFragment() {
        fragmentManager.beginTransaction()
            .replace(containerId, FeedFragment.newInstance())
            .commit()
    }

    fun openItemDetailsFragment(model: FeedItemModel) {
        fragmentManager.beginTransaction()
            .add(containerId, DetailsFragment.newInstance(model))
            .addToBackStack(null)
            .commit()
    }

}