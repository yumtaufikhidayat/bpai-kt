package com.taufik.ceritaku.ui.auth.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.taufik.ceritaku.data.UserPreference
import com.taufik.ceritaku.data.remote.response.auth.login.LoginResult
import kotlinx.coroutines.launch

class LoginLocalViewModel(private val pref: UserPreference) : ViewModel() {
    fun loginUser(loginResult: LoginResult) = viewModelScope.launch { pref.loginUser(loginResult) }
}