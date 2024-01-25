package com.example.ProjetAndroid.ViewModel.viewmodels

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ProjetAndroid.Model.api.ApiService.Companion.AUTH_TOKEN
import com.example.ProjetAndroid.Model.DTOs.User
import com.example.ProjetAndroid.Model.DTOs.queries.GetUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

class UserViewModel @Inject constructor(
    private val getUser: GetUser,
    private val sharedPreferences: SharedPreferences
) : ViewModel() {

    private val _loginState: MutableStateFlow<Content> = MutableStateFlow(Content())
    val loginState = _loginState.asStateFlow()

    private val editor = sharedPreferences.edit()

    init {
        sharedPreferences.getString(AUTH_TOKEN, "")?.let { login(it) }
    }

    fun login(token: String) {
        editor.apply {
            putString(AUTH_TOKEN, token)
            apply()
        }
        _loginState.update { it.copy(state = LoginState.Logging) }
        viewModelScope.launch {
            val user: User? = getUser(token).last()
            if (user != null) {
                _loginState.update { Content(user, LoginState.Logged) }
            } else {
                _loginState.update { it.copy(state = LoginState.Error) }
            }
        }
    }

    fun logout() {
        editor.apply {
            remove(AUTH_TOKEN)
            apply()
        }
        _loginState.update { Content(null, LoginState.NotLogged) }
    }

    data class Content(
        var user: User? = null,
        var state: LoginState = LoginState.NotLogged
    )
}

sealed class LoginState {
    object NotLogged : LoginState()
    object Logging : LoginState()
    object Error : LoginState()
    object Logged : LoginState()
}
