package com.example.ProjetAndroid.Model.database.dbmodels

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.ProjetAndroid.Model.api.dataclass.visualnovel.Tags
import com.example.ProjetAndroid.Model.DTOs.ScreenshotList

@Entity(tableName = "vn_additional_info")
data class VnAdditionalInfoDbModel(
    val description: String?,
    val tags: List<Tags>,
    val screenshots: List<ScreenshotList>,
    @PrimaryKey
    val id: String
)
