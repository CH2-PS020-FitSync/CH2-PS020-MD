package com.example.CH2_PS020.fitsync.ui.register

import androidx.lifecycle.ViewModel
import com.example.CH2_PS020.fitsync.data.FitSyncRepository

class RegisterViewModel(private val repository: FitSyncRepository) : ViewModel() {

    fun register(name: String, email: String, password: String, confirmPassword: String) =
        repository.register(name, email, password, confirmPassword)

}