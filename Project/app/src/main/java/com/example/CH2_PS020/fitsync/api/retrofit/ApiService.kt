package com.example.CH2_PS020.fitsync.api.retrofit

import com.example.CH2_PS020.fitsync.api.response.BMIResponse
import com.example.CH2_PS020.fitsync.api.response.ExercisesResponse
import com.example.CH2_PS020.fitsync.api.response.PostBMIResponse
import com.example.CH2_PS020.fitsync.api.response.UserResponse
import okhttp3.MultipartBody
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Query

interface ApiService {

    @FormUrlEncoded
    @POST("auth/register")
    suspend fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("passwordConfirmation") confirmPassword: String,
    ): UserResponse

    @FormUrlEncoded
    @POST("auth/register/otp")
    suspend fun registerOtp(
        @Field("userId") userId: String,
        @Field("code") code: String
    ): UserResponse

    @FormUrlEncoded
    @POST("auth/login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String,
    ): UserResponse

    @FormUrlEncoded
    @POST("auth/logout")
    suspend fun logout(
        @Field("authorization") authorization: String,
    ): UserResponse

    @FormUrlEncoded
    @POST("auth/refresh-token")
    suspend fun refreshToken(
        @Field("refreshToken") refreshToken: String,
    ): UserResponse

    @FormUrlEncoded
    @POST("auth/forgot-password/request")
    suspend fun forgotPassReq(
        @Field("email") email: String,
    ): UserResponse

    @FormUrlEncoded
    @POST("auth/forgot-password/otp")
    suspend fun forgotPassOtp(
        @Field("userId") userId: String,
        @Field("code") code: String,
    ): UserResponse

    @FormUrlEncoded
    @POST("auth/forgot-password/change")
    suspend fun forgotPassChange(
        @Field("userId") userId: String,
        @Field("passwordConfirmation") passwordConfirmation: String,
        @Field("password") password: String,
    ): UserResponse

    @FormUrlEncoded
    @POST("auth/otp/refresh")
    suspend fun refreshOtp(
        @Field("userId") userId: String,
    ): UserResponse

    @FormUrlEncoded
    @PATCH("me")
    suspend fun getPatchedMe(
        @Header("Authorization") authorization: String,
        @Field("gender") gender: String,
        @Field("birthDate") birthDate: String,
        @Field("level") level: String,
        @Field("goalWeight") goalWeight: String,
        @Field("height") height: String,
        @Field("weight") weight: String
    ): UserResponse

    @FormUrlEncoded
    @PATCH("me")
    suspend fun editProfile(
        @Field("name") name: String,
        @Field("height") height: String,
        @Field("weight") weight: String,
        @Field("gender") gender: String,
    ): UserResponse

    @GET("me")
    suspend fun getMe(): UserResponse

    @GET("exercises")
    suspend fun getExercises(
        @Query("title") titleStartsWith: String? = null,
        @Query("type") type: String? = null,
        @Query("level") level: String? = null,
        @Query("gender") gender: String? = null,
        @Query("limit") limit: Int? = null,
        @Query("offset") offset: Int? = null
    ): ExercisesResponse


    @GET("me/bmis")
    suspend fun getBMIs(
        @Query("orderType") orderType: String? = null,
        @Query("from") from: String? = null,
        @Query("to") to: String? = null,
        @Query("limit") limit: Int? = null,
        @Query("offset") offset: Int? = null
    ): BMIResponse

    @FormUrlEncoded
    @PUT("me/bmis")
    suspend fun postBMI(
        @Field("height") height: Float,
        @Field("weight") weight: Float,
        @Field("date") date: String? = null
    ): PostBMIResponse

    @Multipart
    @PUT("me/photo")
    suspend fun updatePhoto(
        @Part file: MultipartBody.Part
    ): UserResponse

    @GET("me/recommendation/exercises")
    suspend fun recommendedExercise(): ExercisesResponse


}