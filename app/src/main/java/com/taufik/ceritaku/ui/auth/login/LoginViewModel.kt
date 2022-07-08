package com.taufik.ceritaku.ui.auth.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.taufik.ceritaku.api.ApiConfig
import com.taufik.ceritaku.ui.auth.login.data.LoginRequest
import com.taufik.ceritaku.ui.auth.login.data.LoginResponse
import com.taufik.ceritaku.utils.Event
import com.taufik.ceritaku.utils.data.CommonResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel : ViewModel() {

    private val apiConfig = ApiConfig.apiInstance

    private val _loginResponse = MutableLiveData<LoginResponse>()
    val loginResponse: LiveData<LoginResponse> = _loginResponse

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _responseMessage = MutableLiveData<Event<String>>()
    val responseMessage: LiveData<Event<String>> = _responseMessage

    fun loginUser(email: String, password: String) {

        val loginUser = LoginRequest(email, password)

        _isLoading.value = true
        apiConfig.login(loginUser)
            .enqueue(object : Callback<LoginResponse>{
                override fun onResponse(
                    call: Call<LoginResponse>,
                    response: Response<LoginResponse>
                ) {
                    _isLoading.value = false
                    if (response.isSuccessful) {
                        _loginResponse.value = response.body()
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

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    _isLoading.value = false
                    Log.e(TAG, "onFailure: ${t.printStackTrace()}")
                }
            })
    }

    companion object {
        private val TAG = LoginViewModel::class.java.simpleName
    }
}