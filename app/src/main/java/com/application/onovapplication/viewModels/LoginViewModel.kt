package com.live.kicktraders.viewModel

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.application.onovapplication.R
import com.application.onovapplication.model.LoginResponse
import com.application.onovapplication.model.UserInfo
import com.application.onovapplication.repository.service.DataManager
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import retrofit2.HttpException
import java.io.IOException
import java.util.concurrent.TimeoutException

class LoginViewModel : ViewModel() {


    private val dataManager: DataManager = DataManager()
    val successful: MutableLiveData<Boolean> = MutableLiveData()
    var message: String = ""
    var status: String = ""
    lateinit var userInfo: UserInfo


    @SuppressLint("CheckResult")
    fun login(
        context: Context,
        email: String,
        password: String,
        mobileType: String,
        token: String
    ) {


        val userEmail: RequestBody =
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), email)
        val userPassword: RequestBody =
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), password)

        val fieldType: RequestBody =
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), "email")

        val OSType: RequestBody =
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), mobileType)

        val deviceToken: RequestBody =
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), token)

//        val userRole: RequestBody =
//            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), role)


        dataManager.login(userEmail, userPassword, fieldType, OSType, deviceToken)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(
                object : DisposableObserver<LoginResponse>() {
                    override fun onComplete() {

                    }

                    override fun onNext(t: LoginResponse) {
                        status = t.status!!

                        if (status == "success") {

                            userInfo = t.userInfo!!
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
                                message = e.message.toString()//context .getString(R.string.error_something_went_wrong)
                            }
                        }
                        successful.value = false
                    }
                })
    }
}