package com.taufik.ceritaku.ui.upload

import androidx.lifecycle.ViewModel
import com.taufik.ceritaku.data.CeritakuRepository
import okhttp3.MultipartBody
import okhttp3.RequestBody

class UploadStoryViewModel(private val repository: CeritakuRepository) : ViewModel() {
    fun uploadStory(file: MultipartBody.Part, description: RequestBody, token: String) = repository.uploadStory(file, description, token)
}