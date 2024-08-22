package com.app.thefruitsspirit.retrofit

import android.content.Context
import com.app.thefruitsspirit.R
import com.app.thefruitsspirit.cache.getToken


import okhttp3.Interceptor
import okhttp3.Response

class AppInterceptor(private val context: Context) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        val token: String = "Bearer " + getToken(context)

        val headers = if (token != null) {
            request.headers.newBuilder()
                .add("Content-Type", "application/json")
                .add("Accept", "application/json")
                .add("Authorization", token)
//                .add("publish_key", context.getString(R.string.publish_key))
//                .add("secret_key", context.getString(R.string.secret_key))
                .build()
        } else {
            request.headers.newBuilder()
                .add("Content-Type", "application/json")
                .add("Accept", "application/json")
//                .add("publish_key", context.getString(R.string.publish_key))
//                .add("secret_key", context.getString(R.string.secret_key))
                //.add("security_key", "")
                .build()
        }
        request = request.newBuilder().headers(headers).build()
        return chain.proceed(request)
    }
}