package com.taufik.ceritaku.ui.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.taufik.ceritaku.api.ApiConfig
import com.taufik.ceritaku.ui.main.data.AllStoriesResponse
import com.taufik.ceritaku.ui.main.data.ListStoryItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel: ViewModel() {

    private val apiConfig = ApiConfig.apiInstance

    private val _listOfStories = MutableLiveData<List<ListStoryItem>>()
    val lisOfStories: LiveData<List<ListStoryItem>> = _listOfStories

    fun listStories(token: String) {
        apiConfig.getAllStories("Bearer $token").enqueue(object : Callback<AllStoriesResponse>{
            override fun onResponse(
                call: Call<AllStoriesResponse>,
                response: Response<AllStoriesResponse>
            ) {
                if (response.isSuccessful) {
                    _listOfStories.value = response.body()?.listStory
                } else {
                    Log.i(TAG, "onResponse: ")
                }
            }

            override fun onFailure(call: Call<AllStoriesResponse>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.printStackTrace()}")
            }
        })
    }

    companion object {
        private val TAG = MainViewModel::class.java.simpleName
    }
}