package com.taufik.ceritaku.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.taufik.ceritaku.data.local.entity.StoryEntity
import com.taufik.ceritaku.data.local.room.StoryDao
import com.taufik.ceritaku.data.remote.Result
import com.taufik.ceritaku.data.remote.network.ApiService
import com.taufik.ceritaku.data.remote.response.auth.login.LoginRequest
import com.taufik.ceritaku.data.remote.response.auth.login.LoginResponse
import com.taufik.ceritaku.data.remote.response.auth.register.RegisterRequest
import com.taufik.ceritaku.utils.data.CommonResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody

class CeritakuRepository private constructor(
    private val apiService: ApiService,
    private val storyDao: StoryDao
) {
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

    fun getListOfStories(token: String): LiveData<Result<List<StoryEntity>>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.getAllOfStories("Bearer $token")
            val listOfStories = response.listStory
            val storyList = listOfStories.map {
                StoryEntity(
                    it.id,
                    it.photoUrl,
                    it.createdAt,
                    it.name,
                    it.description,
                    it.lon,
                    it.lat
                )
            }
            emit(Result.Success(storyList))
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

    fun getFavoriteStory(): LiveData<List<StoryEntity>> {
        return storyDao.getFavoriteStory()
    }

    fun insertStory(storyEntity: StoryEntity) {
        CoroutineScope(Dispatchers.IO).launch {
            storyDao.insertStory(storyEntity)
        }
    }

    fun removeStory(id: String) {
        CoroutineScope(Dispatchers.IO).launch {
            storyDao.removeStory(id)
        }
    }

    fun isStoryFavorite(id: String): LiveData<Boolean> {
        return storyDao.isStoryFavorite(id)
    }

    companion object {
        private val TAG = CeritakuRepository::class.java.simpleName

        @Volatile
        private var instance: CeritakuRepository? = null
        fun getInstance(
            apiService: ApiService,
            storyDao: StoryDao
        ): CeritakuRepository = instance ?: synchronized(this) {
            instance ?: CeritakuRepository(apiService, storyDao)
        }.also { instance = it }
    }
}