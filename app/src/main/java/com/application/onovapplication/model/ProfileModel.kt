package com.application.onovapplication.model

import com.google.gson.annotations.SerializedName

data class ProfileModel(
    @SerializedName("UserInfo")
    val UserInfo: UserInfo,
    @SerializedName("base_url")
    val base_url: String,
    @SerializedName("feedList")
    val feedList: List<FeedsData>,
    @SerializedName("msg")
    val msg: String,
    @SerializedName("status")
    val status: String
)
data class Feed(
    val created_at: String,
    val description: String,
    val donationGoal: String,
    val filePath: String,
    val fileType: String,
    val id: String,
    val petitionId: String,
    val recordType: String,
    val title: String,
    val updated_at: String,
    val userRef: String
)

data class UserData(
    val Is_Active: String,
    val about: String,
    val countryCode: String,
    val coverPhoto: String,
    val created_at: String,
    val deviceToken: String,
    val deviceType: String,
    val donationsVisible: String,
    val email: String,
    val followCount: String,
    val fullName: String,
    val id: String,
    val loginStatus: String,
    val notification: String,
    val password: String,
    val phone: String,
    val profilePic: String,
    val role: String,
    val supporter: String,
    val updated_at: String,
    val userRef: String,
    val validationCode: String,
    val webUrl: String
)