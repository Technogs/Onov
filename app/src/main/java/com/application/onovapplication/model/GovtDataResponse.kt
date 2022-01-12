package com.application.onovapplication.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class GovtDataResponse(
    @SerializedName("govtData")
    val govtData: List<GovtData>,
    @SerializedName("msg")
    val msg: String,
    @SerializedName("status")
    val status: String
):Parcelable

@Parcelize
data class GovtData(
    @SerializedName("created_at")
    val created_at: String,
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("party")
    val party: String,
    @SerializedName("picture")
    val picture: String,
    @SerializedName("role")
    val role: String,
    @SerializedName("state")
    val state: String,
    @SerializedName("updated_at")
    val updated_at: String
):Parcelable