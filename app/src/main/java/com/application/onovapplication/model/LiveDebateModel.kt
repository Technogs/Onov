package com.application.onovapplication.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import com.google.gson.annotations.SerializedName

@Parcelize
data class LiveDebateModel(
    @SerializedName("liveDebate")
    val liveDebate: List<LiveDebate>,
    @SerializedName("msg")
    val msg: String,
    @SerializedName("pastDebate")
    val pastDebate: List<LiveDebate>,
    @SerializedName("status")
    val status: String,
    @SerializedName("upcomingDebate")
    val upcomingDebate: List<LiveDebate>
):Parcelable
@Parcelize
data class LiveDebate(
    @SerializedName("coverImage")
    val coverImage: String?="",
    @SerializedName("created_at")
    val created_at: String?="",
    @SerializedName("date")
    val date: String?="",
    @SerializedName("id")
    val id: String?="",
    @SerializedName("message")
    val message: String?="",
    @SerializedName("time")
    val time: String?="",
    @SerializedName("title")
    val title: String?="",
    @SerializedName("topic")
    val topic: String?="",
    @SerializedName("updated_at")
    val updated_at: String?="",
    @SerializedName("isPublic")
    val isPublic: String?="",
    @SerializedName("debateDuration")
    val debateDuration: String?="",
    @SerializedName("userRef")
    val userRef: String?="",
    @SerializedName("view")
    val view: String?="",
    @SerializedName("vote")
    val vote: String?=""
):Parcelable