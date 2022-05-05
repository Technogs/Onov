package com.application.onovapplication.activities

import android.Manifest
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.application.onovapplication.BuildConfig
import com.application.onovapplication.R
import com.application.onovapplication.activities.common.BaseAppCompatActivity
import com.application.onovapplication.activities.common.HomeTabActivity
import com.application.onovapplication.adapters.PollingAdapter
import com.application.onovapplication.databinding.ActivityPollBinding
import com.application.onovapplication.model.FeedsData
import com.application.onovapplication.model.PollingOptions
import com.application.onovapplication.repository.BaseUrl
import com.application.onovapplication.utils.CustomSpinnerAdapter
import com.application.onovapplication.viewModels.PollingViewModel
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import id.zelory.compressor.Compressor
import kotlinx.coroutines.launch
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class PollActivity : BaseAppCompatActivity(),View.OnClickListener , PollingAdapter.onclickCross{
    var publicStatus = "0"
    var multipleStatus = "0"
    lateinit var binding: ActivityPollBinding
    val pollingViewModel by lazy { ViewModelProvider(this).get(PollingViewModel::class.java) }
    val ids = ArrayList<String>()
    private val spinnerList = arrayOf("Select Radius", "Local", "State", "National")
    var radius = ""
    var date = ""
    var time = ""
    var size: Int = 2
    var mPhotoFile: File? = null
    private var compressedImage: File? = null
    private var photopath: String=""
    private val TAKE_PHOTO = 4
    var feeds: FeedsData? = null
    lateinit var mtype:String
    private val REQUEST_GALLERY_PHOTO = 201
    var votingAdapter: PollingAdapter? = null
    private val pollingOptions by lazy { PollingOptions(size) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_poll)
        binding = ActivityPollBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        mtype=intent.getStringExtra("type").toString()
        if (intent.getParcelableExtra<FeedsData>("feed")!=null) {
            feeds = intent.getParcelableExtra("feed")
            if (feeds!=null)binding.createPoll.setText("Edit")
            binding.etPollTitle.setText(feeds?.polTitle)
//            binding.dontnDesc.setText(feeds?.description)
            if (feeds?.fileType=="photo") {
                Glide.with(this).load(feeds?.filePath).into(binding.uploadPhoto)
            }


        }
        initView()
        votingAdapter = PollingAdapter(this, this, pollingOptions)
        setSpinner()
        setSwitchEventListener()
        observeViewModel()
        binding.rvVoting.adapter = votingAdapter
        binding.etAddOption.setOnClickListener {
            pollingOptions.count = pollingOptions.count + 1
            size=pollingOptions.count
            votingAdapter!!.notifyDataSetChanged()
        }
//        binding.etPollingDate.setOnClickListener { setDatePicker(binding.etPollingDate) }
//        binding.etPollingTime.setOnClickListener { setTimePicker(binding.etPollingTime) }
//        binding.createPoll.setOnClickListener {
//
//            showDialog()
////            if (ids.isNullOrEmpty()) {
//            Log.d("arrayids", ids.toString())
//
//            pollingViewModel.cretePolling(
//                this, userPreferences.getuserDetails()?.userRef.toString(),
//                binding.etPollTitle.text.toString(), ids,
//                binding.etPollingDate.text.toString(), binding.etPollingTime.text.toString()
//            )
////            }
//        }
    }
fun initView(){
    Glide.with(this).load(BaseUrl.photoUrl + userPreferences.getuserDetails()?.profilePic)
        .apply(RequestOptions().placeholder(R.drawable.ic_baseline_account_circle_24))
        .into(binding.ivChatProfile)
    binding.userName.setText(userPreferences.getuserDetails()?.fullName)
}

    override fun onclick() {
        pollingOptions.count = pollingOptions.count - 1
        size=pollingOptions.count
        votingAdapter!!.notifyDataSetChanged()
    }
    private fun setSpinner() {
        val spinnerAdapter = CustomSpinnerAdapter(
       this,  // Use our custom adapter
            R.layout.spinner_text, spinnerList
        )


        spinnerAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown)
        binding.spPetition.adapter = spinnerAdapter


        binding.spPetition.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                radius = spinnerList[position]
            }
        }
    }
    override fun onClickOption(option: String) {
        ids.add(option)
    }

    private fun setSwitchEventListener() {
        binding.multipleAnswers.setOnClickListener {
            multipleStatus = if (binding.multipleAnswers.isChecked) { "1" } else { "0" }
        }

        binding.publicSwitch.setOnClickListener {
            publicStatus = if (binding.publicSwitch.isChecked) { "1" } else { "0" }

        }

    }

    private fun setDatePicker(editText: TextView?) {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)


        val dpd: DatePickerDialog = DatePickerDialog(
            this,
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                // Display Selected date in textbox

                val selectedMonth = String.format("%02d", monthOfYear + 1)
                val selectedDate = String.format("%02d", dayOfMonth)
date="$year-$selectedMonth-$selectedDate"
                editText?.text = convertDateFormat(
                    "$year-$selectedMonth-$selectedDate",
                    "yyyy-MM-dd",
                    "MMM dd,yyyy"
                ).toString() //"$year-$selectedMonth-$selectedDate"
//                convertDateFormat(
//                    "$year-$selectedMonth-$selectedDate",
//                    "yyyy-MM-dd hh:mm:ss",
//                    "hh:mm a"
//                ).trim()
            },
            year,
            month,
            day
        )
        dpd.show()
    }

    private fun setTimePicker(editText: TextView?) {
        val cal = Calendar.getInstance()
        val timeSetListener =
            TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
                cal.set(Calendar.HOUR_OF_DAY, hour)
                cal.set(Calendar.MINUTE, minute)
                time = SimpleDateFormat("HH:mm").format(cal.time)
                editText?.text = SimpleDateFormat("hh:mm a").format(cal.time)
            }
        TimePickerDialog(
           this,
            timeSetListener,
            cal.get(Calendar.HOUR_OF_DAY),
            cal.get(Calendar.MINUTE),
            true
        ).show()
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
                Log.d("checkcamerapic",""+"photoFile is:"+photoFile+" photoURI"+photoURI+" mPhotoFile"+mPhotoFile)
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                startActivityForResult(
                    takePictureIntent, TAKE_PHOTO
                )
            }
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(data!= null){
            when (requestCode) {

                REQUEST_GALLERY_PHOTO -> {
                    val selectedImage = data.data
                    try {

                        mPhotoFile = File(getRealPathFromUri(selectedImage)!!)

                        photopath=mPhotoFile.toString()
                        compressImage(mPhotoFile!!)
                        Glide.with(this).load(mPhotoFile).into(binding.uploadPhoto)
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }


            }
        }
        if (requestCode== TAKE_PHOTO && resultCode== AppCompatActivity.RESULT_OK){
            binding.uploadPhoto.setImageURI(Uri.parse(mPhotoFile.toString()))


            try {

//                mPhotoFile = File(getRealPathFromUri(Uri.parse(mPhotoFile.toString()))!!)

                photopath=mPhotoFile.toString()
                compressImage(mPhotoFile!!)

            } catch (e: IOException) {
                e.printStackTrace()
            }

            Glide.with(this).load(mPhotoFile).into(binding.uploadPhoto)
        }
    }

    private fun getRealPathFromUri(contentUri: Uri?): String? {
        var cursor: Cursor? = null
        return try {
            val proj =
                arrayOf(MediaStore.Images.Media.DATA)
            cursor =contentResolver.query(contentUri!!, proj, null, null, null)
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
    override fun onClick(v: View?) {
        when (v?.id) {
           R.id.etPollingDate->{
               setDatePicker(binding.etPollingDate)
           }
           R.id.etPollingTime->{
               setTimePicker(binding.etPollingTime)
           }  R.id.add_photo->{
            if (
                ContextCompat.checkSelfPermission(
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
           } R.id.createPoll->{
            showDialog()
            Log.d("arrayids", ids.toString())
            val opt: String = TextUtils.join(", ", ids)
            Log.d("arrayopt", opt)
            pollingViewModel.cretePolling(
                this, userPreferences.getuserDetails()?.userRef.toString(),
                binding.etPollTitle.text.toString(), opt,
                date, time,
                radius,publicStatus,multipleStatus,compressedImage
            )
           }
        }
    }
    private fun compressImage(photoFile: File) {

        Log.e("komal", "old file ${photoFile.length()}")

        photoFile.let { imageFile ->
            this.lifecycleScope.launch {
                // Default compression
                compressedImage = Compressor.compress(this@PollActivity, imageFile)
                Glide.with(this@PollActivity).load(compressedImage).into(binding.uploadPhoto)

                Log.e("komal", "new file ${compressedImage!!.length()}")

            }
        }

    }
    private fun observeViewModel() {

        pollingViewModel.successful.observe(this, androidx.lifecycle.Observer {
            dismissDialog()
            if (it != null) {
                if (it) {
                    if (pollingViewModel.status == "success") {
                        startActivity(Intent(this, HomeTabActivity::class.java))

                    } else { setError(pollingViewModel.message) }
                }
            } else { setError(pollingViewModel.message) }

        })
    }
}