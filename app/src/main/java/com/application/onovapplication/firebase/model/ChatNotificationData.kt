package com.application.onovapplication.firebase.model

data class Data(
    val user: String,
    val icon: Int,
    val body: String,
    val title: String,
    var sented: String
)

data class Notification(
    val body: String,
    val title: String

)