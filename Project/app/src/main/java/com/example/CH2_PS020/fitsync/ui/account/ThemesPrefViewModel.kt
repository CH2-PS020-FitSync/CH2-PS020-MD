package com.example.CH2_PS020.fitsync.ui.account

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.CH2_PS020.fitsync.util.ThemePreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ThemesPrefViewModel(private val pref: ThemePreferences) : ViewModel() {

    fun getThemeSettings(): LiveData<Boolean> {
        return pref.getThemeSetting().asLiveData()
    }

    fun saveThemeSetting(isDarkModeActive: Boolean) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                pref.saveThemeSetting(isDarkModeActive)
            }
        }
    }
}