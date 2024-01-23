package com.example.ProjetAndroid.presentation.viewmodels

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.ProjetAndroid.domain.usecases.GetUserUseCase
import javax.inject.Inject

class UserViewModelFactory @Inject constructor(
    private val getUserUseCase: GetUserUseCase,
    private val sharedPreferences: SharedPreferences
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass == UserViewModel::class.java) {
            return UserViewModel(getUserUseCase, sharedPreferences) as T
        }
        throw RuntimeException("Wrong view model class $modelClass")
    }
}