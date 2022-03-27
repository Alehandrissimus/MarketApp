package com.marketapp.items_feed.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.marketapp.R
import com.marketapp.databinding.FragmentFeedBinding
import com.marketapp.items_feed.data.models.FeedItemModel
import com.marketapp.shared.navigation.BaseFragment
import kotlinx.coroutines.*
import java.io.BufferedReader
import java.io.InputStreamReader
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

    private fun setupRecycleView() {
        feedAdapter = FeedRVAdapter(navigator::openItemDetailsFragment)
        binding.rvFeed.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = feedAdapter
        }

        feedAdapter.submitList(jsonRawDecode())
    }

    //TODO VM and loading
    private fun jsonRawDecode(): List<FeedItemModel> {
        val itemsListType = object : TypeToken<List<FeedItemModel?>?>() {}.type

        val sb = StringBuffer()
        val br = BufferedReader(InputStreamReader(resources.openRawResource(R.raw.sample)))
        var temp: String?
        while (br.readLine().also { temp = it } != null) sb.append(temp)

        return Gson().fromJson(sb.toString(), itemsListType)
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
                        //feedViewModel.searchItems(searchText)
                        Log.d("TAG", "Searching for ${searchText}")
                    }
                }
            }

            override fun afterTextChanged(s: Editable?) = Unit
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) =
                Unit
        }
        binding.etFeedSearch.addTextChangedListener(watcher)
    }

    private fun setupObserver() {
        //feedViewModel.feedState.observe(viewLifecycleOwner, ::showFeedResults)
    }

    private fun showFeedResults(feedState: FeedViewState) {
        when (feedState) {
            is FeedViewState.FeedSuccess -> {

            }
            is FeedViewState.FeedLoading -> {

            }
            is FeedViewState.FeedError -> {

            }
        }
    }

    /*
    private fun setupViewPager() {
        val list = arrayListOf("asdasd", "asdas13123d")

        binding.apply {
            mainpager.adapter = FeedViewPagerAdapter(list)
            TabLayoutMediator(mainTabLayout, mainpager) { tab, position ->
                tab.text = list[position]
            }.attach()
        }
    }

     */

}