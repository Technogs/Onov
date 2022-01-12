package com.application.onovapplication.viewModels

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.application.onovapplication.R
import com.application.onovapplication.model.LoginResponse
import com.application.onovapplication.model.RegisterResponse
import com.application.onovapplication.model.UserInfo
import com.application.onovapplication.repository.service.DataManager
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import retrofit2.HttpException
import java.io.IOException
import java.util.concurrent.TimeoutException

class VerifyViewModel : ViewModel() {


    private val dataManager: DataManager = DataManager()
    val successfullyVerified: MutableLiveData<Boolean> = MutableLiveData()
    val otpSentSuccess: MutableLiveData<Boolean> = MutableLiveData()


    var message: String = ""
    var verifyStatus: String = ""
    var otpStatus: String = ""
    lateinit var userInfo: UserInfo


    @SuppressLint("CheckResult")
    fun verifyOtp(
        context: Context,
        userEmail: String,
        code: String
    ) {

        val email: RequestBody =
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), userEmail)
        val otp: RequestBody =
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), code)

        dataManager.verifyOtp(email, otp)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(
                object : DisposableObserver<LoginResponse>() {
                    override fun onComplete() {

                    }

                    override fun onNext(t: LoginResponse) {
                        verifyStatus = t.status!!

                        if (verifyStatus == "success") {

                            userInfo = t.userInfo!!
                        }

                        message = t.msg!!
                        successfullyVerified.value = true
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
                            }
                        }
                        successfullyVerified.value = false
                    }
                })

    }

    @SuppressLint("CheckResult")
    fun forgetPassword(
        context: Context,
        userEmail: String
    ) {


        val email: RequestBody = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), userEmail)



        dataManager.forgetPassword(email)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(
                object : DisposableObserver<RegisterResponse>() {
                    override fun onComplete() {

                    }

                    override fun onNext(t: RegisterResponse) {
                        verifyStatus = t.status!!


                        message = t.msg!!
                        otpSentSuccess.value = true
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
                            }
                        }
                        otpSentSuccess.value = false
                    }
                })

    }

}