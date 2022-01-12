package com.application.onovapplication.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class EventModel(
    @SerializedName("eventData")
    val eventData: List<EventData>,
    @SerializedName("msg")
    val msg: String,
    @SerializedName("status")
    val status: String
) : Parcelable

@Parcelize
data class EventData(
    @SerializedName("cover_image")
    val cover_image: String?=null,
    @SerializedName("created_at")
    val created_at: String?=null,
    @SerializedName("description")
    val description: String?=null,
    @SerializedName("end_date")
    val end_date: String?=null,
    @SerializedName("end_time")
    val end_time: String?=null,
    @SerializedName("ent_video")
    val ent_video: String?=null,
    @SerializedName("id")
    val id: String?=null,
    var like: Boolean=false,
    var profile: Boolean=false,
    var dislike: Boolean=false,
    @SerializedName("price")
    val price: String?=null,
    @SerializedName("likeCount")
    var likeCount: String?=null,
    @SerializedName("dislikeCount")
    var dislikeCount: String?=null,
    @SerializedName("CommentCount")
    val CommentCount: String?=null,
    @SerializedName("Liked")
    var Liked: String?=null,
    @SerializedName("Disliked")
    var Disliked: String?=null,
    @SerializedName("start_date")
    val start_date: String?=null,
    @SerializedName("start_time")
    val start_time: String?=null,
    @SerializedName("title")
    val title: String?=null,
    @SerializedName("purchaseCount")
    val purchaseCount: String?=null,
    @SerializedName("updated_at")
    val updated_at: String?=null,
    @SerializedName("userRef")
    val userRef: String?=null
) : Parcelable