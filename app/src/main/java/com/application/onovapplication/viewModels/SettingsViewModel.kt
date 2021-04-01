package com.application.onovapplication.viewModels

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.application.onovapplication.R
import com.application.onovapplication.model.GetSettingsResponse
import com.application.onovapplication.model.RegisterResponse
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

class SettingsViewModel : ViewModel() {

    private val dataManager: DataManager = DataManager()
    val successful: MutableLiveData<Boolean> = MutableLiveData()
    val getSettingsSuccess: MutableLiveData<Boolean> = MutableLiveData()
    var message: String = ""
    var status: String = ""
    var getSettingsStatus: String = ""

    lateinit var getSettingsResponse: GetSettingsResponse

    @SuppressLint("CheckResult")
    fun saveSettings(
        context: Context,
        userRef: String,
        notification: String,
        donationVisible: String
    ) {

        val userId: RequestBody =
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), userRef)

        val notificationStatus =
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), notification)

        val donationStatus =
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), donationVisible)

        dataManager.saveSettings(donationStatus, notificationStatus, userId)
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
    fun getSettings(
        context: Context,
        userId: String
    ) {


        val userRef: RequestBody =

            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), userId)


        dataManager.getSettings(userRef)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(
                object : DisposableObserver<GetSettingsResponse>() {
                    override fun onComplete() {

                    }

                    override fun onNext(t: GetSettingsResponse) {
                        getSettingsResponse = t
                        getSettingsStatus = t.status!!
                        message = t.msg!!
                        getSettingsSuccess.value = true
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
}