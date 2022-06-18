package com.marketapp

import android.os.Build
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
        window.navigationBarColor = resources.getColor(R.color.black)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            window.navigationBarDividerColor = resources.getColor(R.color.item_fg)
        }
        setContentView(R.layout.activity_main)

        navigator.openFeedFragment()
    }
}