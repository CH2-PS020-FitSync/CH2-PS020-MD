package com.example.CH2_PS020.fitsync.api.response

import com.google.gson.annotations.SerializedName

data class WorkoutsResponse(

	@field:SerializedName("workouts")
	val workouts: List<WorkoutsItem?>? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class WorkoutsItem(

	@field:SerializedName("createdAt")
	val createdAt: String? = null,

	@field:SerializedName("exerciseId")
	val exerciseId: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("updatedAt")
	val updatedAt: String? = null
)
