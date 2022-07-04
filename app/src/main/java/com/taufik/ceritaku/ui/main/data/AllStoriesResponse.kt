package com.taufik.ceritaku.ui.main.data

import com.google.gson.annotations.SerializedName

data class AllStoriesResponse(

	@SerializedName("listStory")
	val listStory: List<ListStoryItem>,

	@SerializedName("error")
	val error: Boolean,

	@SerializedName("message")
	val message: String
)