package com.application.onovapplication.repository

import com.application.onovapplication.repository.BaseUrl.GOOGlE_CLOUD_URL
import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import com.application.onovapplication.repository.service.API
import com.live.kicktraders.repository.CustomInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

import java.util.concurrent.TimeUnit


object ApiManager {

    val authService: API



        private var apiManager: ApiManager? = null

        val instance: ApiManager
            get() {
                if (apiManager == null) {
                    apiManager = ApiManager
                }
                return apiManager as ApiManager
            }


    init {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .readTimeout(300, TimeUnit.SECONDS)
            .connectTimeout(300, TimeUnit.SECONDS)
            .addInterceptor(CustomInterceptor())
            .build()
        val gson = GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .create()


        val retrofit = Retrofit.Builder()
            .baseUrl(BaseUrl.apiBaseUrl)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(okHttpClient)
            .build()

        authService = retrofit.create(API::class.java)

    }


    fun retrofitBuilderForChat(): Retrofit {
        //                    .addHeader("Authorization","key=AAAAjQUvR-U:APA91bFmGFVxBs_-cEwUIoBcwWk-9yMrDBj7vEw_FNex87dayjTv-2I8GgHlY8Ayq7da_jrzUG3-t3RYzGFbpYs3NzFpx102d3eyfN6SaQMX7XEdnxiBMthIqq3NiLf1AjHGjG9fy_hg")


        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        val client = OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(40 * 1000, TimeUnit.MILLISECONDS)
            .addInterceptor(interceptor)
            .addInterceptor { chain ->
                val newRequest = chain.request().newBuilder()
                    .addHeader(
                        "Authorization",
                        "key=AAAA37nLzvw:APA91bFVpXbIJSutDT3U6-Ab76NlnXTMp5YtVUsydVe79MGbOo9aO1QM6wvW65DTS40g0miEM0wzbnV4Ak5pNGqJeXt324106Kg402Drt0T3xZRhQvALIV7mzsWjMK_akQqW2jrRZCj0"
                    )
                    .addHeader("Content-Type", "application/json")
                    .build()
                chain.proceed(newRequest)

            }.build()


        val retrofit = Retrofit.Builder()
            .baseUrl(GOOGlE_CLOUD_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
        return retrofit
    }

    val apiService: API = retrofitBuilderForChat().create(API::class.java)


}