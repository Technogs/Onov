package com.application.onovapplication.model

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName


class GetNotificationResponse {
    @SerializedName("status")
    @Expose
    var status: String? = null

    @SerializedName("msg")
    @Expose
    var msg: String? = null

    @SerializedName("data")
    @Expose
    var notificationList: List<NotificationList>? = null
}

class NotificationList {
    @SerializedName("id")
    @Expose
    var id: String? = null

    @SerializedName("notifyTo")
    @Expose
    var notifyTo: String? = null

    @SerializedName("notifyFrom")
    @Expose
    var notifyFrom: String? = null

    @SerializedName("notificationText")
    @Expose
    var notificationText: String? = null

    @SerializedName("create_at")
    @Expose
    var createAt: String? = null

    @SerializedName("update_at")
    @Expose
    var updateAt: String? = null
}