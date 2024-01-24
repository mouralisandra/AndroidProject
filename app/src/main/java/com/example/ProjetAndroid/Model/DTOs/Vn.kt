package com.example.ProjetAndroid.Model.DTOs

import com.example.ProjetAndroid.Model.api.DataClasses.Tags

data class Vn(
    val id: String = "",
    val image: String = "",
    val rating: Double = 0.0,
    val title: String = "",
    val description: String? = "",
    val tags: List<Tags> = listOf(),
    val screenshots: List<ScreenshotList> = listOf()
)