package com.marketapp.item_details.ui

import android.content.Context
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.marketapp.AppApplication
import com.marketapp.R
import com.marketapp.databinding.FragmentDetailsBinding
import com.marketapp.models.FeedItemModel
import com.marketapp.shared.navigation.BaseFragment
import java.text.DecimalFormat


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
            val formatter = DecimalFormat("#,###,###")
            tvDetailsItemName.text = itemModel.name
            tvDetailsItemShortName.text = itemModel.shortName
            tvDetailsItemTag.text = itemModel.tags?.get(0)

            tvDetailsFleaPrice.text = resources.getString(R.string.details_price_currency, formatter.format(itemModel.price))

            tvDetailsPricePerSlot.text = resources.getString(R.string.details_price_currency, formatter.format(itemModel.slots?.let { itemModel.price?.div(it) }))
            tvDetailsSlots.text = resources.getString(R.string.details_slots, itemModel.slots)

            tvDetailsTrader.text = itemModel.traderName
            tvDetailsTraderPrice.text = resources.getString(
                R.string.details_trader_price,
                formatter.format(itemModel.traderPrice),
                itemModel.traderPriceCur,
                formatter.format(itemModel.traderPriceRub)
            )

            val spanText24hPrice = SpannableString("24 hours: " + resources.getString(R.string.details_price_currency, formatter.format(itemModel.avg24hPrice)))
            spanText24hPrice.setSpan(ForegroundColorSpan(resources.getColor(R.color.white)), 10, spanText24hPrice.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            val spanText7dPrice = SpannableString("7 days: " + resources.getString(R.string.details_price_currency, formatter.format(itemModel.avg7daysPrice)))
            spanText7dPrice.setSpan(ForegroundColorSpan(resources.getColor(R.color.white)), 8, spanText7dPrice.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

            tvDetailsAvgPrice24h.text = spanText24hPrice
            tvDetailsAvgPrice7d.text = spanText7dPrice

            val spanText24hDiff = SpannableString("24 hours: " + resources.getString(R.string.details_price_change, formatter.format(itemModel.diff24h)))
            itemModel.diff24h?.let { diff24h ->
                if (diff24h > 0) {
                    spanText24hDiff.setSpan(ForegroundColorSpan(resources.getColor(R.color.details_text_green)), 8, spanText24hDiff.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                } else if (diff24h <= 0) {
                    spanText24hDiff.setSpan(ForegroundColorSpan(resources.getColor(R.color.white)), 8, spanText24hDiff.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                }
            }
            val spanText7dDiff = SpannableString("7 days: " + resources.getString(R.string.details_price_change, formatter.format(itemModel.diff7days)))
            itemModel.diff7days?.let { diff7days ->
                if (diff7days > 0) {
                    spanText7dDiff.setSpan(ForegroundColorSpan(resources.getColor(R.color.details_text_green)), 8, spanText7dDiff.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                } else if (diff7days <= 0) {
                    spanText7dDiff.setSpan(ForegroundColorSpan(resources.getColor(R.color.white)), 8, spanText7dDiff.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                }
            }

            tvDetailsChange24h.text = spanText24hDiff
            tvDetailsChange7d.text = spanText7dDiff

            Glide.with(ivDetailsIcon)
                .load(itemModel.imgBig)
                .placeholder(R.drawable.placeholder_small_dark)
                .dontAnimate()
                .into(ivDetailsIcon)
            Glide.with(ivDetailsIconSmall)
                .load(itemModel.img)
                .placeholder(R.drawable.placeholder_small_dark)
                .dontAnimate()
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
                // TODO Favourite
            }
        }
    }

}