package com.example.ProjetAndroid.ViewModel.viewmodels

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.ProjetAndroid.Model.DTOs.queries.GetUser
import javax.inject.Inject

class UserViewModelFactory @Inject constructor(
    private val getUser: GetUser,
    private val sharedPreferences: SharedPreferences
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass == UserViewModel::class.java) {
            return UserViewModel(getUser, sharedPreferences) as T
        }
        throw RuntimeException("Wrong view model class $modelClass")
    }
}