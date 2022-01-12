package com.application.onovapplication.model

import com.google.gson.annotations.SerializedName

data class DonationModel(
    @SerializedName("data")
    val `data`: DonationData,
    @SerializedName("msg")
    val msg: String,
    @SerializedName("status")
    val status: String
)

data class DonationData(
    @SerializedName("donorList")
    val donorList: List<ReceivedList>,
    @SerializedName("receivedList")
    val receivedList: List<ReceivedList>
)

data class ReceivedList(
    @SerializedName("amount")
    val amount: String,
    @SerializedName("created_at")
    val created_at: String,
    @SerializedName("donateFrom")
    val donateFrom: String,
    @SerializedName("donateTo")
    val donateTo: String,
    @SerializedName("donorRef")
    val donorRef: String,
    @SerializedName("id")
    val id: String,
    @SerializedName("receiptid")
    val receiptid: String,
    @SerializedName("toRef")
    val toRef: String
)
