package com.example.CH2_PS020.fitsync.ui.login

import androidx.lifecycle.ViewModel
import com.example.CH2_PS020.fitsync.data.FitSyncRepository
import com.example.CH2_PS020.fitsync.data.model.UserModel

class LoginViewModel(private val repository: FitSyncRepository) : ViewModel() {

    fun login(email: String, password: String) = repository.login(email, password)

    suspend fun saveSessions(user: UserModel) {
        return repository.saveSession(user)
    }
}