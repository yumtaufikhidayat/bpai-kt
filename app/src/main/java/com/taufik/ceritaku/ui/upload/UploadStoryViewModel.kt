package com.taufik.ceritaku.ui.upload

import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.taufik.ceritaku.data.CeritakuRepository
import okhttp3.MultipartBody
import okhttp3.RequestBody

class UploadStoryViewModel(private val repository: CeritakuRepository) : ViewModel() {

    private val myLocation = MutableLiveData<Location>()

    fun getMyLocation(): LiveData<Location> = myLocation
    fun setMyLocation(location: Location) { myLocation.postValue(location) }

    fun uploadStory(
        token: String,
        file: MultipartBody.Part,
        description: RequestBody,
        latitude: Float,
        longitude: Float
    ) = repository.uploadStory(token, file, description, latitude, longitude)
}