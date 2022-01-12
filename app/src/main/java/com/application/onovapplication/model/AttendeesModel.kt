package com.application.onovapplication.model

import com.google.gson.annotations.SerializedName

data class AttendeesModel(
    @SerializedName("attendeeData")
    val attendeeData: List<AttendeeData>,
    @SerializedName("msg")
    val msg: String,
    @SerializedName("status")
    val status: String
)


data class AttendeeData(
    val create_at: String,
    @SerializedName("fullName")
    val fullName: String,
    val id: String,
    val productId: String,
    @SerializedName("profilePic")
    val profilePic: String,
    val ticketCount: String,
    val update_at: String,
    val userRef: String
)