package com.example.ProjetAndroid.data

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.example.ProjetAndroid.data.database.AppDatabase
import com.example.ProjetAndroid.data.database.dbmodels.UserDbModel
import com.example.ProjetAndroid.data.database.dbmodels.VnAdditionalInfoDbModel
import com.example.ProjetAndroid.data.database.dbmodels.VnBasicInfoDbModel
import com.example.ProjetAndroid.data.network.api.ApiService
import com.example.ProjetAndroid.data.network.dataclasses.vn.VnRequest
import com.example.ProjetAndroid.data.network.dataclasses.vn.VnResults
import com.example.ProjetAndroid.domain.User
import com.example.ProjetAndroid.domain.Vn
import com.example.ProjetAndroid.domain.VnListRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class VnListRepositoryImpl @Inject constructor(
    private val db: AppDatabase,
    private val service: ApiService,
    private val mapper: VnMapper,
    private val remoteMediator: VnListRemoteMediator
) : VnListRepository {

    @OptIn(ExperimentalPagingApi::class)
    override fun getVnList(): Flow<PagingData<Vn>> {
        val pagingSourceFactory = { db.vnDao().getVnList() }
        val flow = Pager(
            config = PagingConfig(
                pageSize = 50,
                enablePlaceholders = true
            ),
            remoteMediator = remoteMediator,
            pagingSourceFactory = pagingSourceFactory
        ).flow
        return flow.map { pagingData ->
            pagingData.map { mapper.mapBasicDbModelInfoToEntity(it) }
        }
    }

    override fun getVnDetails(id: String): Flow<Vn> = flow {
        loadCertainVnInfo(id)
        emit(mapper.mapFullInfoToEntity(db.vnDao().getVnFullInfo(id)))
    }

    override fun getUser(token: String): Flow<User?> = flow {
        getUserAuthInfo(token)
        emit(mapper.mapUserDbModelToUser(db.vnDao().getCurrentUser()))
    }
    @OptIn(ExperimentalPagingApi::class)
    override fun searchVn(newText: String?): Flow<PagingData<Vn>> {
        remoteMediator.searchVn(newText)
        val pagingSourceFactory = { db.vnDao().searchVn(newText) }
        val flow = Pager(
            config = PagingConfig(
                pageSize = 50,
                enablePlaceholders = true
            ),
            remoteMediator = remoteMediator,
            pagingSourceFactory = pagingSourceFactory

        ).flow
        return flow.map { pagingData ->
            pagingData.map { mapper.mapBasicDbModelInfoToEntity(it)
            }
        }

    }

    private suspend fun addVnList(list: List<VnBasicInfoDbModel>) {
        db.vnDao().insertVnList(list)
    }

    private suspend fun updateVnDetails(vn: VnAdditionalInfoDbModel) {
        db.vnDao().insertVnAdditionalInfo(vn)
    }

    private suspend fun updateUser(user: UserDbModel) {
        db.vnDao().updateCurrentUser(user)
    }

    private suspend fun loadCertainVnInfo(id: String) {
        try {
            val result: List<VnResults> =
                service.postToVnEndpoint(
                    VnRequest(
                        filters = listOf("id", "=", id),
                        fields = "title, image.url, rating, votecount, description, tags.rating, tags.spoiler, tags.name, tags.category, screenshots.thumbnail, screenshots.release.title"
                    )
                ).vnListResults
            updateVnDetails(mapper.mapVnResponseToAdditionalDbModelInfo(result.first()))
        } catch (e: Exception) {
            e.message?.let { Log.e("loadCertainVnInfo", it) }
        }
    }

    private suspend fun getUserAuthInfo(token: String) {
        try {
            val result =
                service.getUserInfo("token $token")
            updateUser(mapper.mapAuthInfoToUserDbModel(result))
        } catch (e: Exception) {
            e.message?.let { Log.e("getUser", it) }
            db.vnDao().removeCurrentUser()
        }
    }
}