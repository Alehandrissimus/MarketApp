package com.marketapp.items_feed.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.marketapp.BuildConfig
import com.marketapp.models.FeedItemModel
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

    private var titleFilterState = FilterState.NOTHING
    private var avgPriceFilterState = FilterState.NOTHING
    private var changeFilterState = FilterState.NOTHING

    private val itemsScope = CoroutineScope(
        SupervisorJob() +
                CoroutineExceptionHandler { _, throwable ->
                    feedState.postValue(FeedViewState.FeedError("${throwable.message}"))
                }
    )

    fun loadFeed() {
        _feedState.value = FeedViewState.FeedLoading
        itemsScope.launch {
            val result = apiService.getItemsByTest(BuildConfig.USERKEY)
            _feedState.postValue(FeedViewState.FeedSuccess(result))
        }
    }

    fun searchItems(item: String) {
        _feedState.postValue(FeedViewState.FeedLoading)
        itemsScope.launch {
            val result = apiService.getItemByName(item, BuildConfig.USERKEY)
            _feedState.postValue(FeedViewState.FeedSuccess(result))
        }
    }

    fun filterTitles(): FilterState {
        avgPriceFilterState = FilterState.NOTHING
        changeFilterState = FilterState.NOTHING
        when(titleFilterState) {
            FilterState.NOTHING -> {
                titleFilterState = FilterState.DESCENDING

                if(_feedState.value is FeedViewState.FeedSuccess) {
                    val itemsData = (_feedState.value as FeedViewState.FeedSuccess).result
                    val sortedItems = itemsData.sortedBy { it.name }
                    feedState.postValue(FeedViewState.FeedSuccess(sortedItems))
                }
                return FilterState.DESCENDING
            }
            FilterState.DESCENDING -> {
                titleFilterState = FilterState.ASCENDING

                if(_feedState.value is FeedViewState.FeedSuccess) {
                    val itemsData = (_feedState.value as FeedViewState.FeedSuccess).result
                    val sortedItems = itemsData.sortedByDescending { it.name }
                    feedState.postValue(FeedViewState.FeedSuccess(sortedItems))
                }
                return FilterState.ASCENDING
            }
            FilterState.ASCENDING -> {
                titleFilterState = FilterState.NOTHING
                return FilterState.NOTHING
            }
        }
    }

    fun filterAvgPrice(): FilterState {
        titleFilterState = FilterState.NOTHING
        changeFilterState = FilterState.NOTHING
        when(avgPriceFilterState) {
            FilterState.NOTHING -> {
                avgPriceFilterState = FilterState.DESCENDING

                if(_feedState.value is FeedViewState.FeedSuccess) {
                    val itemsData = (_feedState.value as FeedViewState.FeedSuccess).result
                    val sortedItems = itemsData.sortedByDescending { it.avg24hPrice }
                    feedState.postValue(FeedViewState.FeedSuccess(sortedItems))
                }
                return FilterState.DESCENDING
            }
            FilterState.DESCENDING -> {
                avgPriceFilterState = FilterState.ASCENDING
                if(_feedState.value is FeedViewState.FeedSuccess) {
                    val itemsData = (_feedState.value as FeedViewState.FeedSuccess).result
                    val sortedItems = itemsData.sortedBy { it.avg24hPrice }
                    feedState.postValue(FeedViewState.FeedSuccess(sortedItems))
                }
                return FilterState.ASCENDING
            }
            FilterState.ASCENDING -> {
                avgPriceFilterState = FilterState.NOTHING
                return FilterState.NOTHING
            }
        }
    }

    fun filterChange(): FilterState {
        titleFilterState = FilterState.NOTHING
        avgPriceFilterState = FilterState.NOTHING
        when(changeFilterState) {
            FilterState.NOTHING -> {
                changeFilterState = FilterState.DESCENDING

                if(_feedState.value is FeedViewState.FeedSuccess) {
                    val itemsData = (_feedState.value as FeedViewState.FeedSuccess).result
                    val sortedItems = itemsData.sortedByDescending { it.diff24h }
                    feedState.postValue(FeedViewState.FeedSuccess(sortedItems))
                }
                return FilterState.DESCENDING
            }
            FilterState.DESCENDING -> {
                changeFilterState = FilterState.ASCENDING

                if(_feedState.value is FeedViewState.FeedSuccess) {
                    val itemsData = (_feedState.value as FeedViewState.FeedSuccess).result
                    val sortedItems = itemsData.sortedBy { it.diff24h }
                    feedState.postValue(FeedViewState.FeedSuccess(sortedItems))
                }
                return FilterState.ASCENDING
            }
            FilterState.ASCENDING -> {
                changeFilterState = FilterState.NOTHING
                return FilterState.NOTHING
            }
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
    data class FeedError(val error: String) : FeedViewState()
}

enum class FilterState {
    NOTHING,
    ASCENDING,
    DESCENDING
}
