package com.example.CH2_PS020.fitsync.api.response

import com.google.gson.annotations.SerializedName

data class NutritionResponse(

	@field:SerializedName("nutrition")
	val nutrition: Nutrition? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class Nutrition(

	@field:SerializedName("estimatedCarbohydrates")
	val estimatedCarbohydrates: Float? = null,

	@field:SerializedName("estimatedCalories")
	val estimatedCalories: Float? = null,

	@field:SerializedName("estimatedFat")
	val estimatedFat: Float? = null,

	@field:SerializedName("estimatedProteinMean")
	val estimatedProteinMean: Float? = null
)
