package com.marketapp.shared.network

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import kotlin.math.log

class HeaderInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request: Request = chain.request()
        val resp = chain.proceed(request).newBuilder()
            .addHeader("x-api-key", "abobaabobus") // TODO secret
            .build()

        logError(resp)

        return resp
    }

    private fun logError(response: Response) {
        Log.d("TAG1", "Response code ${response.code()}, ${response.headers()}")
    }
}