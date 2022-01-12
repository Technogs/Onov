package com.application.onovapplication.viewModels

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.application.onovapplication.R
import com.application.onovapplication.model.EventModel
import com.application.onovapplication.model.GetStatsResponse
import com.application.onovapplication.model.SearchModel
import com.application.onovapplication.repository.service.DataManager
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import retrofit2.HttpException
import java.io.IOException
import java.util.concurrent.TimeoutException

class EventViewModel:ViewModel() {
    private val dataManager: DataManager = DataManager()
    val successful: MutableLiveData<Boolean> = MutableLiveData()
    var message: String = ""
    var status: String = ""
    lateinit var eventResponse: EventModel

    @SuppressLint("CheckResult")
    fun     getEvent(context: Context,
                     userRef: String) {


        val userId: RequestBody =
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), userRef)

        dataManager.getEvent(userId)
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
}