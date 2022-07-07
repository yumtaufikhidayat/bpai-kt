package com.taufik.ceritaku.ui.auth.register

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.taufik.ceritaku.api.ApiConfig
import com.taufik.ceritaku.ui.auth.register.data.RegisterRequest
import com.taufik.ceritaku.utils.data.CommonResponse
import com.taufik.ceritaku.utils.Event
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class RegisterViewModel : ViewModel() {

    private val apiConfig = ApiConfig.apiInstance

    private val _registerResponse = MutableLiveData<CommonResponse>()
    val registerResponse: LiveData<CommonResponse> = _registerResponse

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _responseMessage = MutableLiveData<Event<String>>()
    val responseMessage: LiveData<Event<String>> = _responseMessage

    fun registerUser(name: String, email: String, password: String) {

        val registerUser = RegisterRequest(name, email, password)

        _isLoading.value = true
        apiConfig.register(registerUser)
            .enqueue(object : Callback<CommonResponse>{
                override fun onResponse(
                    call: Call<CommonResponse>,
                    response: Response<CommonResponse>
                ) {
                    _isLoading.value = false
                    if (response.isSuccessful) {
                        _registerResponse.value = response.body()
                        _responseMessage.value = Event(response.body()?.message.toString())
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
        private val TAG = RegisterViewModel::class.java.simpleName
    }
}