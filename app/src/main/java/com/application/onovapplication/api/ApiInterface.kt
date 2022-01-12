package com.application.onovapplication.api

import com.application.onovapplication.model.DebateJoinerResponse
import com.application.onovapplication.model.PresidentResponse
import com.application.onovapplication.model.SenateResponse
import retrofit2.Call
import retrofit2.http.*

interface ApiInterface {

    @Headers("X-Api-Key: eL7l0ylsnLmmRE5vlPQEowt1RoAxtPcS5s9RjJAz")
    @GET("members.json")
    fun classlist(): Call<SenateResponse?>?


    //@FormUrlEncoded  https://stackoverflow.com/questions/40214392/android-retrofit2-posting-image-file-in-multipart-request
    @Headers("X-Api-Key: onovApp@onovapplication.com")
    @FormUrlEncoded
    @POST("debaterVote")
    fun debaterVote(
        @Field("voteFrom") voteFrom: String?,
        @Field("voteTo") voteTo: String?,
        @Field("debateId") debateId: String?
    ): Call<DebateJoinerResponse?>?

    @Headers("X-Api-Key: onovApp@onovapplication.com")
    @FormUrlEncoded
    @POST("getdebaterjoiner")
    fun getdebaterjoiner(
        @Field("debateId") deviceType: String?
    ): Call<DebateJoinerResponse?>?

//    @Headers("X-Api-Key: eL7l0ylsnLmmRE5vlPQEowt1RoAxtPcS5s9RjJAz")
    @GET("page?q=POTUS/")
    fun presidentData(): Call<PresidentResponse?>?
}