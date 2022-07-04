package com.taufik.ceritaku.ui.main.data

import com.google.gson.annotations.SerializedName

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
)