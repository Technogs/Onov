package com.application.onovapplication.model

import com.google.gson.annotations.SerializedName

data class ChatImageResponse(
    @SerializedName("chatImgUrl")
    val chatImgUrl: String,
    @SerializedName("msg")
    val msg: String,
    @SerializedName("status")
    val status: String
)