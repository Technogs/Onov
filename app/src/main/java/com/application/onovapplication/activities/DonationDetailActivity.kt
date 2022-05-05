package com.application.onovapplication.activities

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.application.onovapplication.activities.common.BaseAppCompatActivity
import com.application.onovapplication.adapters.ViewFollowersAdapter
import com.application.onovapplication.databinding.ActivityDonationDetailBinding
import com.application.onovapplication.databinding.DonateDialogBinding
import com.application.onovapplication.gpay.CheckoutActivity
import com.application.onovapplication.model.DonationDetailResponse
import com.application.onovapplication.model.Follow
import com.application.onovapplication.repository.BaseUrl
import com.application.onovapplication.viewModels.DonationViewModel
import com.bumptech.glide.Glide
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory
import com.google.android.exoplayer2.extractor.ExtractorsFactory
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.trackselection.TrackSelector
import com.google.android.exoplayer2.ui.SimpleExoPlayerView
import com.google.android.exoplayer2.upstream.BandwidthMeter
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory
import com.tranxit.stripepayment.activity.add_card.StripeAddCardActivity

class DonationDetailActivity : BaseAppCompatActivity(),ViewFollowersAdapter.OnPeopleClick {
    val donationViewModel by lazy { ViewModelProvider(this).get(DonationViewModel::class.java) }
    private lateinit var binding: ActivityDonationDetailBinding
     var dnid: String=""
    var damount: String = ""
    private val STRIPE_PAYMENT_REQUEST_CODE = 100
    lateinit var donationDetailResponse: DonationDetailResponse

    private var debateJoineeAdapter: ViewFollowersAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDonationDetailBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        dnid = intent.getStringExtra("dnid").toString()


        showDialog()
donationViewModel.donationdetail(this,dnid)
observeViewModel()

        binding.donate.setOnClickListener {openDonationsDialog()
        }
    }

    private fun openDonationsDialog() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        val binding = DonateDialogBinding.inflate(dialog.layoutInflater)
        val view = binding.root
        dialog.setContentView(view)
        dialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.show()

        binding.submitDonation.setOnClickListener {
            showDonationDialog(binding.edDonationPrice.text.toString())
            dialog.dismiss()
        }

        binding.cancelDonation.setOnClickListener {
            dialog.dismiss()

        }

    }
    private fun showDonationDialog(amount: String) {
        val pictureDialog: AlertDialog.Builder = AlertDialog.Builder(this)
        pictureDialog.setTitle("Select Action")
        val pictureDialogItems = arrayOf(
            "Pay with Stripe",
            "Pay with Google Pay"
        )
        pictureDialog.setItems(
            pictureDialogItems
        ) { _, which ->
            when (which) {
                0 -> {
                    if (amount == "")
                        Toast.makeText(this, "Enter an amount", Toast.LENGTH_SHORT).show()
                    else {
                        onclickdn( amount, "stripe")
                    }
                }
                1 -> {
                    if (amount == "")
                        Toast.makeText(this, "Enter an amount", Toast.LENGTH_SHORT).show()
                    else {
                        onclickdn( amount, "gpay")
                    }
                }
            }
        }
        pictureDialog.show()
    }

  fun onclickdn( amount: String, method: String) {
        //  showDialog()
        damount = amount
      if (method == "stripe") {
            val intent = Intent(this, StripeAddCardActivity::class.java)
            intent.putExtra(
                "stripe_token",
                "pk_test_51K7DgkCqEZCeL0aJqIG6IlKvuxg1HAY0OBDIR8mlzJ1hEPw94X0WSjkloVavG1V0mc6Xa4sFGR0D7XZOxhNdMYJf00TBWfzH0M"
            )
            startActivityForResult(intent, STRIPE_PAYMENT_REQUEST_CODE)
        } else if (method == "gpay") {
            val i = Intent(this, CheckoutActivity::class.java)
            i.putExtra("amount",amount)
            startActivity(i)
        }

    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
//        if (requestCode == LAUNCH_SECOND_ACTIVITY) {
//            if (resultCode == AppCompatActivity.RESULT_OK) {
//                result= data?.getStringExtra("result").toString()
//
//                 dntnViewModel.addDonations(requireActivity(),userPreferences.getUserREf(),result,damount)
//
//            }
//            if (resultCode == AppCompatActivity.RESULT_CANCELED) {
//                // Write your code if there's no result
//                Log.e("result","no result found")
//            }
//        }

        if (data != null) if (requestCode == STRIPE_PAYMENT_REQUEST_CODE) {
            showDialog()
            Log.d("_DonActivityResult", "onActivityResult: " + data.getStringExtra("stripetoken"))
            val token = data.getStringExtra("stripetoken")
            donationViewModel.paymentStripe(this, token.toString(), damount, "payment")

        }

    }

    private fun observeViewModel() {
            donationViewModel.successfulPayment.observe(this, androidx.lifecycle.Observer {
            dismissDialog()
                if (it != null) {
                    if (it) {

                    if (donationViewModel.status == "success") {
                        showDialog()
                        donationViewModel.addDonations(
                            this,
                            userPreferences.getUserREf(),
                            donationDetailResponse.donationDetail.userRef,
                            donationViewModel.paymentModel.transaction_id,
                            damount
                        )

                    } else {
                      setError(donationViewModel.message)
                    }

                } else {
               setError(donationViewModel.message)
                }
            }
        })

            donationViewModel.successfulAddDonation.observe(this, androidx.lifecycle.Observer {

                dismissDialog()
                if (it != null) {
                    if (it) {

                    if (donationViewModel.status == "success") {

                        Toast.makeText(this, donationViewModel.message, Toast.LENGTH_SHORT)
                            .show()


                    } else {
                       setError(donationViewModel.message)
                    }

                } else {
                   setError(donationViewModel.message)
                }
            }
        })

        donationViewModel.successfulDonationDetail.observe(this, androidx.lifecycle.Observer {
            dismissDialog()
            if (it != null) {
                if (it) {
                    if (donationViewModel.status == "success") {

                        donationDetailResponse = donationViewModel.donationDetailResponse
if (donationDetailResponse.donationDetail.userRef==userPreferences.getuserDetails()?.userRef)binding.donate.visibility=View.GONE
                        else binding.donate.visibility=View.VISIBLE
                        binding.debateDetailsTitleValue.text=donationDetailResponse.donationDetail.title
                        binding.debateDetailsDateValue.text=donationDetailResponse.donationDetail.description
                        binding.debateDetailsTimeValue.text=donationDetailResponse.donationDetail.donationGoal
                  if (donationDetailResponse.donationDetail.filePath!="")      {
                      binding.feedMedia.visibility=View.VISIBLE
                      if (donationDetailResponse.donationDetail.fileType=="photo") {
                          binding.vidLyt.visibility=View.GONE
                          binding.ivFeed.visibility=View.VISIBLE

                          Glide.with(this)
                              .load(BaseUrl.photoUrl + donationDetailResponse.donationDetail.filePath.toString())
                              .into(binding.ivFeed)
                          binding.ivFeed.setOnClickListener { showDialogMedia(donationDetailResponse.donationDetail.filePath,"photo") }

                      }else if (donationDetailResponse.donationDetail.fileType=="video"){
                          binding.vidLyt.visibility=View.VISIBLE
                          binding.ivFeed.visibility=View.GONE
                          exoplayer(BaseUrl.photoUrl + donationDetailResponse.donationDetail.filePath,binding.idExoPlayerVIew)
                          binding.zoomVid.setOnClickListener { showDialogMedia(donationDetailResponse.donationDetail.filePath,"video") }

                      }

                        }else binding.feedMedia.visibility=View.GONE

                        debateJoineeAdapter = ViewFollowersAdapter(
                            this,
                          "",
                            "",
                            "","",this,  donationDetailResponse.donationDetail.tagPeopleList
                        )
                        binding.rvDebateRequests.adapter = debateJoineeAdapter


                    } else {
                        setError(donationViewModel.message)
                        finish()
                    }
                }
            } else {
                setError(donationViewModel.message)
            }

        })
    }

    override fun onPClick(datatem: Follow) {
        TODO("Not yet implemented")
    }

    override fun onCheckboxClick(datatem: Follow) {
        TODO("Not yet implemented")
    }

    override fun onRemoveFollow(datatem: Follow) {
        TODO("Not yet implemented")
    }

    override fun onRemoveSupport(datatem: Follow) {
        TODO("Not yet implemented")
    }


}