package com.example.CH2_PS020.fitsync.ui.login

import androidx.lifecycle.ViewModel
import com.example.CH2_PS020.fitsync.data.FitSyncRepository
import com.example.CH2_PS020.fitsync.data.model.UserModel

class LoginViewModel(private val repository: FitSyncRepository) : ViewModel() {

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
}