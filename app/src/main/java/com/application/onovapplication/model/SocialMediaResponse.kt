package com.application.onovapplication.model

import com.google.gson.annotations.SerializedName

data class SocialMediaResponse(
    @SerializedName("msg")
    val msg: String,
    @SerializedName("socialData")
    val socialData: SocialData,
    @SerializedName("status")
    val status: String
)

data class SocialData(
    @SerializedName("created_at")
    val created_at: String,
    @SerializedName("facebook")
    var facebook: String,
    @SerializedName("fullName")
    val fullName: String,
    @SerializedName("id")
    val id: String,
    @SerializedName("instagram")
    var instagram: String,
    @SerializedName("profilePic")
    val profilePic: String,
    @SerializedName("twitter")
    var twitter: String,
    @SerializedName("updated_at")
    val updated_at: String,
    @SerializedName("userRef")
    val userRef: String
)