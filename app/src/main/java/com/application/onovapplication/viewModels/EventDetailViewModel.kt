package com.application.onovapplication.viewModels

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.application.onovapplication.R
import com.application.onovapplication.model.AttendeesModel
import com.application.onovapplication.model.EventModel
import com.application.onovapplication.model.RegisterResponse
import com.application.onovapplication.repository.service.DataManager
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import retrofit2.HttpException
import java.io.IOException
import java.util.concurrent.TimeoutException

class EventDetailViewModel:ViewModel() {
    private val dataManager: DataManager = DataManager()
    val successful: MutableLiveData<Boolean> = MutableLiveData()
    val successfulprivacy: MutableLiveData<Boolean> = MutableLiveData()
    val successfullyBuyEvent: MutableLiveData<Boolean> = MutableLiveData()
    var message: String = ""
   lateinit var data: AttendeesModel
    var status: String = ""
    var number=1
    var total=0
    var price=0
    fun addNumber(){
        number++
        total=number*price
    }

    fun decNumber(){
        if (number>1){
        number--}
        total=number*price

    }

    @SuppressLint("CheckResult")
    fun buyEvent(
        context: Context,
        userRef: String,
        eventId: String,
        ticketCount: String,
        totalAmt: String
    ) {


        val userRef: RequestBody =
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), userRef)
        val eventId: RequestBody =
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), eventId)
        val ticketCount: RequestBody =
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), ticketCount)
        val totalAmt: RequestBody =
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), totalAmt)


        dataManager.buyEvent(userRef, eventId,ticketCount,totalAmt)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(
                object : DisposableObserver<EventModel>() {
                    override fun onComplete() {

                    }

                    override fun onNext(t: EventModel) {
                        status = t.status

                        message = t.msg
                        successfullyBuyEvent.value = true
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
                        successfullyBuyEvent.value = false
                    }
                })
    }
    @SuppressLint("CheckResult")
    fun eventPrivacy(
        context: Context,
        userRef: String,
        eventId: String,
        action: String
    ) {


        val userRef: RequestBody =
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), userRef)
        val eventId: RequestBody =
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), eventId)
        val action: RequestBody =
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), action)



        dataManager.eventPrivacy(userRef, eventId,action)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(
                object : DisposableObserver<EventModel>() {
                    override fun onComplete() {

                    }

                    override fun onNext(t: EventModel) {
                        status = t.status!!

                        message = t.msg!!
                        successfulprivacy.value = true
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
                        successfulprivacy.value = false
                    }
                })
    }


 @SuppressLint("CheckResult")
    fun getAttendees(
        context: Context,
        eventId: String
    ) {


        val eventId: RequestBody =
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), eventId)




        dataManager.getAttendees(eventId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(
                object : DisposableObserver<AttendeesModel>() {
                    override fun onComplete() {

                    }

                    override fun onNext(t: AttendeesModel) {
                        status = t.status
                        data=t
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

}