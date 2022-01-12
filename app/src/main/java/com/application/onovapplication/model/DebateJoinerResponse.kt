package com.application.onovapplication.model

import com.google.gson.annotations.SerializedName

data class DebateJoinerResponse(
    @SerializedName("joinerData")
    val joinerData: List<JoinerData>,
    @SerializedName("msg")
    val msg: String,
    @SerializedName("status")
    val status: String
)

data class JoinerData(
    @SerializedName("profilePic")
    val profilePic: String,
    @SerializedName("requestStatus")
    val requestStatus: String,
    @SerializedName("uid")
    val uid: String,
    @SerializedName("userName")
    val userName: String,
    @SerializedName("userRef")
    val userRef: String
)