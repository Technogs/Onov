package com.application.onovapplication.model

import android.os.Parcelable
import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize


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

@Parcelize
data class UserInfo(
    @SerializedName("id")
    @Expose
    var id: String? = null,

    @SerializedName("userRef")
    @Expose
    var userRef: String? = null,

    @SerializedName("fullName")
    @Expose
    var fullName: String? = null,

    @SerializedName("email")
    @Expose
    var email: String? = null,

    @SerializedName("countryCode")
    @Expose
    var countryCode: String? = null,

    @SerializedName("phone")
    @Expose
    var phone: String? = null,

    @SerializedName("password")
    @Expose
    var password: String? = null,

    @SerializedName("notification")
    @Expose
    var notification: String? = null,

    @SerializedName("donationsVisible")
    @Expose
    var donationsVisible: String? = null,

    @SerializedName("profilePic")
    @Expose
    var profilePic: String? = null,

    @SerializedName("coverPhoto")
    @Expose
    var coverPhoto: String? = null,

    @SerializedName("role")
    @Expose
    var role: String? = null,

    @SerializedName("supporter")
    @Expose
    var supporter: String? = null,

    @SerializedName("about")
    @Expose
    var about: String? = null,

    @SerializedName("webUrl")
    @Expose
    var webUrl: String? = null,

    @SerializedName("deviceType")
    @Expose
    var deviceType: String? = null,

    @SerializedName("deviceToken")
    @Expose
    var deviceToken: String? = null,

    @SerializedName("loginStatus")
    @Expose
    var loginStatus: String? = null,

    @SerializedName("Is_Active")
    @Expose
    var isActive: String? = null,

    @SerializedName("validationCode")
    @Expose
    var validationCode: String? = null,

    @SerializedName("created_at")
    @Expose
    var createdAt: String? = null,

    @SerializedName("updated_at")
    @Expose
    var updatedAt: String? = null
) : Parcelable