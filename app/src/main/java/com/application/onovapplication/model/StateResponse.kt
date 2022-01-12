package com.application.onovapplication.model

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize


@Parcelize
class StateResponse : Parcelable {
    @SerializedName("status")
    @Expose
    var status: String? = null

    @SerializedName("msg")
    @Expose
    var msg: String? = null

    @SerializedName("StatesData")
    @Expose
    var statesData: MutableList<statesData>? = null
}


@Parcelize
class statesData : Parcelable {
    @SerializedName("state")
    @Expose
    var state: String? = null
}


