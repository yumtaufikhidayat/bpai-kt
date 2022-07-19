package com.taufik.ceritaku.ui.favorite

import androidx.lifecycle.ViewModel
import com.taufik.ceritaku.data.CeritakuRepository

class FavoriteViewModel(private val repository: CeritakuRepository) : ViewModel() {
    fun getFavoriteStory() = repository.getFavoriteStory()
}