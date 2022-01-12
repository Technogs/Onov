package com.application.onovapplication.model

import com.google.gson.annotations.SerializedName

data class SearchDebateResponse(
    @SerializedName("msg")
    val msg: String,
    @SerializedName("searchResult")
    val searchResult: List<LiveDebate>,
    @SerializedName("status")
    val status: String
)

data class SearchResult(
    val coverImage: String,
    val created_at: String,
    val date: String,
    val id: String,
    val message: String,
    val time: String,
    val title: String,
    val topic: String,
    val updated_at: String,
    val userRef: String,
    val view: String,
    val vote: String
)