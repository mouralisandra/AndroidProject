package com.example.ProjetAndroid.Model.DTOs.queries

import com.example.ProjetAndroid.Model.DTOs.User
import com.example.ProjetAndroid.Model.DTOs.VnListRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetUser @Inject constructor(private val vnListRepository: VnListRepository) {

    operator fun invoke(token: String): Flow<User?> {
        return vnListRepository.getUser(token)
    }
}