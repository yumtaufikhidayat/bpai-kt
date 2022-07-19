package com.taufik.ceritaku.data.local.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.taufik.ceritaku.utils.common.CommonConstant
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = CommonConstant.TABLE_STORY)
data class StoryEntity (

    @ColumnInfo(name = CommonConstant.COLUMN_ID)
    @PrimaryKey
    val id: String,

    @ColumnInfo(name = CommonConstant.COLUMN_PHOTO_URL)
    val photoUrl: String,

    @ColumnInfo(name = CommonConstant.COLUMN_CREATED_AT)
    val createdAt: String,

    @ColumnInfo(name = CommonConstant.COLUMN_NAME)
    val name: String,

    @ColumnInfo(name = CommonConstant.COLUMN_DESCRIPTION)
    val description: String,

    @ColumnInfo(name = CommonConstant.COLUMN_LON)
    val lon: Double,

    @ColumnInfo(name = CommonConstant.COLUMN_LAT)
    val lat: Double
): Parcelable