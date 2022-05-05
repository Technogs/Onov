package com.application.onovapplication.viewModels

import android.R.attr.data
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Bitmap.CompressFormat
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
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.util.concurrent.TimeoutException


class PetitionViewModel:ViewModel() {

    private val dataManager: DataManager = DataManager()
    val successful: MutableLiveData<Boolean> = MutableLiveData()
    val successfulAbout: MutableLiveData<Boolean> = MutableLiveData()
    val successfulSignPetition: MutableLiveData<Boolean> = MutableLiveData()
    val successfulViewPetition: MutableLiveData<Boolean> = MutableLiveData()
    val successfulAddPetition: MutableLiveData<Boolean> = MutableLiveData()
    val successfulEditPetition: MutableLiveData<Boolean> = MutableLiveData()
    var message: String = ""
    lateinit var loginResponse: LoginResponse

    lateinit var dndata: EventModel
    lateinit var aboutdata: PolicyModel
    lateinit var viewPetitionResponse: ViewPetitionResponse
    lateinit var petitionSignResponse: PetitionSignResponse
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
            MultipartBody.Part.createFormData("signImage", "", requestFile)
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
    }


    @SuppressLint("CheckResult")
    fun addPetition(
        context: Context,
        userRef: String,
        title: String,
        discription: String,
        petitionDate: String,
        websitelink: String,
        duration: String,
        signtureCount: String,
        mediaType: String,
        areaLimit: String,
        location: String,
        petitionMedia: File?
    ) {

        var body: MultipartBody.Part? = null
       if (petitionMedia != null) {
            val requestFile = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), petitionMedia)
           body = MultipartBody.Part.createFormData("petitionMedia", petitionMedia.name, requestFile)
        }
        val userRef: RequestBody =
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), userRef)
        val title: RequestBody =
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), title)
        val discription: RequestBody =
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), discription)
        val petitionDate: RequestBody =
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), petitionDate)
        val websitelink: RequestBody =
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), websitelink)
        val duration: RequestBody =
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), duration)
        val signtureCount: RequestBody =
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), signtureCount)
        val mediaType: RequestBody =
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), mediaType)
        val areaLimit: RequestBody =
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), areaLimit)
        val location: RequestBody =
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), location)


        dataManager.addPetition( userRef,
            title,
            discription,
            petitionDate,
            websitelink,
            duration,
            signtureCount,
            mediaType,
            areaLimit,
            location,
            body!!)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(
                object : DisposableObserver<LoginResponse>() {
                    override fun onComplete() {

                    }

                    override fun onNext(t: LoginResponse) {
                        status = t.status.toString()
                        loginResponse=t
                        message = t.msg.toString()
                        successfulAddPetition.value = true
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
                        successfulAddPetition.value = false
                    }
                })
    }


    @SuppressLint("CheckResult")
    fun editPetition(
        context: Context,
        feedId: String,
        title: String,
        discription: String,
//        websitelink: String,
        websitelink: String,
        duration: String,
        signtureCount: String,
        mediaType: String,
        areaLimit: String,
        location: String,
        petitionMedia: File?
    ) {

        var body: MultipartBody.Part? = null
       if (petitionMedia != null) {
            val requestFile = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), petitionMedia)
           body = MultipartBody.Part.createFormData("petitionMedia", petitionMedia.name, requestFile)
        }else {
           val requestFile = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), "")
           body =  MultipartBody.Part.createFormData("petitionMedia", "", requestFile)
       }
        val feedId: RequestBody =
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), feedId)
        val title: RequestBody =
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), title)
        val discription: RequestBody =
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), discription)
//        val petitionDate: RequestBody =
//            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), petitionDate)
        val websitelink: RequestBody =
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), websitelink)
        val duration: RequestBody =
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), duration)
        val signtureCount: RequestBody =
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), signtureCount)
        val mediaType: RequestBody =
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), mediaType)
        val areaLimit: RequestBody =
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), areaLimit)
        val location: RequestBody =
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), location)


        dataManager.editPetition( feedId,
            title,
            discription,
            websitelink,
            duration,
            signtureCount,
            areaLimit,
            location,
            mediaType,
            body!!)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(
                object : DisposableObserver<LoginResponse>() {
                    override fun onComplete() {

                    }

                    override fun onNext(t: LoginResponse) {
                        status = t.status.toString()
                        loginResponse=t
                        message = t.msg.toString()
                        successfulEditPetition.value = true
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
                        successfulEditPetition.value = false
                    }
                })
    }

    @SuppressLint("CheckResult")
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

    @SuppressLint("CheckResult")
    fun viewPetition(
        context: Context,
        past: String

    ) {
        val past: RequestBody =
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), past)

        dataManager.viewPetition(past)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(
                object : DisposableObserver<ViewPetitionResponse>() {
                    override fun onComplete() {

                    }

                    override fun onNext(t: ViewPetitionResponse) {
                        status = t.status
                        viewPetitionResponse=t
                        message = t.msg
                        successfulViewPetition.value = true
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
                        successfulViewPetition.value = false
                    }
                })
    }

    @SuppressLint("CheckResult")
    fun getSignPetition(
        context: Context,
        petitionId: String

    ) {
        val petitionId: RequestBody =
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), petitionId)

        dataManager.getSignPetition(petitionId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(
                object : DisposableObserver<PetitionSignResponse>() {
                    override fun onComplete() {

                    }

                    override fun onNext(t: PetitionSignResponse) {
                        status = t.status
                        petitionSignResponse=t
                        message = t.msg
                        successfulSignPetition.value = true
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
                        successfulSignPetition.value = false
                    }
                })
    }
}