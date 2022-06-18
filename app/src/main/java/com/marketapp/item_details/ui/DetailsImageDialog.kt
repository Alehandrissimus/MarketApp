package com.marketapp.item_details.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory
import com.marketapp.databinding.FragmentDetailsImageDialogBinding
import com.marketapp.models.FeedItemModel

class DetailsImageDialog(
    private val itemModel: FeedItemModel
) : DialogFragment() {

    lateinit var binding: FragmentDetailsImageDialogBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailsImageDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        itemModel.imgBig?.let { url ->
            binding.apply {
                Glide.with(ivDetailsDialogImage)
                    .load(url)
                    .transition(withCrossFade(DrawableCrossFadeFactory.Builder().setCrossFadeEnabled(true).build()))
                    .into(ivDetailsDialogImage)
            }
        }
    }

}