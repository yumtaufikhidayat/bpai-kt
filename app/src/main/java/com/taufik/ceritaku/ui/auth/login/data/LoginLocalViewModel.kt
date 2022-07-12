package com.taufik.ceritaku.ui.auth.login.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.taufik.ceritaku.utils.UserPreference
import kotlinx.coroutines.launch

class LoginLocalViewModel(private val pref: UserPreference): ViewModel() {
    fun login(loginResult: LoginResult) = viewModelScope.launch { pref.login(loginResult) }
}