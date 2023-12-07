package com.example.CH2_PS020.fitsync.ui.tracker

import androidx.lifecycle.ViewModel
import com.example.CH2_PS020.fitsync.data.FitSyncRepository

class TrackerViewModel(private val repository: FitSyncRepository) : ViewModel() {
    //TODO LIST OF DATES,LIST OF WEIGHT WITH DATE,LATEST BMI

    fun getUser() = repository.getMe()
    fun getBMIs(
        orderType: String? = null,
        from: String? = null,
        to: String? = null,
        limit: Int? = null,
        offset: Int? = null
    ) = repository.getBMIs(orderType, from, to, limit, offset)

    fun postBMI(
        height: Float,
        weight: Float,
        date: String? = null
    ) = repository.postBMI(height, weight, date)

    fun getLatestBMI() = repository.getMe()
}