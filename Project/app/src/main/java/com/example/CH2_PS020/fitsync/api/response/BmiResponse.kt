package com.example.CH2_PS020.fitsync.api.response

import com.google.gson.annotations.SerializedName

data class BmiResponse(

	@field:SerializedName("bmis")
	val bmis: List<BmisItem?>? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class BmisItem(

	@field:SerializedName("createdAt")
	val createdAt: String? = null,

	@field:SerializedName("weight")
	val weight: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("height")
	val height: String? = null,

	@field:SerializedName("updatedAt")
	val updatedAt: String? = null
)
