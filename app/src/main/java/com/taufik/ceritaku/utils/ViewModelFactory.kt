package com.taufik.ceritaku.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.taufik.ceritaku.model.UserPreference
import com.taufik.ceritaku.ui.auth.login.data.LoginLocalViewModel
import com.taufik.ceritaku.ui.auth.register.RegisterLocalViewModel
import com.taufik.ceritaku.ui.main.MainViewModel

class ViewModelFactory(private val pref: UserPreference): ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(MainViewModel::class.java) -> MainViewModel(pref) as T
            modelClass.isAssignableFrom(RegisterLocalViewModel::class.java) -> RegisterLocalViewModel(pref) as T
            modelClass.isAssignableFrom(LoginLocalViewModel::class.java) -> LoginLocalViewModel(pref) as T
            else -> throw IllegalAccessException("Unknown ViewModel class: " + modelClass.name)
        }
    }
}