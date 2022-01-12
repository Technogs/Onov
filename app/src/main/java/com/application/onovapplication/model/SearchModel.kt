package com.application.onovapplication.model

import com.google.gson.annotations.SerializedName

data class SearchModel(

    @SerializedName("Data")
    val dataList: List<SearchData>,
    val base_url: String,
    @SerializedName("msg")
    val msg: String,
    @SerializedName("status")
    val status: String
)

data class SearchData(
    @SerializedName("follow")
    var follow: String,
    @SerializedName("fullName")
    val fullName: String,
    @SerializedName("profilePic")
    val profilePic: String,
    val role: String,
    @SerializedName("userRef")
    val userRef: String
)