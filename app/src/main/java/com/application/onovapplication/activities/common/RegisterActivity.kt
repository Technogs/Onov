package com.application.onovapplication.activities.common

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.AdapterView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProvider
import com.application.onovapplication.BuildConfig
import com.application.onovapplication.R
import com.application.onovapplication.databinding.ActivityProfile2Binding
import com.application.onovapplication.databinding.ActivityRegisterBinding
import com.application.onovapplication.model.UserInfo
import com.application.onovapplication.utils.CustomSpinnerAdapter
import com.application.onovapplication.viewModels.SignUpViewModel
import com.bumptech.glide.Glide
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class RegisterActivity : BaseAppCompatActivity(), View.OnClickListener {

    var mPhotoFile: File? = null
    var userInfo:UserInfo?=null
    var mPhotoCoverFile: File? = null
    val REQUEST_TAKE_PHOTO = 101
    val REQUEST_GALLERY_PHOTO = 201

    private val REQUEST_TAKE_COVER_PHOTO = 105
    private val REQUEST_COVER_GALLERY_PHOTO = 205

    private var selectedRole: String = ""
    private var selectedSupport: String = ""
    private val signUpViewModel by lazy {
        ViewModelProvider(this).get(SignUpViewModel::class.java)
    }
    private val rolesList =
        arrayOf("Select Role", "Citizens", "Politicians", "Organizations", "Entertainers", "lpa")

    private val supportList =
        arrayOf("Select Support", "Republican", "Democrat", "Independent")
    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        setSpinner()

        observeViewModel()
    }

    private fun observeViewModel() {
        signUpViewModel.successful.observe(this, androidx.lifecycle.Observer { successful ->
            dismissDialog()
            if (successful) {
                if (signUpViewModel.status == "success") {

                    val intent = Intent(this, ForgotPasswordOtpActivity::class.java)
                    userPreferences.saveUserRef(signUpViewModel.userInfo!!.userRef)
                 userInfo?.userRef=signUpViewModel.userInfo?.userRef
                 userInfo?.id=signUpViewModel.userInfo?.id
                 userInfo?.profilePic=signUpViewModel.userInfo?.profilePic
           userPreferences.setUserDetails(userInfo)
                    intent.putExtra("role", signUpViewModel.userInfo!!.role)
                    intent.putExtra("type", "verify")
                    intent.putExtra("otp", signUpViewModel.userInfo!!.validationCode.toString())
                    intent.putExtra("email", binding.edRegisterEmail.text.toString().trim())
                    startActivity(intent)
                    finish()
                } else {
                    setError(signUpViewModel.message)

                }
            }
        })

    }

    private fun setSpinner() {
        val spinnerAdapter = CustomSpinnerAdapter(
            this,  // Use our custom adapter
            R.layout.spinner_text, rolesList
        )


        spinnerAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown)
        binding.spRegister.adapter = spinnerAdapter


        binding.spRegister.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                selectedRole = parent?.getItemAtPosition(position).toString()
            }
        }


        val spinnerAdapter2 = CustomSpinnerAdapter(
            this,  // Use our custom adapter
            R.layout.spinner_text, supportList
        )


        spinnerAdapter2.setDropDownViewResource(R.layout.simple_spinner_dropdown)
        binding.spSupport.adapter = spinnerAdapter2


        binding.spSupport.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                selectedSupport = parent?.getItemAtPosition(position).toString()
            }

        }
    }


    override fun onClick(v: View?) {
        when (v!!.id) {

            R.id.btSignUp -> {

                when {
                    checkEmpty(binding.edRegisterName) -> {
                        setError(getString(R.string.name_error))
                    }

                    checkEmpty(binding.edRegisterEmail) -> {
                        setError(getString(R.string.email_error))
                    }

                    checkEmpty(binding.edRegisterPhone) -> {
                        setError(getString(R.string.phone_error))
                    }

                    checkEmpty(binding.edRegisterPassword) -> {
                        setError(getString(R.string.password_error))
                    }

                    selectedRole == "Select Role" -> {
                        setError(getString(R.string.role_error))
                    }

                    checkEmpty(binding.edWebsiteUrl) -> {
                        setError("Website Url cannot be empty")
                    }

                    checkEmpty(binding.etAboutMe) -> {
                        setError("About me cannot be empty")
                    }

                    selectedRole == "Select Support" -> {
                        setError("Please select support")
                    }

                    else -> {
                        signUpViewModel.register(
                            this,
                            binding.edRegisterEmail.text.toString().trim(),
                            binding.edRegisterPassword.text.toString().trim(),
                            binding.edRegisterName.text.toString().trim(),
                            binding.edRegisterPhone.text.toString().trim(),
                            binding.ccPicker.selectedCountryCode,
                            mPhotoFile,
                            selectedRole,
                            "3223",
                            "Android",
                            binding.etAboutMe.text.toString().trim(),
                            binding.edWebsiteUrl.text.toString().trim(),
                            mPhotoCoverFile,
                            selectedSupport

                        )
                        showDialog()
                    }
                }
                //  signUpViewModel.register(this , )
            }


            R.id.tvLogin -> {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
            }

            R.id.ivRegister -> {
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

            R.id.coverPhoto -> {
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
        }
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
                mPhotoCoverFile = photoFile
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                startActivityForResult(
                    takePictureIntent, REQUEST_TAKE_COVER_PHOTO
                )
            }
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

            //Toast.makeText(this, "New Logo Added", Toast.LENGTH_SHORT).show()


            Glide.with(this).load(mPhotoFile).into(binding.ivRegister)

        } else if (requestCode == REQUEST_GALLERY_PHOTO) {
            val selectedImage = data?.data
            try {

                Log.e("PRACHI", getRealPathFromUri(selectedImage)!!)
                mPhotoFile = File(getRealPathFromUri(selectedImage)!!)
                Glide.with(this).load(mPhotoFile).into(binding.ivRegister)

            } catch (e: IOException) {

                e.printStackTrace()
            }
        }

        if (requestCode == REQUEST_TAKE_COVER_PHOTO) {


            Glide.with(this).load(mPhotoCoverFile).into(binding.coverPhoto)

        } else if (requestCode == REQUEST_COVER_GALLERY_PHOTO) {
            val selectedImage = data?.data
            try {
                mPhotoCoverFile = File(getRealPathFromUri(selectedImage))
                Glide.with(this).load(mPhotoCoverFile).into(binding.coverPhoto)

            } catch (e: IOException) {

                e.printStackTrace()
            }
        }


    }
}