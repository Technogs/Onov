package com.application.onovapplication.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

data class SearchModel(

    @SerializedName("Data")
    val dataList: List<Follow>,
    val base_url: String,
    @SerializedName("msg")
    val msg: String,
    @SerializedName("status")
    val status: String
)
@Parcelize
data class SearchData(
    @SerializedName("follow")
    var follow: String,
    @SerializedName("fullName")
    val fullName: String,
    @SerializedName("profilePic")
    val profilePic: String,
    val role: String,
    @SerializedName("userRef")
    val userRef: String
): Parcelable
