package com.marketapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import com.marketapp.shared.navigation.Navigator

class NavigationActivity : AppCompatActivity() {

    val navigator by lazy { Navigator(supportFragmentManager, R.id.nvContainer) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_main)

        navigator.openFeedFragment()
    }
}