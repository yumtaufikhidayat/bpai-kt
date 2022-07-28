package com.taufik.ceritaku.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.taufik.ceritaku.data.CeritakuUserPreference
import com.taufik.ceritaku.ui.auth.login.LoginLocalViewModel
import com.taufik.ceritaku.ui.main.MainLocalViewModel

class LocalViewModelFactory(private val pref: CeritakuUserPreference) :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(MainLocalViewModel::class.java) -> MainLocalViewModel(pref) as T
            modelClass.isAssignableFrom(LoginLocalViewModel::class.java) -> LoginLocalViewModel(pref) as T
            else -> throw IllegalArgumentException("Unknown ViewModel Class: ${modelClass.name}")
        }
    }
}