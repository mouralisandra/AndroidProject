package com.example.ProjetAndroid.Model.database.dbmodels

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "vn_basic_info")
data class VnBasicInfoDbModel(
    val image: String,
    val rating: Double,
    val votecount: Int,
    val title: String,
    @PrimaryKey
    val id: String
)

