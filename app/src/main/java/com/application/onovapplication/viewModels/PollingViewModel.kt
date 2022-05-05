package com.application.onovapplication.viewModels

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.application.onovapplication.R
import com.application.onovapplication.model.*
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
import okhttp3.MultipartBody
import java.io.File


class PollingViewModel:ViewModel() {

    private val dataManager: DataManager = DataManager()
    val successful: MutableLiveData<Boolean> = MutableLiveData()
    val successfulSubmitPoll: MutableLiveData<Boolean> = MutableLiveData()
    val successfulResultPoll: MutableLiveData<Boolean> = MutableLiveData()
    val successfulEndPoll: MutableLiveData<Boolean> = MutableLiveData()
    val successfulAllPoll: MutableLiveData<Boolean> = MutableLiveData()
    var message: String = ""
    var status: String = ""
    lateinit var pollResultResponse: PollResultResponse
    lateinit var searchModel: SearchModel
    lateinit var pollListsResponse: PollListsResponse


    @SuppressLint("CheckResult")
    fun cretePolling(
        context: Context,
        createBy: String,
        pollTitle: String,
        options: String,
        tillDate: String,
        tillTime: String,
        areaLimit: String,
        isPublic: String,
        isMultiple: String,
        pollImage: File?
    ) {
//Log.d("sizeis" ,ids.size.toString())
        var body: MultipartBody.Part? = null
        body = if (pollImage != null) {
            val requestFile = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), pollImage)
            MultipartBody.Part.createFormData("pollImage", pollImage.name, requestFile)
        } else {
            val requestFile = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), "")
            MultipartBody.Part.createFormData("pollImage", "", requestFile)
        }

        val createBy: RequestBody =
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), createBy)
        val pollTitle: RequestBody =
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), pollTitle)

        val tillDate: RequestBody =
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), tillDate)

        val tillTime: RequestBody =
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), tillTime)
        val options: RequestBody =
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), options)
        val areaLimit: RequestBody =
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), areaLimit)
        val isPublic: RequestBody =
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), isPublic)
        val isMultiple: RequestBody =
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), isMultiple)


        dataManager.createpolling(createBy, pollTitle, options, tillDate, tillTime, areaLimit, isPublic, isMultiple,body)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(
                object : DisposableObserver<LoginResponse>() {
                    override fun onComplete() {

                    }

                    override fun onNext(t: LoginResponse) {
                        status = t.status!!

                        if (status == "success") {

                          //  userInfo = t.userInfo!!
                        }

                        message = t.msg!!
                        successful.value = true
                    }

                    override fun onError(e: Throwable) {
                        Log.d("error",e.message.toString())
                        when (e) {
                            is IOException -> {

                                message =e.message.toString() //context.getString(R.string.error_please_check_internet)
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
    fun polling(
        context: Context,
        pollingId: String,
        pollByRef: String,
        pollOption: String
    ) {
        val pollingId: RequestBody =
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), pollingId)
        val pollByRef: RequestBody =
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), pollByRef)

        val pollOption: RequestBody =
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), pollOption)

        dataManager.polling( pollingId, pollByRef, pollOption)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(
                object : DisposableObserver<RegisterResponse>() {
                    override fun onComplete() {

                    }

                    override fun onNext(t: RegisterResponse) {
                        status = t.status!!

                        if (status == "success") {

                        }

                        message = t.msg!!
                        successfulSubmitPoll.value = true
                    }

                    override fun onError(e: Throwable) {
                        Log.d("error",e.message.toString())
                        when (e) {
                            is IOException -> {

                                message =e.message.toString() //context.getString(R.string.error_please_check_internet)
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
                        successfulSubmitPoll.value = false
                    }
                })
    } @SuppressLint("CheckResult")

    fun endpoll(
        context: Context,
        userRef: String,
        pollId: String
    ) {
        val userRef: RequestBody =
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), userRef)
        val pollId: RequestBody =
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), pollId)

        dataManager.endpoll( userRef, pollId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(
                object : DisposableObserver<SearchModel>() {
                    override fun onComplete() {}

                    override fun onNext(t: SearchModel) {
                        status = t.status!!

                        if (status == "success") {
                         searchModel=t
                        }

                        message = t.msg!!
                        successfulEndPoll.value = true
                    }

                    override fun onError(e: Throwable) {
                        Log.d("error",e.message.toString())
                        when (e) {
                            is IOException -> {

                                message =e.message.toString() //context.getString(R.string.error_please_check_internet)
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
                        successfulEndPoll.value = false
                    }
                })
    }

    @SuppressLint("CheckResult")
    fun getpollresult(
        context: Context,
        pollId: String,
        userRef: String
    ) {
        val pollId: RequestBody =
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), pollId)
        val userRef: RequestBody =
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), userRef)

        dataManager.getpollresult( pollId,userRef)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(
                object : DisposableObserver<PollResultResponse>() {
                    override fun onComplete() {

                    }

                    override fun onNext(t: PollResultResponse) {
                        status = t.status!!

                        if (status == "success") {
                            pollResultResponse=t

                        }

                        message = t.msg!!
                        successfulResultPoll.value = true
                    }

                    override fun onError(e: Throwable) {
                        Log.d("error",e.message.toString())
                        when (e) {
                            is IOException -> {

                                message =e.message.toString() //context.getString(R.string.error_please_check_internet)
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
                        successfulResultPoll.value = false
                    }
                })
    }


    @SuppressLint("CheckResult")
    fun getallpoll(
        context: Context,
        userRef: String
    ) {
        val userRef: RequestBody =
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), userRef)

        dataManager.getallpoll( userRef)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(
                object : DisposableObserver<PollListsResponse>() {
                    override fun onComplete() {}

                    override fun onNext(t: PollListsResponse) {
                        status = t.status!!

                        if (status == "success") {
                            pollListsResponse=t

                        }

                        message = t.msg!!
                        successfulAllPoll.value = true
                    }

                    override fun onError(e: Throwable) {
                        Log.d("error",e.message.toString())
                        when (e) {
                            is IOException -> {

                                message =e.message.toString() //context.getString(R.string.error_please_check_internet)
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
                        successfulAllPoll.value = false
                    }
                })
    }
}