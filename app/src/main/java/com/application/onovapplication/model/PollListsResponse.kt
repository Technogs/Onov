package com.application.onovapplication.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

data class PollListsResponse(
    @SerializedName("msg")
    val msg: String,
    @SerializedName("pollData")
    val pollData: PollsData,
    @SerializedName("status")
    val status: String
)
@Parcelize
data class PollsData(
    @SerializedName("activePoll")
    val activePoll: List<ActivePoll>?=null,
    @SerializedName("expiredPoll")
    val expiredPoll: List<ActivePoll>?=null
): Parcelable
@Parcelize
data class ActivePoll(
    @SerializedName("areaLimit")
    val areaLimit: String,
    @SerializedName("createBy")
    val createBy: String,
    @SerializedName("created_at")
    val created_at: String,
    @SerializedName("fullName")
    val fullName: String,
    @SerializedName("id")
    val id: String,
    @SerializedName("isMultiple")
    val isMultiple: String,
    @SerializedName("isPublic")
    val isPublic: String,
    @SerializedName("isPoll")
    val isPoll: String,
    @SerializedName("options")
    val options: List<String>,
    @SerializedName("pollEndStatus")
    val pollEndStatus: String,
    @SerializedName("profilePic")
    val profilePic: String,
    @SerializedName("pollImage")
    val pollImage: String,
    @SerializedName("tillDateTime")
    val tillDateTime: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("totalVote")
    val totalVote: String,
    @SerializedName("updated_at")
    val updated_at: String
): Parcelable