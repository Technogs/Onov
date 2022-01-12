package com.application.onovapplication.model

import com.google.gson.annotations.SerializedName

data class ChatModel(
    @SerializedName("chatList")
    val chatList: List<Follow>,
    @SerializedName("msg")
    val msg: String,
    @SerializedName("status")
    val status: String
)


data class Chat(
    val fullName: String,
    val profilePic: String,
    val userRef: String
)