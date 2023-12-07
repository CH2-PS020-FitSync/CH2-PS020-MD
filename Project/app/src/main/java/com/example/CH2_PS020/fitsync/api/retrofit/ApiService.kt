package com.example.CH2_PS020.fitsync.api.retrofit

import com.example.CH2_PS020.fitsync.api.response.BmiResponse
import com.example.CH2_PS020.fitsync.api.response.ExercisesResponse
import com.example.CH2_PS020.fitsync.api.response.UserResponse
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
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
        @Header("authorization") authorization: String,
        @Field("gender") gender: String,
        @Field("birthDate") birthDate: String,
        @Field("level") level: String,
        @Field("goalWeight") goalWeight: String,
        @Field("height") height: String,
        @Field("weight") weight: String
    ): UserResponse

    @GET("me")
    suspend fun getMe(): UserResponse

    @GET("exercises")
    suspend fun getExercises(
        @Query("titleStartsWith") titleStartsWith: String? = null,
        @Query("type") type: String? = null,
        @Query("level") level: String? = null,
        @Query("gender") gender: String? = null,
        @Query("limit") limit: Int? = null,
        @Query("offset") offset: Int? = null
    ): ExercisesResponse

    @FormUrlEncoded
    @GET("me/bmis")
    suspend fun getBMIs(
        @Field("orderType") orderType: String? = null,
        @Field("from") from: String? = null,
        @Field("to") to: String? = null,
        @Field("limit") limit: Int? = null,
        @Field("offset") offset: Int? = null
    ): BmiResponse

    @FormUrlEncoded
    @POST("me/bmis")
    suspend fun postBMI(
        @Field("height") height: Float,
        @Field("weight") weight: Float,
        @Field("date") date: String? = null
    ): BmiResponse


}