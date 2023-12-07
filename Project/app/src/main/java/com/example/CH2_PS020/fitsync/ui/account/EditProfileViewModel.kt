package com.example.CH2_PS020.fitsync.ui.account

import androidx.lifecycle.ViewModel
import com.example.CH2_PS020.fitsync.data.FitSyncRepository
import okhttp3.MultipartBody
import java.io.File

class EditProfileViewModel(private val  repository: FitSyncRepository) : ViewModel() {

    fun updatePhoto(multipartBody: File) = repository.uploadPhoto(multipartBody)
}