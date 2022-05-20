package com.marketapp.items_feed.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.marketapp.AppApplication
import com.marketapp.databinding.FragmentDetailsBinding
import com.marketapp.items_feed.data.models.FeedItemModel
import com.marketapp.shared.navigation.BaseFragment

class DetailsFragment : BaseFragment() {

    companion object {

        private const val KEY_MODEL = "KEY_MODEL"

        fun newInstance(itemModel: FeedItemModel): DetailsFragment {
            val args = Bundle()
            args.putParcelable(KEY_MODEL, itemModel)

            val fragment = DetailsFragment()
            fragment.arguments = args
            return fragment
        }
    }


    private lateinit var binding: FragmentDetailsBinding

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (context.applicationContext as AppApplication).appComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.getParcelable<FeedItemModel>(KEY_MODEL)?.let { model ->
            setupModel(model)
        }
    }

    private fun setupModel(itemModel: FeedItemModel) {
        binding.apply {
            tvDetailsName.text = itemModel.name
        }
    }

}