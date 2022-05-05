package com.application.onovapplication.viewModels

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.application.onovapplication.R
import com.application.onovapplication.model.DebateDetailResponse
import com.application.onovapplication.model.DebateResponse
import com.application.onovapplication.model.LiveDebateModel
import com.application.onovapplication.model.SearchDebateResponse
import com.application.onovapplication.repository.service.DataManager
import com.tencent.bugly.proguard.r
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.HttpException
import java.io.IOException
import java.util.concurrent.TimeoutException

class DebatesViewModel:ViewModel() {
    private val dataManager: DataManager = DataManager()
    val successful: MutableLiveData<Boolean> = MutableLiveData()
    val successfulLiveDebate: MutableLiveData<Boolean> = MutableLiveData()
    val successfulRequestDebate: MutableLiveData<Boolean> = MutableLiveData()
    val successfulDebateAccept: MutableLiveData<Boolean> = MutableLiveData()
    val successfulSearchDebate: MutableLiveData<Boolean> = MutableLiveData()
    val successfulDebateDetail: MutableLiveData<Boolean> = MutableLiveData()
    var message: String = ""
    lateinit var debates: DebateResponse
    lateinit var liveDebateModel: LiveDebateModel
    lateinit var debateDetailResponse: DebateDetailResponse
    var status: String = ""

    @SuppressLint("CheckResult")
    fun     getDebateRequest(
        context: Context,
        userRef: String
    ) {


        dataManager.getDebateRequest(userRef)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(
                object : DisposableObserver<DebateResponse>() {
                    override fun onComplete() {}

                    override fun onNext(t: DebateResponse) {
                        status = t.status

                        if (status == "success") {
                                debates=t

                        }

                        message = t.msg
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
    fun     getDebateDetail(
        context: Context,
        userRef: String
    ) {


        dataManager.getDebateDetail(userRef)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(
                object : DisposableObserver<DebateDetailResponse>() {
                    override fun onComplete() {}

                    override fun onNext(t: DebateDetailResponse) {
                        status = t.status

                        if (status == "success") {
                                debateDetailResponse=t

                        }

                        message = t.msg
                        successfulDebateDetail.value = true
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
                        successfulDebateDetail.value = false
                    }
                })
    }


    @SuppressLint("CheckResult")
    fun     searchDebate(
        context: Context,
        userRef: String,
        keyword: String
    ) {

        val keyword: RequestBody =
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), keyword)
        val userRef: RequestBody =
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), userRef)

        dataManager.searchDebate(userRef,keyword)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(
                object : DisposableObserver<LiveDebateModel>() {
                    override fun onComplete() {}

                    override fun onNext(t: LiveDebateModel) {
                        status = t.status

                        if (status == "success") {

liveDebateModel=t
                        }

                        message = t.msg
                        successfulSearchDebate.value = true
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
                        successfulSearchDebate.value = false
                    }
                })
    }

    @SuppressLint("CheckResult")
    fun     getlivedebate(
        context: Context,
        //userRef: String
    ) {


        dataManager.getlivedebate()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(
                object : DisposableObserver<LiveDebateModel>() {
                    override fun onComplete() {}

                    override fun onNext(t: LiveDebateModel) {
                        status = t.status

                        if (status == "success") {
                                liveDebateModel=t

                        }

                        message = t.msg
                        successfulLiveDebate.value = true
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
                        successfulLiveDebate.value = false
                    }
                })
    }


    @SuppressLint("CheckResult")
    fun requestDebate(
        context: Context,
        userRefTo: String,
        userRefFrom: String,
        title: String,
        message: String,
        date: String,
        time: String,
        isPublic: String,
        debateDuration: String,
        areaLimit: String
    ) {



        val userRefTo: RequestBody =
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), userRefTo)
        val userRefFrom: RequestBody =
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), userRefFrom)
        val title: RequestBody =
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), title)
        var message_: RequestBody =
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), message)
        val date_: RequestBody =
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), date)
        val time: RequestBody =
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), time)
        val areaLimit: RequestBody =
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), areaLimit)
        val isPublic: RequestBody =
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), isPublic)
        val debateDuration: RequestBody =
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), debateDuration)

        dataManager.requestDebate(userRefTo,userRefFrom,title,message_,date_,time,isPublic,debateDuration,areaLimit)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(
                object : DisposableObserver<DebateResponse>() {
                    override fun onComplete() {}

                    override fun onNext(t: DebateResponse) {
//                        getFeedResponse = t
                      status = t.status
                      this@DebatesViewModel.message = t.msg
                        successfulRequestDebate.value = true

                    }

                    override fun onError(e: Throwable) {
                        when (e) {
                            is IOException -> {
                                this@DebatesViewModel.message = context.getString(R.string.error_please_check_internet)
                            }
                            is TimeoutException -> {
                                this@DebatesViewModel.message = context.getString(R.string.error_request_timed_out)
                            }
                            is HttpException -> {
                                this@DebatesViewModel.message = e.message.toString()
                            }
                            else -> {
                                this@DebatesViewModel.message = context.getString(R.string.error_something_went_wrong)
                            }
                        }
                        successfulRequestDebate.value = false
                    }
                })
    }



    @SuppressLint("CheckResult")
    fun acceptRequest(
        context: Context,
        requestId: String,
        isAccept: String,

    ) {

        val requestId: RequestBody =
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), requestId)
        val isAccept: RequestBody =
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), isAccept)


        dataManager.acceptRequest(requestId,isAccept)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(
                object : DisposableObserver<DebateResponse>() {
                    override fun onComplete() {}

                    override fun onNext(t: DebateResponse) {
//                        getFeedResponse = t
                        status = t.status
                        message = t.msg
                        successfulDebateAccept.value = true

                    }

                    override fun onError(e: Throwable) {
                        when (e) {
                            is IOException -> {
                                this@DebatesViewModel.message = context.getString(R.string.error_please_check_internet)
                            }
                            is TimeoutException -> {
                                this@DebatesViewModel.message = context.getString(R.string.error_request_timed_out)
                            }
                            is HttpException -> {
                                this@DebatesViewModel.message = e.message.toString()
                            }
                            else -> {
                                this@DebatesViewModel.message = context.getString(R.string.error_something_went_wrong)
                            }
                        }
                        successfulDebateAccept.value = false
                    }
                })
    }

}