package com.application.onovapplication.model

import com.google.gson.annotations.SerializedName

data class DonationDetailResponse(
    @SerializedName("donationDetail")
    val donationDetail: DonationDetail,
    @SerializedName("msg")
    val msg: String,
    @SerializedName("status")
    val status: String
)

data class DonationDetail(
    @SerializedName("areaLimit")
    val areaLimit: String,
    @SerializedName("created_at")
    val created_at: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("donationGoal")
    val donationGoal: String,
    @SerializedName("filePath")
    val filePath: String,
    @SerializedName("fileType")
    val fileType: String,
    @SerializedName("fullName")
    val fullName: String,
    @SerializedName("profilePic")
    val profilePic: String,
    @SerializedName("recordId")
    val recordId: String,
    @SerializedName("tagPeopleList")
    val tagPeopleList: List<Follow>,
    @SerializedName("title")
    val title: String,
    @SerializedName("updated_at")
    val updated_at: String,
    @SerializedName("userRef")
    val userRef: String
)

data class TagPeople(
    val created_at: String,
    val donationId: String,
    val fullName: String,
    val id: String,
    val peopleRef: String,
    val profilePic: String,
    val updated_at: String
)