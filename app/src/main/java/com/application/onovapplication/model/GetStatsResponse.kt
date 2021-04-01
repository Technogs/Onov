package com.application.onovapplication.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class GetStatsResponse {
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
    var statsDataList: List<StatsDataList>? = null
}

class StatsDataList {
    @SerializedName("id")
    @Expose
    var id: String? = null

    @SerializedName("userRef")
    @Expose
    var userRef: String? = null


    @SerializedName("debateId")
    @Expose
    var debateId: String? = null

    @SerializedName("title")
    @Expose
    var title: String? = null

    @SerializedName("coverImage")
    @Expose
    var coverImage: String? = null

    @SerializedName("updated_at")
    @Expose
    var updatedAt: String? = null

    @SerializedName("created_at")
    @Expose
    var createdAt: String? = null

    @SerializedName("voteCount")
    @Expose
    var voteCount: String? = null
}