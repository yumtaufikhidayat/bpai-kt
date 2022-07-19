package com.taufik.ceritaku.ui.detail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import com.taufik.ceritaku.data.CeritakuRepository
import com.taufik.ceritaku.data.local.entity.StoryEntity
import kotlinx.coroutines.launch

class DetailViewModel(private val repository: CeritakuRepository) : ViewModel() {
    private val storyData = MutableLiveData<StoryEntity>()

    fun setStoryData(storyEntity: StoryEntity) {
        storyData.value = storyEntity
    }

    val favoriteStatus = storyData.switchMap {
        repository.isStoryFavorite(it.id)
    }

    fun changeFavorite(storyEntity: StoryEntity) {
        viewModelScope.launch {
            if (favoriteStatus.value as Boolean) {
                repository.removeStory(storyEntity.id)
            } else {
                repository.insertStory(storyEntity)
            }
        }
    }
}