package com.application.onovapplication.viewModels

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.application.onovapplication.R
import com.application.onovapplication.model.GovtDataResponse
import com.application.onovapplication.model.JudicialModel
import com.application.onovapplication.model.StateResponse
import com.application.onovapplication.repository.service.DataManager
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException
import java.io.IOException
import java.util.concurrent.TimeoutException

class GovernmentViewModel : ViewModel() {

    private val dataManager: DataManager = DataManager()
    val successful: MutableLiveData<Boolean> = MutableLiveData()
    val successfulState: MutableLiveData<Boolean> = MutableLiveData()
    val successfulGovtData: MutableLiveData<Boolean> = MutableLiveData()
    var message: String = ""
    var status: String = ""
    lateinit var judicialModel: JudicialModel
    lateinit var govtDataResponse: GovtDataResponse
    lateinit var stateResponse: StateResponse

    @SuppressLint("CheckResult")
    fun getjudicial(context: Context) {

        dataManager.getjudicial()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(
                object : DisposableObserver<JudicialModel>() {
                    override fun onComplete() {}

                    override fun onNext(t: JudicialModel) {
                        status = t.status
//                        successful.value = true

                        if (status == "success") {

                            judicialModel = t
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
    fun getgovtstates(
        context: Context,
        //userRef: String
    ) {
        dataManager.getgovtstates()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(
                object : DisposableObserver<StateResponse>() {
                    override fun onComplete() {}

                    override fun onNext(t: StateResponse) {
                        status = t.status!!

                        if (status == "success") {

                            stateResponse = t
                        }

                        message = t.msg!!
                        successfulState.value = true
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
                        successfulState.value = false
                    }
                })
    }

    @SuppressLint("CheckResult")
    fun getgovtdata(
        context: Context,
        role: String,
        state: String
    ) {

        val role: RequestBody =
            role.toRequestBody("multipart/form-data".toMediaTypeOrNull())

        val state: RequestBody =
            state.toRequestBody("multipart/form-data".toMediaTypeOrNull())



        dataManager.getgovtdata(role, state)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(
                object : DisposableObserver<GovtDataResponse>() {
                    override fun onComplete() {

                    }

                    override fun onNext(t: GovtDataResponse) {
                        status = t.status
                        message = t.msg
                        govtDataResponse = t
                        successfulGovtData.value = true
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
                        successfulGovtData.value = false
                    }
                })
    }
}