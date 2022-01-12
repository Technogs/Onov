package com.application.onovapplication.viewModels

import android.R.attr.data
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Bitmap.CompressFormat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.application.onovapplication.R
import com.application.onovapplication.model.EventModel
import com.application.onovapplication.model.PolicyModel
import com.application.onovapplication.repository.service.DataManager
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.HttpException
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.util.concurrent.TimeoutException


class PetitionViewModel:ViewModel() {

    private val dataManager: DataManager = DataManager()
    val successful: MutableLiveData<Boolean> = MutableLiveData()
    val successfulAbout: MutableLiveData<Boolean> = MutableLiveData()
    var message: String = ""
    lateinit var dndata: EventModel
    lateinit var aboutdata: PolicyModel
    var status: String = ""
    @SuppressLint("CheckResult")
    fun signPetition(
        context: Context,
        userRef: String,
        petitionId: String,
        signImage: Bitmap?
    ) {

        val bos = ByteArrayOutputStream()
        signImage?.compress(CompressFormat.JPEG, 100, bos)
        val data = bos.toByteArray()
        val file= File(data.toString())
        var body: MultipartBody.Part? = null
        body = if (data != null) {
            val requestFile = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), data)
            MultipartBody.Part.createFormData("signImage", file.name, requestFile)
        } else {
            val requestFile = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), "")
            MultipartBody.Part.createFormData("profilePic", "", requestFile)
        }
        val userRef: RequestBody =
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), userRef)
        val petitionId: RequestBody =
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), petitionId)


        dataManager.signPetition(userRef,petitionId,body)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(
                object : DisposableObserver<EventModel>() {
                    override fun onComplete() {

                    }

                    override fun onNext(t: EventModel) {
                        status = t.status
                        dndata=t
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
    }   @SuppressLint("CheckResult")


    fun getPolicy(
        context: Context,
        privacy: String

    ) {


        dataManager.getAbout(privacy)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(
                object : DisposableObserver<PolicyModel>() {
                    override fun onComplete() {

                    }

                    override fun onNext(t: PolicyModel) {
                        status = t.status
                        aboutdata=t
                        message = t.msg
                        successfulAbout.value = true
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
                        successfulAbout.value = false
                    }
                })
    }
}