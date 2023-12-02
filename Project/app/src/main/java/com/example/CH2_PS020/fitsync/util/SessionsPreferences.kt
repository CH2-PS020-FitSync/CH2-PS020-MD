package com.example.CH2_PS020.fitsync.util

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.CH2_PS020.fitsync.data.model.UserModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStoreSessions: DataStore<Preferences> by preferencesDataStore(name = "session")

class SessionsPreferences private constructor(private val dataStore: DataStore<Preferences>) {

    suspend fun saveSessions(user: UserModel) {
        dataStore.edit { pref ->
            pref[EMAIL_KEY] = user.email
            pref[NAME_KEY] = user.name
            pref[ACCESS_TOKEN_KEY] = user.accessToken
            pref[REFRESH_TOKEN_KEY] = user.refreshToken
            pref[IS_LOGIN_KEY] = true
        }
    }

    fun getSession(): Flow<UserModel> {
        return dataStore.data.map { pref ->
            UserModel(
                pref[EMAIL_KEY] ?: "",
                pref[NAME_KEY] ?: "",
                pref[ACCESS_TOKEN_KEY] ?: "",
                pref[REFRESH_TOKEN_KEY] ?: "",
                pref[IS_LOGIN_KEY] ?: false
            )
        }
    }

    suspend fun logout() {
        dataStore.edit { pref ->
            pref.clear()
        }
    }

    companion object {
        private var INSTANCE: SessionsPreferences? = null

        private val EMAIL_KEY = stringPreferencesKey("email")
        private val NAME_KEY = stringPreferencesKey("name")
        val ACCESS_TOKEN_KEY = stringPreferencesKey("accessToken")
        val REFRESH_TOKEN_KEY = stringPreferencesKey("refreshToken")
        private val IS_LOGIN_KEY = booleanPreferencesKey("isLogin")

        fun getInstance(dataStore: DataStore<Preferences>): SessionsPreferences {
            return INSTANCE ?: synchronized(this) {
                val instance = SessionsPreferences(dataStore)
                INSTANCE = instance
                instance
            }
        }


    }
}