package com.example.ProjetAndroid.data.network.dataclasses.vn

import com.google.gson.annotations.SerializedName

data class VnResponse(
    val more: Boolean,
    @SerializedName("results")
    val vnListResults: List<VnResults>
)