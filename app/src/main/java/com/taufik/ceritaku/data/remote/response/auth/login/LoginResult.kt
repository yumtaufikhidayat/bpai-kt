package com.taufik.ceritaku.data.remote.response.auth.login

import com.google.gson.annotations.SerializedName

data class LoginResult(

    @SerializedName("name")
    val name: String,

    @SerializedName("userId")
    val userId: String,

    @SerializedName("token")
    val token: String
)