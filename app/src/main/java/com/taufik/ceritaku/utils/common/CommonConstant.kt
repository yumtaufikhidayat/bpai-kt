package com.taufik.ceritaku.utils.common

import com.taufik.ceritaku.BuildConfig

object CommonConstant {
    const val LEFT = -30f
    const val RIGHT = 30f
    const val DURATION = 6000L
    const val DURATION_ALT = 500L
    const val VALUES = 1f

    const val DATE_DD_MMMM_YYYY_FULL_FORMAT = "dd MMMM yyyy - HH:mm"

    const val BASE_URL = BuildConfig.DICODING_BASE_URL
    const val REGISTER = "register"
    const val LOGIN = "login"
    const val ADD_NEW_STORY = "stories"
    const val GET_ALL_STORIES = "stories"

    const val TABLE_STORY = "tbl_story"
    const val COLUMN_ID = "id"
    const val COLUMN_PHOTO_URL = "photo_url"
    const val COLUMN_CREATED_AT = "created_at"
    const val COLUMN_NAME = "name"
    const val COLUMN_DESCRIPTION = "description"
    const val COLUMN_LAT = "lat"
    const val COLUMN_LON = "lon"
    const val DB_VERSION = 1
    const val DB_NAME = "db_story.db"
}