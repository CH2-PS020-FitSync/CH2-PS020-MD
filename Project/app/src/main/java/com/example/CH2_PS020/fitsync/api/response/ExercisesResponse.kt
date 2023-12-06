package com.example.CH2_PS020.fitsync.api.response

import com.google.gson.annotations.SerializedName

data class ExercisesResponse(

	@field:SerializedName("exercises")
	val exercises: List<ExercisesItem?>? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class Duration(

	@field:SerializedName("min")
	val min: String? = null,

	@field:SerializedName("desc")
	val desc: String? = null,

	@field:SerializedName("set")
	val set: String? = null,

	@field:SerializedName("rep")
	val rep: String? = null,

	@field:SerializedName("sec")
	val sec: String? = null
)

data class ExercisesItem(

	@field:SerializedName("duration")
	val duration: Duration? = null,

	@field:SerializedName("jpg")
	val jpg: String? = null,

	@field:SerializedName("gender")
	val gender: String? = null,

	@field:SerializedName("level")
	val level: String? = null,

	@field:SerializedName("gif")
	val gif: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("title")
	val title: String? = null,

	@field:SerializedName("type")
	val type: String? = null,

	@field:SerializedName("bodyPart")
	val bodyPart: String? = null,

	@field:SerializedName("desc")
	val desc: String? = null
)
