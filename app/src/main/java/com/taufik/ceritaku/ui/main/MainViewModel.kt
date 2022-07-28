package com.taufik.ceritaku.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.CombinedLoadStates
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.taufik.ceritaku.data.CeritakuRepository
import com.taufik.ceritaku.data.local.entity.StoryEntity

class MainViewModel(private val repository: CeritakuRepository): ViewModel() {
    fun getListOfStories(token: String): LiveData<PagingData<StoryEntity>> = repository.getListOfStories(token).cachedIn(viewModelScope)
    fun dataResult(combinedLoadStates: CombinedLoadStates) = repository.dataResult(combinedLoadStates).asLiveData()
    fun getLocation(token: String) = repository.getLocations(token)
}