package com.application.onovapplication.activities.common

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.*
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.application.onovapplication.BuildConfig
import com.application.onovapplication.R
import com.application.onovapplication.UrlConnection.MultipartUtility
import com.application.onovapplication.api.ApiClient
import com.application.onovapplication.api.ApiInterface
import com.application.onovapplication.databinding.ActivityRegisterBinding
import com.application.onovapplication.model.CitiesResponse
import com.application.onovapplication.model.RegisterResponse
import com.application.onovapplication.model.UserInfo
import com.application.onovapplication.utils.CustomSpinnerAdapter
import com.application.onovapplication.viewModels.SignUpViewModel
import com.bumptech.glide.Glide
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId
import com.google.gson.Gson
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
import kotlin.concurrent.thread

class RegisterActivity : BaseAppCompatActivity(), View.OnClickListener {

    var mPhotoFile: File? = null
    var userInfo:UserInfo?=null
    var mPhotoCoverFile: File? = null
    val REQUEST_TAKE_PHOTO = 101
    val REQUEST_GALLERY_PHOTO = 201
    private var apiInterface: ApiInterface? = null
    private var compressedImage: File? = null
    private var compressedImagep: File? = null
    var data: RegisterResponse? = null
    var token = ""
    private val REQUEST_TAKE_COVER_PHOTO = 105
    private val REQUEST_COVER_GALLERY_PHOTO = 205

    private var selectedRole: String = ""
    private var selectedParty: String = ""
    private var selectedState: String = ""
    private var selectedCities: String = ""
    private var selectedSupport: String = ""
    private val signUpViewModel by lazy {
        ViewModelProvider(this).get(SignUpViewModel::class.java)
    }
    private val rolesList =
        arrayOf("Select Role", "Citizens", "Politicians", "Organizations", "Entertainers", "lpa")

    private val partyList =
        arrayOf("Select Party", "American Independent Party", "Democratic Party","Green Party",
          "Libertarian Party", "Peace and Freedom Party","Republican Party")

    private val statesList = arrayOf("Select State","Alaska","Arizona","Arkansas","California","Colorado","Connecticut"
        ,"Delaware","Florida", "Georgia","Hawaii","Idaho","Illinois","Indiana","Iowa","Kansas","Kentucky", "Louisiana"
        ,"Maine", "Maryland", "Massachusetts","Michigan","Minnesota","Mississippi","Missouri","Montana","Nebraska",
        "Nevada","New Hampshire", "New Jersey","New Mexico","New York","North Carolina", "North Dakota","Ohio",
        "Oklahoma","Oregon","Pennsylvania", "Rhode Island","South Carolina", "South Dakota", "Tennessee",
        "Texas","Utah","Vermont","Virginia", "Washington","West Virginia", "Wisconsin","Wyoming")
    private val supportList =
        arrayOf("Select Support", "Republican", "Democrat", "Independent")
    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        if (Build.VERSION.SDK_INT > 9) {
            val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
            StrictMode.setThreadPolicy(policy) }
        setSpinner()
deviceToken()
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

        val spinnerAdapterparty = CustomSpinnerAdapter(
            this,  // Use our custom adapter
            R.layout.spinner_text, partyList
        )


        spinnerAdapterparty.setDropDownViewResource(R.layout.simple_spinner_dropdown)
        binding.spparties.adapter = spinnerAdapterparty


        binding.spparties.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                selectedParty = parent?.getItemAtPosition(position).toString()
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
               // getCitiesData(selectedState)
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
                    val r1 = response1.data.toTypedArray()
                    val spinnerAdapter3 = CustomSpinnerAdapter(
                        this@RegisterActivity,  // Use our custom adapter
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
                    Toast.makeText(this@RegisterActivity, t.message, Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@RegisterActivity, "Something went wrong", Toast.LENGTH_SHORT).show()
                }
            }
        })
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

                    compressedImage==null->{
                        setError("Please add profile image")
                    }
                    compressedImagep==null->{
                        setError("Please add cover image")
                    }
                    selectedState == "Select State" -> {
                        setError("Please select State")
                    }
                    checkEmpty(binding.cityEditText) -> {
                        setError("Enter your city")
                    }

                    selectedParty == "Select Party" -> {
                        setError("Please select a Party")
                    }
                    checkEmpty(binding.etAboutMe) -> {
                        setError("About me cannot be empty")
                    }

                    selectedRole == "Select Support" -> {
                        setError("Please select support")
                    }

                    else -> {
//                        signUpViewModel.register(
//                            this,
//                            binding.edRegisterEmail.text.toString().trim(),
//                            binding.edRegisterPassword.text.toString().trim(),
//                            binding.edRegisterName.text.toString().trim(),
//                            binding.edRegisterPhone.text.toString().trim(),
//                            binding.edCountry.text.toString().trim(),
//                            selectedState,
//                            selectedCities,
//                            binding.ccPicker.selectedCountryCode,
//                            mPhotoFile,
//                            selectedRole,
//                            "3223",
//                            "Android",
//                            binding.etAboutMe.text.toString().trim(),
//                            binding.edWebsiteUrl.text.toString().trim(),
//                            mPhotoCoverFile,
//                            selectedSupport
//
//                        )
                        register()
                        showDialog()
                    }
                }
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
    private fun compressImage(photoFile: File) {

        Log.e("komal", "old file ${photoFile.length()}")

        photoFile.let { imageFile ->
            this.lifecycleScope.launch {
                // Default compression
                compressedImage = Compressor.compress(this@RegisterActivity, imageFile)
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

      // if (data!=null) {
            if (requestCode == REQUEST_TAKE_PHOTO && resultCode== RESULT_OK) {

                Glide.with(this).load(mPhotoFile).into(binding.ivRegister)
                mPhotoFile.let { imageFile ->
                    this.lifecycleScope.launch {
                        // Default compression
                        compressedImagep = Compressor.compress(this@RegisterActivity, imageFile!!)
                    }
                }


            } else if (requestCode == REQUEST_GALLERY_PHOTO && resultCode== RESULT_OK) {
                val selectedImage = data?.data
                try {

                    Log.e("PRACHI", getRealPathFromUri(selectedImage)!!)
                    mPhotoFile = File(getRealPathFromUri(selectedImage)!!)
                    Glide.with(this).load(mPhotoFile).into(binding.ivRegister)
                    mPhotoFile.let { imageFile ->
                        this.lifecycleScope.launch {
                            // Default compression
                            compressedImagep =
                                Compressor.compress(this@RegisterActivity, imageFile!!)
                        }
                    }

                } catch (e: IOException) {

                    e.printStackTrace()
                }
            }

            if (requestCode == REQUEST_TAKE_COVER_PHOTO && resultCode== RESULT_OK) {


                Glide.with(this).load(mPhotoCoverFile).into(binding.coverPhoto)
                compressImage(File(mPhotoCoverFile.toString()))

            } else if (requestCode == REQUEST_COVER_GALLERY_PHOTO && resultCode== RESULT_OK) {
                val selectedImage = data?.data
                try {

                    mPhotoCoverFile = File(getRealPathFromUri(selectedImage))
//                    mPhotoCoverFile =  compressImage(File(selectedImage.toString()))
                    Glide.with(this).load(mPhotoCoverFile).into(binding.coverPhoto)
//                    compressImage(File(selectedImage.toString()))

//                    Log.e("PRACHI", getRealPathFromUri(selectedImage)!!)
//                    mPhotoFile = File(getRealPathFromUri(selectedImage)!!)
//                    Glide.with(this).load(mPhotoFile).into(binding.ivRegister)
                    mPhotoCoverFile.let { imageFile ->
                        this.lifecycleScope.launch {
                            // Default compression
                            compressedImage =
                                Compressor.compress(this@RegisterActivity, imageFile!!)
                        }
                    }
                } catch (e: IOException) {

                    e.printStackTrace()
                }
            }


    }
    fun deviceToken(){


        // 1
        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener(OnCompleteListener { task ->
                // 2
                if (!task.isSuccessful) {
                    Log.w("TAG token failed", "getInstanceId failed", task.exception)
                    return@OnCompleteListener
                }
                // 3
                token = task.result?.token.toString()

                // 4
                val msg = token
                Log.d("TAG token", msg.toString())
                userPreferences.saveUserToken(msg.toString())

            }) }

    fun register(){

        val charset = "UTF-8"
        showDialog()
        try {
            var uploadFile1=File("")
            var uploadFile2=File("")
            thread {

                //  val uploadFile2 = File(path)
                val requestURL = "https://bdztl.com/onov/api/v1/userRegister"
                /*     runOnUiThread {  }*/

                val multipart = MultipartUtility(requestURL, charset)
                multipart.addFormField("fullName", binding.edRegisterName.text.toString().trim())
                multipart.addFormField("email", binding.edRegisterEmail.text.toString().trim())
                multipart.addFormField("countryCode",  binding.ccPicker.selectedCountryCode)
                multipart.addFormField("phone",  binding.edRegisterPhone.text.toString().trim())
                multipart.addFormField("password", binding.edRegisterPassword.text.toString().trim())
                multipart.addFormField("role", selectedRole)
                multipart.addFormField("about", binding.etAboutMe.text.toString().trim())
                multipart.addFormField("countryName", binding.edCountry.text.toString().trim())
                multipart.addFormField("stateName", selectedState)
                multipart.addFormField("cityName",binding.cityEditText.text.toString()) //selectedCities)
                multipart.addFormField("webUrl",  binding.edWebsiteUrl.text.toString().trim())
                multipart.addFormField("deviceToken", token)
                multipart.addFormField("deviceType", "Android")
                multipart.addFormField("politicalParty", selectedParty)
                multipart.addFilePart("profilePic", compressedImagep)
                multipart.addFilePart("coverPhoto", compressedImage)
                val response: String = multipart.finish()

                println("SERVER REPLIED:")

                val gson = Gson() // Or use new GsonBuilder().create();

                data = gson.fromJson(response, RegisterResponse::class.java)

                if (data!=null) {
                    setError(data?.msg.toString())
                    dismissDialog()
                    Handler(Looper.getMainLooper()).postDelayed({
                        //finish()
                        if (data?.status=="success") {
                            val intent = Intent(this, ForgotPasswordOtpActivity::class.java)
                            userPreferences.saveUserRef(data?.registrationData?.userRef)
                            userInfo?.userRef = data?.registrationData?.userRef
                            userInfo?.id = data?.registrationData?.id
                            userInfo?.profilePic = data?.registrationData?.profilePic
                            userPreferences.setUserDetails(data?.registrationData)
                            intent.putExtra("role", data?.registrationData?.role)
                            intent.putExtra("type", "verify")
                            intent.putExtra(
                                "otp",
                                data?.registrationData?.validationCode.toString()
                            )
                            intent.putExtra("email", binding.edRegisterEmail.text.toString().trim())
                            startActivity(intent)
                            finish()
                            finishAffinity()
                        }else setError(data?.msg.toString())

                                                                }, 2000)
                }

            }


        } catch (ex: IOException) {
            System.err.println(ex)
        }
    }
}