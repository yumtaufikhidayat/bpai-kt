package com.taufik.ceritaku.ui.auth.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.taufik.ceritaku.model.User
import com.taufik.ceritaku.model.UserPreference

class RegisterLocalViewModel(private val pref: UserPreference) : ViewModel() {
    fun getUser(): LiveData<User> = pref.getUser().asLiveData()
}