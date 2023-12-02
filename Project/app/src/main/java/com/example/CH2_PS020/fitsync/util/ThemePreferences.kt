package com.example.CH2_PS020.fitsync.util

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStoreTheme: DataStore<Preferences> by preferencesDataStore("themes")

class ThemePreferences private constructor(private val dataStore: DataStore<Preferences>) {

    fun getThemeSetting(): Flow<Boolean> {
        return dataStore.data.map { preferences ->
            preferences[THEME_KEY] ?: false
        }
    }

    suspend fun saveThemeSetting(isDarkModeActive: Boolean) {
        dataStore.edit { preferences ->
            preferences[THEME_KEY] = isDarkModeActive
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: ThemePreferences? = null
        private val THEME_KEY = booleanPreferencesKey("theme_settings")


        @OptIn(InternalCoroutinesApi::class)
        fun getInstance(dataStore: DataStore<Preferences>): ThemePreferences {
            return INSTANCE ?: kotlinx.coroutines.internal.synchronized(this) {
                val instance = ThemePreferences(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}