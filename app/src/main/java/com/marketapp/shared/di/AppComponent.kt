package com.marketapp.shared.di

import com.marketapp.items_feed.ui.DetailsFragment
import com.marketapp.items_feed.ui.FeedFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {
    fun inject(fragment: FeedFragment)
    fun inject(fragment: DetailsFragment)
}