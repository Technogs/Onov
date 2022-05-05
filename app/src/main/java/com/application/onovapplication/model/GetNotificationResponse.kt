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
    @SerializedName("sender_id")
    @Expose
    var sender_id: String? = null

    @SerializedName("sender_fullName")
    @Expose
    var sennder_fullName: String? = null

    @SerializedName("sender_profilePic")
    @Expose
    var sender_profilePic: String? = null

    @SerializedName("notifyTo")
    @Expose
    var notifyTo: String? = null

    @SerializedName("notifyFrom")
    @Expose
    var notifyFrom: String? = null

    @SerializedName("notificationText")
    @Expose
    var notificationText: String? = null

    @SerializedName("seen")
    @Expose
    var seen: String? = null

    @SerializedName("create_at")
    @Expose
    var create_at: String? = null

    @SerializedName("update_at")
    @Expose
    var update_at: String? = null


}