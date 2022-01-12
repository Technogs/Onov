package com.application.onovapplication.model

import com.google.gson.annotations.SerializedName

data class DonorsResponse(
    @SerializedName("donnerList")
    val donnerList: List<Follow>,
    @SerializedName("msg")
    val msg: String,
    @SerializedName("status")
    val status: String
)
data class Donner(
    val fullName: String,
    val id: String,
    val profilePic: String,
    val userRef: String
)