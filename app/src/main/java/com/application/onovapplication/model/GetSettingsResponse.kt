package com.application.onovapplication.model

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName




class GetSettingsResponse {
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
    var settingsData :SettingsData? = null
}
class SettingsData{
    @SerializedName("userRef")
    @Expose
    var userRef: String? = null

    @SerializedName("fullName")
    @Expose
    var fullName: String? = null

    @SerializedName("profilePic")
    @Expose
    var profilePic: String? = null

    @SerializedName("notification")
    @Expose
    var notification: String? = null

    @SerializedName("donationsVisible")
    @Expose
    var donationsVisible: String? = null
}