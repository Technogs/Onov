package com.application.onovapplication.model

import com.google.gson.annotations.SerializedName

data class CommentsResponse(
    @SerializedName("commentData")
    val commentData: List<CommentData>,
    @SerializedName("msg")
    val msg: String,
    @SerializedName("status")
    val status: String
)

data class CommentData(
    @SerializedName("comment")
    val comment: String,
    @SerializedName("commentFrom")
    val commentFrom: String,
    @SerializedName("commentTo")
    val commentTo: String,
    @SerializedName("conmmentOn")
    val conmmentOn: String,
    @SerializedName("Liked")
    var Liked: String,
    @SerializedName("Disliked")
    var Disliked: String,
    @SerializedName("dislikeCount")
    var dislikeCount: String,
    var like: Boolean=false,
    var dislike: Boolean=false,
    @SerializedName("likeCount")
    var likeCount: String,
    @SerializedName("replyCount")
    val replyCount: String,
    @SerializedName("created_at")
    val created_at: String,
    @SerializedName("fullName")
    val fullName: String,
    @SerializedName("id")
    val id: String,
    @SerializedName("profilePic")
    val profilePic: String,
    @SerializedName("updated_at")
    val updated_at: String
)