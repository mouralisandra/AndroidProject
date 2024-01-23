package com.example.ProjetAndroid.data.database.dbmodels

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.ProjetAndroid.data.network.dataclasses.vn.Tags
import com.example.ProjetAndroid.domain.ScreenshotList

@Entity(tableName = "vn_additional_info")
data class VnAdditionalInfoDbModel(
    val description: String?,
    val tags: List<Tags>,
    val screenshots: List<ScreenshotList>,
    @PrimaryKey
    val id: String
)
