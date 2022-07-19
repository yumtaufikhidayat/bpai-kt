package com.taufik.ceritaku.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.taufik.ceritaku.data.local.entity.StoryEntity

@Dao
interface StoryDao {

    @Query("SELECT * FROM tbl_story")
    fun getFavoriteStory(): LiveData<List<StoryEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertStory(storyEntity: StoryEntity)

    @Query("DELETE FROM tbl_story WHERE tbl_story.id = :id")
    fun removeStory(id: String)

    @Query("SELECT EXISTS(SELECT * FROM tbl_story WHERE tbl_story.id = :id)")
    fun isStoryFavorite(id: String): LiveData<Boolean>
}