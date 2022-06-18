package com.marketapp.items_feed.ui

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.marketapp.AppApplication
import com.marketapp.R
import com.marketapp.databinding.FragmentFeedBinding
import com.marketapp.shared.navigation.BaseFragment
import kotlinx.coroutines.*
import javax.inject.Inject


class FeedFragment : BaseFragment() {

    companion object {
        fun newInstance(): FeedFragment {
            val args = Bundle()

            val fragment = FeedFragment()
            fragment.arguments = args
            return fragment
        }
    }

    private lateinit var binding: FragmentFeedBinding
    private lateinit var feedAdapter: FeedRVAdapter

    @Inject
    lateinit var feedViewModel: FeedViewModel

    private val coroutineScope = CoroutineScope(Job())

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (context.applicationContext as AppApplication).appComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFeedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecycleView()
        setupDebounce()
        setupObserver()
        setupOnClickListeners()
        setupRVAdapterDataObserver()
    }

    override fun onDestroy() {
        super.onDestroy()
        coroutineScope.cancel()
    }

    private fun setupObserver() {
        feedViewModel.loadFeed()
        feedViewModel.feedState.observe(viewLifecycleOwner, ::showFeedResults)
    }

    private fun setupRecycleView() {
        feedAdapter = FeedRVAdapter(navigator::openItemDetailsFragment)
        binding.rvFeed.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = feedAdapter
        }
    }

    private fun setupDebounce() {
        val watcher = object : TextWatcher {
            var searchFor = ""

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val searchText = s.toString().trim()
                if (searchText == searchFor)
                    return

                searchFor = searchText

                coroutineScope.launch {
                    delay(300)
                    if (searchText != searchFor)
                        return@launch

                    if (searchText.isNotEmpty()) {
                        feedViewModel.searchItems(searchText)
                    }
                }
            }

            override fun afterTextChanged(s: Editable?) = Unit
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) =
                Unit
        }
        binding.etFeedSearch.addTextChangedListener(watcher)
    }

    private fun setupOnClickListeners() {
        binding.apply {
            feedTableLl2.setOnClickListener {
                hideFilterIcons()
                val state = feedViewModel.filterTitles()
                when(state) {
                    FilterState.ASCENDING -> {
                        ivFeedTableTitleFilter.visibility = View.VISIBLE
                        ivFeedTableTitleFilter.background = ResourcesCompat.getDrawable(resources, R.drawable.ic_baseline_arrow_drop_up_24, null)
                    }
                    FilterState.DESCENDING -> {
                        ivFeedTableTitleFilter.visibility = View.VISIBLE
                        ivFeedTableTitleFilter.background = ResourcesCompat.getDrawable(resources, R.drawable.ic_baseline_arrow_drop_down_24, null)
                    }
                    FilterState.NOTHING -> {
                        ivFeedTableTitleFilter.visibility = View.GONE
                    }
                }
            }
            feedTableLl3.setOnClickListener {
                hideFilterIcons()
                val state = feedViewModel.filterAvgPrice()
                when(state) {
                    FilterState.ASCENDING -> {
                        ivFeedTableAvgpriceFilter.visibility = View.VISIBLE
                        ivFeedTableAvgpriceFilter.background = ResourcesCompat.getDrawable(resources, R.drawable.ic_baseline_arrow_drop_up_24, null)
                    }
                    FilterState.DESCENDING -> {
                        ivFeedTableAvgpriceFilter.visibility = View.VISIBLE
                        ivFeedTableAvgpriceFilter.background = ResourcesCompat.getDrawable(resources, R.drawable.ic_baseline_arrow_drop_down_24, null)
                    }
                    FilterState.NOTHING -> {
                        ivFeedTableAvgpriceFilter.visibility = View.GONE
                    }
                }
            }
            feedTableLl4.setOnClickListener {
                hideFilterIcons()
                val state = feedViewModel.filterChange()
                when(state) {
                    FilterState.ASCENDING -> {
                        ivFeedTableChangeFilter.visibility = View.VISIBLE
                        ivFeedTableChangeFilter.background = ResourcesCompat.getDrawable(resources, R.drawable.ic_baseline_arrow_drop_up_24, null)
                    }
                    FilterState.DESCENDING -> {
                        ivFeedTableChangeFilter.visibility = View.VISIBLE
                        ivFeedTableChangeFilter.background = ResourcesCompat.getDrawable(resources, R.drawable.ic_baseline_arrow_drop_down_24, null)
                    }
                    FilterState.NOTHING -> {
                        ivFeedTableChangeFilter.visibility = View.GONE
                    }
                }
            }
        }
    }

    private fun hideFilterIcons() {
        binding.apply {
            ivFeedTableTitleFilter.visibility = View.GONE
            ivFeedTableAvgpriceFilter.visibility = View.GONE
            ivFeedTableChangeFilter.visibility = View.GONE
        }
    }

    private fun showFeedResults(feedState: FeedViewState) {
        when (feedState) {
            is FeedViewState.FeedSuccess -> {
                binding.apply {
                    errTvFeed.visibility = View.GONE
                    pbFeed.visibility = View.GONE
                    rvFeed.visibility = View.VISIBLE
                }
                feedAdapter.submitList(feedState.result)
                binding.rvFeed.scrollToPosition(0)
            }
            is FeedViewState.FeedLoading -> {
                binding.apply {
                    errTvFeed.visibility = View.GONE
                    pbFeed.visibility = View.VISIBLE
                    rvFeed.visibility = View.GONE
                }
            }
            is FeedViewState.FeedError -> {
                binding.apply {
                    pbFeed.visibility = View.GONE
                    rvFeed.visibility = View.GONE
                    errTvFeed.visibility = View.VISIBLE
                    errTvFeed.text = "Error while loading Feed: ${feedState.error}"
                }
            }
        }
    }

    private fun setupRVAdapterDataObserver() {
        feedAdapter.registerAdapterDataObserver(object: RecyclerView.AdapterDataObserver() {
            override fun onChanged() {
                binding.rvFeed.scrollToPosition(0)
            }
            override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
                binding.rvFeed.scrollToPosition(0)
            }
            override fun onItemRangeMoved(fromPosition: Int, toPosition: Int, itemCount: Int) {
                binding.rvFeed.scrollToPosition(0)
            }
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                binding.rvFeed.scrollToPosition(0)
            }
            override fun onItemRangeChanged(positionStart: Int, itemCount: Int) {
                binding.rvFeed.scrollToPosition(0)
            }
            override fun onItemRangeChanged(positionStart: Int, itemCount: Int, payload: Any?) {
                binding.rvFeed.scrollToPosition(0)
            }
        })
    }
}
