package com.example.ProjetAndroid.domain

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow

interface VnListRepository {

    fun getVnList(): Flow<PagingData<Vn>>

    fun getVnDetails(id: String): Flow<Vn>

    fun getUser(token: String): Flow<User?>
}