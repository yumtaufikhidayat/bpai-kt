package com.taufik.ceritaku.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.taufik.ceritaku.data.remote.Result
import com.taufik.ceritaku.data.remote.network.ApiService
import com.taufik.ceritaku.data.remote.response.auth.login.LoginRequest
import com.taufik.ceritaku.data.remote.response.auth.login.LoginResponse
import com.taufik.ceritaku.data.remote.response.auth.register.RegisterRequest
import com.taufik.ceritaku.data.remote.response.main.AllStoriesResponse
import com.taufik.ceritaku.utils.data.CommonResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody

class CeritakuRepository private constructor(private val apiService: ApiService) {
    fun loginUser(email: String, password: String): LiveData<Result<LoginResponse>> = liveData {
        emit(Result.Loading)
        try {
            val loginRequest = LoginRequest(email, password)
            val response = apiService.loginUser(loginRequest)
            emit(Result.Success(response))
        } catch (e: Exception) {
            val errorMessage = e.message.toString()
            Log.e(TAG, "listOfStories: $errorMessage")
            emit(Result.Error(errorMessage))
        }
    }

    fun registerUser(name: String, email: String, password: String): LiveData<Result<CommonResponse>> = liveData {
        emit(Result.Loading)
        try {
            val registerRequest = RegisterRequest(name, email, password)
            val response = apiService.registerUser(registerRequest)
            emit(Result.Success(response))
        } catch (e: Exception) {
            val errorMessage = e.message.toString()
            Log.e(TAG, "listOfStories: $errorMessage")
            emit(Result.Error(errorMessage))
        }
    }

    fun listOfStories(token: String): LiveData<Result<AllStoriesResponse>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.getAllOfStories("Bearer $token")
            emit(Result.Success(response))
        } catch (e: Exception) {
            val errorMessage = e.message.toString()
            Log.e(TAG, "listOfStories: $errorMessage")
            emit(Result.Error(errorMessage))
        }
    }

    fun uploadStory(file: MultipartBody.Part, description: RequestBody, token: String): LiveData<Result<CommonResponse>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.uploadStory(file, description, "Bearer $token")
            emit(Result.Success(response))
        } catch (e: Exception) {
            val errorMessage = e.message.toString()
            Log.e(TAG, "listOfStories: $errorMessage")
            emit(Result.Error(errorMessage))
        }
    }

    companion object {
        private val TAG = CeritakuRepository::class.java.simpleName

        @Volatile
        private var instance: CeritakuRepository? = null
        fun getInstance(
            apiService: ApiService
        ): CeritakuRepository = instance ?: synchronized(this) {
            instance ?: CeritakuRepository(apiService)
        }.also { instance = it }
    }
}