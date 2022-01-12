package com.application.onovapplication.model

import com.google.gson.annotations.SerializedName

data class PolicyModel(
    @SerializedName("data")
    val `data`: String,
    @SerializedName("msg")
    val msg: String,
    @SerializedName("status")
    val status: String
)