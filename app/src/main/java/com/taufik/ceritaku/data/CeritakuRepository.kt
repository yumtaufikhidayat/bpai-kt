package com.taufik.ceritaku.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.paging.*
import com.taufik.ceritaku.data.local.entity.StoryEntity
import com.taufik.ceritaku.data.local.room.StoryDao
import com.taufik.ceritaku.data.remote.CeritakuPagingSource
import com.taufik.ceritaku.data.remote.Result
import com.taufik.ceritaku.data.remote.network.ApiService
import com.taufik.ceritaku.data.remote.response.auth.login.LoginRequest
import com.taufik.ceritaku.data.remote.response.auth.login.LoginResponse
import com.taufik.ceritaku.data.remote.response.auth.register.RegisterRequest
import com.taufik.ceritaku.utils.data.CommonResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
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

    fun getListOfStories(token: String): LiveData<PagingData<StoryEntity>> {
        return Pager(
            config = PagingConfig(pageSize = 6),
            pagingSourceFactory = {
                CeritakuPagingSource(apiService, "Bearer $token")
            }
        ).liveData
    }

    fun dataResult(loadState: CombinedLoadStates) : Flow<Result<Unit>> = flow {
        when(val result = loadState.source.refresh) {
            is LoadState.Loading -> emit(Result.Loading)
            is LoadState.NotLoading -> emit(Result.Success(Unit))
            is LoadState.Error -> {
                val error = result.error.message?.split(" - ")
                val codeResponse = error?.get(0)
                val message = error?.get(1)
                when (codeResponse?.toInt()) {
                    401 -> emit(Result.Unauthorized(message.toString()))
                    500, 502 -> emit(Result.ServerError(message.toString()))
                    else -> emit(Result.Error(message.toString()))
                }
            }
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