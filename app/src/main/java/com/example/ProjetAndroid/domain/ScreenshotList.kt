package com.example.ProjetAndroid.domain

data class ScreenshotList(
    val title: String,
    val releaseId: String,
    val screenshotList: List<Pair<String, Double>>
)