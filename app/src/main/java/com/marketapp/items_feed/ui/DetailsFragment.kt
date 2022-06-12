package com.marketapp.items_feed.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory
import com.marketapp.AppApplication
import com.marketapp.R
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
            setupOnClickListeners(model)
        }
    }

    private fun setupModel(itemModel: FeedItemModel) {
        binding.apply {
            tvDetailsItemName.text = itemModel.name
            tvDetailsItemShortName.text = itemModel.shortName
            tvDetailsItemTag.text = itemModel.tags?.get(0)

            Glide.with(ivDetailsIcon)
                .load(itemModel.imgBig)
                .transition(withCrossFade(DrawableCrossFadeFactory.Builder().setCrossFadeEnabled(true).build()))
                .placeholder(R.drawable.placeholder_small_dark)
                .into(ivDetailsIcon)
            Glide.with(ivDetailsIconSmall)
                .load(itemModel.img)
                .transition(withCrossFade(DrawableCrossFadeFactory.Builder().setCrossFadeEnabled(true).build()))
                .placeholder(R.drawable.placeholder_small_dark)
                .into(ivDetailsIconSmall)
        }
    }

    private fun setupOnClickListeners(itemModel: FeedItemModel) {
        binding.apply {
            ivDetailsIcon.setOnClickListener {
                fragmentManager?.beginTransaction()
                    ?.add(DetailsImageDialog(itemModel), "")
                    ?.commit()
            }
            ivDetailsFavourite.setOnClickListener {

            }
        }
    }

}