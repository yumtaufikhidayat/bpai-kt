package com.taufik.ceritaku.data.remote.network

import com.taufik.ceritaku.data.remote.response.auth.login.LoginRequest
import com.taufik.ceritaku.data.remote.response.auth.login.LoginResponse
import com.taufik.ceritaku.data.remote.response.auth.register.RegisterRequest
import com.taufik.ceritaku.data.remote.response.main.AllStoriesResponse
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

    @POST(REGISTER)
    suspend fun registerUser(
        @Body registerRequest: RegisterRequest
    ): CommonResponse

    @POST(LOGIN)
    fun login(
        @Body loginRequest: LoginRequest
    ): Call<LoginResponse>

    @POST(LOGIN)
    suspend fun loginUser(
        @Body loginRequest: LoginRequest
    ): LoginResponse

    @GET(GET_ALL_STORIES)
    fun getAllStories(
        @Header("Authorization") token: String
    ): Call<AllStoriesResponse>

    @GET(GET_ALL_STORIES)
    suspend fun getAllOfStories(
        @Header("Authorization") token: String
    ): AllStoriesResponse

    @Multipart
    @POST(ADD_NEW_STORY)
    fun uploadImage(
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
        @Header("Authorization") token: String
    ): Call<CommonResponse>

    @Multipart
    @POST(ADD_NEW_STORY)
    suspend fun uploadStory(
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
        @Header("Authorization") token: String
    ): CommonResponse
}