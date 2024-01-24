package com.example.ProjetAndroid.Model.api.dataclass.visualnovel

import com.example.ProjetAndroid.Model.api.dataclass.Image

data class VnResults(
    val id: String,
    val image: Image,
    val rating: Double,
    val votecount: Int,
    val title: String,
    val description: String,
    val tags: List<Tags>,
    val screenshots: List<Screenshot>
)
