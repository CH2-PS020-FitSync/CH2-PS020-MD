package com.example.CH2_PS020.fitsync.ui.home

import androidx.lifecycle.ViewModel
import com.example.CH2_PS020.fitsync.data.FitSyncRepository

class HomeViewModel(private val repository: FitSyncRepository) : ViewModel() {

    fun getMe() = repository.getMe()
}