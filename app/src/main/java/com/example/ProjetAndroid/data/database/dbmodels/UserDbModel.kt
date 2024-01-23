package com.example.ProjetAndroid.data.database.dbmodels

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class UserDbModel(
    val id: String,
    @PrimaryKey
    val username: String
)
