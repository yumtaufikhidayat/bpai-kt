package com.taufik.ceritaku.utils

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.taufik.ceritaku.ui.auth.login.data.LoginResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserPreference private constructor(private val dataStore: DataStore<Preferences>){

    fun getUser(): Flow<LoginResult> {
        return dataStore.data.map {
            LoginResult(
                it[NAME_KEY] ?: "",
                it[USER_ID] ?: "",
                it[TOKEN_KEY] ?: ""
            )
        }
    }

    suspend fun login(loginResult: LoginResult) {
        dataStore.edit { preferences ->
            preferences[NAME_KEY] = loginResult.name
            preferences[TOKEN_KEY] = loginResult.token
        }
    }

    suspend fun logout() {
        dataStore.edit { preferences ->
            preferences.remove(NAME_KEY)
            preferences.remove(TOKEN_KEY)
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: UserPreference? = null

        private val NAME_KEY = stringPreferencesKey("name")
        private val USER_ID = stringPreferencesKey("userId")
        private val TOKEN_KEY = stringPreferencesKey("token")

        fun getInstance(dataStore: DataStore<Preferences>): UserPreference {
            return INSTANCE ?: synchronized(this) {
                val instance = UserPreference(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}