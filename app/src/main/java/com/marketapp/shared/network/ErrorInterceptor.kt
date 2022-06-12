package com.marketapp.shared.network

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response

class ErrorInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val response = chain.proceed(request)

        if(!response.isSuccessful) {
            Log.d("TAG1", "  CODE: ${response.code()}, ${response.body()}")
        }

        return response
    }

}