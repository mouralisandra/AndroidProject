package com.example.ProjetAndroid.data.database

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.ProjetAndroid.data.database.dbmodels.UserDbModel
import com.example.ProjetAndroid.data.database.dbmodels.VnAdditionalInfoDbModel
import com.example.ProjetAndroid.data.database.dbmodels.VnBasicInfoDbModel
import com.example.ProjetAndroid.data.database.dbmodels.VnFullInfoDbModel

@Dao
interface VnDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVnList(vnList: List<VnBasicInfoDbModel>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVnAdditionalInfo(vn: VnAdditionalInfoDbModel)

    @Transaction
    @Query("SELECT * FROM vn_basic_info WHERE id == :id LIMIT 1")
    suspend fun getVnFullInfo(id: String): VnFullInfoDbModel

    @Query("DELETE FROM vn_basic_info")
    suspend fun clearVnList()

    @Query("SELECT * FROM user LIMIT 1")
    suspend fun getCurrentUser(): UserDbModel

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateCurrentUser(user: UserDbModel)

    @Query("DELETE FROM user")
    suspend fun removeCurrentUser()

    @Query("SELECT * FROM vn_basic_info")
    fun getVnList(): PagingSource<Int, VnBasicInfoDbModel>

    @Query("SELECT * FROM vn_basic_info WHERE title LIKE '%' || :newText || '%'")
    fun searchVn(newText: String?): PagingSource<Int, VnBasicInfoDbModel>

}