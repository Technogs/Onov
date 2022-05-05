package com.application.onovapplication.viewModels

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.application.onovapplication.R
import com.application.onovapplication.model.ChatImageResponse
import com.application.onovapplication.model.ChatModel
import com.application.onovapplication.model.LoginResponse
import com.application.onovapplication.model.RegisterResponse
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

class PostViewModel: ViewModel() {

    private val dataManager: DataManager = DataManager()
    val successful: MutableLiveData<Boolean> = MutableLiveData()
    val successfulShareFeed: MutableLiveData<Boolean> = MutableLiveData()
    val successfulChatImg: MutableLiveData<Boolean> = MutableLiveData()
    val successfulDeleteMedia: MutableLiveData<Boolean> = MutableLiveData()
    var message: String = ""
    lateinit var loginResponse: LoginResponse
    lateinit var chatImgdata: ChatImageResponse
    var status: String = ""


    @SuppressLint("CheckResult")
    fun createPost(
        context: Context,
        userRef: String,
        title: String,
        description: String,
        fileType: String,
        areaLimit: String,
        mediaFile: File?
    ) {

        var body: MultipartBody.Part? = null
        var body2: MultipartBody.Part? = null

        if (mediaFile != null) {
            val requestFile = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), mediaFile)
            body = MultipartBody.Part.createFormData("mediaFile", mediaFile.name, requestFile)
        }

        val userRef: RequestBody =
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), userRef)
        val title: RequestBody =
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), title)
        val description: RequestBody =
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), description)
        val fileType: RequestBody =
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), fileType)
        val areaLimit: RequestBody =
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), areaLimit)

        dataManager.createPost(
            userRef,
            title,
            description,
            fileType,
            areaLimit,
            body!!
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
                            Log.e("tag", e.localizedMessage!!)
                        }
                    }
                    successful.value = false
                }
            })
    }
}