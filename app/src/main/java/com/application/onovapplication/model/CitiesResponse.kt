package com.application.onovapplication.model

data class CitiesResponse(
    val `data`: List<String>,
    val error: Boolean,
    val msg: String
)