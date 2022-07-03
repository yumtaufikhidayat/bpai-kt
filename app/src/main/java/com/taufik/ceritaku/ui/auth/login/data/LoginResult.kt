package com.taufik.ceritaku.ui.auth.login.data

import com.google.gson.annotations.SerializedName

data class LoginResult(

    @SerializedName("name")
    val name: String,

    @SerializedName("userId")
    val userId: String,

    @SerializedName("token")
    val token: String
)