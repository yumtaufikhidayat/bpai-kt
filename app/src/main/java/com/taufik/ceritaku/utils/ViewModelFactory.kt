package com.taufik.ceritaku.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.taufik.ceritaku.model.UserPreference
import com.taufik.ceritaku.ui.auth.login.LoginViewModel
import com.taufik.ceritaku.ui.main.MainViewModel
import com.taufik.ceritaku.ui.auth.signup.SignupViewModel

class ViewModelFactory(private val pref: UserPreference): ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(MainViewModel::class.java) -> MainViewModel(pref) as T
            modelClass.isAssignableFrom(SignupViewModel::class.java) -> SignupViewModel(pref) as T
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> LoginViewModel(pref) as T
            else -> throw IllegalAccessException("Unknown ViewModel class: " + modelClass.name)
        }
    }
}