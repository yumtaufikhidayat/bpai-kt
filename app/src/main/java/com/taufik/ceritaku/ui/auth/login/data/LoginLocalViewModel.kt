package com.taufik.ceritaku.ui.auth.login.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.taufik.ceritaku.model.User
import com.taufik.ceritaku.model.UserPreference
import kotlinx.coroutines.launch

class LoginLocalViewModel(private val pref: UserPreference): ViewModel() {
    fun getUser(): LiveData<User> = pref.getUser().asLiveData()
    fun login(loginResult: LoginResult) = viewModelScope.launch { pref.login(loginResult) }
}