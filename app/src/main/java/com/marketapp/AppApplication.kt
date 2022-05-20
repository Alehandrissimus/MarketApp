package com.marketapp

import android.app.Application
import com.marketapp.shared.di.AppComponent
import com.marketapp.shared.di.AppModule
import com.marketapp.shared.di.DaggerAppComponent

class AppApplication : Application() {

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()

        appComponent = DaggerAppComponent.builder()
            .appModule(AppModule(this))
            .build()
    }

}