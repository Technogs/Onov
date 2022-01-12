package com.application.onovapplication.viewModels

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.application.onovapplication.R
import com.application.onovapplication.model.AttendeesModel
import com.application.onovapplication.model.DonationModel
import com.application.onovapplication.model.DonorsResponse
import com.application.onovapplication.model.PaymentModel
import com.application.onovapplication.repository.service.DataManager
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import retrofit2.HttpException
import java.io.IOException
import java.util.concurrent.TimeoutException

class DonationViewModel:ViewModel() {
    private val dataManager: DataManager = DataManager()
    val successful: MutableLiveData<Boolean> = MutableLiveData()
    val successfulAddDonation: MutableLiveData<Boolean> = MutableLiveData()
    val successfulMyDonors: MutableLiveData<Boolean> = MutableLiveData()
    val successfulPayment: MutableLiveData<Boolean> = MutableLiveData()
    var message: String = ""
    lateinit var dndata: DonationModel
    lateinit var paymentModel: PaymentModel
    lateinit var dnrdata: DonorsResponse
    var status: String = ""



    @SuppressLint("CheckResult")
    fun getDonations(
        context: Context,
        userRef: String
    ) {


        val userRef: RequestBody =
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), userRef)


        dataManager.getAllDonation(userRef)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(
                object : DisposableObserver<DonationModel>() {
                    override fun onComplete() {

                    }

                    override fun onNext(t: DonationModel) {
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
    fun addDonations(
        context: Context,
        fromRef: String,
        toRef: String,
        txnId: String,
        amount: String
    ) {


        val fromRef: RequestBody =
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), fromRef)
        val toRef: RequestBody =
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), toRef)
        val amount: RequestBody =
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), amount)
        val txnId: RequestBody =
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), txnId)


        dataManager.addDonation(fromRef,toRef,txnId,amount)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(
                object : DisposableObserver<DonationModel>() {
                    override fun onComplete() {

                    }

                    override fun onNext(t: DonationModel) {
                        status = t.status
                        dndata=t
                        message = t.msg
                        successfulAddDonation.value = true
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
                        successfulAddDonation.value = false
                    }
                })
    }

    @SuppressLint("CheckResult")
    fun paymentStripe(
        context: Context,
        cardToken: String,
        amount: String,
        description: String
    ) {


        val cardToken: RequestBody =
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), cardToken)
        val amount: RequestBody =
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), amount)
        val description: RequestBody =
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), description)


        dataManager.paymentStripe(cardToken,amount,description)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(
                object : DisposableObserver<PaymentModel>() {
                    override fun onComplete() {

                    }

                    override fun onNext(t: PaymentModel) {
                        status = t.status
                        paymentModel=t
                        message = t.msg
                        successfulPayment.value = true
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
                        successfulPayment.value = false
                    }
                })
    }

    @SuppressLint("CheckResult")
    fun getmydonner(
        context: Context,
        userRef: String
    ) {


        val userRef: RequestBody =
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), userRef)

        dataManager.getmydonner(userRef)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(
                object : DisposableObserver<DonorsResponse>() {
                    override fun onComplete() {

                    }

                    override fun onNext(t: DonorsResponse) {
                        status = t.status
                        message = t.msg
                        dnrdata=t
                        successfulMyDonors.value = true
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
                        successfulMyDonors.value = false
                    }
                })
    }
}