package com.application.onovapplication.viewModels

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.application.onovapplication.R
import com.application.onovapplication.model.EventModel
import com.application.onovapplication.model.GetStatsResponse
import com.application.onovapplication.model.LoginResponse
import com.application.onovapplication.model.SearchModel
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

class EventViewModel:ViewModel() {
    private val dataManager: DataManager = DataManager()
    val successful: MutableLiveData<Boolean> = MutableLiveData()
    val successfulAddEvent: MutableLiveData<Boolean> = MutableLiveData()
    var message: String = ""
    var status: String = ""
    lateinit var loginResponse: LoginResponse
    lateinit var eventResponse: EventModel

    @SuppressLint("CheckResult")
    fun     getEvent(context: Context,userRef: String,
                     searchkeyword: String) {


        val userId: RequestBody =
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), userRef)
        val searchkeyword: RequestBody =
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), searchkeyword)

        dataManager.getEvent(userId,searchkeyword)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(
                object : DisposableObserver<EventModel>() {
                    override fun onComplete() {}

                    override fun onNext(t: EventModel) {
                        status = t.status

                        if (status == "success") {

                            eventResponse = t
                        }

                        message = t.msg
                        successful.value = true
                    }

                    override fun onError(e: Throwable) {
                        when (e) {
                            is IOException -> {
                                message = context.getString(R.string.error_please_check_internet)
                            }
                            is TimeoutException -> {
                                message = context.getString(R.string.error_request_timed_out)
                            }
                            is HttpException -> {
                                message = e.message.toString()
                            }
                            else -> {
                                message = context.getString(R.string.error_something_went_wrong)
                            }
                        }
                        successful.value = false
                    }
                })
    }

    @SuppressLint("CheckResult")
    fun createEvent(
        context: Context,
        userRef: String,
        title: String,
        description: String,
        price: String,
        start_date: String,
        start_time: String,
        end_date: String,
        end_time: String,
        areaLimit: String,
        cover_image: File?,
        ent_video: File?
    ) {

        var bodyimage: MultipartBody.Part? = null
        var bodyvideo: MultipartBody.Part? = null

        bodyimage =   if (cover_image != null) {
            val requestFile = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), cover_image)
          MultipartBody.Part.createFormData("cover_image", cover_image.name, requestFile)
        }else{
            val requestFile = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), "")
            MultipartBody.Part.createFormData("signImage", "", requestFile)
        }
        bodyvideo =   if (ent_video != null) {
            val requestFile = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), ent_video)
           MultipartBody.Part.createFormData("ent_video", ent_video.name, requestFile)
        }else{
            val requestFile = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), "")
            MultipartBody.Part.createFormData("signImage", "", requestFile)
        }

        val userRef: RequestBody =
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), userRef)
        val title: RequestBody =
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), title)
        val description: RequestBody =
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), description)
        val price: RequestBody =
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), price)
        val start_date: RequestBody =
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), start_date)
        val start_time: RequestBody =
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), start_time)
        val end_date: RequestBody =
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), end_date)
        val end_time: RequestBody =
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), end_time)
        val areaLimit: RequestBody =
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), areaLimit)

        dataManager.createEvent(
            userRef,
            title,
            description,
            price,
            start_date,
            start_time,
            end_date,
            end_time,
            areaLimit,
            bodyimage!!,
            bodyvideo!!
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : DisposableObserver<LoginResponse>() {
                override fun onComplete() {

                }

                override fun onNext(t: LoginResponse) {
                    status = t.status!!
                    if (t.status == "success") {
                        loginResponse = t
                    }

                    message = t.msg!!
                    successfulAddEvent.value = true

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
                            Log.e("tag", e.localizedMessage!!)
                        }
                    }
                    successfulAddEvent.value = false
                }
            })
    }
}