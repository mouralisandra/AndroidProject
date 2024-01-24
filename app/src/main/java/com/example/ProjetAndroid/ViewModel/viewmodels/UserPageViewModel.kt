package com.example.ProjetAndroid.ViewModel.viewmodels

import android.content.SharedPreferences
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.ProjetAndroid.Model.api.ApiService.Companion.AUTH_TOKEN
import com.example.ProjetAndroid.Model.DTOs.queries.GetUser
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

class UserPageViewModel @AssistedInject constructor(
    private val getUser: GetUser,
    private val sharedPreferences: SharedPreferences,
) : ViewModel() {

    private val token = sharedPreferences.getString(AUTH_TOKEN, "")

    val user = token?.let { getUser(it) }

    @AssistedFactory
    interface Factory {
        fun create(savedStateHandle: SavedStateHandle): UserPageViewModel
    }
}