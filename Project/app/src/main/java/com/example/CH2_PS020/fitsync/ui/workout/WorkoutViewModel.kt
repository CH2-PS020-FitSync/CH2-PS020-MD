package com.example.CH2_PS020.fitsync.ui.workout

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import com.example.CH2_PS020.fitsync.api.response.ExercisesResponse
import com.example.CH2_PS020.fitsync.data.FitSyncRepository
import com.example.CH2_PS020.fitsync.data.Result
import retrofit2.Response

class WorkoutViewModel(private val repository: FitSyncRepository) : ViewModel() {
    fun getExercises(
        titleStartsWith: String? = null,
        type: String? = null,
        level: String? = null,
        gender: String? = null,
        limit: Int? = null,
        offset: Int? = null
    )= repository.getExercises(titleStartsWith, type, level, gender, limit, offset)
}