package com.example.ProjetAndroid.domain

import com.example.ProjetAndroid.data.network.dataclasses.vn.Tags

data class Vn(
    val id: String = "",
    val image: String = "",
    val rating: Double = 0.0,
    val title: String = "",
    val description: String? = "",
    val tags: List<Tags> = listOf(),
    val screenshots: List<ScreenshotList> = listOf()
)