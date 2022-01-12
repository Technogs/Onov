package com.application.onovapplication.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
@Parcelize
data class SenateResponse(
    val copyright: String,
    val results: List<Result>,
    val status: String
):Parcelable
@Parcelize
data class Result(
    val chamber: String,
    val congress: String,
    val members: List<Member>,
    val num_results: Int,
    val offset: Int
):Parcelable

@Parcelize
data class Member(
    @SerializedName("api_uri")
    val api_uri: String?="",
    @SerializedName("contact_form")
    val contact_form: String?="",
    @SerializedName("cook_pvi")
    val cook_pvi: String?="",
    @SerializedName("crp_id")
    val crp_id: String?="",
    @SerializedName("cspan_id")
    val cspan_id: String?="",
    @SerializedName("date_of_birth")
    val date_of_birth: String?="",
    @SerializedName("dw_nominate")
    val dw_nominate: Double?=0.0,
    @SerializedName("facebook_account")
    val facebook_account: String?="",
    @SerializedName("fax")
    val fax: String?="",
    @SerializedName("fec_candidate_id")
    val fec_candidate_id: String?="",
    @SerializedName("first_name")
    val first_name: String?="",
    @SerializedName("gender")
    val gender: String?="",
    @SerializedName("google_entity_id")
    val google_entity_id: String?="",
    @SerializedName("govtrack_id")
    val govtrack_id: String?="",
    @SerializedName("icpsr_id")
    val icpsr_id: String?="",
    @SerializedName("id")
    val id: String?="",
    @SerializedName("ideal_point")
    val ideal_point: String?="",
    @SerializedName("in_office")
    val in_office: Boolean?=false,
    @SerializedName("last_name")
    val last_name: String?="",
    @SerializedName("last_updated")
    val last_updated: String?="",
    @SerializedName("leadership_role")
    val leadership_role: String?="",
    @SerializedName("lis_id")
    val lis_id: String?="",
    @SerializedName("middle_name")
    val middle_name: String?="",
    @SerializedName("missed_votes")
    val missed_votes: Int?=0,
    @SerializedName("missed_votes_pct")
    val missed_votes_pct: Double?=0.0,
    @SerializedName("next_election")
    val next_election: String?="",
    @SerializedName("ocd_id")
    val ocd_id: String?="",
    @SerializedName("office")
    val office: String?="",
    @SerializedName("party")
    val party: String?="",
    @SerializedName("phone")
    val phone: String?="",
    @SerializedName("rss_url")
    val rss_url: String?="",
    @SerializedName("senate_class")
    val senate_class: String?="",
    @SerializedName("seniority")
    val seniority: String?="",
    @SerializedName("short_title")
    val short_title: String?="",
    @SerializedName("state")
    val state: String?="",
    @SerializedName("state_rank")
    val state_rank: String?="",
    @SerializedName("suffix")
    val suffix: String?="",
    @SerializedName("title")
    val title: String?="",
    @SerializedName("total_present")
    val total_present: Int?=0,
    @SerializedName("total_votes")
    val total_votes: Int?=0,
    @SerializedName("twitter_account")
    val twitter_account: String?="",
    @SerializedName("url")
    val url: String?="",
    @SerializedName("votes_against_party_pct")
    val votes_against_party_pct: Double?=0.0,
    @SerializedName("votes_with_party_pct")
    val votes_with_party_pct: Double?=0.0,
    @SerializedName("votesmart_id")
    val votesmart_id: String?="",
    @SerializedName("youtube_account")
    val youtube_account: String?=""
):Parcelable