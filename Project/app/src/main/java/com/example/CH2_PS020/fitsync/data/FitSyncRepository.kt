package com.example.CH2_PS020.fitsync.data

import android.util.Log
import androidx.lifecycle.liveData
import com.example.CH2_PS020.fitsync.api.response.BMIResponse
import com.example.CH2_PS020.fitsync.api.response.ExerciseByIDResponse
import com.example.CH2_PS020.fitsync.api.response.ExercisesResponse
import com.example.CH2_PS020.fitsync.api.response.NutritionResponse
import com.example.CH2_PS020.fitsync.api.response.PostBMIResponse
import com.example.CH2_PS020.fitsync.api.response.UserResponse
import com.example.CH2_PS020.fitsync.api.response.WorkoutsResponse
import com.example.CH2_PS020.fitsync.api.retrofit.ApiService
import com.example.CH2_PS020.fitsync.data.model.UserModel
import com.example.CH2_PS020.fitsync.util.SessionsPreferences
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.HttpException
import retrofit2.http.Field
import retrofit2.http.Query
import java.io.File

class FitSyncRepository constructor(
    val userPreferences: SessionsPreferences,
    val apiService: ApiService
) {

    //dataStore Method's
    suspend fun saveSession(user: UserModel) {
        userPreferences.saveSessions(user)
    }

    fun getSession(): Flow<UserModel> {
        return userPreferences.getSession()
    }

    suspend fun logoutSessions() {
        userPreferences.logout()
    }

    fun register(name: String, email: String, password: String, confirmPassword: String) =
        liveData {
            emit(Result.Loading)
            try {
                val success = apiService.register(name, email, password, confirmPassword)
                emit(Result.Success(success))
            } catch (e: HttpException) {
                val jsonInString = e.response()?.errorBody()?.string()
                val errorBody = Gson().fromJson(jsonInString, UserResponse::class.java)
                val errorMessage = errorBody.message.toString()
                emit(Result.Error(errorMessage))
            }
        }

    fun login(email: String, password: String) = liveData {
        emit(Result.Loading)
        try {
            val success = apiService.login(email, password)
            emit(Result.Success(success))
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, UserResponse::class.java)
            val errorMessage = errorBody.message.toString()
            emit(Result.Error(errorMessage))
        }
    }

    fun registerOtp(userId: String, code: String) = liveData {
        emit(Result.Loading)
        try {
            val success = apiService.registerOtp(userId, code)
            emit(Result.Success(success))
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, UserResponse::class.java)
            val errorMessage = errorBody.message.toString()
            emit(Result.Error(errorMessage))
        }
    }

    fun refreshOtp(userId: String) = liveData {
        emit(Result.Loading)
        try {
            val success = apiService.refreshOtp(userId)
            emit(Result.Success(success))
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, UserResponse::class.java)
            val errorMessage = errorBody.message.toString()
            emit(Result.Error(errorMessage))
        }
    }

    fun logout(authorization: String) = liveData {
        emit(Result.Loading)
        try {
            val success = apiService.logout(authorization)
            emit(Result.Success(success))
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, UserResponse::class.java)
            val errorMessage = errorBody.message.toString()
            emit(Result.Error(errorMessage))
        }
    }

    fun getMe() = liveData {
        emit(Result.Loading)
        try {
            val success = apiService.getMe()
            emit(Result.Success(success))
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, UserResponse::class.java)
            val errorMessage = errorBody.message.toString()
            emit(Result.Error(errorMessage))
        }
    }

    fun getPatchMe(
        authorization: String,
        gender: String,
        birthDate: String,
        level: String,
        goalWeight: String,
        weight: String,
        height: String
    ) = liveData {
        emit(Result.Loading)
        try {
            val success =
                apiService.getPatchedMe(
                    authorization,
                    gender,
                    birthDate,
                    level,
                    goalWeight,
                    height,
                    weight
                )
            emit(Result.Success(success))
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, UserResponse::class.java)
            val errorMessage = errorBody.message.toString()
            emit(Result.Error(errorMessage))
        }
    }

    fun editProfile(name: String, height: String, weight: String, gender: String) = liveData {
        emit(Result.Loading)
        try {
            val success =
                apiService.editProfile(
                    name, height, weight, gender
                )
            emit(Result.Success(success))
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, UserResponse::class.java)
            val errorMessage = errorBody.message.toString()
            emit(Result.Error(errorMessage))
        }
    }

    fun getExercises(
        titleStartsWith: String? = null,
        type: String? = null,
        level: String? = null,
        gender: String? = null,
        limit: Int? = null,
        offset: Int? = null
    ) = liveData {
        emit(Result.Loading)
        try {
            val success =
                apiService.getExercises(titleStartsWith, type, level, gender, limit, offset)
            emit(Result.Success(success))
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, ExercisesResponse::class.java)
            val errorMessage = errorBody.message.toString()
            emit(Result.Error(errorMessage))
        }
    }

    fun getBMIs(
        orderType: String? = null,
        from: String? = null,
        to: String? = null,
        limit: Int? = null,
        offset: Int? = null
    ) = liveData {
        emit(Result.Loading)
        try {
            val success = apiService.getBMIs(orderType, from, to, limit, offset)
            emit(Result.Success(success))
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, BMIResponse::class.java)
            val errorMessage = errorBody.message.toString()
            emit(Result.Error(errorMessage))
        }
    }

    fun postBMI(
        height: Float,
        weight: Float,
        date: String? = null
    ) = liveData {
        emit(Result.Loading)
        try {
            val success = apiService.postBMI(height, weight, date)
            emit(Result.Success(success))
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, PostBMIResponse::class.java)
            val errorMessage = errorBody.message.toString()
            emit(Result.Error(errorMessage))
        }
    }

    fun getEstimatedNutrition() = liveData {
        emit(Result.Loading)
        try {
            val success = apiService.getEstimatedNutrition()
            emit(Result.Success(success))
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, NutritionResponse::class.java)
            val errorMessage = errorBody.message.toString()
            emit(Result.Error(errorMessage))
        }
    }

    fun uploadPhoto(imageFile: File) = liveData {
        emit(Result.Loading)
        try {
            val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
            val multipartBody = MultipartBody.Part.createFormData(
                "photo",
                imageFile.name,
                requestImageFile
            )
            val success = apiService.updatePhoto(multipartBody)
            emit(Result.Success(success))
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, UserResponse::class.java)
            val errorMessage = errorBody.message.toString()
            emit(Result.Error(errorMessage))
        }
    }

    fun recommendedExercise() = liveData {
        emit(Result.Loading)
        try {
            val success = apiService.recommendedExercise()
            emit(Result.Success(success))
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            Log.d("FitSyncRepository", "JSON response: $jsonInString")
            val errorBody = Gson().fromJson(jsonInString, ExercisesResponse::class.java)
            val errorMessage = errorBody.message.toString()
            emit(Result.Error(errorMessage))
        }
    }

    fun getMeWorkouts(
        dateFrom: String? = null,
        dateTo: String? = null,
        ratingFrom: Int? = null,
        ratingTo: Int? = null,
        orderType: String? = null,
        limit: Int? = null,
        offset: Int? = null,
    ) = liveData {
        emit(Result.Loading)
        try {
            val success = apiService.getMeWorkouts(
                dateFrom,
                dateTo,
                ratingFrom,
                ratingTo,
                orderType,
                limit,
                offset
            )
            emit(Result.Success(success))
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            Log.d("FitSyncRepository", "JSON response: $jsonInString")
            val errorBody = Gson().fromJson(jsonInString, WorkoutsResponse::class.java)
            val errorMessage = errorBody.message.toString()
            emit(Result.Error(errorMessage))
        }
    }

    fun postWorkout(
      exerciseId: String,
      rating: Int? = null,
      date: String? = null
    ) = liveData {
        emit(Result.Loading)
        try {
            val success = apiService.postWorkout(exerciseId, rating, date)
            emit(Result.Success(success))
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            Log.d("FitSyncRepository", "JSON response: $jsonInString")
            val errorBody = Gson().fromJson(jsonInString, WorkoutsResponse::class.java)
            val errorMessage = errorBody.message.toString()
            emit(Result.Error(errorMessage))
        }
    }

    fun getExerciseByID(
        exerciseID:String
    ) = liveData{
        emit(Result.Loading)
        try {
            val success = apiService.getExerciseByID(exerciseID)
            emit(Result.Success(success))
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            Log.d("FitSyncRepository", "JSON response: $jsonInString")
            val errorBody = Gson().fromJson(jsonInString, ExerciseByIDResponse::class.java)
            val errorMessage = errorBody.message.toString()
            emit(Result.Error(errorMessage))
        }
    }

}