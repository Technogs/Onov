package com.application.onovapplication.viewModels

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.application.onovapplication.R
import com.application.onovapplication.model.LoginResponse
import com.application.onovapplication.model.PeopleList
import com.application.onovapplication.model.ProfileModel
import com.application.onovapplication.model.UserInfo
import com.application.onovapplication.repository.service.DataManager
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.HttpException
import java.io.File
import java.io.IOException
import java.util.concurrent.TimeoutException

class ProfileViewModel : ViewModel() {


    private val dataManager: DataManager = DataManager()
    val successful: MutableLiveData<Boolean> = MutableLiveData()
    val successfulPeople: MutableLiveData<Boolean> = MutableLiveData()
    val successfullyUpdated: MutableLiveData<Boolean> = MutableLiveData()
    var message: String = ""
    var photoPath: String = ""
    var status: String = ""
    var updateProfileStatus: String = ""

    var userInfo: UserInfo? = null
    var userfeed: ProfileModel? = null
    var peoples: PeopleList? = null

    @SuppressLint("CheckResult")
    fun getProfile(
        context: Context,
        userRef: String
    ) {


        val value: RequestBody =
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), userRef)
        val key: RequestBody =
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), "userRef")


        dataManager.getProfile(key, value)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(
                object : DisposableObserver<ProfileModel>() {
                    override fun onComplete() {

                    }

                    override fun onNext(t: ProfileModel) {
                        status = t.status!!

                        if (status == "success") {
                            userInfo = t.UserInfo
                            userfeed=t
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
    }  @SuppressLint("CheckResult")


    fun getPeople(
        context: Context,
        userRef: String
    ) {


        val userRef: RequestBody =
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), userRef)
//        val key: RequestBody =
//            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), "userRef")


        dataManager.getPeople(userRef)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(
                object : DisposableObserver<PeopleList>() {
                    override fun onComplete() {

                    }

                    override fun onNext(t: PeopleList) {
                        status = t.status

                        if (status == "success") {
                          peoples=t
                        }

                        message = t.msg
                        successfulPeople.value = true
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
                        successfulPeople.value = false
                    }
                })
    }


    @SuppressLint("CheckResult")
    fun editProfile(
        context: Context, name: String, aboutusr: String, webUrlusr: String, userRef: String, coverPic: File?,
        photo: File?
    ) {
        var body: MultipartBody.Part? = null
        var cpbody: MultipartBody.Part? = null

        body = if (photo != null) {
            val requestFile = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), photo)
            MultipartBody.Part.createFormData("profilePic", photo.name, requestFile)
        } else {
            val requestFile = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), "")
            MultipartBody.Part.createFormData("profilePic", "", requestFile)
        }
        cpbody = if (coverPic != null) {
            val requestFilecp = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), coverPic)
            MultipartBody.Part.createFormData("coverPhoto", coverPic.name, requestFilecp)
        } else {
            val requestFilecp = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), "")
            MultipartBody.Part.createFormData("coverPhoto", "", requestFilecp)
        }
        val about: RequestBody =
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), aboutusr)
        val userReference: RequestBody =
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), userRef)
        val userName: RequestBody =
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), name)

        val webUrl: RequestBody =
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), webUrlusr)
//        val coverPhoto: RequestBody =
//            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), coverPic)

        dataManager.editProfile(
            userName, about, webUrl, userReference,cpbody,  body
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(
                object : DisposableObserver<LoginResponse>() {
                    override fun onComplete() {

                    }

                    override fun onNext(t: LoginResponse) {
                        updateProfileStatus = t.status!!
                        userInfo=t.userInfo
                        if (updateProfileStatus == "success") {
                            photoPath = t.userInfo!!.profilePic!!
                        }
                        message = t.msg!!
                        successfullyUpdated.value = true
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
                        successfullyUpdated.value = false
                    }
                })
    }
}