package com.application.onovapplication.activities.common

import android.Manifest
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
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProvider
//import androidx.multidex.BuildConfig
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
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.tranxit.stripepayment.activity.add_card.StripeAddCardActivity

import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class ProfileActivity2 : BaseAppCompatActivity(), View.OnClickListener,ViewDebatesAdapter.OnClickItem ,ViewDebatesAdapter.OnClickShare{
    var mPhotoFile: File? = null
    var mPhotoFilecp: File? = null
    val LAUNCH_SECOND_ACTIVITY = 1
    var result:String=""
    var damount:String=""
    var paymentDesc:String=""
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
    private val homeViewModel by lazy { ViewModelProvider(this).get(HomeViewModel::class.java)}
    private val dntnViewModel by lazy { ViewModelProvider(this).get(DonationViewModel::class.java)}

    var feedsObj:FeedsData? =null
    var feedList: MutableList<FeedsData>? =null




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
            binding.profileClick.visibility = View.VISIBLE
            binding.ivCoverPhoto.setOnClickListener(this)
            binding.editName.visibility = View.VISIBLE
        } else {
            binding.profileClick.visibility = View.GONE
            binding.ivCoverPhoto.setOnClickListener(null)
            binding.editName.visibility = View.GONE
            binding.aboutInfo.visibility = View.GONE
            binding.profileImage.setOnClickListener(null)
            binding.editName.setOnClickListener(null)
        }
        if (usrRef == null) {
            profileViewModel.getProfile(this, userPreferences.getUserREf())
            profileViewModel.getPeople(this,userPreferences.getUserREf())
        } else {
            usrRef?.let { profileViewModel.getProfile(this, it) }
            profileViewModel.getPeople(this,usrRef.toString())
            binding.buttonEdit.visibility = View.GONE
        }




        binding.backBtn.setOnClickListener {
            finish()
        }


        //  profileViewModel.getProfile(this, userPreferences.getUserREf())
        showDialog()
        observeViewModel()

    }

    override fun onClick(v: View?) {
        when (v!!.id) {

            R.id.tv_followers -> {
                val intent = Intent(this, ViewFollowersActivity::class.java)
                intent.putExtra("type", "followers")
                intent.putExtra("data", peoplesdata)
                startActivity(intent)
            }

            R.id.tv_following -> {
                val intent = Intent(this, ViewFollowersActivity::class.java)
                intent.putExtra("type", "following")
                intent.putExtra("data", peoplesdata)
                startActivity(intent)
            }

            R.id.tv_donors -> {
                val intent = Intent(this, ViewFollowersActivity::class.java)
                intent.putExtra("type", "donors")
                intent.putExtra("data", peoplesdata)
                startActivity(intent)
            }

            R.id.aboutInfo -> {

                val intent = Intent(this, AboutInfoActivity::class.java)
                intent.putExtra("userinfo", userInfo)
                //  startActivity(intent)
                startActivityForResult(intent, 33)

            }


            R.id.profileImage -> {
                if (ContextCompat.checkSelfPermission(
                        this,
                        Manifest.permission.CAMERA
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf(
                            Manifest.permission.CAMERA,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE
                        ),
                        0
                    )
                } else {
                    showPictureDialog()
                }
            }

//            R.id.editName -> {
//                setFocusableTrue(profileName)
//
//            }

//            R.id.ivCoverPhoto -> {
            R.id.editName -> {
                if (ContextCompat.checkSelfPermission(
                        this,
                        Manifest.permission.CAMERA
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf(
                            Manifest.permission.CAMERA,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE
                        ),
                        0
                    )
                } else {
                    showCoverPictureDialog()
                }
            }
            R.id.button_edit -> {
                binding.buttonSave.visibility = View.VISIBLE
                binding.buttonEdit.visibility = View.GONE
                setFocusableTrue(binding.profileName)
            }
            R.id.button_save -> {

                profileViewModel.editProfile(
                    this,
                    binding.profileName.text.toString().trim(),
                    about.toString(),
                    weburl.toString(),
                    userPreferences.getUserREf(),
                    mPhotoFilecp,//ccPickerProfile.selectedCountryCode,
                    mPhotoFile
                )
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
feedList=profileViewModel.userfeed!!.feedList as MutableList<FeedsData>
                        debatesAdapter = ViewDebatesAdapter(this, profileViewModel.userfeed!!.feedList,this,this)

                        binding.rvProfileFeeds.adapter = debatesAdapter
                        if ( profileViewModel.userfeed!!.feedList.isNotEmpty()) profileViewModel.userfeed!!.feedList[0].profile=true


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
                        binding.tvFollowers.setText(peoplesdata!!.followCount.toString()+" Citizens")
                        binding.tvFollowing.setText(peoplesdata!!.followingCount.toString()+" Support")
                        binding.tvDonors.setText(peoplesdata!!.donnerCount.toString()+" Donors")
//                        setLayout(profileViewModel.userInfo!!)
//                        debatesAdapter = ViewDebatesAdapter(this, profileViewModel.userfeed!!.feedList,this)
//
//                        rv_profile_feeds.adapter = debatesAdapter

                    } else {
                        setError(profileViewModel.message)
                        finish()
                    }
                }
            } else {
                setError(profileViewModel.message)
            }

        })

        profileViewModel.successfullyUpdated.observe(this, androidx.lifecycle.Observer {
            dismissDialog()
            if (it) {
                if (profileViewModel.status == "success") {
                    setError(profileViewModel.message)
                    userInfo=profileViewModel.userInfo
                    setLayout(profileViewModel.userInfo!!)
//                    if (usrRef == null) {
//                        profileViewModel.getProfile(this, userPreferences.getUserREf())
//                    } else {
//                        usrRef?.let { profileViewModel.getProfile(this, it) }
//
//                    }
                    // userPreferences.savePhoto(profileViewModel.photoPath)
                } else {
                    setError(profileViewModel.message)
                    finish()
                }
            } else {
                setError(profileViewModel.message)
            }
        })

        homeViewModel.successfulFeedDelete.observe(this, androidx.lifecycle.Observer {
            dismissDialog()
            if (it) {
                if (homeViewModel.status == "success") {
//                    setError(profileViewModel.message)
                    if (feedList?.isNotEmpty() == true)
                  feedList!!.remove(feedList!![pos!!])
                    debatesAdapter?.notifyDataSetChanged()

                } else {
                    setError(homeViewModel.message)

                }
            } else {
                setError(homeViewModel.message)
            }
        })
        homeViewModel.successfulLike.observe(this, androidx.lifecycle.Observer {
            dismissDialog()
            if (it) {
                if (homeViewModel.status == "success") {
                   // setError(homeViewModel.message)
                    if (homeViewModel.message=="Disliked successfully"){
                        feedsObj?.dislike=true
                        feedsObj?.like=false

                        if ((feedsObj?.likeCount)!!.toInt()>0 && (feedsObj?.Liked=="1" ))
                            feedsObj?.likeCount=((feedsObj?.likeCount)!!.toInt()-1).toString()
                        feedsObj?.dislikeCount=((feedsObj?.dislikeCount)!!.toInt()+1).toString()
                        feedsObj?.Liked="0"
                        feedsObj?.Disliked="1"
                        debatesAdapter?.notifyDataSetChanged()
                    }else if (homeViewModel.message=="Liked successfully"){
                        feedsObj?.like=true
                        feedsObj?.dislike=false

                        feedsObj?.likeCount=((feedsObj?.likeCount)!!.toInt()+1).toString()
                        if ((feedsObj?.dislikeCount)!!.toInt()>0&& (feedsObj?.Disliked=="1" ))
                            feedsObj?.dislikeCount=((feedsObj?.dislikeCount)!!.toInt()-1).toString()
                        feedsObj?.Liked="1"
                        feedsObj?.Disliked="0"
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
//

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
                    dntnViewModel.addDonations(this,userPreferences.getUserREf(),result,dntnViewModel.paymentModel.transaction_id,damount)

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
        // partyFollower.text = "(".plus(userInfo.supporter!!.first().toString()).plus(")")
        if (userInfo.supporter != "") {
            binding.partyFollower.text = "(".plus(userInfo.supporter!!.first().toString()).plus(")")
        }
        Glide.with(this).load(BaseUrl.photoUrl + userInfo.profilePic)
            .apply(RequestOptions().placeholder(R.drawable.ic_baseline_account_circle_24))
            .into(binding.profileImage)
        Glide.with(this).load(BaseUrl.photoUrl + userInfo.coverPhoto).into(binding.ivCoverPhoto)
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            showPictureDialog()

        }
    }


    private fun showPictureDialog() {
        val pictureDialog: AlertDialog.Builder = AlertDialog.Builder(this, R.style.TimePickerTheme)
        pictureDialog.setTitle("Select Action")
        val pictureDialogItems = arrayOf(
            "Select image from gallery",
            "Select image from camera"
        )
        pictureDialog.setItems(
            pictureDialogItems
        ) { _, which ->
            when (which) {
                0 -> dispatchGalleryIntent()
                1 -> dispatchTakePictureIntent()
            }
        }
        pictureDialog.show()
    }

    private fun dispatchGalleryIntent() {
        val pickPhoto = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )
        pickPhoto.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        startActivityForResult(
            pickPhoto, REQUEST_GALLERY_PHOTO
        )
    }

    private fun dispatchTakePictureIntent() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(packageManager) != null) {
            // Create the File where the photo should go
            var photoFile: File? = null
            try {
                photoFile = createImageFile()
            } catch (ex: IOException) {
                ex.printStackTrace()
                // Error occurred while creating the File
            }
            if (photoFile != null) {
                val photoURI = FileProvider.getUriForFile(
                    this, "${BuildConfig.APPLICATION_ID}.provider",
                    photoFile
                )
                mPhotoFile = photoFile
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                startActivityForResult(
                    takePictureIntent, REQUEST_TAKE_PHOTO
                )
            }
        }
    }

    @Throws(IOException::class)
    private fun createImageFile(): File? {
        // Create an image file name
        val timeStamp =
            SimpleDateFormat("yyyyMMddHHmmss").format(Date())
        val mFileName = "JPEG_" + timeStamp + "_"
        val storageDir =
            getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(mFileName, ".jpg", storageDir)
    }


    private fun getRealPathFromUri(contentUri: Uri?): String? {
        var cursor: Cursor? = null
        return try {
            val proj =
                arrayOf(MediaStore.Images.Media.DATA)
            cursor = contentResolver.query(contentUri!!, proj, null, null, null)
            if (BuildConfig.DEBUG && cursor == null) {
                error("Assertion failed")
            }
            val column_index = cursor!!.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            cursor.moveToFirst()
            cursor.getString(column_index)
        } finally {
            cursor?.close()
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (data?.data != null) {

            if (requestCode == REQUEST_TAKE_PHOTO) {
//                Toast.makeText(this, ""+data, Toast.LENGTH_SHORT).show()
                val uri = intent.data

                val   bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri)
                binding.profileImage.setImageBitmap(bitmap)
               // Glide.with(this).load(mPhotoFile).into(binding.profileImage)

            } else if (requestCode == REQUEST_GALLERY_PHOTO) {
                val selectedImage = data.data
                try {
                    mPhotoFile = File(getRealPathFromUri(selectedImage))

                    Glide.with(this).load(mPhotoFile).into(binding.profileImage)

                } catch (e: IOException) {

                    e.printStackTrace()
                }
            }


            if (requestCode == REQUEST_TAKE_COVER_PHOTO) {


                Glide.with(this).load(mPhotoFilecp).into(binding.ivCoverPhoto)

            } else if (requestCode == REQUEST_COVER_GALLERY_PHOTO ) {
                val selectedImage = data.data
                try {
                    mPhotoFilecp = File(getRealPathFromUri(selectedImage))
                    Glide.with(this).load(mPhotoFilecp).into(binding.ivCoverPhoto)

                } catch (e: IOException) {

                    e.printStackTrace()
                }
            }


        }

        if (resultCode == RESULT_OK) {
            binding.profileImage.setImageURI(Uri.parse(mPhotoFile.toString()))
//            Toast.makeText(this, "" + data + requestCode + resultCode, Toast.LENGTH_SHORT).show()
            Log.d("PRACHI", "" + data?.getStringExtra("about"))
            about = data?.getStringExtra("about")
            weburl = data?.getStringExtra("weburl")
            role = data?.getStringExtra("role")
//            Toast.makeText(this, about + weburl + role, Toast.LENGTH_SHORT).show()

            profileViewModel.editProfile(
                this,
                binding.profileName.text.toString().trim(),
                about.toString(),
                weburl.toString(),
                userPreferences.getUserREf(),
                mPhotoFilecp,//ccPickerProfile.selectedCountryCode,
                mPhotoFile)


        }

        if (requestCode == LAUNCH_SECOND_ACTIVITY) {
            if (resultCode == AppCompatActivity.RESULT_OK) {
                result= data?.getStringExtra("result").toString()

                dntnViewModel.addDonations(this,userPreferences.getUserREf(),result,result,damount)

            }
            if (resultCode == AppCompatActivity.RESULT_CANCELED) {
                // Write your code if there's no result
                Log.e("result","no result found")
            }
        }

        if (data != null) if (requestCode == STRIPE_PAYMENT_REQUEST_CODE) {
            showDialog()
            Log.d("_DonActivityResult", "onActivityResult: " + data.getStringExtra("stripetoken"));
            val token=data.getStringExtra("stripetoken")
            dntnViewModel.paymentStripe(this,token.toString(),damount,paymentDesc)
//            dntnViewModel.addDonations(requireActivity(),userPreferences.getUserREf(),result,damount)

        }

    }




    private fun setFocusableFalse(editText: EditText) {
        editText.isFocusable = false
    }

    private fun setFocusableTrue(editText: EditText) {
        editText.isFocusableInTouchMode = true
        editText.requestFocus()
        editText.setSelection(editText.text.length)

        val imm: InputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT)
    }


    private fun showCoverPictureDialog() {
        val pictureDialog: AlertDialog.Builder = AlertDialog.Builder(this, R.style.TimePickerTheme)
        pictureDialog.setTitle("Select Action")
        val pictureDialogItems = arrayOf(
            "Select image from gallery",
            "Select image from camera"
        )
        pictureDialog.setItems(
            pictureDialogItems
        ) { _, which ->
            when (which) {
                0 -> dispatchCoverGalleryIntent()
                1 -> dispatchTakeCoverPictureIntent()
            }
        }
        pictureDialog.show()
    }

    private fun dispatchCoverGalleryIntent() {
        val pickPhoto = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )
        pickPhoto.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        startActivityForResult(
            pickPhoto, REQUEST_COVER_GALLERY_PHOTO
        )
    }

    private fun dispatchTakeCoverPictureIntent() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(packageManager) != null) {
            // Create the File where the photo should go
            var photoFile: File? = null
            try {
                photoFile = createImageFile()
            } catch (ex: IOException) {
                ex.printStackTrace()
                // Error occurred while creating the File
            }
            if (photoFile != null) {
                val photoURI = FileProvider.getUriForFile(
                    this, "${BuildConfig.APPLICATION_ID}.provider",
                    photoFile
                )
                mPhotoFile = photoFile
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                startActivityForResult(
                    takePictureIntent, REQUEST_TAKE_COVER_PHOTO
                )
            }
        }
    }

    override fun onclickdn(feeditem: FeedsData, amount: String,method: String) {
        damount=amount
        result=feeditem.userRef.toString()
        paymentDesc=feeditem.title.toString()
        if (method=="stripe") {
            val intent = Intent(this, StripeAddCardActivity::class.java)
            intent.putExtra("stripe_token", R.string.stripe_publishable_key)
            startActivityForResult(intent, STRIPE_PAYMENT_REQUEST_CODE)
        } else if (method=="gpay") {

        }
    }


    override fun onclickShr(toRef: String, feeditem: FeedsData) {
        val intent= Intent(this@ProfileActivity2, UsersActivity::class.java)
        intent.putExtra("feeds",feeditem)
        startActivity(intent)
    }

    override fun onclickCmnt( feeditem: FeedsData) {
        val intent= Intent(this@ProfileActivity2, CommentActivity::class.java)
        intent.putExtra("feeds",feeditem)
        startActivity(intent)
    }

    override fun onclicklike(type: String, feeditem: FeedsData) {
        showDialog()
        feedsObj=feeditem
        if (feeditem.recordType=="post")
            homeViewModel.likeFeed(this,userPreferences.getuserDetails()?.userRef.toString(),
                feeditem.recordId.toString(),"post",type)
        else if (feeditem.recordType=="petition")
            homeViewModel.likeFeed(this,userPreferences.getuserDetails()?.userRef.toString(),
                feeditem.recordId.toString(),"petition",type)
        else if (feeditem.recordType=="donationRequest")
            homeViewModel.likeFeed(this,userPreferences.getuserDetails()?.userRef.toString(),
                feeditem.recordId.toString(),"donationRequest",type)
        else if (feeditem.recordType=="law")
            homeViewModel.likeFeed(this,userPreferences.getuserDetails()?.userRef.toString(),
                feeditem.recordId.toString(),"law",type)
    }



    override fun onDeletePost( feeditem: FeedsData,position: Int) {
        showDialog()
        pos=position
//        if (feeditem.recordType=="post")
            homeViewModel.deleteFeed(this, feeditem.id.toString())

//        else if (feeditem.recordType=="petition")
//            homeViewModel.deleteFeed(this, feeditem.id.toString())
//        else if (feeditem.recordType=="donationRequest")
//            homeViewModel.deleteFeed(this, feeditem.id.toString())
//        else if (feeditem.recordType=="law")
//            homeViewModel.deleteFeed(this, feeditem.id.toString())
    }



}