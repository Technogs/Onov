package com.application.onovapplication.viewModels

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.application.onovapplication.R
import com.application.onovapplication.model.DebateJoinerResponse
import com.application.onovapplication.model.DebateResponse
import com.application.onovapplication.model.LoginResponse
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

class SearchViewModel:ViewModel() {

    private val dataManager: DataManager = DataManager()
    val successful: MutableLiveData<Boolean> = MutableLiveData()
    val successfullyUpdated: MutableLiveData<Boolean> = MutableLiveData()
    val successfullyVote: MutableLiveData<Boolean> = MutableLiveData()
    val successfullyDebateJoinee: MutableLiveData<Boolean> = MutableLiveData()
    var message: String = ""
    var status: String = ""
    lateinit var searchList: SearchModel
    lateinit var debateResponse: DebateResponse
    lateinit var debateJoinerResponse: DebateJoinerResponse
    @SuppressLint("CheckResult")
    fun searchUser( context: Context, userRef: String ,find: String) {

        val userRef: RequestBody = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), userRef)
        val find: RequestBody = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), find)

        dataManager.searchUsr(find, userRef)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(
                object : DisposableObserver<SearchModel>() {
                    override fun onComplete() {}

                    override fun onNext(t: SearchModel) {
                        status = t.status

                        if (status == "success") {
                            searchList = t

                            message = t.msg
                        successful.value = true} }

                    override fun onError(e: Throwable) {
                        when (e) {
                            is IOException -> {
                                message = context.getString(R.string.error_please_check_internet)
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
                    } })
    }
    fun followUser( context: Context, userRef: String ,type: String ,flRef: String) {

        val userRef: RequestBody = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), userRef)
        val type: RequestBody = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), type)
        val flRef: RequestBody = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), flRef)

        dataManager.followUsr( userRef,type,flRef)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(
                object : DisposableObserver<SearchModel>() {
                    override fun onComplete() {}

                    override fun onNext(t: SearchModel) {
                        status = t.status

                        if (status == "success") {
//                            searchList = t

                            message = t.msg
                            successfullyUpdated.value = true} }

                    override fun onError(e: Throwable) {
                        when (e) {
                            is IOException -> {
                                message = context.getString(R.string.error_please_check_internet)
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
                }
            )
    }

    @SuppressLint("CheckResult")
    fun debaterVote( context: Context, voteFrom: String ,voteTo: String ,debateId: String) {

        val voteFrom: RequestBody = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), voteFrom)
        val voteTo: RequestBody = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), voteTo)
        val debateId: RequestBody = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), debateId)

        dataManager.debaterVote(voteFrom, voteTo,debateId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(
                object : DisposableObserver<DebateResponse>() {
                    override fun onComplete() {}

                    override fun onNext(t: DebateResponse) {
                        status = t.status

                        if (status == "success") {
                            debateResponse = t

                            message = t.msg
                            successfullyVote.value = true} }

                    override fun onError(e: Throwable) {
                        when (e) {
                            is IOException -> {
                                message = context.getString(R.string.error_please_check_internet)
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
                        successfullyVote.value = false
                    } })
    }

    @SuppressLint("CheckResult")
    fun getdebaterjoiner( context: Context, debateId: String ) {

        val debateId: RequestBody = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), debateId)


        dataManager.getdebaterjoiner(debateId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(
                object : DisposableObserver<DebateJoinerResponse>() {
                    override fun onComplete() {}

                    override fun onNext(t: DebateJoinerResponse) {
                        status = t.status

                        if (status == "success") {
                            debateJoinerResponse = t

                            message = t.msg
                            successfullyDebateJoinee.value = true} }

                    override fun onError(e: Throwable) {
                        when (e) {
                            is IOException -> {
                                message = context.getString(R.string.error_please_check_internet)
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
                        successfullyDebateJoinee.value = false
                    } })
    }
}