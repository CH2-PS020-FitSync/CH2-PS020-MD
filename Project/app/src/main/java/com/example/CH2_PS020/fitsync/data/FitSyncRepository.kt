package com.example.CH2_PS020.fitsync.data

import androidx.lifecycle.liveData
import com.example.CH2_PS020.fitsync.api.response.UserResponse
import com.example.CH2_PS020.fitsync.api.retrofit.ApiService
import com.example.CH2_PS020.fitsync.data.model.UserModel
import com.example.CH2_PS020.fitsync.util.SessionsPreferences
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import retrofit2.HttpException

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

    suspend fun logout() {
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
}