package com.application.onovapplication.model

import com.google.gson.annotations.SerializedName

data class SimpleResponse(
    @SerializedName("msg")
    val msg: String,
    @SerializedName("status")
    val status: String
)