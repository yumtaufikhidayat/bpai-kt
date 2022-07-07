package com.taufik.ceritaku.api

import com.taufik.ceritaku.ui.auth.login.data.LoginRequest
import com.taufik.ceritaku.ui.auth.login.data.LoginResponse
import com.taufik.ceritaku.ui.auth.register.data.RegisterRequest
import com.taufik.ceritaku.ui.main.data.AllStoriesResponse
import com.taufik.ceritaku.utils.common.CommonConstant.ADD_NEW_STORY
import com.taufik.ceritaku.utils.common.CommonConstant.GET_ALL_STORIES
import com.taufik.ceritaku.utils.common.CommonConstant.LOGIN
import com.taufik.ceritaku.utils.common.CommonConstant.REGISTER
import com.taufik.ceritaku.utils.data.CommonResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @POST(REGISTER)
    fun register(
        @Body registerRequest: RegisterRequest
    ): Call<CommonResponse>

    @POST(LOGIN)
    fun login(
        @Body loginRequest: LoginRequest
    ): Call<LoginResponse>

    @GET(GET_ALL_STORIES)
    fun getAllStories(
        @Header("Authorization") token: String
    ): Call<AllStoriesResponse>

    @Multipart
    @POST(ADD_NEW_STORY)
    fun uploadImage(
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
        @Header("Authorization") token: String
    ): Call<CommonResponse>
}