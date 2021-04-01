package com.application.onovapplication.model

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName

class GetAllWinnersResponse {
    @SerializedName("status")
    @Expose
    var status: String? = null

    @SerializedName("msg")
    @Expose
    var msg: String? = null

    @SerializedName("base_url")
    @Expose
    var baseUrl: String? = null

    @SerializedName("data")
    @Expose
    var allWinnersList: List<AllWinnersList>? = null
}

class AllWinnersList{
    @SerializedName("id")
    @Expose
    var id: String? = null

    @SerializedName("debateId")
    @Expose
    var debateId: String? = null

    @SerializedName("winnerRef")
    @Expose
    var winnerRef: String? = null

    @SerializedName("create_at")
    @Expose
    var createAt: String? = null

    @SerializedName("update_at")
    @Expose
    var updateAt: String? = null

    @SerializedName("winCount")
    @Expose
    var winCount: String? = null

    @SerializedName("fullName")
    @Expose
    var fullName: String? = null

    @SerializedName("profilePic")
    @Expose
    var profilePic: String? = null

    @SerializedName("Rank")
    @Expose
    var rank: Int? = null
}