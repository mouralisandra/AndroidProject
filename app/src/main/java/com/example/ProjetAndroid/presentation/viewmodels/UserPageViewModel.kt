package com.example.ProjetAndroid.presentation.viewmodels

import android.content.SharedPreferences
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.ProjetAndroid.data.network.api.ApiService.Companion.AUTH_TOKEN
import com.example.ProjetAndroid.domain.usecases.GetUserUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

class UserPageViewModel @AssistedInject constructor(
    private val getUserUseCase: GetUserUseCase,
    private val sharedPreferences: SharedPreferences,
    @Assisted private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val token = sharedPreferences.getString(AUTH_TOKEN, "")

    val user = token?.let { getUserUseCase(it) }

    @AssistedFactory
    interface Factory {
        fun create(savedStateHandle: SavedStateHandle): UserPageViewModel
    }
}