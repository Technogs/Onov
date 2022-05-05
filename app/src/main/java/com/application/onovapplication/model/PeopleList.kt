package com.application.onovapplication.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PeopleList(
    @SerializedName("msg")
    val msg: String,
    @SerializedName("peopleData")
    val peopleData: PeopleData,
    @SerializedName("status")
    val status: String
) : Parcelable


@Parcelize
data class PeopleData(
    @SerializedName("donnerCount")
    val donnerCount: Int,
    @SerializedName("donnerList")
    val donnerList: List<Follow>,
    @SerializedName("followCount")
    val followCount: Int,
    @SerializedName("followList")
    val followList: List<Follow>,
    @SerializedName("followingCount")
    val followingCount: Int,
    @SerializedName("followingList")
    val followingList: List<Follow>,
    @SerializedName("donetedCount")
    val donetedCount: Int,
    @SerializedName("donetedList")
    val donetedList: List<Follow>
) : Parcelable


@Parcelize
data class Follow(
    @SerializedName("fullName")
    val fullName: String = "",
    @SerializedName("profilePic")
    val profilePic: String = "",
    @SerializedName("userRef")
    val userRef2: String = "",
    @SerializedName("id")
    val id: String?="",
    @SerializedName("lastMessage")
    val lastMessage: String? = "",
    @SerializedName("lastSender")
    val lastSender: String? = "",
    @SerializedName("fromRef")
    val fromRef: String? = "",
    @SerializedName("msgCount")
    val msgCount: String? = "",
    @SerializedName("toRef")
    val userRef: String? = "",
    @SerializedName("user_id")
    val user_id: String? = "",

    @SerializedName("follow")
    var follow: String? = "",
) : Parcelable


@Parcelize
data class Chat(
    @SerializedName("fullName")
    val fullName: String,
    @SerializedName("profilePic")
    val profilePic: String,
    @SerializedName("toRef")
    val userRef: String,
    @SerializedName("id")
    val id: String,
) : Parcelable


data class Following(
    val fullName: String,
    val profilePic: String,
    val userRef: String
)