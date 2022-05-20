package com.marketapp.shared.di

import android.content.Context
import androidx.annotation.NonNull
import com.google.gson.GsonBuilder
import com.marketapp.items_feed.ui.FeedViewModel
import com.marketapp.shared.network.ApiService
import com.marketapp.shared.network.HeaderInterceptor
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class AppModule(private var context: Context) {

    @Provides
    @Singleton
    @NonNull
    fun provideContext(): Context {
        return context
    }

    @Provides
    @Singleton
    fun provideRetrofit(gsonConverterFactory: GsonConverterFactory): Retrofit {
        return Retrofit.Builder()
            .client(
                OkHttpClient().newBuilder().addInterceptor(HeaderInterceptor()).build()
            )
            .baseUrl("https://tarkov-market.com/")
            .addConverterFactory(gsonConverterFactory)
            .build()
    }

    @Provides
    @Singleton
    fun provideGsonConverterFactory(): GsonConverterFactory {
        return GsonConverterFactory.create(
            GsonBuilder().setLenient().create()
        )
    }

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideFeedViewModel(apiService: ApiService): FeedViewModel {
        return FeedViewModel(apiService)
    }
}