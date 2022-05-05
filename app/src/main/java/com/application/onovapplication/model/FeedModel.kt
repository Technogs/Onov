package com.application.onovapplication.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class FeedModel(
    @SerializedName("data")
    val `data`: List<FeedsData>,
    @SerializedName("msg")
    val msg: String,
    @SerializedName("NotificationCount")
    val NotificationCount: String,
    @SerializedName("status")
    val status: String) : Parcelable


@Parcelize
data class FeedsData(
    @SerializedName("Name")
    val Name: String? = "",
    @SerializedName("fullName")
    val fullName: String? = "",
    @SerializedName("created_at")
    val created_at: String? = "",
    @SerializedName("description")
    val description: String? = "",
    @SerializedName("polTitle")
    val polTitle: String? = "",
    @SerializedName("polOptions")
    val polOptions: List<String>?=ArrayList(),
    @SerializedName("filePath")
    val filePath: String? = "",
    @SerializedName("polTillDateTime")
    val polTillDateTime: String? = "",
    @SerializedName("areaLimit")
    val areaLimit: String? = "",
    @SerializedName("isPoll")
    val isPoll: String? = "",
    @SerializedName("polCreateBy")
    val polCreateBy: String? = "",
    @SerializedName("polId")
    val polId: String? = "",
    @SerializedName("fileType")
    val fileType: String? = "",
    @SerializedName("id")
    val id: String? = "",
    @SerializedName("recordId")
    val recordId: String? = "",
    var like: Boolean = false,
    var profile: Boolean = false,
    var dislike: Boolean = false,
    @SerializedName("petitionDiscription")
    val petitionDiscription: String? = "",
    @SerializedName("petitionLocation")
    val petitionLocation: String? = "",
    @SerializedName("petitionDuration")
    val petitionDuration: String? = "",
    @SerializedName("Liked")
    var Liked: String? = "",
    @SerializedName("Disliked")
    var Disliked: String? = "",
    @SerializedName("petitionMedia")
    val petitionMedia: String? = "",
    @SerializedName("petitionRadius")
    val petitionRadius: String? = "",
    @SerializedName("petitionReceiveSignCount")
    val petitionReceiveSignCount: String? = "",
    @SerializedName("petitionSignCount")
    val petitionSignCount: String? = "",
    @SerializedName("petitionTitle")
    val petitionTitle: String? = "",
    @SerializedName("petitionId")
    val petitionId: String? = "",
    @SerializedName("petitionWebsiteLink")
    val petitionWebsiteLink: String? = "",
    @SerializedName("profilePic")
    val profilePic: String? = "",
    @SerializedName("isShared")
    val isShared: String? = "",
    @SerializedName("recordType")
    val recordType: String? = "",
    @SerializedName("title")
    val title: String? = "",
    @SerializedName("polisPublic")
    val polisPublic: String? = "",
    @SerializedName("pollImage")
    val pollImage: String? = "",
    @SerializedName("polisMultiple")
    val polisMultiple: String? = "",
    @SerializedName("userRef")
    val userRef: String? = "",
    @SerializedName("totalVote")
    val totalVote: String? = "",
    @SerializedName("donationGoal")
    val donationGoal: String? = "",
    @SerializedName("dislikeCount")
    var dislikeCount: String? = "",
    @SerializedName("cityName")
    var cityName: String? = "",
    @SerializedName("stateName")
    var stateName: String? = "",
    @SerializedName("countryName")
    var countryName: String? = "",
    @SerializedName("commentCount")
    val commentCount: String? = "",
    @SerializedName("likeCount")
    var likeCount: String? = ""
) : Parcelable
