package com.application.onovapplication.viewModels


import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.application.onovapplication.R
import com.application.onovapplication.model.RegisterResponse
import com.application.onovapplication.repository.service.DataManager
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import retrofit2.HttpException
import java.io.IOException
import java.util.concurrent.TimeoutException

class NewPasswordViewModel : ViewModel() {

    private val dataManager: DataManager = DataManager()
    val successful: MutableLiveData<Boolean> = MutableLiveData()
    val successfullyChangedPassword: MutableLiveData<Boolean> = MutableLiveData()
    var message: String = ""
    var status: String = ""

    @SuppressLint("CheckResult")
    fun createNewPassword(
        context: Context,
        email: String,
        otp: String,
        password: String
    ) {


        val userEmail: RequestBody =
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), email)
        val userPassword: RequestBody =
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), password)
        val otp: RequestBody =
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), otp)


        dataManager.createNewPassword(userEmail, otp, userPassword)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(
                object : DisposableObserver<RegisterResponse>() {
                    override fun onComplete() {

                    }

                    override fun onNext(t: RegisterResponse) {
                        status = t.status!!

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
                            }
                        }
                        successful.value = false
                    }
                })
    }

    @SuppressLint("CheckResult")
    fun changePassword(
        context: Context,
        userReference: String,
        oldPassword: String,
        newPassword: String
    ) {


        val userRef: RequestBody =
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), userReference)

        val oldPass: RequestBody =
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), oldPassword)

        val newUserPassword: RequestBody =
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), newPassword)


        dataManager.changePassword(userRef, oldPass, newUserPassword)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(
                object : DisposableObserver<RegisterResponse>() {
                    override fun onComplete() {

                    }

                    override fun onNext(t: RegisterResponse) {
                        status = t.status!!

                        message = t.msg!!
                        successfullyChangedPassword.value = true
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
                        successfullyChangedPassword.value = false
                    }
                })
    }

}