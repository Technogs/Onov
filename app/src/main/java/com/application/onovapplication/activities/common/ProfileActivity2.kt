package com.application.onovapplication.activities.common


import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProvider
import com.application.onovapplication.BuildConfig
import com.application.onovapplication.R
import com.application.onovapplication.activities.CommentActivity
import com.application.onovapplication.activities.UsersActivity
import com.application.onovapplication.adapters.ViewDebatesAdapter
import com.application.onovapplication.databinding.ActivityProfile2Binding
import com.application.onovapplication.model.*
import com.application.onovapplication.repository.BaseUrl
import com.application.onovapplication.viewModels.DonationViewModel
import com.application.onovapplication.viewModels.HomeViewModel
import com.application.onovapplication.viewModels.ProfileViewModel
import com.application.onovapplication.viewModels.SearchViewModel
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.live.kicktraders.viewModel.LoginViewModel
import com.tranxit.stripepayment.activity.add_card.StripeAddCardActivity
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class ProfileActivity2 : BaseAppCompatActivity(), View.OnClickListener,
    ViewDebatesAdapter.OnClickItem, ViewDebatesAdapter.OnClickShare {
    var mPhotoFile: File? = null
    var mPhotoFilecp: File? = null
    val LAUNCH_SECOND_ACTIVITY = 1
    var result: String = ""
    var usertype: String = ""
    var stateus: String = ""
    var city: String = ""
    var damount: String = ""
    var paymentDesc: String = ""
    private val STRIPE_PAYMENT_REQUEST_CODE = 100
    private val REQUEST_TAKE_PHOTO = 101
    private val REQUEST_GALLERY_PHOTO = 201
    private val REQUEST_TAKE_COVER_PHOTO = 105
    private val REQUEST_COVER_GALLERY_PHOTO = 205
    private var type: String? = null
    private var usrRef: String? = null
    private var weburl: String? = null
    private var role: String? = null
    private var about: String? = null
    private var pos: Int? = null
    private var debatesAdapter: ViewDebatesAdapter? = null
    val feedData: ArrayList<FeedsData> = ArrayList()
    private var userInfo: UserInfo? = null
    private var people: PeopleList? = null
    private var peoplesdata: PeopleData? = null
    private val homeViewModel by lazy { ViewModelProvider(this).get(HomeViewModel::class.java) }
    private val loginViewModel by lazy { ViewModelProvider(this).get(LoginViewModel::class.java) }
    private val dntnViewModel by lazy { ViewModelProvider(this).get(DonationViewModel::class.java) }
    val searchViewModel by lazy { ViewModelProvider(this).get(SearchViewModel::class.java) }

    var feedsObj: FeedsData? = null
    var feedList: MutableList<FeedsData>? = null


    private val profileViewModel by lazy {
        ViewModelProvider(this).get(ProfileViewModel::class.java)
    }
    private lateinit var binding: ActivityProfile2Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityProfile2Binding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        type = intent.getStringExtra("type")
        usrRef = intent.getStringExtra("usrRef")

        if (type == "user") {
            usertype = "user"
            binding.ivCoverPhoto.setOnClickListener(this)
            binding.aboutInfo.visibility = View.VISIBLE

            binding.sll.visibility = View.GONE

        } else {
            binding.profileClick.visibility = View.GONE
            binding.sll.visibility = View.VISIBLE
            binding.ivCoverPhoto.setOnClickListener(null)
            binding.editName.visibility = View.GONE
            binding.aboutInfo.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
            binding.profileImage.setOnClickListener(null)
            binding.editName.setOnClickListener(null)
        }
        if (usrRef == null) {
            profileViewModel.getProfile(this, userPreferences.getUserREf(), "")
            profileViewModel.getPeople(this, userPreferences.getUserREf())
        } else {
            if (usrRef != userPreferences.getuserDetails()?.userRef.toString()) {
//                if (usrRef != null && usrRef != userPreferences.getuserDetails()?.userRef.toString()) {
                usrRef?.let {
                    profileViewModel.getProfile(
                        this,
                        it,
                        userPreferences.getuserDetails()?.userRef.toString()
                    )
                }
                profileViewModel.getPeople(this, usrRef.toString())
                binding.sll.visibility = View.VISIBLE
                    binding.sll.setOnClickListener {

                        if (binding.sprtText.text=="Support") {
                            showDialog()
                            searchViewModel.followUser(
                                this,
                                userPreferences.getUserREf(),
                                "1",
                                usrRef!!
                            )
                        } else  if (binding.sprtText.text=="UnSupport") {
                            showDialog()
                            searchViewModel.followUser(
                                this,
                                userPreferences.getUserREf(),
                                "0",
                                usrRef!!
                            )
                        }
                    }
//                }


            } else {
                usrRef?.let { profileViewModel.getProfile(this, it, "") }
                profileViewModel.getPeople(this, usrRef.toString())
                binding.sll.visibility = View.GONE
            }
        }




        binding.backBtn.setOnClickListener {
            finish()
        }

        showDialog()
        observeViewModel()

    }

    override fun onClick(v: View?) {
        when (v!!.id) {

            R.id.tv_followers -> {
                val intent = Intent(this, ViewFollowersActivity::class.java)
                intent.putExtra("type", "followers")
                intent.putExtra("user", usertype)
                intent.putExtra("data", peoplesdata)
                startActivity(intent)
            }

            R.id.tv_following -> {
                val intent = Intent(this, ViewFollowersActivity::class.java)
                intent.putExtra("type", "following")
                intent.putExtra("user", usertype)
                intent.putExtra("data", peoplesdata)
                startActivity(intent)
            }

            R.id.tv_donors -> {
                val intent = Intent(this, ViewFollowersActivity::class.java)
                intent.putExtra("type", "donors")
                intent.putExtra("user", usertype)
                intent.putExtra("data", peoplesdata)
                startActivity(intent)
            }
            R.id.tvDonatedTo -> {
                val intent = Intent(this, ViewFollowersActivity::class.java)
                intent.putExtra("type", "DonatedTo")
                intent.putExtra("user", usertype)
                intent.putExtra("data", peoplesdata)
                startActivity(intent)
            }

            R.id.aboutInfo -> {

                val intent = Intent(this, AboutInfoActivity::class.java)
                intent.putExtra("userinfo", userInfo)
                startActivity(intent)

            }



        }
    }

    private fun observeViewModel() {

        profileViewModel.successful.observe(this, androidx.lifecycle.Observer {
            dismissDialog()
            if (it != null) {
                if (it) {
                    if (profileViewModel.status == "success") {
                        userInfo = profileViewModel.userInfo
                        setLayout(profileViewModel.userInfo!!)
                        if (userInfo?.isFollow == "1") {
                            binding.sprtText.text = "UnSupport"
                        } else if (userInfo?.isFollow == "0") {
                            binding.supportClick.colorFilter = null

                        }
                        feedList = profileViewModel.userfeed!!.feedList as MutableList<FeedsData>
                        debatesAdapter = ViewDebatesAdapter(
                            this,
                            profileViewModel.userfeed!!.feedList,
                            this,
                            this, "yes"
                        )

                        binding.rvProfileFeeds.adapter = debatesAdapter
                        if (profileViewModel.userfeed!!.feedList.isNotEmpty())
                            profileViewModel.userfeed!!.feedList[0].profile =
                                true


                    } else {
                        setError(profileViewModel.message)
                        finish()
                    }
                }
            } else {
                setError(profileViewModel.message)
            }

        })

        profileViewModel.successfulPeople.observe(this, androidx.lifecycle.Observer {
            dismissDialog()
            if (it != null) {
                if (it) {
                    if (profileViewModel.status == "success") {
                        peoplesdata = profileViewModel.peoples!!.peopleData
                        binding.tvFollowers.text =
                            peoplesdata!!.followCount.toString() + "\n" + " Citizens"
                        binding.tvFollowing.text =
                            peoplesdata!!.followingCount.toString() + "\n" + " Support"
                        binding.tvDonors.text =
                            peoplesdata!!.donnerCount.toString() + "\n" + " Donors"
                        binding.tvDonatedTo.text =
                            peoplesdata!!.donetedCount.toString() + "\n" + " Donated"

                    } else {
                        setError(profileViewModel.message)
                        finish()
                    }
                }
            } else {
                setError(profileViewModel.message)
            }

        })

        searchViewModel.successfullyUpdated.observe(this, androidx.lifecycle.Observer {
            dismissDialog()
            if (it) {
                if (searchViewModel.status == "success") {
//                    setError(searchViewModel.message)
                    if (searchViewModel.message=="Unfollow successfully")
                    binding.sprtText.text = "Support"
                   else   if (searchViewModel.message=="Follow successfully")  binding.sprtText.text = "UnSupport"
                    profileViewModel.getPeople(this, usrRef.toString())
                } else {
                    setError(searchViewModel.message)
                    finish()
                }
            } else {
                setError(searchViewModel.message)
            }
        })

        loginViewModel.successfulDeleteMedia.observe(this, androidx.lifecycle.Observer {
            dismissDialog()
            if (it) {
                if (loginViewModel.status == "success") {
                    if (feedList?.isNotEmpty() == true)
                        feedList!!.remove(feedList!![pos!!])
                    debatesAdapter?.notifyDataSetChanged()

                } else {
                    setError(loginViewModel.message)

                }
            } else {
                setError(loginViewModel.message)
            }
        })
        homeViewModel.successfulLike.observe(this, androidx.lifecycle.Observer {
            dismissDialog()
            if (it) {
                if (homeViewModel.status == "success") {
                    // setError(homeViewModel.message)
                    if (homeViewModel.message == "Disliked successfully") {
                        feedsObj?.dislike = true
                        feedsObj?.like = false

                        if ((feedsObj?.likeCount)!!.toInt() > 0 && (feedsObj?.Liked == "1"))
                            feedsObj?.likeCount = ((feedsObj?.likeCount)!!.toInt() - 1).toString()
                        feedsObj?.dislikeCount = ((feedsObj?.dislikeCount)!!.toInt() + 1).toString()
                        feedsObj?.Liked = "0"
                        feedsObj?.Disliked = "1"
                        debatesAdapter?.notifyDataSetChanged()
                    } else if (homeViewModel.message == "Liked successfully") {
                        feedsObj?.like = true
                        feedsObj?.dislike = false

                        feedsObj?.likeCount = ((feedsObj?.likeCount)!!.toInt() + 1).toString()
                        if ((feedsObj?.dislikeCount)!!.toInt() > 0 && (feedsObj?.Disliked == "1"))
                            feedsObj?.dislikeCount =
                                ((feedsObj?.dislikeCount)!!.toInt() - 1).toString()
                        feedsObj?.Liked = "1"
                        feedsObj?.Disliked = "0"
                        debatesAdapter?.notifyDataSetChanged()

                    }

                } else {
                    setError(homeViewModel.message)
                    finish()
                }
            } else {
                setError(homeViewModel.message)
            }
        })
        dntnViewModel.successfulAddDonation.observe(this, androidx.lifecycle.Observer {
            dismissDialog()
            if (it) {
                if (dntnViewModel.status == "success") {
                    setError(dntnViewModel.message)
                } else {
                    setError(dntnViewModel.message)

                }
            } else {
                setError(dntnViewModel.message)
            }
        })
        dntnViewModel.successfulPayment.observe(this, androidx.lifecycle.Observer {
            dismissDialog()
            if (it) {
                if (dntnViewModel.status == "success") {

                    showDialog()
                    dntnViewModel.addDonations(
                        this,
                        userPreferences.getUserREf(),
                        result,
                        dntnViewModel.paymentModel.transaction_id,
                        damount
                    )

                } else {
                    setError(dntnViewModel.message)

                }
            } else {
                setError(dntnViewModel.message)
            }
        })


    }

    private fun setLayout(userInfo: UserInfo) {
        binding.profileName.setText(userInfo.fullName)
        binding.nationalRank.setText("N: "+userInfo.rank.National)
        binding.stateRank.setText("S: "+userInfo.rank.State)
        binding.localRank.setText("L: "+userInfo.rank.Local)
        binding.role.text = userInfo.role
        binding.adress.text = userInfo.cityName + ", " + userInfo.stateName + "\n" + userInfo.countryName
        if (userInfo.politicalParty != "") {
            binding.partyFollower.text =
                "(".plus(userInfo.politicalParty!!.first().toString()).plus(")")
        }
        Glide.with(this).load(BaseUrl.photoUrl + userInfo.profilePic)
            .apply(RequestOptions().placeholder(R.drawable.ic_baseline_account_circle_24))
            .into(binding.profileImage)
        Glide.with(this).load(BaseUrl.photoUrl + userInfo.coverPhoto).into(binding.ivCoverPhoto)
    }


//    override fun onRequestPermissionsResult(
//        requestCode: Int,
//        permissions: Array<out String>,
//        grantResults: IntArray
//    ) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//
//        if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
////            showPictureDialog()
//
//        }
//    }




    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

//        if (data?.data != null) {
//
//            if (requestCode == REQUEST_TAKE_PHOTO) {
//                val uri = intent.data
//
//                val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, uri)
//                binding.profileImage.setImageBitmap(bitmap)
//                // Glide.with(this).load(mPhotoFile).into(binding.profileImage)
//
//            } else if (requestCode == REQUEST_GALLERY_PHOTO) {
//                val selectedImage = data.data
//                try {
//                    mPhotoFile = File(getRealPathFromUri(selectedImage))
//
//                    Glide.with(this).load(mPhotoFile).into(binding.profileImage)
//
//                } catch (e: IOException) {
//
//                    e.printStackTrace()
//                }
//            }
//
//
//            if (requestCode == REQUEST_TAKE_COVER_PHOTO) {
//
//
//                Glide.with(this).load(mPhotoFilecp).into(binding.ivCoverPhoto)
//
//            } else if (requestCode == REQUEST_COVER_GALLERY_PHOTO) {
//                val selectedImage = data.data
//                try {
//                    mPhotoFilecp = File(getRealPathFromUri(selectedImage))
//                    Glide.with(this).load(mPhotoFilecp).into(binding.ivCoverPhoto)
//
//                } catch (e: IOException) {
//
//                    e.printStackTrace()
//                }
//            }
//
//
//        }

        if (resultCode == RESULT_OK) {

            Log.d("PRACHI", "" + data?.getStringExtra("about"))
            val uid = data?.getStringExtra("uid")
            uid?.let { profileViewModel.getProfile(this, it, "") }
            profileViewModel.getPeople(this, uid.toString())


        }

        if (requestCode == LAUNCH_SECOND_ACTIVITY) {
            if (resultCode == AppCompatActivity.RESULT_OK) {
                result = data?.getStringExtra("result").toString()

                dntnViewModel.addDonations(
                    this,
                    userPreferences.getUserREf(),
                    result,
                    result,
                    damount
                )

            }
            if (resultCode == AppCompatActivity.RESULT_CANCELED) {
                // Write your code if there's no result
                Log.e("result", "no result found")
            }
        }

        if (data != null) if (requestCode == STRIPE_PAYMENT_REQUEST_CODE) {
            showDialog()
            Log.d("_DonActivityResult", "onActivityResult: " + data.getStringExtra("stripetoken"))
            val token = data.getStringExtra("stripetoken")
            dntnViewModel.paymentStripe(this, token.toString(), damount, paymentDesc)

        }

    }


    override fun onclickdn(feeditem: FeedsData, amount: String, method: String) {
        damount = amount
        result = feeditem.userRef.toString()
        paymentDesc = feeditem.title.toString()
        if (method == "stripe") {
            val intent = Intent(this, StripeAddCardActivity::class.java)
            intent.putExtra("stripe_token", R.string.stripe_publishable_key)
            startActivityForResult(intent, STRIPE_PAYMENT_REQUEST_CODE)
        } else if (method == "gpay") {

        }
    }


    override fun onclickShr(toRef: String, feeditem: FeedsData) {
        val intent = Intent(this@ProfileActivity2, UsersActivity::class.java)
        intent.putExtra("feeds", feeditem)
        startActivity(intent)
    }

    override fun onclickCmnt(feeditem: FeedsData) {
        val intent = Intent(this@ProfileActivity2, CommentActivity::class.java)
        intent.putExtra("feeds", feeditem)
        startActivity(intent)
    }

    override fun onclicklike(type: String, feeditem: FeedsData) {
        showDialog()
        feedsObj = feeditem
        if (feeditem.recordType == "post")
            homeViewModel.likeFeed(
                this, userPreferences.getuserDetails()?.userRef.toString(),
                feeditem.recordId.toString(), "post", type
            )
        else if (feeditem.recordType == "petition")
            homeViewModel.likeFeed(
                this, userPreferences.getuserDetails()?.userRef.toString(),
                feeditem.recordId.toString(), "petition", type
            )
        else if (feeditem.recordType == "donationRequest")
            homeViewModel.likeFeed(
                this, userPreferences.getuserDetails()?.userRef.toString(),
                feeditem.recordId.toString(), "donationRequest", type
            )
        else if (feeditem.recordType == "law")
            homeViewModel.likeFeed(
                this, userPreferences.getuserDetails()?.userRef.toString(),
                feeditem.recordId.toString(), "law", type
            )
    }

    override fun onBackPressed() {
        startActivity(Intent(this, HomeTabActivity::class.java))
        super.onBackPressed()
    }

    override fun onRestart() {

        super.onRestart()
        val i = Intent(this, ProfileActivity2::class.java)
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(i)
    }

    override fun onDeletePost(feeditem: FeedsData, position: Int) {
        showDialog()
        pos = position
        loginViewModel.deleteMedia(this, feeditem.id.toString(), feeditem.recordType.toString())

    }


}