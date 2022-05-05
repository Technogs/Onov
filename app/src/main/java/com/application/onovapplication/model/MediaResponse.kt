package com.application.onovapplication.model

import com.google.gson.annotations.SerializedName

data class MediaResponse(
    @SerializedName("mediaData")
    val mediaData: List<MediaData>,
    @SerializedName("msg")
    val msg: String,
    @SerializedName("status")
    val status: String
)

data class MediaData(
    @SerializedName("created_at")
    val created_at: String,
    @SerializedName("filePath")
    val filePath: String,
    @SerializedName("fileType")
    val fileType: String,
    @SerializedName("id")
    val id: String,
    @SerializedName("recordType")
    val recordType: String
)