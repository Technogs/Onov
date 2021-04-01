package com.application.onovapplication.model

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName




class LoginResponse {
    @SerializedName("status")
    @Expose
    var status: String? = null

    @SerializedName("msg")
    @Expose
    var msg: String? = null

    @SerializedName("varification")
    @Expose
    var varification: String? = null

    @SerializedName("base_url")
    @Expose
    var baseUrl: String? = null

    @SerializedName("UserInfo")
    @Expose
    var userInfo: UserInfo? = null
}

class UserInfo{
    @SerializedName("id")
    @Expose
    var id: String? = null

    @SerializedName("userRef")
    @Expose
    var userRef: String? = null

    @SerializedName("countryCode")
    @Expose
    var countryCode: String? = null

    @SerializedName("fullName")
    @Expose
    var fullName: String? = null

    @SerializedName("email")
    @Expose
    var email: String? = null

    @SerializedName("phone")
    @Expose
    var phone: String? = null

    @SerializedName("password")
    @Expose
    var password: String? = null

    @SerializedName("gender")
    @Expose
    var gender: String? = null

    @SerializedName("notification")
    @Expose
    var notification: String? = null

    @SerializedName("profilePic")
    @Expose
    var profilePic: String? = null

    @SerializedName("role")
    @Expose
    var role: String? = null

    @SerializedName("deviceType")
    @Expose
    var deviceType: String? = null

    @SerializedName("deviceToken")
    @Expose
    var deviceToken: String? = null

    @SerializedName("loginStatus")
    @Expose
    var loginStatus: String? = null

    @SerializedName("Is_Active")
    @Expose
    var isActive: String? = null

    @SerializedName("validationCode")
    @Expose
    var validationCode: String? = null

    @SerializedName("created_at")
    @Expose
    var createdAt: String? = null

    @SerializedName("updated_at")
    @Expose
    var updatedAt: String? = null
}