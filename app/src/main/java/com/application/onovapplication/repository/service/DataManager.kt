package com.application.onovapplication.repository.service

import com.application.onovapplication.model.*

import com.live.kicktraders.repository.ApiManager
import io.reactivex.Observable
import okhttp3.MultipartBody
import okhttp3.RequestBody

class DataManager {

    private val apiManager = ApiManager.instance

    fun register(
        name: RequestBody,
        email: RequestBody,
        phone: RequestBody,
        password: RequestBody,
        role: RequestBody,
        device_token: RequestBody,
        phoneCountryCode: RequestBody,
        mobileType: RequestBody,
        image: MultipartBody.Part
    ): Observable<RegisterResponse> {
        return apiManager.authService.register(
            name,
            email,
            phone,
            password,
            role,
            device_token,
            phoneCountryCode,
            mobileType,
            image
        )
    }


    fun login(
        email: RequestBody,
        password: RequestBody,
        fieldType: RequestBody,
        osType: RequestBody,
        deviceToken: RequestBody,
        role: RequestBody
    ): Observable<LoginResponse> {

        return apiManager.authService.login(fieldType, email , password, osType, deviceToken, role)
    }


    fun getProfile(
        key: RequestBody, value: RequestBody
    ): Observable<LoginResponse> {
        return apiManager.authService.getUserInfo(key, value)
    }


    fun editProfile(
        name: RequestBody,
        email: RequestBody,
        phone: RequestBody,
        userRef: RequestBody,
        countryCode: RequestBody,
        photo: MultipartBody.Part
    ): Observable<LoginResponse> {

        return apiManager.authService.editProfile(
            name, email, phone, userRef,countryCode, photo
        )
    }


    fun saveSettings(
        donationVisible: RequestBody, notification: RequestBody, userRef: RequestBody
    ): Observable<RegisterResponse> {
        return apiManager.authService.saveSetting(userRef, notification, donationVisible)
    }

    fun getSettings(
        userRef: RequestBody
    ): Observable<GetSettingsResponse> {
        return apiManager.authService.getSettings(userRef)
    }

    fun getStats(
        userRef: String
    ): Observable<GetStatsResponse> {
        return apiManager.authService.getStats(userRef)
    }


    fun getWinnings(
        userRef: String
    ): Observable<GetStatsResponse> {
        return apiManager.authService.getWinnings(userRef)
    }


    fun getAllWinners(
    ): Observable<GetAllWinnersResponse> {
        return apiManager.authService.getWinners()
    }


    fun getNotifications(
        userRef: String
    ): Observable<GetNotificationResponse> {
        return apiManager.authService.getNotifications(userRef)
    }

    fun forgetPassword(
        email: RequestBody
    ): Observable<RegisterResponse> {
        return apiManager.authService.forgetPassword(email)
    }


    fun createNewPassword(
        email: RequestBody, password: RequestBody
    ): Observable<RegisterResponse> {
        return apiManager.authService.createNewPassword(email, password)
    }

    fun logout(
        userRef: RequestBody
    ): Observable<RegisterResponse> {
        return apiManager.authService.logOut(userRef)
    }



    fun changePassword(
        userRef: RequestBody, oldPassword: RequestBody, newPassword: RequestBody
    ): Observable<RegisterResponse> {
        return apiManager.authService.changePassword(userRef, newPassword, oldPassword)
    }

    fun verifyOtp(email: RequestBody, otp: RequestBody): Observable<LoginResponse> {

        return apiManager.authService.verify(email , otp)

    }

}