package com.application.onovapplication.model

import com.google.gson.annotations.SerializedName

data class PetitionSignResponse(
    @SerializedName("msg")
    val msg: String,
    @SerializedName("signList")
    val signList: SignList,
    @SerializedName("status")
    val status: String
)

data class SignList(
    @SerializedName("SignList")
    val SignList: List<Sign>,
    @SerializedName("created_at")
    val created_at: String,
    @SerializedName("discription")
    val discription: String,
    @SerializedName("duration")
    val duration: String,
    @SerializedName("getSignCount")
    val getSignCount: String,
    @SerializedName("id")
    val id: String,
    @SerializedName("mediaType")
    val mediaType: String,
    @SerializedName("petitionMedia")
    val petitionMedia: String,
    @SerializedName("areaLimit")
    val areaLimit: String,
    @SerializedName("signCount")
    val signCount: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("updated_at")
    val updated_at: String,
    @SerializedName("userRef")
    val userRef: String,
    @SerializedName("websiteLink")
    val websiteLink: String
)

data class Sign(
    @SerializedName("created_at")
    val created_at: String,
    @SerializedName("fullName")
    val fullName: String,
    @SerializedName("id")
    val id: String,
    @SerializedName("profilePic")
    val profilePic: String,
    @SerializedName("signImageUrl")
    val signImageUrl: String,
    @SerializedName("updated_at")
    val updated_at: String,
    @SerializedName("userRef")
    val userRef: String
)