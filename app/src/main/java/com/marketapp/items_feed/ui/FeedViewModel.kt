package com.marketapp.items_feed.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.marketapp.items_feed.data.models.FeedItemModel
import com.marketapp.shared.network.ApiService
import kotlinx.coroutines.*
import javax.inject.Inject

class FeedViewModel @Inject constructor(
    private val apiService: ApiService
) : ViewModel() {

    private val _feedState = MutableLiveData<FeedViewState>()
    val feedState = _feedState

    private val _errorLiveData = MutableLiveData<String>()
    val errorState = _errorLiveData

    private val itemsScope = CoroutineScope(
        SupervisorJob() +
                CoroutineExceptionHandler { _, throwable ->
                    errorState.postValue("Error: $throwable")
                }
    )

    fun loadFeed() {
        _feedState.value = FeedViewState.FeedLoading
        itemsScope.launch {
            val result = apiService.getItemsByTest("aAeeRCzSeLI2KOVh")
            _feedState.postValue(FeedViewState.FeedSuccess(result))
        }
    }

    fun searchItems(item: String) {
        _feedState.postValue(FeedViewState.FeedLoading)
        itemsScope.launch {
            val result = apiService.getItemByName(item, "aAeeRCzSeLI2KOVh")
            _feedState.postValue(FeedViewState.FeedSuccess(result))
        }
    }

    override fun onCleared() {
        super.onCleared()
        itemsScope.cancel()
    }

}

sealed class FeedViewState {
    object FeedLoading : FeedViewState()
    data class FeedSuccess(val result: List<FeedItemModel>) : FeedViewState()
    data class FeedError(val a: Int) : FeedViewState()
}