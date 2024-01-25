package com.example.ProjetAndroid.Model.DTOs.queries

import com.example.ProjetAndroid.Model.DTOs.Vn
import com.example.ProjetAndroid.Model.DTOs.VnListRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetVnDetails @Inject constructor(private val vnListRepository: VnListRepository) {

    operator fun invoke(id: String): Flow<Vn> {
        return vnListRepository.getVnDetails(id)
    }
}