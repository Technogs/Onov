package com.application.onovapplication.viewModels

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.application.onovapplication.R
import com.application.onovapplication.model.CommentsResponse
import com.application.onovapplication.model.FeedModel
import com.application.onovapplication.model.GetFeedResponse
import com.application.onovapplication.repository.service.DataManager
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import retrofit2.HttpException
import java.io.IOException
import java.util.concurrent.TimeoutException

class HomeViewModel : ViewModel() {

    private val dataManager: DataManager = DataManager()
    val successful: MutableLiveData<Boolean> = MutableLiveData()
    val successfulLike: MutableLiveData<Boolean> = MutableLiveData()
    val successfulCommentAdd: MutableLiveData<Boolean> = MutableLiveData()
    val successfulCommentLike: MutableLiveData<Boolean> = MutableLiveData()
    val successfulCommentGet: MutableLiveData<Boolean> = MutableLiveData()
    val successfulFeedDelete: MutableLiveData<Boolean> = MutableLiveData()
    var message: String = ""
    var status: String = ""
    lateinit var getFeedResponse: FeedModel
    lateinit var cmntResponse: CommentsResponse

    @SuppressLint("CheckResult")
    fun getFeed(
        context: Context,
        userPref: String
    ) {

        val userId: RequestBody =
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), userPref)

        dataManager.getFeed(userId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(
                object : DisposableObserver<FeedModel>() {
                    override fun onComplete() {
                    }

                    override fun onNext(t: FeedModel) {
                        getFeedResponse = t
                        status = t.status
                        message = t.msg
                        successful.value = true
                    }

                    override fun onError(e: Throwable) {
                        when (e) {
                            is IOException -> {
                                message =e.message.toString() //context.getString(R.string.error_please_check_internet)
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
    fun likeFeed(
        context: Context,
        userPref: String,
        likedId: String,
        likeTo: String,
        type: String
    ) {

        val userId: RequestBody =
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), userPref)
        val likedId: RequestBody =
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), likedId)
        val likeTo: RequestBody =
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), likeTo)
        val type: RequestBody =
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), type)

        dataManager.likeFeed(userId,likedId,likeTo,type)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(
                object : DisposableObserver<FeedModel>() {
                    override fun onComplete() {
                    }

                    override fun onNext(t: FeedModel) {
                        getFeedResponse = t
                        status = t.status
                        message = t.msg
                        successfulLike.value = true
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
                        successfulLike.value = false
                    }
                })
    }



    @SuppressLint("CheckResult")
    fun addComment(
        context: Context,
        userPref: String,
        commentTo: String,
        conmmentOn: String,
        comment: String,
        parentCommentId: String
    ) {

        val userId: RequestBody =
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), userPref)
        val commentTo: RequestBody =
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), commentTo)
        val conmmentOn: RequestBody =
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), conmmentOn)
        val comment: RequestBody =
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), comment)
        val parentCommentId: RequestBody =
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), parentCommentId)

        dataManager.addComment(userId,commentTo,conmmentOn,comment,parentCommentId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(
                object : DisposableObserver<FeedModel>() {
                    override fun onComplete() {}

                    override fun onNext(t: FeedModel) {
                        getFeedResponse = t
                        status = t.status
                        message = t.msg
                        successfulCommentAdd.value = true



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
                        successfulCommentAdd.value = false



                    }

                })
    }



    @SuppressLint("CheckResult")
    fun getComment(
        context: Context,
        commentTo: String,
        conmmentOn: String ,  userRef: String ,  parentCommentId: String
    ) {

        val commentTo: RequestBody =
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), commentTo)
        val conmmentOn: RequestBody =
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), conmmentOn)
        val userRef: RequestBody =
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), userRef)
        val parentCommentId: RequestBody =
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), parentCommentId)


        dataManager.getComment(commentTo,conmmentOn,userRef,parentCommentId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(
                object : DisposableObserver<CommentsResponse>() {
                    override fun onComplete() {}

                    override fun onNext(t: CommentsResponse) {
                        cmntResponse=t
                        status = t.status
                        message = t.msg
                        successfulCommentGet.value = true
                    }

                    override fun onError(e: Throwable) {
                        when (e) {
                            is IOException -> {
                                message =e.message.toString() //context.getString(R.string.error_please_check_internet)
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
                        successfulCommentGet.value = false
                    }
                })
    }

    @SuppressLint("CheckResult")
    fun commentlike(
        context: Context,
        userPref: String,
        recordId: String,
        likeTo: String,
        type: String
    ) {

        val userId: RequestBody =
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), userPref)
        val likedId: RequestBody =
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), recordId)
        val likeTo: RequestBody =
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), likeTo)
        val type: RequestBody =
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), type)

        dataManager.commentlike(userId,likedId,likeTo,type)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(
                object : DisposableObserver<FeedModel>() {
                    override fun onComplete() {
                    }

                    override fun onNext(t: FeedModel) {
                        getFeedResponse = t
                        status = t.status
                        message = t.msg
                        successfulCommentLike.value = true
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
                        successfulCommentLike.value = false
                    }
                })
    }


    @SuppressLint("CheckResult")
    fun deleteFeed(
        context: Context,
        recordId: String
    ) {


        val feedId: RequestBody =
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), recordId)



        dataManager.deleteFeed(feedId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(
                object : DisposableObserver<FeedModel>() {
                    override fun onComplete() {
                    }

                    override fun onNext(t: FeedModel) {
                        getFeedResponse = t
                        status = t.status
                        message = t.msg
                        successfulFeedDelete.value = true
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
                        successfulFeedDelete.value = false
                    }
                })
    }
}