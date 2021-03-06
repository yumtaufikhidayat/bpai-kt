package com.taufik.ceritaku.ui.main

import androidx.lifecycle.ViewModel
import com.taufik.ceritaku.data.CeritakuRepository

class MainViewModel(private val repository: CeritakuRepository): ViewModel() {
    fun getListOfStories(token: String) = repository.getListOfStories(token)
}