package com.taufik.ceritaku.data.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.taufik.ceritaku.data.local.entity.StoryEntity
import com.taufik.ceritaku.utils.common.CommonConstant

@Database(
    entities = [StoryEntity::class],
    version = CommonConstant.DB_VERSION,
    exportSchema = false
)
abstract class StoryDatabase : RoomDatabase() {
    abstract fun storyDao(): StoryDao

    companion object {
        @Volatile
        private var instance: StoryDatabase? = null
        fun getInstance(context: Context): StoryDatabase =
            instance ?: Room.databaseBuilder(
                context.applicationContext,
                StoryDatabase::class.java,
                CommonConstant.DB_NAME
            ).fallbackToDestructiveMigration().build()
    }
}