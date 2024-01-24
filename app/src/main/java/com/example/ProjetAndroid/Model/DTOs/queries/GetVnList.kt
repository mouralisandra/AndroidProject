package com.example.ProjetAndroid.Model.DTOs.queries

import androidx.paging.PagingData
import com.example.ProjetAndroid.Model.DTOs.Vn
import com.example.ProjetAndroid.Model.DTOs.VnListRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetVnList @Inject constructor(private val vnListRepository: VnListRepository) {

    operator fun invoke(): Flow<PagingData<Vn>> {
        return vnListRepository.getVnList()
    }

    fun searchVn(newText: String?): Flow<PagingData<Vn>>
    {
        return vnListRepository.searchVn(newText)

    }

 
}