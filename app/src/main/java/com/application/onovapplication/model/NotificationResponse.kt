package com.application.onovapplication.model

data class NotificationResponse(
    val `data`: Data,
    val message: String,
    val notification: Notification,
    val type: String
)
data class Data(
    val icon: String,
    val receiver_userRef: String,
    val receiver_user_id: String,
    val receiver_user_name: String,
    val type: String
)

data class Notification(
    val body: String,
    val title: String
)