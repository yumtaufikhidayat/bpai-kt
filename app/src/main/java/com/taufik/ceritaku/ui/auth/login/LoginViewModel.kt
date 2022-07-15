package com.taufik.ceritaku.ui.auth.login

import androidx.lifecycle.ViewModel
import com.taufik.ceritaku.data.CeritakuRepository

class LoginViewModel(private val repository: CeritakuRepository) : ViewModel() {
    fun loginUser(email: String, password: String) = repository.loginUser(email, password)
}