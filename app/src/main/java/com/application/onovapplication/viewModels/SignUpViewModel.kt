package com.application.onovapplication.viewModels

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.application.onovapplication.R
import com.application.onovapplication.model.RegisterResponse
import com.application.onovapplication.model.RegistrationData
import com.application.onovapplication.model.UserInfo
import com.application.onovapplication.repository.service.DataManager

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.HttpException
import java.io.File
import java.io.IOException
import java.util.concurrent.TimeoutException

class SignUpViewModel : ViewModel() {

    var tag = SignUpViewModel::class.java.simpleName

    private val dataManager: DataManager = DataManager()
    val successful: MutableLiveData<Boolean> = MutableLiveData()
    var message: String = ""
    var status: String = ""
    var userInfo: UserInfo? = null

    @SuppressLint("CheckResult")
    fun register(
        context: Context,
        email: String,
        password: String,
        user_name: String,
        phone_number: String,
        countryName: String,
        stateName: String,
        cityName: String,
        countryCode: String,
        image: File?,
        role: String,
        device_token: String,
        oSTYpe: String,
        aboutSection: String,
        websiteUrl : String,
        coverPhoto: File?,
        party:String
    ) {

        var body: MultipartBody.Part? = null
        var body2: MultipartBody.Part? = null

        if (image != null) {
            val requestFile = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), image)
            body = MultipartBody.Part.createFormData("profilePic", image.name, requestFile)
        }

        if (coverPhoto!=null)
        {
            val requestFile = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), coverPhoto)
            body2 = MultipartBody.Part.createFormData("coverPhoto", coverPhoto.name, requestFile)
        }

        val about : RequestBody =  RequestBody.create("multipart/form-data".toMediaTypeOrNull(), aboutSection)
        val website : RequestBody =  RequestBody.create("multipart/form-data".toMediaTypeOrNull(), websiteUrl)
        val partySupport : RequestBody =  RequestBody.create("multipart/form-data".toMediaTypeOrNull(), party)



        val countryName: RequestBody =
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), countryName)
        val stateName: RequestBody =
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), stateName)
        val cityName: RequestBody =
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), cityName)
        val user_email: RequestBody =
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), email)

        val userPassword: RequestBody =
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), password)

        val name: RequestBody =
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), user_name)

        val phoneNumber: RequestBody =
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), phone_number)

        val phoneCountryCode: RequestBody =
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), countryCode)

        val userRole: RequestBody = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), role)
        val deviceToken: RequestBody =
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), device_token)

        val mobileType: RequestBody =
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), oSTYpe)



        dataManager.register(
            name,
            user_email,
            phoneNumber,
            userPassword,
            userRole,
            deviceToken,
            countryName,
            stateName,
            cityName,
            phoneCountryCode,
            mobileType,
            body!!,
            body2!!,
            about,
            website,
            partySupport
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : DisposableObserver<RegisterResponse>() {
                override fun onComplete() {

                }

                override fun onNext(t: RegisterResponse) {
                    status = t.status!!
                    if (t.status == "success") {
                        userInfo = t.registrationData!!
                    }

                    message = t.msg!!
                    successful.value = true

                }

                override fun onError(e: Throwable) {
                    when (e) {
                        is IOException -> {
                            message = context
                                .getString(R.string.error_please_check_internet)
                        }
                        is TimeoutException -> {
                            message = context
                                .getString(R.string.error_request_timed_out)
                        }
                        is HttpException -> {
                            message = e.message.toString()
                        }
                        else -> {
                            message = context
                                .getString(R.string.error_something_went_wrong)
                            Log.e(tag, e.localizedMessage!!)
                        }
                    }
                    successful.value = false
                }
            })
    }

}


