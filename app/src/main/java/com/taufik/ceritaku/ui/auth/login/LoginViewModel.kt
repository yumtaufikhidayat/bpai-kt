package com.taufik.ceritaku.ui.auth.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.taufik.ceritaku.api.ApiConfig
import com.taufik.ceritaku.ui.auth.login.data.LoginRequest
import com.taufik.ceritaku.ui.auth.login.data.LoginResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel : ViewModel() {

    private val apiConfig = ApiConfig.apiInstance

    private val _loginResponse = MutableLiveData<LoginResponse>()
    val loginResponse: LiveData<LoginResponse> = _loginResponse

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun login(email: String, password: String) {

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