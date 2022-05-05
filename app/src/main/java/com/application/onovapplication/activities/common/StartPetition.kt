package com.application.onovapplication.activities.common

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.location.Location
import android.media.MediaPlayer
import android.net.Uri
import android.os.*
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.abedelazizshe.lightcompressorlibrary.CompressionListener
import com.abedelazizshe.lightcompressorlibrary.VideoCompressor
import com.abedelazizshe.lightcompressorlibrary.VideoQuality
import com.application.onovapplication.BuildConfig
import com.application.onovapplication.R
import com.application.onovapplication.UrlConnection.MultipartUtility
import com.application.onovapplication.databinding.ActivitySettingsBinding
import com.application.onovapplication.databinding.ActivityStartPetitionBinding
import com.application.onovapplication.model.EventModel
import com.application.onovapplication.model.FeedsData
import com.application.onovapplication.repository.BaseUrl
import com.application.onovapplication.utils.CustomSpinnerAdapter
import com.application.onovapplication.viewModels.EventViewModel
import com.application.onovapplication.viewModels.PetitionViewModel
import com.application.onovapplication.viewModels.StatsViewModel
import com.bumptech.glide.Glide
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.gson.Gson
import id.zelory.compressor.Compressor

import kotlinx.coroutines.launch
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.concurrent.thread

class StartPetition : BaseAppCompatActivity(), View.OnClickListener {
    private var compressedImage: File? = null
    var mPhotoFile: File? = null
    var mVideoFile: File? = null
    var radius = ""
    var type = ""
    var pdate = ""
    var feeds: FeedsData? = null
    private val GALLERYVIDEO = 1
    private var CAMERAVIDEO: Int = 2
    private val REQUEST_TAKE_PHOTO = 101
    private val REQUEST_GALLERY_PHOTO = 201
    private var videoFile: File? = null
    private var path: String = ""
    private var photopath: String = ""
    var data: EventModel? = null;
    private val spinnerList =
        arrayOf("Select Petition Radius", "Local", "State", "National")
    private lateinit var binding: ActivityStartPetitionBinding
    val petitionViewModel by lazy { ViewModelProvider(this).get(PetitionViewModel::class.java) }
    lateinit var fusedLocationClient: FusedLocationProviderClient
    private val statsViewModel by lazy { ViewModelProvider(this).get(StatsViewModel::class.java) }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStartPetitionBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        fusedLocationClient  = LocationServices.getFusedLocationProviderClient(this)
        if (Build.VERSION.SDK_INT > 9) {
            val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
            StrictMode.setThreadPolicy(policy)
        }

        type = intent.getStringExtra("type").toString()
           setSpinner()
        if (intent.getParcelableExtra<FeedsData>("feed")!=null) {
            feeds = intent.getParcelableExtra("feed")
            if (feeds!=null)binding.createPt.setText("Edit")
            binding.etptTitle.setText(feeds?.petitionTitle)
            binding.etPtDescription.setText(feeds?.petitionDiscription)
            binding.pteWbLink.setText(feeds?.petitionWebsiteLink)
            binding.petitionDuration.setText(feeds?.petitionDuration)
            binding.petitionSignatureCount.setText(feeds?.petitionSignCount)
            binding.pLocation.setText(feeds?.petitionLocation)//cityName+", "+feeds?.stateName+", "+feeds?.countryName
            for (i in spinnerList.indices) {
                if (spinnerList[i] == feeds?.areaLimit) {
                    binding.spPetition.setSelection(i)
                }
            }
            if (feeds?.fileType=="photo") {
                Glide.with(this).load(BaseUrl.photoUrl+feeds?.petitionMedia).into(binding.uploadPhoto)
              //  photopath=feeds?.fileType.toString()
            }else if (feeds?.fileType=="video") {
                binding.uploadVideo.visibility=View.GONE
                binding.viewVideo.visibility=View.VISIBLE
                binding.viewVideo.setVideoURI(Uri.parse(feeds?.petitionMedia))            }

        }


        if (type == "photo") {
            binding.petitionPic.visibility = View.VISIBLE
            binding.petitionVideo.visibility = View.GONE
        } else if (type == "video") {
            binding.petitionPic.visibility = View.GONE
            binding.petitionVideo.visibility = View.VISIBLE
        }
        init()
        observeViewModel()
    }



    fun init() {

        val sdf = SimpleDateFormat("yyyy/MM/dd")
        pdate = sdf.format(Date())

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location : Location? ->
                binding.pLocation.setText(location.toString())
                // Got last known location. In some rare situations this can be null.
            }
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

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.petition_pic -> {
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
            }
            R.id.petition_video -> {
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
                        ), 0
                    )
                } else {
                    showVideoDialog()
                }
            }
            R.id.create_pt -> {
                if (binding.etptTitle.text.toString() == "") {
                    setError(getString(R.string.title_error))

                } else if (binding.etPtDescription.text.toString() == "") {
                    setError(getString(R.string.description_error))

                } else if (binding.petitionDuration.text.toString() == "" || binding.petitionDuration.text.toString()
                        .toInt() > 12
                ) {
                    setError(getString(R.string.duration_error))

                } else if (binding.petitionSignatureCount.text.toString() == "") {
                    setError(getString(R.string.sg_count_error))

//                } else if (type == "photo" && mPhotoFile.toString() == "") {
//                    setError(getString(R.string.photo_error))
//
//                } else if (type == "video" && path == "") {
//                    setError(getString(R.string.video_error))

                } else if (binding.pteWbLink.text.toString() == "") {
                    setError("Enter website link")

                } else    if ( radius=="Select Radius" )
                    setError("Please select a radius")
                else {

                    if (binding.createPt.text=="Post") {

                        if (type == "photo") {
                            Log.d("addPetition image Post" ,"yes")
                            if (compressedImage == null)
                                setError(getString(R.string.photo_error))
                        else { showDialog()
                                petitionViewModel.addPetition(
                                    this,
                                    userPreferences.getuserDetails()?.userRef.toString(),
                                    binding.etptTitle.text.toString(),
                                    binding.etPtDescription.text.toString(),
                                    pdate,
                                    binding.pteWbLink.text.toString(),
                                    binding.petitionDuration.text.toString(),
                                    binding.petitionSignatureCount.text.toString(),
                                    "photo",
                                    radius,
                                    binding.pLocation.text.toString(),
                                    compressedImage
                                )
                            }
                        } else if (type == "video") {
                            Log.d("addPetition video Post" ,"yes")
                            if (type == "video" && path == "")
                                setError(getString(R.string.video_error))
                          else      {showDialog()
                                    petitionViewModel.addPetition(
                                        this,
                                        userPreferences.getuserDetails()?.userRef.toString(),
                                        binding.etptTitle.text.toString(),
                                        binding.etPtDescription.text.toString(),
                                        pdate,
                                        binding.pteWbLink.text.toString(),
                                        binding.petitionDuration.text.toString(),
                                        binding.petitionSignatureCount.text.toString(),
                                        "video",
                                        radius,
                                        binding.pLocation.text.toString(),
                                        videoFile
                                    )
                                }

                        }
                    } else if (binding.createPt.text=="Edit") {
                        if (feeds?.fileType == "photo") {
                            Log.d("addPetition photo Edit" ,"yes")
                            petitionViewModel.editPetition(this,feeds?.id.toString(),binding.etptTitle.text.toString(),
                                binding.etPtDescription.text.toString(),binding.pteWbLink.text.toString(),binding.petitionDuration.text.toString(),
                                binding.petitionSignatureCount.text.toString(), "photo",radius,binding.pLocation.text.toString(),compressedImage)
                        } else if (feeds?.fileType == "video") {
                            Log.d("addPetition video Edit" ,"yes")
                            petitionViewModel.editPetition(this,feeds?.id.toString(),binding.etptTitle.text.toString(),
                                binding.etPtDescription.text.toString(),binding.pteWbLink.text.toString(),binding.petitionDuration.text.toString(),
                                binding.petitionSignatureCount.text.toString(), "video",radius,binding.pLocation.text.toString(),videoFile)
                        }
                    }
                }
            }
        }
    }


    private fun showVideoDialog() {
        val pictureDialog: AlertDialog.Builder = AlertDialog.Builder(this)
        pictureDialog.setTitle("Select Action")
        val pictureDialogItems = arrayOf(
            "Select video from gallery",
            "Record video from camera"
        )
        pictureDialog.setItems(
            pictureDialogItems
        ) { _, which ->
            when (which) {
                0 -> chooseVideoFromGallery()
                1 -> takeVideoFromCamera()
            }
        }
        pictureDialog.show()
    }

    private fun chooseVideoFromGallery() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this, arrayOf(
                    Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ), 0
            )
        } else {
            val galleryIntent =
                Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI)

            startActivityForResult(galleryIntent, GALLERYVIDEO)
        }
    }

    private fun takeVideoFromCamera() {
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
            val intent = Intent(MediaStore.ACTION_VIDEO_CAPTURE)
            intent.putExtra("android.intent.extra.durationLimit", 10)
            intent.putExtra("EXTRA_VIDEO_QUALITY", 0)
            startActivityForResult(intent, CAMERAVIDEO)
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
        if (takePictureIntent.resolveActivity(this.packageManager) != null) {
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
            this.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(mFileName, ".jpg", storageDir)
    }

    private fun observeViewModel() {
        petitionViewModel.successfulAddPetition.observe(this, androidx.lifecycle.Observer {
            dismissDialog()
            if (it != null) {
                if (it) {
                    if (petitionViewModel.status == "success") {
                        setError(petitionViewModel.loginResponse.msg.toString())
                     startActivity(Intent(this,HomeTabActivity::class.java))
                        finish()

                    } else {
                        setError(petitionViewModel.message)
                        finish()
                    }
                }
            } else {
                setError(petitionViewModel.message)
            }
        })
        petitionViewModel.successfulEditPetition.observe(this, androidx.lifecycle.Observer {
            dismissDialog()
            if (it != null) {
                if (it) {
                    if (petitionViewModel.status == "success") {
                        setError(petitionViewModel.loginResponse.msg.toString())
                     startActivity(Intent(this,HomeTabActivity::class.java))
                        finish()

                    } else {
                        setError(petitionViewModel.message)
                        finish()
                    }
                }
            } else {
                setError(petitionViewModel.message)
            }
        })
    }
    private fun getRealPathFromUri(contentUri: Uri?): String? {
        var cursor: Cursor? = null
        return try {
            val proj =
                arrayOf(MediaStore.Images.Media.DATA)
            cursor = this.contentResolver.query(contentUri!!, proj, null, null, null)
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
        if (data != null) {
            when (requestCode) {
                GALLERYVIDEO -> {
                    if (resultCode== RESULT_OK) {

                        binding.uploadVideo.visibility=View.GONE
                        binding.viewVideo.visibility=View.VISIBLE
                        val contentURI: Uri = data.data!!

                        binding.viewVideo.setVideoURI(contentURI)
                        binding.viewVideo.start()
                        path = getMediaPath(contentURI)


//                        val mp: MediaPlayer = MediaPlayer.create(this, Uri.fromFile(File(path)))
//                        val duration: Int = mp.duration
//                        mp.release()
//                        if (duration / 10000 > 10) {
//
//                            showPopUp()
//                        } else {
                        videoFile = saveVideoFile(path)
                        compressVideo(path, videoFile)



                    //}
                }
                }

                CAMERAVIDEO -> {
                    if (data != null) {
                        val contentURI: Uri = data.data!!
                        path = getMediaPath(contentURI)
                        binding.uploadVideo.visibility=View.GONE
                        binding.viewVideo.visibility=View.VISIBLE
//                    addVideo.visibility = View.VISIBLE
//                    addVideoImage.visibility = View.GONE
//                    addVideo.setVideoPath(path);
//                    addVideo.start();
                        binding.viewVideo.setVideoURI(contentURI)
                        binding.viewVideo.start()
                        videoFile = saveVideoFile(path)
                        compressVideo(path, videoFile)
                    }
                }
                REQUEST_GALLERY_PHOTO -> {
                    val selectedImage = data.data
                    try {

                        mPhotoFile = File(getRealPathFromUri(selectedImage)!!)

                        photopath = mPhotoFile.toString()
                        compressImage(mPhotoFile!!)
                        //Glide.with(this).load(mPhotoFile).into(addCoverPhoto)

                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }

//                REQUEST_TAKE_PHOTO -> {
//                    val selectedImage = data!!.data
//                    try {
//
//                        mPhotoFile = File(getRealPathFromUri(selectedImage)!!)
//
//                        photopath = mPhotoFile.toString()
//                        compressImage(mPhotoFile!!)
//                        //Glide.with(this).load(mPhotoFile).into(addCoverPhoto)
//
//                    } catch (e: IOException) {
//                        e.printStackTrace()
//                    }
//
//
//                    compressImage(mPhotoFile!!)
//
//                    //Glide.with(this).load(mPhotoFile).into(addCoverPhoto)
//
//                }
            }
        }

        if (requestCode== REQUEST_TAKE_PHOTO && resultCode== AppCompatActivity.RESULT_OK){
            binding.uploadPhoto.setImageURI(Uri.parse(mPhotoFile.toString()))


            try {

//                mPhotoFile = File(getRealPathFromUri(Uri.parse(mPhotoFile.toString()))!!)

                photopath=mPhotoFile.toString()
                compressImage(mPhotoFile!!)

            } catch (e: IOException) {
                e.printStackTrace()
            }


            compressImage(mPhotoFile!!)

//            Glide.with(this).load(mPhotoFile).into(binding.uploadPhoto)
        }

    }

    private fun showPopUp() {
        val builder =
            androidx.appcompat.app.AlertDialog.Builder(this)
        builder.setTitle("Alert!")
        builder.setMessage("Video limit is 30 seconds. Would you like to add again?")
        builder.setCancelable(true)
        builder.setPositiveButton(
            "Yes"
        ) { dialog, which ->

            showVideoDialog()
        }
        builder.setNegativeButton("Cancel")
        { dialog, which ->
            dialog.dismiss()
        }
        builder.show()
    }

    private fun compressImage(photoFile: File) {

        Log.e("komal", "old file ${photoFile.length()}")

        photoFile.let { imageFile ->
            this.lifecycleScope.launch {
                // Default compression
                compressedImage = Compressor.compress(this@StartPetition, imageFile)

                Glide.with(this@StartPetition).load(compressedImage).into(binding.uploadPhoto )

                Log.e("komal", "new file ${compressedImage!!.length()}")

            }
        }

    }

    private fun compressVideo(path: String, videoFile: File?) {
        videoFile?.let {
            VideoCompressor.start(
                path, videoFile.path,
                object : CompressionListener {
                    override fun onProgress(percent: Float) {
                        //Update UI
                        if (percent <= 100 && percent.toInt() % 5 == 0)
                            this@StartPetition.runOnUiThread {

                               binding.progressBar.progress = percent.toInt()
                            }
                    }

                    override fun onStart() {


                        binding.progressBar.visibility = View.VISIBLE
                        binding.progressBar.progress = 0
                    }

                    override fun onSuccess() {


                        Looper.myLooper()?.let {

                            Looper.myLooper()?.let {
                                Handler(it).postDelayed({
                                    binding.progressBar.visibility = View.GONE
                                }, 50)
                            }
                        }

//                        addVideo.visibility = View.VISIBLE
//                        add_pic.visibility = View.GONE
//                        addVideo.setVideoPath(path)
//                        addVideo.start();
                    }

                    override fun onFailure(failureMessage: String) {
                        Log.wtf("failureMessage", failureMessage)
                    }

                    override fun onCancelled() {
                        Log.wtf("TAG", "compression has been cancelled")
                        // make UI changes, cleanup, etc
                    }
                },
                VideoQuality.MEDIUM,
                isMinBitRateEnabled = true,
                keepOriginalResolution = false
            )
        }
    }

    private fun saveVideoFile(filePath: String?): File? {
        filePath?.let {
            val videoFile = File(filePath)
            val videoFileName = "${System.currentTimeMillis()}_${videoFile.name}"
            val folderName = Environment.DIRECTORY_MOVIES
            if (Build.VERSION.SDK_INT >= 30) {

                val values = ContentValues().apply {

                    put(
                        MediaStore.Images.Media.DISPLAY_NAME,
                        videoFileName
                    )
                    put(MediaStore.Images.Media.MIME_TYPE, "video/mp4")
                    put(MediaStore.Images.Media.RELATIVE_PATH, folderName)
                    put(MediaStore.Images.Media.IS_PENDING, 1)
                }

                val collection =
                    MediaStore.Video.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)

                val fileUri = this.applicationContext.contentResolver.insert(collection, values)

                fileUri?.let {
                    this.application.contentResolver.openFileDescriptor(fileUri, "rw")
                        .use { descriptor ->
                            descriptor?.let {
                                FileOutputStream(descriptor.fileDescriptor).use { out ->
                                    FileInputStream(videoFile).use { inputStream ->
                                        val buf = ByteArray(4096)
                                        while (true) {
                                            val sz = inputStream.read(buf)
                                            if (sz <= 0) break
                                            out.write(buf, 0, sz)
                                        }
                                    }
                                }
                            }
                        }

                    values.clear()
                    values.put(MediaStore.Video.Media.IS_PENDING, 0)
                    this.applicationContext.contentResolver.update(fileUri, values, null, null)

                    return File(getMediaPath(fileUri))
                }
            } else {
                val downloadsPath =
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                val desFile = File(downloadsPath, videoFileName)

                if (desFile.exists())
                    desFile.delete()

                try {
                    desFile.createNewFile()
                } catch (e: IOException) {
                    e.printStackTrace()
                }

                return desFile
            }
        }
        return null
    }

    private fun getMediaPath(uri: Uri): String {

        val resolver = this.contentResolver
        val projection = arrayOf(MediaStore.Video.Media.DATA)
        var cursor: Cursor? = null
        try {
            cursor = resolver.query(uri, projection, null, null, null)
            return if (cursor != null) {
                val columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA)
                cursor.moveToFirst()
                cursor.getString(columnIndex)

            } else ""

        } catch (e: Exception) {
            resolver.let {
                val filePath = (this.applicationInfo.dataDir + File.separator
                        + System.currentTimeMillis())
                val file = File(filePath)

                resolver.openInputStream(uri)?.use { inputStream ->
                    FileOutputStream(file).use { outputStream ->
                        val buf = ByteArray(4096)
                        var len: Int
                        while (inputStream.read(buf).also { len = it } > 0) outputStream.write(
                            buf,
                            0,
                            len
                        )
                    }
                }
                return file.absolutePath
            }
        } finally {
            cursor?.close()
        }
    }
}