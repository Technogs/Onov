package com.application.onovapplication.model

import com.google.gson.annotations.SerializedName

data class DebateDetailResponse(
    @SerializedName("debateDetail")
    val debateDetail: DebateDetail,
    @SerializedName("msg")
    val msg: String,
    @SerializedName("status")
    val status: String
)
data class DebateDetail(
    @SerializedName("coverImage")
    val coverImage: String,
    @SerializedName("created_at")
    val created_at: String,
    @SerializedName("date")
    val date: String,
    @SerializedName("debateJoinner")
    val debateJoinner: List<JoinerData>,
    @SerializedName("id")
    val id: String,
    @SerializedName("message")
    val message: String,
    @SerializedName("profilePic")
    val profilePic: String,
    @SerializedName("time")
    val time: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("topic")
    val topic: String,
    @SerializedName("uid")
    val uid: String,
    @SerializedName("updated_at")
    val updated_at: String,
    @SerializedName("userName")
    val userName: String,
    @SerializedName("userRef")
    val userRef: String
)

data class DebateJoinner(
    val profilePic: String,
    val requestStatus: String,
    val uid: String,
    val userName: String,
    val userRef: String
)