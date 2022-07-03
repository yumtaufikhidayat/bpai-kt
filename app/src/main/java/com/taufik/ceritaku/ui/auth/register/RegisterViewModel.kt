package com.taufik.ceritaku.ui.auth.register

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.taufik.ceritaku.api.ApiConfig
import com.taufik.ceritaku.ui.auth.register.data.RegisterRequest
import com.taufik.ceritaku.ui.auth.register.data.RegisterResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/*
class RegisterViewModel(private val pref: UserPreference): ViewModel() {
    fun saveUser(user: User) = viewModelScope.launch { pref.saveUser(user) }
}
*/

class RegisterViewModel : ViewModel() {

    private val apiConfig = ApiConfig.apiInstance

    private val _registerResponse = MutableLiveData<RegisterResponse>()
    val registerResponse: LiveData<RegisterResponse> = _registerResponse

    private val _isRegisterStatus = MutableLiveData<Boolean>()
    val isRegisterStatus: LiveData<Boolean> = _isRegisterStatus

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun registerUser(name: String, email: String, password: String) {

        val registerUser = RegisterRequest(name, email, password)

        _isLoading.value = true
        apiConfig.register(registerUser)
            .enqueue(object : Callback<RegisterResponse>{
                override fun onResponse(
                    call: Call<RegisterResponse>,
                    response: Response<RegisterResponse>
                ) {
                    _isLoading.value = false
                    _isRegisterStatus.value = true
                    if (response.isSuccessful) {
                        _registerResponse.value = response.body()
                    }
                }

                override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                    _isLoading.value = false
                    _isRegisterStatus.value = false
                    Log.e(TAG, "onFailure: ${t.printStackTrace()}")
                }
            })
    }

    companion object {
        private val TAG = RegisterViewModel::class.java.simpleName
    }
}