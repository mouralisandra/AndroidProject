package com.example.ProjetAndroid.Model.DTOs

data class ScreenshotList(
    val title: String,
    val releaseId: String,
    val screenshotList: List<Pair<String, Double>>
)