package com.live.kicktraders.repository

import androidx.annotation.NonNull
import okhttp3.Interceptor
import okhttp3.Response

class CustomInterceptor : Interceptor {

//    var preferenceManager: PreferenceManager =
//        PreferenceManager()

    override fun intercept(@NonNull chain: Interceptor.Chain): Response {

        val chainRequest = chain.request()
        val builder = chainRequest.newBuilder()

        builder.header("X-API-KEY", "onovApp@onovapplication.com")
        val request = builder.build()
        return chain.proceed(request)
    }
}