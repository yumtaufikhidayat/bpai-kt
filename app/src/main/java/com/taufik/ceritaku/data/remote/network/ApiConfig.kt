package com.taufik.ceritaku.data.remote.network

import com.taufik.ceritaku.BuildConfig
import com.taufik.ceritaku.utils.common.CommonConstant
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiConfig {
    fun getApiService(): ApiService {
        val timeOutTime = 30L

        val interceptor = if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        } else {
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.NONE)
        }

        val builder = OkHttpClient.Builder()
            .connectTimeout(timeOutTime, TimeUnit.SECONDS)
            .writeTimeout(timeOutTime, TimeUnit.SECONDS)
            .readTimeout(timeOutTime, TimeUnit.SECONDS)
            .addInterceptor(interceptor)

        val client = builder.build()

        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(CommonConstant.BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit.create(ApiService::class.java)
    }
}