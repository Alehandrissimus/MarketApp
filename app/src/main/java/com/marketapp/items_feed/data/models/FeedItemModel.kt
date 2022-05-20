package com.marketapp.items_feed.data.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class FeedItemModel (

    @SerializedName("uid")
    var uid: String? = null,

    @SerializedName("bsgId")
    var bsgId: String? = null,

    @SerializedName("name")
    var name: String? = null,

    @SerializedName("shortName")
    var shortName: String? = null,

    @SerializedName("price")
    var price: Int? = null,

    @SerializedName("basePrice")
    var basePrice: Int? = null,

    @SerializedName("avg24hPrice")
    var avg24hPrice: Int? = null,

    @SerializedName("avg7daysPrice")
    var avg7daysPrice: Int? = null,

    @SerializedName("traderName")
    var traderName: String? = null,

    @SerializedName("traderPrice")
    var traderPrice: Int? = null,

    @SerializedName("traderPriceCur")
    var traderPriceCur: String? = null,

    @SerializedName("updated")
    var updated: String? = null,

    @SerializedName("slots")
    var slots: Int? = null,

    @SerializedName("diff24h")
    var diff24h: Float? = null,

    @SerializedName("diff7days")
    var diff7days: Float? = null,

    @SerializedName("icon")
    var icon: String? = null,

    @SerializedName("link")
    var link: String? = null,

    @SerializedName("wikiLink")
    var wikiLink: String? = null,

    @SerializedName("img")
    var img: String? = null,

    @SerializedName("imgBig")
    var imgBig: String? = null,

    @SerializedName("reference")
    var reference: String? = null,
) : Parcelable