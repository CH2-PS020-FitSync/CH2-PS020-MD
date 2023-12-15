package com.example.CH2_PS020.fitsync.api.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

data class ExerciseByIDResponse(

	@field:SerializedName("exercise")
	val exercise: ExercisesItem? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)

