package com.marketapp.items_feed.ui

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory
import com.marketapp.R
import com.marketapp.databinding.FragmentDetailsImageDialogBinding
import com.marketapp.items_feed.data.models.FeedItemModel

class DetailsImageDialog(
    private val itemModel: FeedItemModel
) : DialogFragment() {

    lateinit var binding: FragmentDetailsImageDialogBinding
//
//    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
//        val builder = AlertDialog.Builder(context)
//        val inflater = requireActivity().layoutInflater
//        builder.setView(inflater.inflate(R.layout.fragment_details_image_dialog, null))
//
//        return builder.create()
//    }

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
            Log.d("TAG1", url)
            binding.apply {
                Glide.with(ivDetailsDialogImage)
                    .load(url)
                    .transition(withCrossFade(DrawableCrossFadeFactory.Builder().setCrossFadeEnabled(true).build()))
                    .into(ivDetailsDialogImage)
            }
        }
    }

}