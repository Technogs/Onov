package com.application.onovapplication.model

import android.os.Parcelable
import com.google.gson.annotations.Expose

import kotlinx.android.parcel.Parcelize
import com.google.gson.annotations.SerializedName


@Parcelize
data class DebateResponse(
    @SerializedName("data")

    val data: List<DebateData>,
    @SerializedName("msg")
    val msg: String,
    @SerializedName("status")
    val status: String
):Parcelable
@Parcelize
data class DebateData(
    @SerializedName("requestId")
val  requestId:String,
@SerializedName("dbatId")
val  dbatId:String,
@SerializedName("title")
val  title :String,
@SerializedName("message")
val  message :String,
@SerializedName("date")
val  date:String,
@SerializedName("time")
val  time:String,
@SerializedName("requestStatus")
val requestStatus:String,
@SerializedName("uid")
val  uid:String,
@SerializedName("userRef")
val  userRef:String,
@SerializedName("userName")
val  userName:String,
@SerializedName("profilePic")
val  profilePic:String,
):Parcelable