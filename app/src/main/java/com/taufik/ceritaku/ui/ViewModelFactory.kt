package com.taufik.ceritaku.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.taufik.ceritaku.data.CeritakuRepository
import com.taufik.ceritaku.di.Injection
import com.taufik.ceritaku.ui.auth.login.LoginViewModel
import com.taufik.ceritaku.ui.auth.register.RegisterViewModel
import com.taufik.ceritaku.ui.main.MainViewModel
import com.taufik.ceritaku.ui.upload.UploadStoryViewModel

class ViewModelFactory private constructor(private val ceritakuRepository: CeritakuRepository) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(RegisterViewModel::class.java) -> RegisterViewModel(ceritakuRepository) as T
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> LoginViewModel(ceritakuRepository) as T
            modelClass.isAssignableFrom(MainViewModel::class.java) -> MainViewModel(ceritakuRepository) as T
            modelClass.isAssignableFrom(UploadStoryViewModel::class.java) -> UploadStoryViewModel(ceritakuRepository) as T
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null
        fun getInstance(context: Context, ): ViewModelFactory = instance ?: synchronized(this) {
                instance ?: ViewModelFactory(Injection.provideRepository(context))
            }.also { instance = it }
    }
}