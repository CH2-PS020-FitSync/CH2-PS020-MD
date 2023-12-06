package com.example.CH2_PS020.fitsync.di

import android.content.Context
import com.example.CH2_PS020.fitsync.api.retrofit.ApiConfig
import com.example.CH2_PS020.fitsync.data.FitSyncRepository
import com.example.CH2_PS020.fitsync.util.SessionsPreferences
import com.example.CH2_PS020.fitsync.util.dataStoreSessions
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object Injection {

    fun provideRepository(context: Context): FitSyncRepository = runBlocking {
        val pref = SessionsPreferences.getInstance(context.dataStoreSessions)
        val user = runBlocking { pref.getSession().first() }
        val apiService = if (user.accessToken != "")
            ApiConfig.getApiService(user.accessToken) else ApiConfig.getApiService(user.refreshToken)
        FitSyncRepository(pref, apiService)
    }
}