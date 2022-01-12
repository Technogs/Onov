package com.application.onovapplication.viewModels

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.application.onovapplication.R
import com.application.onovapplication.model.LoginResponse
import com.application.onovapplication.model.PollResultResponse
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
import okhttp3.MultipartBody



class PollingViewModel:ViewModel() {

    private val dataManager: DataManager = DataManager()
    val successful: MutableLiveData<Boolean> = MutableLiveData()
    val successfulSubmitPoll: MutableLiveData<Boolean> = MutableLiveData()
    val successfulResultPoll: MutableLiveData<Boolean> = MutableLiveData()
    var message: String = ""
    var status: String = ""
    lateinit var pollResultResponse: PollResultResponse


    @SuppressLint("CheckResult")
    fun cretePolling(
        context: Context,
        createBy: String,
        pollTitle: String,
        options: ArrayList<String>,
        tillDate: String,
        tillTime: String
    ) {
        var requestBody: RequestBody
    //    val hashMap: LinkedHashMap<String, RequestBody> = LinkedHashMap()
        val ids = ArrayList<RequestBody>()

        var descriptionList: MutableList<MultipartBody.Part> = ArrayList()

        for (i in 0 until options.size) {
            requestBody = RequestBody.create("text/plain".toMediaTypeOrNull(),options.get(i) )
           // hashMap["options[$i]"] = requestBody*/
          //  descriptionList.add(MultipartBody.Part.createFormData("options", options.get(i)));

ids.add(requestBody)
        }


Log.d("sizeis" ,ids.size.toString())
        val createBy: RequestBody =
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), createBy)
        val pollTitle: RequestBody =
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), pollTitle)

        val tillDate: RequestBody =
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), tillDate)

        val tillTime: RequestBody =
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), tillTime)


        dataManager.createpolling(createBy, pollTitle, ids, tillDate, tillTime)
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
    }

    @SuppressLint("CheckResult")
    fun getpollresult(
        context: Context,
        pollId: String
    ) {
        val pollId: RequestBody =
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), pollId)

        dataManager.getpollresult( pollId)
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
}