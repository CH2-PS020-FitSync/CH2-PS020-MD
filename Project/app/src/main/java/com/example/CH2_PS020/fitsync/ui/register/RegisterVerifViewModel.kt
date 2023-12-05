package com.example.CH2_PS020.fitsync.ui.register

import androidx.lifecycle.ViewModel
import com.example.CH2_PS020.fitsync.data.FitSyncRepository

class RegisterVerifViewModel(private val repository: FitSyncRepository) : ViewModel() {

    fun registerOtp(userId: String, code: String) = repository.registerOtp(userId, code)

    fun refreshOtp(userId: String) = repository.refreshOtp(userId)


}