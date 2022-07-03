package com.taufik.ceritaku.ui.auth.login.data

import com.google.gson.annotations.SerializedName

data class LoginResponse(

	@SerializedName("loginResult")
	val loginResult: LoginResult,

	@SerializedName("error")
	val error: Boolean,

	@SerializedName("message")
	val message: String
)
