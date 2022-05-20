package com.marketapp.shared.network

import com.marketapp.items_feed.data.models.FeedItemModel
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    @GET("/api/v1/item")
    suspend fun getItemByUuid(
        @Query("uid") item_uuid: String,
        @Query("x-api-key") api: String
    ): List<FeedItemModel>

    @GET("/api/v1/item")
    suspend fun getItemByName(
        @Query("q") item_name: String,
        @Query("x-api-key") api: String
    ): List<FeedItemModel>

    @GET("/api/v1/items/all")
    suspend fun getAllItems(
        @Query("x-api-key") api: String
    ): List<FeedItemModel>

    @GET("/api/v1/items/all")
    suspend fun getItemsByTag(
        @Query("tags") tags: List<String>,
        @Query("x-api-key") api: String
    ): List<FeedItemModel>

    @GET("/api/v1/items/all?tags=Ammo,Magazines")
    suspend fun getItemsByTest(
        @Query("x-api-key") api: String
    ): List<FeedItemModel>
}