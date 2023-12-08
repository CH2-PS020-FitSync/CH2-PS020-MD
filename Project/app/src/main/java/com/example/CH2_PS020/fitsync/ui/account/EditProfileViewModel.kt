package com.example.CH2_PS020.fitsync.ui.account

import androidx.lifecycle.ViewModel
import com.example.CH2_PS020.fitsync.data.FitSyncRepository
import java.io.File

class EditProfileViewModel(private val  repository: FitSyncRepository) : ViewModel() {

    fun updatePhoto(multipartBody: File) = repository.uploadPhoto(multipartBody)
    fun editProfile(name : String, height : String, weight : String, gender: String) = repository.editProfile(name, height, weight, gender)
    fun getMe()  = repository.getMe()

}