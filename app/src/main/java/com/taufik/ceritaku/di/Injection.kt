package com.taufik.ceritaku.di

import android.content.Context
import com.taufik.ceritaku.data.CeritakuRepository
import com.taufik.ceritaku.data.remote.network.ApiConfig


object Injection {
    fun provideRepository(context: Context): CeritakuRepository {
        val apiService = ApiConfig.apiInstance
        return CeritakuRepository.getInstance(apiService)
    }
}