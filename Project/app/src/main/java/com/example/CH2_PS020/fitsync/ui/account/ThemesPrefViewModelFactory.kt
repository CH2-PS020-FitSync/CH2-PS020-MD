package com.example.CH2_PS020.fitsync.ui.account

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.CH2_PS020.fitsync.util.ThemePreferences

@Suppress("UNCHECKED_CAST")
class ThemesPrefViewModelFactory(private val pref: ThemePreferences) :
    ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ThemesPrefViewModel::class.java)) {
            return ThemesPrefViewModel(pref) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }
}