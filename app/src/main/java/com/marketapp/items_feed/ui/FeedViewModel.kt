package com.marketapp.items_feed.ui

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class FeedViewModel : ViewModel() {

    private val _feedState = MutableLiveData<FeedViewState>()
    val feedState = _feedState

    fun loadFeed() {

    }

    fun searchItems(item: String) {
        Log.d("TAG", "Debounce: " )
    }

}

sealed class FeedViewState {
    object FeedLoading: FeedViewState()
    data class FeedSuccess(val a: Int): FeedViewState()
    data class FeedError(val a: Int): FeedViewState()
}