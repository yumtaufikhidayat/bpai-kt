package com.taufik.ceritaku.data.remote

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.taufik.ceritaku.data.local.entity.StoryEntity
import com.taufik.ceritaku.data.remote.network.ApiService

class CeritakuPagingSource(private val apiService: ApiService, private val token: String): PagingSource<Int, StoryEntity>() {
    override fun getRefreshKey(state: PagingState<Int, StoryEntity>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, StoryEntity> {
        val page = params.key ?: INITIAL_PAGE_INDEX
        return try {
            val responseData = apiService.getAllOfStories(token, page, params.loadSize)
            val listOfStories = responseData.listStory
            val storyList = listOfStories.map {
                StoryEntity(
                    it.id,
                    it.photoUrl,
                    it.createdAt,
                    it.name,
                    it.description,
                    it.lon,
                    it.lat
                )
            }

            LoadResult.Page(
                data = storyList,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (listOfStories.isEmpty()) null else page + 1
            )
        } catch (e: Exception) {
            return LoadResult.Error(e)
        }
    }

    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }
}