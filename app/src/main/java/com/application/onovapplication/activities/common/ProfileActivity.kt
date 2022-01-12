package com.application.onovapplication.activities.common

import android.Manifest
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import android.view.Window
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.multidex.BuildConfig
//import com.application.onovapplication.BuildConfig
import com.application.onovapplication.R
import com.application.onovapplication.databinding.ActivityNotificationsBinding
import com.application.onovapplication.databinding.ActivityProfileBinding
import com.application.onovapplication.databinding.DonateDialogBinding
import com.application.onovapplication.model.UserInfo
import com.application.onovapplication.repository.BaseUrl
import com.application.onovapplication.viewModels.ProfileViewModel
import com.bumptech.glide.Glide

import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class ProfileActivity : BaseAppCompatActivity(), View.OnClickListener {

    var mPhotoFile: File? = null
    private val REQUEST_TAKE_PHOTO = 101
    private val REQUEST_GALLERY_PHOTO = 201
    private var type: String? = null

    private val profileViewModel by lazy {
        ViewModelProvider(this).get(ProfileViewModel::class.java)
    }
    private lateinit var binding: ActivityProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        type = intent.getStringExtra("type")

        if (type == "user") {
            binding.donate.visibility = View.GONE
            binding.btnUpdateProfile.visibility = View.VISIBLE
            binding.profileClick.visibility = View.VISIBLE
            binding.editName.visibility = View.VISIBLE
            binding.emailEdit.visibility = View.VISIBLE
            binding.phoneEdit.visibility = View.VISIBLE
        } else {
            binding.donate.visibility = View.VISIBLE
            binding.btnUpdateProfile.visibility = View.GONE
            binding.profileClick.visibility = View.GONE
            binding.editName.visibility = View.INVISIBLE
            binding.emailEdit.visibility = View.INVISIBLE
            binding.phoneEdit.visibility = View.INVISIBLE
        }

        profileViewModel.getProfile(this, userPreferences.getUserREf())
        showDialog()
        observeViewModel()

        binding.backBtn.setOnClickListener {
            finish()
        }
    }

    private fun observeViewModel() {

        profileViewModel.successful.observe(this, Observer {
            dismissDialog()
            if (it != null) {
                if (it) {
                    if (profileViewModel.status == "success") {
                        setLayout(profileViewModel.userInfo!!)

                    } else {
                        setError(profileViewModel.message)
                        finish()
                    }
                }
            } else {
                setError(profileViewModel.message)
            }

        })

        profileViewModel.successfullyUpdated.observe(this, Observer {
            dismissDialog()
            if (it) {
                if (profileViewModel.status == "success") {
                    setError(profileViewModel.message)
                    // userPreferences.savePhoto(profileViewModel.photoPath)
                } else {
                    setError(profileViewModel.message)
                    finish()
                }
            } else {
                setError(profileViewModel.message)
            }
        })
    }

    private fun setLayout(userInfo: UserInfo) {
        binding.edProfileEmail.setText(userInfo.email)
        binding.edProfilePhone.setText(userInfo.phone)
        binding.roleValue.text = userInfo.role
        binding.profileName.setText(userInfo.fullName)
        binding.ccPickerProfile.setCountryForPhoneCode(userInfo.countryCode!!.toInt())
        Glide.with(this).load(BaseUrl.photoUrl + userInfo.profilePic).into(binding.profileImage)
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

        if (requestCode == REQUEST_TAKE_PHOTO) {


            Glide.with(this).load(mPhotoFile).into(binding.profileImage)

        } else if (requestCode == REQUEST_GALLERY_PHOTO) {
            val selectedImage = data!!.data
            try {
                mPhotoFile = File(getRealPathFromUri(selectedImage)!!)
                Glide.with(this).load(mPhotoFile).into(binding.profileImage)

            } catch (e: IOException) {

                e.printStackTrace()
            }
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

    override fun onClick(p0: View?) {
        when (p0!!.id) {


            R.id.followers -> {
                val intent = Intent(this, ViewFollowersActivity::class.java)
                intent.putExtra("type", "followers")
                startActivity(intent)
            }

            R.id.following -> {
                val intent = Intent(this, ViewFollowersActivity::class.java)
                intent.putExtra("type", "following")
                startActivity(intent)
            }

            R.id.donors -> {
                val intent = Intent(this, ViewFollowersActivity::class.java)
                intent.putExtra("type", "donors")
                startActivity(intent)
            }

            R.id.donate -> {
                openDonationsDialog()
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

            R.id.editName -> {
                setFocusableTrue(binding.profileName)
            }

            R.id.emailEdit -> {
                setFocusableTrue(binding.edProfileEmail)
            }


            R.id.phoneEdit -> {
                setFocusableTrue(binding.edProfilePhone)
                binding.ccPickerProfile.isClickable = true
            }

            R.id.btnUpdateProfile -> {
                setFocusableFalse(binding.profileName)
                setFocusableFalse(binding.edProfileEmail)
                setFocusableFalse(binding.edProfilePhone)
                binding.ccPickerProfile.isClickable = true

//                profileViewModel.editProfile(
//                    this,
//                    profileName.text.toString().trim(),
//                    edProfileEmail.text.toString().trim(),
//                    edProfilePhone.text.toString().trim(),
//                    userPreferences.getUserREf(),
//                    ccPickerProfile.selectedCountryCode,
//                    mPhotoFile
//                )
                showDialog()
            }
        }
    }

    private fun openDonationsDialog() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        val dialogb:DonateDialogBinding
        dialogb = DonateDialogBinding.inflate(layoutInflater)
        val view = binding.root
        dialog.setContentView(view)
        ///dialog.setContentView(R.layout.donate_dialog)
        dialog.window!!.setBackgroundDrawableResource(android.R.color.transparent);
        dialog.show()


        dialogb.submitDonation.setOnClickListener {
            dialog.dismiss()
        }

        dialogb.cancelDonation.setOnClickListener {
            dialog.dismiss()

        }

    }
}

