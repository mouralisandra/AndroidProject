package com.example.ProjetAndroid.Model.api.DataClasses

import com.google.gson.annotations.SerializedName

data class VnResponse(
    val more: Boolean,
    @SerializedName("results")
    val vnListResults: List<VnResults>
)