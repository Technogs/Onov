package com.application.onovapplication.api

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ApiError {
    @SerializedName("type")
    @Expose
    private var type: List<String?>? = null

    fun getType(): List<String?>? {
        return type
    }

    fun setType(type: List<String?>?) {
        this.type = type
    }

    fun withType(type: List<String?>?): ApiError? {
        this.type = type
        return this
    }
}