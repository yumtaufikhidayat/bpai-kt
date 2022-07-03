package com.taufik.ceritaku.api

import com.taufik.ceritaku.BuildConfig
import com.taufik.ceritaku.model.UserPreference
import com.taufik.ceritaku.utils.CommonConstant
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiConfig {
    private const val timeOutTime = 30L

    private var preference: UserPreference? = null

    private val interceptor = if (BuildConfig.DEBUG) {
        HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    } else {
        HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.NONE)
    }

    private val builder = OkHttpClient.Builder()
        .connectTimeout(timeOutTime, TimeUnit.SECONDS)
        .writeTimeout(timeOutTime, TimeUnit.SECONDS)
        .readTimeout(timeOutTime, TimeUnit.SECONDS)
        .addInterceptor(interceptor)
        .addNetworkInterceptor(Interceptor { chain: Interceptor.Chain ->
            val token = runBlocking {
                preference?.getToken()?.first()
            }
            val request = chain.request().newBuilder()
                .addHeader("Authorization:", "Bearer $token")
                .build()
            chain.proceed(request)
        })

    private val client = builder.build()

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(CommonConstant.BASE_URL)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val apiInstance: ApiService = retrofit.create(ApiService::class.java)
}