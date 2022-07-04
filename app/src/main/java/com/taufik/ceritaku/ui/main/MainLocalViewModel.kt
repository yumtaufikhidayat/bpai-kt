package com.taufik.ceritaku.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.taufik.ceritaku.model.User
import com.taufik.ceritaku.model.UserPreference
import com.taufik.ceritaku.ui.auth.login.data.LoginResult
import kotlinx.coroutines.launch

class MainLocalViewModel(private val pref: UserPreference): ViewModel() {
    fun getUser(): LiveData<User> = pref.getUser().asLiveData()
    fun logout() = viewModelScope.launch { pref.logout() }
    fun getToken(): LiveData<LoginResult> = pref.getToken().asLiveData()
}