package com.marketapp.shared.preferences

import android.content.Context
import android.content.SharedPreferences

class SharedPrefs constructor(
    private val context: Context
) {
    companion object {
        const val KEY_TOKEN = "KEY_TOKEN"
        const val KEY_USERNAME = "KEY_USERNAME"
    }

    private val sharedPreferences: SharedPreferences by lazy {
        context.getSharedPreferences("preferences", Context.MODE_PRIVATE)
    }

    var token: String by SharedPrefDelegate(sharedPreferences, KEY_TOKEN, "")

    var userLogin: String by SharedPrefDelegate(sharedPreferences, KEY_USERNAME, "")
}