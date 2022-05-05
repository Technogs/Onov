package com.application.onovapplication.model

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ViewPetitionResponse(
    @SerializedName("msg")
    val msg: String,
    @SerializedName("petitionArray")
    val petitionArray: List<PetitionArray>,
    @SerializedName("status")
    val status: String
):Parcelable


@Parcelize
data class PetitionArray(
    @SerializedName("created_at")
    val created_at: String,
    @SerializedName("discription")
    val discription: String,
    @SerializedName("id")
    val id: String,
    @SerializedName("sentFrom")
    val sentFrom: String,
    @SerializedName("signCount")
    val signCount: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("userRef")
    val userRef: String,
    @SerializedName("profilePic")
    val profilePic: String,
    @SerializedName("websiteLink")
    val websiteLink: String,
    @SerializedName("getSignCount")
    val getSignCount: String,
    @SerializedName("duration")
    val duration: String,
    @SerializedName("mediaType")
    val mediaType: String,
    @SerializedName("petitionMedia")
    val petitionMedia: String,
    @SerializedName("areaLimit")
    val areaLimit: String

):Parcelable