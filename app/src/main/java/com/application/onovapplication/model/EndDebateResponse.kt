package com.application.onovapplication.model

import com.google.gson.annotations.SerializedName

data class EndDebateResponse(
    @SerializedName("msg")
    val msg: String,
    @SerializedName("status")
    val status: String,
    @SerializedName("winData")
    val winData: List<WinData>
)

data class WinData(
    @SerializedName("created_at")
    val created_at: String,
    @SerializedName("debateId")
    val debateId: String,
    @SerializedName("debateResult")
    val debateResult: String,
    @SerializedName("rank")
    val rank: Int,
    @SerializedName("updated_at")
    val updated_at: String,
    @SerializedName("winnerRef")
    val winnerRef: String
)