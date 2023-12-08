package com.example.CH2_PS020.fitsync.ui.account

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.CH2_PS020.fitsync.api.response.UserResponse
import com.example.CH2_PS020.fitsync.data.FitSyncRepository
import com.example.CH2_PS020.fitsync.data.Result
import com.example.CH2_PS020.fitsync.data.model.UserModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AccountViewModel(private val repository: FitSyncRepository) : ViewModel() {

    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }

    fun logout(authorization: String) = repository.logout(authorization)

    fun deleteSessions() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                repository.logoutSessions()
            }
        }
    }

    fun getMe() = repository.getMe()
}