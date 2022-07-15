package com.taufik.ceritaku.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.taufik.ceritaku.data.UserPreference
import com.taufik.ceritaku.data.remote.response.auth.login.LoginResult
import kotlinx.coroutines.launch

class MainLocalViewModel(private val pref: UserPreference) : ViewModel() {
    fun logoutUser() = viewModelScope.launch { pref.logoutUser() }
    fun getUser(): LiveData<LoginResult> = pref.getUser().asLiveData()
}