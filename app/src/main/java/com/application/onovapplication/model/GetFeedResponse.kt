package com.application.onovapplication.model

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName




class GetFeedResponse {
    @SerializedName("status")
    @Expose
    var status: String? = null

    @SerializedName("msg")
    @Expose
    var msg: String? = null

    @SerializedName("data")
    @Expose
    var feedData: ArrayList<FeedData>? = null
}


class FeedData{
    @SerializedName("id")
    @Expose
    var id: String? = null

    @SerializedName("userId")
    @Expose
    var userId: String? = null

    @SerializedName("Name")
    @Expose
    var name: String? = null

    @SerializedName("profilePic")
    @Expose
    var profilePic: String? = null

    @SerializedName("title")
    @Expose
    var title: String? = null

    @SerializedName("description")
    @Expose
    var description: String? = null

    @SerializedName("filePath")
    @Expose
    var filePath: String? = null

    @SerializedName("fileType")
    @Expose
    var fileType: String? = null

    @SerializedName("recordType")
    @Expose
    var recordType: String? = null

    @SerializedName("created_at")
    @Expose
    var createdAt: String? = null
}