package com.application.onovapplication.model

import com.google.gson.annotations.SerializedName

data class DonationRequestsResponse(
    @SerializedName("allRequestData")
    val allRequestData: List<AllRequestData>,
    @SerializedName("msg")
    val msg: String,
    @SerializedName("status")
    val status: String
)

data class AllRequestData(
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
    @SerializedName("id")
    val id: String,
    @SerializedName("isShared")
    val isShared: String,
    @SerializedName("petitionId")
    val petitionId: String,
    @SerializedName("recordId")
    val recordId: String,
    @SerializedName("recordType")
    val recordType: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("updated_at")
    val updated_at: String,
    @SerializedName("userRef")
    val userRef: String
)