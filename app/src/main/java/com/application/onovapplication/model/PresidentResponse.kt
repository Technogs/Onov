package com.application.onovapplication.model

data class PresidentResponse(
    val pages: List<Page>
)

data class Page(
    val description: String,
    val excerpt: String,
    val id: Int,
    val key: String,
    val thumbnail: Thumbnail,
    val title: String
)