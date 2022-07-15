package com.taufik.ceritaku.ui.auth.register

import androidx.lifecycle.ViewModel
import com.taufik.ceritaku.data.CeritakuRepository


class RegisterViewModel(private val repository: CeritakuRepository) : ViewModel() {
    fun registerUser(name: String, email: String, password: String) = repository.registerUser(name, email, password)
}