package com.application.onovapplication.viewModels

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.application.onovapplication.R
import com.application.onovapplication.model.GetAllWinnersResponse
import com.application.onovapplication.model.GetStatsResponse
import com.application.onovapplication.repository.service.DataManager
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import retrofit2.HttpException
import java.io.IOException
import java.util.concurrent.TimeoutException

class WinningsViewModel : ViewModel() {


    private val dataManager: DataManager = DataManager()
    val successful: MutableLiveData<Boolean> = MutableLiveData()
    val successfullyGotWinnersList: MutableLiveData<Boolean> = MutableLiveData()
    var message: String = ""
    var status: String = ""
    lateinit var statsResponse: GetStatsResponse
    lateinit var winnersResponse: GetAllWinnersResponse


    @SuppressLint("CheckResult")
    fun getWinnings(
        context: Context,
        userRef: String
    ) {


        dataManager.getWinnings(userRef)
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
    fun getAllWinners(
        context: Context
    ) {

        dataManager.getAllWinners()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(
                object : DisposableObserver<GetAllWinnersResponse>() {
                    override fun onComplete() {

                    }

                    override fun onNext(t: GetAllWinnersResponse) {
                        status = t.status!!

                        if (status == "success") {

                            winnersResponse = t
                        }

                        message = t.msg!!
                        successfullyGotWinnersList.value = true
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
                        successfullyGotWinnersList.value = false
                    }
                })
    }
}