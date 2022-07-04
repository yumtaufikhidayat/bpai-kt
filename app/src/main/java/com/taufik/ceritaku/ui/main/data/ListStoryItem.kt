package com.taufik.ceritaku.ui.main.data

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class ListStoryItem(

	@SerializedName("photoUrl")
	val photoUrl: String,

	@SerializedName("createdAt")
	val createdAt: String,

	@SerializedName("name")
	val name: String,

	@SerializedName("description")
	val description: String,

	@SerializedName("lon")
	val lon: Double,

	@SerializedName("id")
	val id: String,

	@SerializedName("lat")
	val lat: Double
): Parcelable