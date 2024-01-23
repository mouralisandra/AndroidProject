package com.example.ProjetAndroid.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.ProjetAndroid.data.database.AppDatabase
import com.example.ProjetAndroid.data.database.dbmodels.RemoteKeys
import com.example.ProjetAndroid.data.database.dbmodels.VnBasicInfoDbModel
import com.example.ProjetAndroid.data.network.api.ApiService
import com.example.ProjetAndroid.data.network.dataclasses.vn.VnRequest
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

private const val STARTING_PAGE_INDEX = 1

@OptIn(ExperimentalPagingApi::class)
class VnListRemoteMediator @Inject constructor(
    private val service: ApiService,
    private val db: AppDatabase,
    private val mapper: VnMapper
) : RemoteMediator<Int, VnBasicInfoDbModel>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, VnBasicInfoDbModel>
    ): MediatorResult {
        val page: Int = when (loadType) {
            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeyForLastItem(state)
                // If remoteKeys is null, that means the refresh result is not in the database yet.
                // We can return Success with endOfPaginationReached = false because Paging
                // will call this method again if RemoteKeys becomes non-null.
                // If remoteKeys is NOT NULL but its nextKey is null, that means we've reached
                // the end of pagination for append.
                val nextKey = remoteKeys?.nextKey
                if (nextKey == null) {
                    return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                }
                nextKey
            }

            LoadType.PREPEND -> {
                val remoteKeys = getRemoteKeyForFirstItem(state)
                // If remoteKeys is null, that means the refresh result is not in the database yet.
                val prevKey = remoteKeys?.prevKey
                if (prevKey == null) {
                    return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                }
                prevKey
            }

            LoadType.REFRESH -> {
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                remoteKeys?.nextKey?.minus(1) ?: STARTING_PAGE_INDEX
            }
        }

        try {
            val request = VnRequest(
                page = page,
                results = state.config.pageSize,
                reverse = true,
                sort = "rating",
                fields = "title, image.url, rating, votecount"
            )
            val response = service.postToVnEndpoint(request)
            val vnList = response.vnListResults
            val endOfPaginationReached = vnList.isEmpty()
            db.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    db.remoteKeysDao().clearRemoteKeys()
                    db.vnDao().clearVnList()
                }
                val prevKey = if (page == STARTING_PAGE_INDEX) null else page - 1
                val nextKey = if (endOfPaginationReached) null else page + 1

                val keys = vnList.map {
                    RemoteKeys(id = it.id, prevKey = prevKey, nextKey = nextKey)
                }
                db.remoteKeysDao().insertAll(keys)
                db.vnDao().insertVnList(mapper.mapListVnResponseToListBasicDbModelInfo(vnList))
            }
            return MediatorResult.Success(endOfPaginationReached)
        } catch (e: IOException) {
            return MediatorResult.Error(e)
        } catch (e: HttpException) {
            return MediatorResult.Error(e)
        }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, VnBasicInfoDbModel>): RemoteKeys? {
        // Get the last page that was retrieved, that contained items.
        // From that last page, get the last item
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let { vn ->
                // Get the remote keys of the last item retrieved
                db.remoteKeysDao().remoteKeysRepoId(vn.id)
            }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, VnBasicInfoDbModel>): RemoteKeys? {

        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
            ?.let { vn ->
                db.remoteKeysDao().remoteKeysRepoId(vn.id)
            }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(
        state: PagingState<Int, VnBasicInfoDbModel>
    ): RemoteKeys? {
        // The paging library is trying to load data after the anchor position
        // Get the item closest to the anchor position
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { vn ->
                db.remoteKeysDao().remoteKeysRepoId(vn)
            }
        }
    }

    fun searchVn(newText: String?) {
        //search for a vn with the title
        //if the title is null, then we search for all the vn
        val request = VnRequest(
            page = STARTING_PAGE_INDEX,
            results = 50,
            reverse = true,
            sort = "rating",
            filters = listOf("title", "ilike", newText ?: ""),
            fields = "title, image.url, rating, votecount"
        )

    }
}