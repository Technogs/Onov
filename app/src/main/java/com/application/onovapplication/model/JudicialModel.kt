package com.application.onovapplication.model
import com.google.gson.annotations.SerializedName


data class JudicialModel(
    @SerializedName("judicialData")
    val judicialData: List<JudicialData>,
    @SerializedName("msg")
    val msg: String,
    @SerializedName("status")
    val status: String
)

data class JudicialData(
    @SerializedName("age")
    val age: String,
    @SerializedName("appointmentBy")
    val appointmentBy: String,
    @SerializedName("appointmentDate")
    val appointmentDate: String,
    @SerializedName("created_at")
    val created_at: String,
    @SerializedName("dob")
    val dob: String,
    @SerializedName("id")
    val id: String,
    @SerializedName("memberPic")
    val memberPic: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("scv")
    val scv: String,
    @SerializedName("updated_at")
    val updated_at: String
)