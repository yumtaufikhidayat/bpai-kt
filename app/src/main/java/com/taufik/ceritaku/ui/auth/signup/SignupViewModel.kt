package com.taufik.ceritaku.ui.auth.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.taufik.ceritaku.model.User
import com.taufik.ceritaku.model.UserPreference
import kotlinx.coroutines.launch

class SignupViewModel(private val pref: UserPreference): ViewModel() {
    fun saveUser(user: User) = viewModelScope.launch { pref.saveUser(user) }
}