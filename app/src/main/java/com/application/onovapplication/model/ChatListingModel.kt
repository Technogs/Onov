package com.application.onovapplication.model

import com.google.gson.annotations.SerializedName

data class ChatListingModel (
    @SerializedName("chatList")
    val chatList: List<Chat>,
    @SerializedName("msg")
    val msg: String,
    @SerializedName("status")
    val status: String
)
