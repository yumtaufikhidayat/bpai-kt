package com.taufik.ceritaku.ui.upload

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.taufik.ceritaku.api.ApiConfig
import com.taufik.ceritaku.utils.Event
import com.taufik.ceritaku.utils.data.CommonResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UploadStoryViewModel : ViewModel() {
    private val apiConfig = ApiConfig.apiInstance

    private val _uploadImage = MutableLiveData<CommonResponse>()
    val uploadImage: LiveData<CommonResponse> = _uploadImage

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _responseMessage = MutableLiveData<Event<String>>()
    val responseMessage: LiveData<Event<String>> = _responseMessage

    fun uploadImage(file: MultipartBody.Part, description: RequestBody, token: String) {
        _isLoading.value = true
        apiConfig.uploadImage(file, description, "Bearer $token")
            .enqueue(object : Callback<CommonResponse>{
                override fun onResponse(
                    call: Call<CommonResponse>,
                    response: Response<CommonResponse>
                ) {
                    if (response.isSuccessful) {
                        _isLoading.value = false
                        _responseMessage.value = Event(response.body()?.message.toString())
                        _uploadImage.value = response.body()
                    } else {
                        _responseMessage.value = Event(response.message())
                        try {
                            val responseBody = Gson().fromJson(
                                response.errorBody()?.charStream(),
                                CommonResponse::class.java
                            )
                            _responseMessage.value = Event(responseBody.message)
                        } catch (e: Exception) {
                            Log.e(TAG, "onResponse: ${e.localizedMessage}")
                        }
                    }
                }

                override fun onFailure(call: Call<CommonResponse>, t: Throwable) {
                    _isLoading.value = false
                    Log.e(TAG, "onFailure: ${t.printStackTrace()}")
                }
            })
    }

    companion object {
        private val TAG = UploadStoryViewModel::class.java.simpleName
    }
}