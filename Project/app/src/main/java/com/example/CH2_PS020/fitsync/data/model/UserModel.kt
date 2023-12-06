package com.example.CH2_PS020.fitsync.data.model

data class UserModel(
    val name: String,
    val email: String,
    val accessToken: String,
    val refreshToken: String,
    val isLogin: Boolean = false
)
