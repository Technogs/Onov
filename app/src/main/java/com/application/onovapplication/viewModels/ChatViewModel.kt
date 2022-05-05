package com.application.onovapplication.viewModels

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.application.onovapplication.R
import com.application.onovapplication.model.*
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

class ChatViewModel:ViewModel() {

    private val dataManager: DataManager = DataManager()
    val successful: MutableLiveData<Boolean> = MutableLiveData()
    val successfulShareFeed: MutableLiveData<Boolean> = MutableLiveData()
    val successfulChatImg: MutableLiveData<Boolean> = MutableLiveData()
    val successfulChatNotification: MutableLiveData<Boolean> = MutableLiveData()
    var message: String = ""
    lateinit var chatdata: ChatModel
    lateinit var chatImgdata: ChatImageResponse
    var status: String = ""


    @SuppressLint("CheckResult")
    fun getMesageSeen(
        context: Context,
        userRef: String,
        otherref: String
    ) {


        val userRef: RequestBody =
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), userRef)
        val otherref: RequestBody =
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), otherref)


        dataManager.getChatSeen(userRef,otherref)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(
                object : DisposableObserver<ChatModel>() {
                    override fun onComplete() {

                    }

                    override fun onNext(t: ChatModel) {
                        status = t.status
                        chatdata=t
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
    fun getChatList(
        context: Context,
        userRef: String,
        keyword: String
    ) {


        val userRef: RequestBody =
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), userRef)
        val keyword: RequestBody =
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), keyword)


        dataManager.getChatList(userRef,keyword)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(
                object : DisposableObserver<ChatModel>() {
                    override fun onComplete() {

                    }

                    override fun onNext(t: ChatModel) {
                        status = t.status
                        chatdata=t
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
    fun chatNotification(
        context: Context,
        fromRef: String,
        toRef: String,
        msge: String
    ) {


        val fromRef: RequestBody =
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), fromRef)
        val toRef: RequestBody =
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), toRef)

        val msg: RequestBody =
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), msge)


        dataManager.chatNotification(fromRef,toRef,msg)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(
                object : DisposableObserver<SimpleResponse>() {
                    override fun onComplete() {

                    }

                    override fun onNext(t: SimpleResponse) {
                        status = t.status
                       message=t.msg
                        successfulChatNotification.value = true
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
                        successfulChatNotification.value = false
                    }
                })
    }


    @SuppressLint("CheckResult")
    fun shareFeed(
        context: Context,
        userId: String,
        sharedId: String
    ) {


        val userRef: RequestBody =
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), userId)
        val sharedId: RequestBody =
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), sharedId)


        dataManager.shareFeed(userRef,sharedId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(
                object : DisposableObserver<ChatModel>() {
                    override fun onComplete() {}

                    override fun onNext(t: ChatModel) {
                        status = t.status
                        chatdata=t
                        message = t.msg
                        successfulShareFeed.value = true
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
                        successfulShareFeed.value = false
                    }
                })
    }


    @SuppressLint("CheckResult")
    fun uploadChatImage(
        context: Context,
        chatImage: File?
    ) {


        var body: MultipartBody.Part? = null
        body = if (chatImage != null) {
            val requestFile = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), chatImage)
            MultipartBody.Part.createFormData("chatImg", chatImage.name, requestFile)
        } else {
            val requestFile = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), "")
            MultipartBody.Part.createFormData("chatImg", "", requestFile)
        }


        dataManager.uploadChatImage(body)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(
                object : DisposableObserver<ChatImageResponse>() {
                    override fun onComplete() {

                    }

                    override fun onNext(t: ChatImageResponse) {
                        status = t.status
                        chatImgdata=t
                        message = t.msg
                        successfulChatImg.value = true
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
                        successfulChatImg.value = false
                    }
                })
    }


}