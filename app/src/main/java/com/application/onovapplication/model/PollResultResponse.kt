package com.application.onovapplication.model
import com.google.gson.annotations.SerializedName

data class PollResultResponse(
    @SerializedName("msg")
    val msg: String,
    @SerializedName("pollData")
    val pollData: PollData,
    @SerializedName("pollResult")
    val pollResult: List<PollResult>,
    @SerializedName("status")
    val status: String
)
data class PollData(
    @SerializedName("createBy")
    val createBy: String,
    @SerializedName("created_at")
    val created_at: String,
    @SerializedName("id")
    val id: String,
    @SerializedName("options")
    val options: String,
    @SerializedName("tillDateTime")
    val tillDateTime: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("updated_at")
    val updated_at: String
)

data class PollResult(
    @SerializedName("name")
    val name: String,
    @SerializedName("valCount")
    val valCount: Int
)