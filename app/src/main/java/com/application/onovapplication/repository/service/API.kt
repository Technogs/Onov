package com.application.onovapplication.repository.service

import com.application.onovapplication.model.*
import io.reactivex.Observable
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface API {

    @Multipart
    @POST("userLogin")
    fun login(
        @Part("FieldType") fieldType: RequestBody?,
        @Part("email") email: RequestBody?,
        @Part("Password") Password: RequestBody?,
        @Part("deviceType") mobileType: RequestBody?,
        @Part("deviceToken") deviceToken: RequestBody?,
        @Part("Role") role: RequestBody?
    ): Observable<LoginResponse>

    @Multipart
    @POST("userRegister")
    fun register(
        @Part("fullName") name: RequestBody?,
        @Part("email") email: RequestBody?,
        @Part("phone") phone: RequestBody?,
        @Part("password") password: RequestBody,
        @Part("role") role: RequestBody,
        @Part("deviceToken") device_token: RequestBody,
        @Part("countryCode") countryCode: RequestBody,
        @Part("deviceType") mobileType: RequestBody,
        @Part profile_image: MultipartBody.Part
    ): Observable<RegisterResponse>


    @Multipart
    @POST("GetUserInfo")
    fun getUserInfo(
        @Part("Key") key: RequestBody,
        @Part("Value") value: RequestBody
    ): Observable<LoginResponse>

    @Multipart
    @POST("editUser")
    fun editProfile(
        @Part("fullName") name: RequestBody,
        @Part("email") email: RequestBody?,
        @Part("phone") phone: RequestBody?,
        @Part("userRef") userRef: RequestBody,
        @Part("countryCode") countryCode: RequestBody,
        @Part profile_image: MultipartBody.Part
    ): Observable<LoginResponse>


    @Multipart
    @POST("CreateNewPassword")
    fun createNewPassword(
        @Part("email") email: RequestBody, @Part("password") password: RequestBody
    ): Observable<RegisterResponse>

    @Multipart
    @POST("ForgetPassword")
    fun forgetPassword(
        @Part("email") email: RequestBody
    ): Observable<RegisterResponse>

    @Multipart
    @POST("userOut")
    fun logOut(@Part("userRef") userRef: RequestBody): Observable<RegisterResponse>

    @Multipart
    @POST("changePassword")
    fun changePassword(
        @Part("userRef") userRef: RequestBody,
        @Part("newPass") newPassword: RequestBody,
        @Part("oldPass") pldPassword: RequestBody
    ): Observable<RegisterResponse>


    @Multipart
    @POST("userVerify")
    fun verify(
        @Part("email") mobile: RequestBody,
        @Part("Code") code: RequestBody
    ): Observable<LoginResponse>


    @Multipart
    @POST("sellerSetting")
    fun saveSetting(
        @Part("userRef") userRef: RequestBody,
        @Part("notification") notification: RequestBody,
        @Part("donationVisible") donationVisible: RequestBody
    ): Observable<RegisterResponse>

    @Multipart
    @POST("getSellerSetting")
    fun getSettings(
        @Part("userRef") userRef: RequestBody
    ): Observable<GetSettingsResponse>


    @GET("getStats/{id}")
    fun getStats(
        @Path("id") userRef: String
    ): Observable<GetStatsResponse>


    @GET("getWinner/{id}")
    fun getWinnings(
        @Path("id") userRef: String
    ): Observable<GetStatsResponse>

    @GET("getAllWinner")
    fun getWinners(
    ): Observable<GetAllWinnersResponse>

    @GET("getNotification/{id}")
    fun getNotifications(
        @Path("id") userRef: String
    ): Observable<GetNotificationResponse>

}