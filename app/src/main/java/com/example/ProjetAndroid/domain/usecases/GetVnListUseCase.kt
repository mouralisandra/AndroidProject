package com.example.ProjetAndroid.domain.usecases

import androidx.paging.PagingData
import com.example.ProjetAndroid.domain.Vn
import com.example.ProjetAndroid.domain.VnListRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetVnListUseCase @Inject constructor(private val vnListRepository: VnListRepository) {

    operator fun invoke(): Flow<PagingData<Vn>> {
        return vnListRepository.getVnList()
    }

    fun searchVn(newText: String?): Flow<PagingData<Vn>>
    {
        return vnListRepository.searchVn(newText)

    }

 
}