package com.example.CH2_PS020.fitsync.ui.login

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.CH2_PS020.fitsync.data.FitSyncRepository
import com.example.CH2_PS020.fitsync.data.model.UserModel

class LoginViewModel(private val repository: FitSyncRepository, private val context: Context) : ViewModel() {

    fun login(email: String, password: String) = repository.login(email, password)

    fun getPatchedMe(
        authorization : String,
        gender: String,
        birthDate: String,
        level: String,
        goalWeight: String,
        height: String,
        weight: String
    ) = repository.getPatchMe(authorization, gender, birthDate, level, goalWeight, weight, height)

    suspend fun saveSessions(user: UserModel) {
        return repository.saveSession(user)
    }

    private val loginPref = context.getSharedPreferences("loginPref", Context.MODE_PRIVATE)

    fun isFirstTimeLogin(): Boolean {
        return loginPref.getBoolean("first_time_login", true)
    }

    fun setFirstTimeLogin(isFirstTime: Boolean) {
        loginPref.edit().putBoolean("first_time_login", isFirstTime).apply()
    }
}