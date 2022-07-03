package com.taufik.ceritaku.ui.auth.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.taufik.ceritaku.model.User
import com.taufik.ceritaku.model.UserPreference
import kotlinx.coroutines.launch

class RegisterLocalViewModel(private val pref: UserPreference) : ViewModel() {
    fun saveUser(user: User) {
        viewModelScope.launch { pref.saveUser(user) }
    }
}