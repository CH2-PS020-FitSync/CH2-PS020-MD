package com.example.CH2_PS020.fitsync.api.response

import com.google.gson.annotations.SerializedName


data class BMIResponse(

	@field:SerializedName("bmis")
	val bmis: List<BmisItem?>? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class BmisItem(

	@field:SerializedName("date")
	val date: String? = null,

	@field:SerializedName("createdAt")
	val createdAt: String? = null,

	@field:SerializedName("weight")
	val weight: Float? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("height")
	val height: Float? = null,

	@field:SerializedName("updatedAt")
	val updatedAt: String? = null
)
