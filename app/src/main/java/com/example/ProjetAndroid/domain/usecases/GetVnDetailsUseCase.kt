package com.example.ProjetAndroid.domain.usecases

import com.example.ProjetAndroid.domain.Vn
import com.example.ProjetAndroid.domain.VnListRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetVnDetailsUseCase @Inject constructor(private val vnListRepository: VnListRepository) {

    operator fun invoke(id: String): Flow<Vn> {
        return vnListRepository.getVnDetails(id)
    }
}