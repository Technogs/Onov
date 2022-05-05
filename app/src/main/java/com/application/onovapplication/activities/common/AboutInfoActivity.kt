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
import android.widget.AdapterView
import android.widget.EditText
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.application.onovapplication.BuildConfig
import com.application.onovapplication.R
import com.application.onovapplication.api.ApiClient
import com.application.onovapplication.api.ApiInterface
import com.application.onovapplication.databinding.ActivityAboutInfoBinding
import com.application.onovapplication.model.CitiesResponse
import com.application.onovapplication.model.UserInfo
import com.application.onovapplication.repository.BaseUrl
import com.application.onovapplication.utils.CustomSpinnerAdapter
import com.application.onovapplication.viewModels.ProfileViewModel
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.gson.JsonObject
import id.zelory.compressor.Compressor
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class AboutInfoActivity : BaseAppCompatActivity() ,View.OnClickListener{
    private lateinit var binding: ActivityAboutInfoBinding
    private var selectedState: String = ""
    private var selectedCities: String = ""
    private var apiInterface: ApiInterface? = null
    private val REQUEST_TAKE_PHOTO = 101
    private val REQUEST_GALLERY_PHOTO = 201
    private val REQUEST_TAKE_COVER_PHOTO = 105
    private val REQUEST_COVER_GALLERY_PHOTO = 205
    var mPhotoFile: File? = null
    var mPhotoFilecp: File? = null
    private var compressedImage: File? = null
    private var compressedImagep: File? = null
    private val profileViewModel by lazy { ViewModelProvider(this).get(ProfileViewModel::class.java) }
    private val rolesList =
        arrayOf("Select Party", "American Independent Party", "Democratic Party","Green Party",
            "Libertarian Party", "Peace and Freedom Party","Republican Party")
    var role:String=""
    private val statesList = arrayOf("Select State","Alaska","Arizona","Arkansas","California","Colorado","Connecticut","Delaware","Florida",
            "Georgia","Hawaii","Idaho","Illinois","Indiana","Iowa","Kansas","Kentucky","Louisiana","Maine","Maryland",
            "Massachusetts","Michigan","Minnesota","Mississippi","Missouri","Montana","Nebraska","Nevada","New Hampshire",
            "New Jersey","New Mexico","New York","North Carolina","North Dakota","Ohio","Oklahoma","Oregon","Pennsylvania",
            "Rhode Island","South Carolina","South Dakota","Tennessee","Texas","Utah","Vermont","Virginia",
            "Washington","West Virginia","Wisconsin","Wyoming")
   lateinit var userInfo: UserInfo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAboutInfoBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        userInfo = intent.getParcelableExtra("userinfo")!!
Log.d("userinfo",""+userInfo)

      if (userInfo.userRef!=userPreferences.getuserDetails()?.userRef){
          binding.etText.isFocusable = false
          binding.websiteLink.isFocusable = false
          binding.etUserName.isFocusable = false
          binding.edCountry.isFocusable = false
          binding.stateLyt.visibility=View.GONE
          binding.cityLyt.visibility=View.GONE
          binding.cityEditText.visibility=View.GONE
          binding.weburltext.setText("Address")
          binding.websiteLink.visibility=View.GONE
          binding.updateInfo.visibility=View.GONE
          binding.edText.visibility=View.GONE
          binding.spAbout.visibility=View.GONE
          binding.partySpinImg.visibility=View.GONE
          binding.etParty.visibility=View.VISIBLE
          binding.etParty.setText(userInfo.politicalParty)
          binding.pname.setText(userInfo.fullName)
          binding.countText.isCounterEnabled=false
          binding.ivProfile.isClickable=false
          binding.coverPhoto.isClickable=false
          binding.updateInfo.visibility=View.GONE
          binding.edCountry.setText(userInfo.cityName+", "+userInfo.stateName+", "+userInfo.countryName)
      }
        setSpinner()
        setData()
        observableViewModel()

    }

    private fun setData() {
        binding.editInfo.setOnClickListener(this)
   binding.updateInfo.setOnClickListener(this)
        binding.etText.setText(userInfo.about)
        binding.websiteLink.setText(userInfo.webUrl)
        binding.etUserName.setText(userInfo.fullName)
        Glide.with(this).load(BaseUrl.photoUrl + userInfo.profilePic)
            .apply(RequestOptions().placeholder(R.drawable.ic_baseline_account_circle_24))
            .into(binding.ivProfile)
        Glide.with(this).load(BaseUrl.photoUrl + userInfo.coverPhoto).into(binding.coverPhoto)

        for (i in rolesList.indices) {
            if (rolesList[i] == userInfo.politicalParty) {
                binding.spAbout.setSelection(i)
            }
        }

        binding.roleValue.text = userInfo.role
    }

    private fun setSpinner() {
        val spinnerAdapter = CustomSpinnerAdapter(
            this,  // Use our custom adapter
            R.layout.spinner_text, rolesList
        )


        spinnerAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown)
        binding.spAbout.adapter = spinnerAdapter


        binding.spAbout.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(
                parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                // selectedRole = parent?.getItemAtPosition(position).toString()
                role=rolesList[position]
            }
        }


        val spinnerAdapter3 = CustomSpinnerAdapter(
            this,  // Use our custom adapter
            R.layout.spinner_text, statesList
        )


        spinnerAdapter3.setDropDownViewResource(R.layout.simple_spinner_dropdown)
        binding.spState.adapter = spinnerAdapter3


        binding.spState.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                selectedState = parent?.getItemAtPosition(position).toString()
              //  getCitiesData(selectedState)
            }
        }
    }

    private  fun observableViewModel(){
        profileViewModel.successfullyUpdated.observe(this, androidx.lifecycle.Observer {
            dismissDialog()
            if (it) {

                Log.e("gfsgff",profileViewModel.status.toString())

                if (profileViewModel.status == "success") {
                    setError(profileViewModel.message)
//                    userInfo = profileViewModel.userInfo
//                    setLayout(profileViewModel.userInfo!!)
                    userPreferences.setUserDetails(profileViewModel.userInfo)
//                    val returnIntent = Intent()
//                    returnIntent.putExtra("uid", userPreferences.getuserDetails()?.userRef.toString())
//                    setResult(RESULT_OK, returnIntent)
//                    startActivity(returnIntent)
                    val intent = Intent(this, ProfileActivity2::class.java)
                    intent.putExtra("type", "user")
                    intent.putExtra("usrRef",userPreferences.getuserDetails()?.userRef.toString() )
                  startActivity(intent)
                    finish()
                   // finishAffinity()


                } else {
                    setError(profileViewModel.message)
                    finish()
                }
            } else {
                setError(profileViewModel.message)
            }
        })

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

//       if (data!=null) {
        if (requestCode == REQUEST_TAKE_PHOTO) {

            //Toast.makeText(this, "New Logo Added", Toast.LENGTH_SHORT).show()
            Glide.with(this).load(mPhotoFile).into(binding.ivProfile)
            mPhotoFile.let { imageFile ->
                this.lifecycleScope.launch {
                    // Default compression
                    compressedImagep = Compressor.compress(this@AboutInfoActivity, imageFile!!)
                }
            }


        } else if (requestCode == REQUEST_GALLERY_PHOTO) {
            val selectedImage = data?.data
            try {
                binding.ivProfile.setImageURI(selectedImage)

                Log.e("PRACHI", getRealPathFromUri(selectedImage)!!)
                mPhotoFile = File(getRealPathFromUri(selectedImage)!!)
//                Glide.with(this).load(mPhotoFile).into(binding.ivProfile)
                mPhotoFile.let { imageFile ->
                    this.lifecycleScope.launch {
                        // Default compression
                        compressedImagep =
                            Compressor.compress(this@AboutInfoActivity, imageFile!!)
                    }
                }

            } catch (e: IOException) {

                e.printStackTrace()
            }
        }

        if (requestCode == REQUEST_TAKE_COVER_PHOTO && resultCode== RESULT_OK) {
            val selectedImage = data?.data
//            binding.coverPhoto.setImageURI(selectedImage)
            Glide.with(this).load(mPhotoFilecp).into(binding.coverPhoto)
//mPhotoFilecp=File(getRealPathFromUri(selectedImage))

//            Glide.with(this).load(mPhotoFilecp).into(binding.coverPhoto)
            compressImage(File(mPhotoFilecp.toString()))

        } else if (requestCode == REQUEST_COVER_GALLERY_PHOTO) {
            val selectedImage = data?.data
            try {
                binding.coverPhoto.setImageURI(selectedImage)

//                Glide.with(this).load(mPhotoFilecp).into(binding.coverPhoto)

                mPhotoFilecp = File(getRealPathFromUri(selectedImage))
//                    mPhotoCoverFile =  compressImage(File(selectedImage.toString()))
//                    compressImage(File(selectedImage.toString()))

//                    Log.e("PRACHI", getRealPathFromUri(selectedImage)!!)
//                    mPhotoFile = File(getRealPathFromUri(selectedImage)!!)
//                    Glide.with(this).load(mPhotoFile).into(binding.ivRegister)
                mPhotoFilecp.let { imageFile ->
                    this.lifecycleScope.launch {
                        // Default compression
                        compressedImage =
                            Compressor.compress(this@AboutInfoActivity, imageFile!!)
                    }
                }
            } catch (e: IOException) {

                e.printStackTrace()
            }
        }
//        }


    }

    private fun getCitiesData(state:String) {
        showDialog()
        apiInterface = ApiClient.getRetrofit("https://countriesnow.space/api/v0.1/countries/state/")?.create(
            ApiInterface::class.java)
//        val call = apiInterface?.postJson(Gson().toJson(CitiesRequest("United States",  state)))
        val jsonObject = JsonObject()
        jsonObject.addProperty("country", "United States")
        jsonObject.addProperty("state", state)
        val call = apiInterface?.postJson(jsonObject)
        call?.enqueue(object : Callback<CitiesResponse?> {
            override fun onResponse(call: Call<CitiesResponse?>, response: Response<CitiesResponse?>) {
                dismissDialog()
                val response1 = response.body()
                if (response1!=null) {
//                    Toast.makeText(getActivity(), response1.msg, Toast.LENGTH_SHORT).show()
                    val r1 = response1.data.toTypedArray()
//                    Toast.makeText(this@AboutInfoActivity, "size is"+r1.size, Toast.LENGTH_SHORT).show()
                    val spinnerAdapter3 = CustomSpinnerAdapter(
                        this@AboutInfoActivity,  // Use our custom adapter
                        R.layout.spinner_text, r1
                    )


                    spinnerAdapter3.setDropDownViewResource(R.layout.simple_spinner_dropdown)
                    binding.spCities.adapter = spinnerAdapter3


                    binding.spCities.onItemSelectedListener = object :
                        AdapterView.OnItemSelectedListener {
                        override fun onNothingSelected(parent: AdapterView<*>?) {

                        }

                        override fun onItemSelected(
                            parent: AdapterView<*>?,
                            view: View?,
                            position: Int,
                            id: Long
                        ) {
                            selectedCities = parent?.getItemAtPosition(position).toString()

                        }
                    }

                }
            }
            override fun onFailure(call: Call<CitiesResponse?>, t: Throwable) {
                dismissDialog()
                if (t is IOException) {
                    Toast.makeText(this@AboutInfoActivity, t.message, Toast.LENGTH_SHORT).show()
                    //  Toast.makeText(requireActivity(), "Something went wrong, Please try again", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@AboutInfoActivity, "Something went wrong", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }
    private fun setFocusableFalse(
        editText: EditText) {
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
                mPhotoFilecp = photoFile
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                startActivityForResult(
                    takePictureIntent, REQUEST_TAKE_COVER_PHOTO
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


    private fun compressImage(photoFile: File) {

        Log.e("komal", "old file ${photoFile.length()}")

        photoFile.let { imageFile ->
            this.lifecycleScope.launch {
                // Default compression
                compressedImage = Compressor.compress(this@AboutInfoActivity, imageFile)
            }
        }

    }
    private fun compressImagecover(photoFile: File) {

        Log.e("komal", "old file ${photoFile.length()}")

        photoFile.let { imageFile ->
            this.lifecycleScope.launch {
                // Default compression
                compressedImagep = Compressor.compress(this@AboutInfoActivity, imageFile)
            }
        }

    }

    override fun onClick(v: View?) {
        when (v!!.id) {
          R.id.edit_info->{
              binding.editInfo.visibility=View.GONE
              binding.updateInfo.visibility=View.VISIBLE
//              binding.etText.isFocusable=true
              setFocusableTrue(binding.etText)
              setFocusableTrue(binding.websiteLink)
              //
          }
            R.id.ivProfile -> {
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
            R.id.update_info->{

                when {
                    checkEmpty(binding.etUserName) -> {
                        setError(getString(R.string.name_error))
                    }

//                    checkEmpty(binding.edRegisterEmail) -> {
//                        setError(getString(R.string.email_error))
//                    }
//
//                    checkEmpty(binding.edRegisterPhone) -> {
//                        setError(getString(R.string.phone_error))
//                    }
//
//                    checkEmpty(binding.edRegisterPassword) -> {
//                        setError(getString(R.string.password_error))
//                    }

                    role == "Select Party" -> {
                        setError("Please select Party")
                    }

                    checkEmpty(binding.websiteLink) -> {
                        setError("Website Url cannot be empty")
                    }

                    checkEmpty(binding.etText) -> {
                        setError("About me cannot be empty")
                    }

                    selectedState == "Select State" -> {
                        setError("Please select State")
                    }

                    else -> {
                        showDialog()
                        profileViewModel.editProfile(
                            this,
                            binding.etUserName.text.toString().trim(),
                            binding.etText.text.toString(),
                            role,
                            binding.websiteLink.text.toString(),
                            binding.edCountry.text.toString(),
                            selectedState,
                            binding.cityEditText.text.toString(),
                            userPreferences.getUserREf(),
                            compressedImage,//ccPickerProfile.selectedCountryCode,
                            compressedImagep
                        )

                    }
                }




            }
        }
    }



}
