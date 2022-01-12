package com.application.onovapplication.viewModels

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.application.onovapplication.R
import com.application.onovapplication.model.FeedModel
import com.application.onovapplication.model.GetStatsResponse
import com.application.onovapplication.model.LoginResponse
import com.application.onovapplication.repository.service.DataManager
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.HttpException
import java.io.File
import java.io.IOException
import java.util.concurrent.TimeoutException

class StatsViewModel : ViewModel() {


    private val dataManager: DataManager = DataManager()
    val successful: MutableLiveData<Boolean> = MutableLiveData()
    val successfulAddLaw: MutableLiveData<Boolean> = MutableLiveData()
    val successfulEditLaw: MutableLiveData<Boolean> = MutableLiveData()
    var message: String = ""
    var status: String = ""
    lateinit var statsResponse: GetStatsResponse


    @SuppressLint("CheckResult")
    fun     getStats(
        context: Context,
        userRef: String
    ) {


        dataManager.getStats(userRef)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(
                object : DisposableObserver<GetStatsResponse>() {
                    override fun onComplete() {

                    }

                    override fun onNext(t: GetStatsResponse) {
                        status = t.status!!

                        if (status == "success") {

                            statsResponse = t
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
                            }
                        }
                        successful.value = false
                    }
                })
    }


    @SuppressLint("CheckResult")
    fun addLaws(
        context: Context, lawTitle: String, userRef: String, description: String, fileType: String, documentFile: File?
    ) {
        var body: MultipartBody.Part? = null

        body = if (documentFile != null) {
            val requestFile = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), documentFile)
            MultipartBody.Part.createFormData("documentFile", documentFile.name, requestFile)
        }

        else {
            val requestFile = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), "")
            MultipartBody.Part.createFormData("documentFile", "", requestFile)
        }


//        var body: MultipartBody.Part? = null
//        body = if (chatImage != null) {
//            val requestFile = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), chatImage)
//            MultipartBody.Part.createFormData("chatImg", chatImage.name, requestFile)
//        } else {
//            val requestFile = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), "")
//            MultipartBody.Part.createFormData("chatImg", "", requestFile)
//        }






        val fileType: RequestBody =
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), fileType)
        val userReference: RequestBody =
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), userRef)
        val lawTitle: RequestBody =
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), lawTitle)

        val desc: RequestBody =
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), description)

        Log.e("file","file addLaws ")

        dataManager.addLaws(
            userReference, lawTitle, desc, fileType,  body
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(
                object : DisposableObserver<FeedModel>() {
                    override fun onComplete() {
                        Log.e("file","file onComplete ")
                    }

                    override fun onNext(t: FeedModel) {
                        Log.e("file","file onNext ")
                        message = t.msg
                        successfulAddLaw.value = true
                    }

                    override fun onError(e: Throwable) {
                        Log.e("file","file error ffff "+e.localizedMessage)
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
                        successfulAddLaw.value = false
                    }
                })
    }


    @SuppressLint("CheckResult")
    fun editLaws(
        context: Context, recordId: String, recordType: String, title: String, description: String,fileType: String,donationGoal: String, mediaFile: File?
    ) {
        var body: MultipartBody.Part? = null

        body = if (mediaFile != null) {
            val requestFile = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), mediaFile)
            MultipartBody.Part.createFormData("mediaFile", mediaFile.name, requestFile)
        } else {
            val requestFile = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), "")
            MultipartBody.Part.createFormData("mediaFile", "", requestFile)
        }

        val recordId: RequestBody =
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), recordId)
        val recordType: RequestBody =
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), recordType)
        val title: RequestBody =
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), title)

        val desc: RequestBody =
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), description)
        val fileType: RequestBody =
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), fileType)
        val donationGoal: RequestBody =
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), donationGoal)

        Log.e("file","file addLaws ")

        dataManager.editFeed(
            recordId, recordType, title,desc,fileType, donationGoal,  body
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(
                object : DisposableObserver<FeedModel>() {
                    override fun onComplete() {
                        Log.e("file","file onComplete ")
                    }

                    override fun onNext(t: FeedModel) {
                        Log.e("file","file onNext ")
                        message = t.msg
                        successfulEditLaw.value = true
                    }

                    override fun onError(e: Throwable) {
                        Log.e("file","file error ffff "+e.localizedMessage)
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
                        successfulEditLaw.value = false
                    }
                })
    }
}