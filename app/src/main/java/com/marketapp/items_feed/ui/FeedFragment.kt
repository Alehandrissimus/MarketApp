package com.marketapp.items_feed.ui

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.marketapp.AppApplication
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

    private fun showFeedResults(feedState: FeedViewState) {
        when (feedState) {
            is FeedViewState.FeedSuccess -> {
                binding.apply {
                    errTvFeed.visibility = View.GONE
                    pbFeed.visibility = View.GONE
                    rvFeed.visibility = View.VISIBLE
                }
                feedAdapter.submitList(feedState.result)
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
}
