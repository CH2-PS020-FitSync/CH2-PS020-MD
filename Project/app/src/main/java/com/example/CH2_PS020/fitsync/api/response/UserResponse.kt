package com.example.CH2_PS020.fitsync.api.response

import com.google.gson.annotations.SerializedName

data class UserResponse(

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("user")
	val user: User? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class User(

	@field:SerializedName("gender")
	val gender: String? = null,

	@field:SerializedName("isVerified")
	val isVerified: String? = null,

	@field:SerializedName("level")
	val level: String? = null,

	@field:SerializedName("goalWeight")
	val goalWeight: String? = null,

	@field:SerializedName("accessToken")
	val accessToken: String? = null,

	@field:SerializedName("birthDate")
	val birthDate: String? = null,

	@field:SerializedName("latestBMI")
	val latestBMI: LatestBMI? = null,

	@field:SerializedName("photoUrl")
	val photoUrl: String? = null,

	@field:SerializedName("createdAt")
	val createdAt: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("email")
	val email: String? = null,

	@field:SerializedName("updatedAt")
	val updatedAt: String? = null,

	@field:SerializedName("refreshToken")
	val refreshToken: String? = null
)

data class LatestBMI(

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
