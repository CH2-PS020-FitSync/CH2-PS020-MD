package com.example.CH2_PS020.fitsync.ui.historyWorkout

import androidx.lifecycle.ViewModel
import com.example.CH2_PS020.fitsync.data.FitSyncRepository

class HistoryViewModel(private val repository: FitSyncRepository) : ViewModel() {

    fun getExerciseByID(exerciseID: String) = repository.getExerciseByID(exerciseID)
    fun getWorkoutHistory(
        dateFrom: String? = null,
        dateTo: String? = null,
        ratingFrom: Int? = null,
        ratingTo: Int? = null,
        orderType: String? = null,
        limit: Int? = null,
        offset: Int? = null,
    ) = repository.getMeWorkouts(dateFrom, dateTo, ratingFrom, ratingTo, orderType, limit, offset)
}