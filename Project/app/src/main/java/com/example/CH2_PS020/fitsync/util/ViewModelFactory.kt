package com.example.CH2_PS020.fitsync.util

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.CH2_PS020.fitsync.MainViewModel
import com.example.CH2_PS020.fitsync.data.FitSyncRepository
import com.example.CH2_PS020.fitsync.di.Injection
import com.example.CH2_PS020.fitsync.ui.account.AccountViewModel
import com.example.CH2_PS020.fitsync.ui.login.LoginViewModel
import com.example.CH2_PS020.fitsync.ui.register.RegisterVerifViewModel
import com.example.CH2_PS020.fitsync.ui.register.RegisterViewModel
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.internal.synchronized

@Suppress("UNCHECKED_CAST")
class ViewModelFactory(private val repository: FitSyncRepository) :
    ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(RegisterViewModel::class.java) -> {
                RegisterViewModel(repository) as T
            }

            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(repository) as T
            }

            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(repository) as T
            }

            modelClass.isAssignableFrom(RegisterVerifViewModel::class.java) -> {
                RegisterVerifViewModel(repository) as T
            }

            modelClass.isAssignableFrom(AccountViewModel::class.java) -> {
                AccountViewModel(repository) as T
            }

            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: ViewModelFactory? = null

        @OptIn(InternalCoroutinesApi::class)
        @JvmStatic
        fun getInstance(context: Context, refresh: Boolean): ViewModelFactory {
            if (INSTANCE == null || refresh) {
                synchronized(ViewModelFactory::class.java) {
                    INSTANCE = ViewModelFactory(Injection.provideRepository(context))
                }
            }
            return INSTANCE as ViewModelFactory
        }
    }
}