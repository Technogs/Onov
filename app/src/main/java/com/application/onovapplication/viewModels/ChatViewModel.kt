package com.application.onovapplication.viewModels

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.application.onovapplication.R
import com.application.onovapplication.model.ChatImageResponse
import com.application.onovapplication.model.ChatModel
import com.application.onovapplication.model.DonationModel
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
    var message: String = ""
    lateinit var chatdata: ChatModel
    lateinit var chatImgdata: ChatImageResponse
    var status: String = ""


    @SuppressLint("CheckResult")
    fun getChatList(
        context: Context,
        userRef: String
    ) {


        val userRef: RequestBody =
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), userRef)


        dataManager.getChatList(userRef)
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
                    override fun onComplete() {

                    }

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