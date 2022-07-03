package com.taufik.ceritaku.api

import com.taufik.ceritaku.ui.auth.login.data.LoginRequest
import com.taufik.ceritaku.ui.auth.login.data.LoginResponse
import com.taufik.ceritaku.ui.auth.register.data.RegisterRequest
import com.taufik.ceritaku.ui.auth.register.data.RegisterResponse
import com.taufik.ceritaku.utils.CommonConstant.LOGIN
import com.taufik.ceritaku.utils.CommonConstant.REGISTER
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {

    @POST(REGISTER)
    fun register(
        @Body registerRequest: RegisterRequest
    ): Call<RegisterResponse>

    @POST(LOGIN)
    fun login(
        @Body loginRequest: LoginRequest
    ): Call<LoginResponse>
}