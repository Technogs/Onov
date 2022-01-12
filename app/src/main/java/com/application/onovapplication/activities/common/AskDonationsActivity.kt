package com.application.onovapplication.activities.common

import android.Manifest
import android.app.AlertDialog
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.media.MediaPlayer
import android.net.Uri
import android.os.*
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.lifecycleScope
import com.abedelazizshe.lightcompressorlibrary.CompressionListener
import com.abedelazizshe.lightcompressorlibrary.VideoCompressor
import com.abedelazizshe.lightcompressorlibrary.VideoQuality
import com.application.onovapplication.BuildConfig
import com.application.onovapplication.R
import com.application.onovapplication.UrlConnection.MultipartUtility
import com.application.onovapplication.activities.DonorsActivity
import com.application.onovapplication.databinding.ActionBarLayout2Binding
import com.application.onovapplication.databinding.ActivityAboutInfoBinding
import com.application.onovapplication.databinding.ActivityAskDonationsBinding
import com.application.onovapplication.fragments.FeedFragment
import com.application.onovapplication.model.DonationModel
import com.application.onovapplication.model.EventModel
import com.application.onovapplication.model.FeedsData
import com.bumptech.glide.Glide
import com.google.gson.Gson
import id.zelory.compressor.Compressor

import kotlinx.coroutines.launch
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.concurrent.thread

class AskDonationsActivity : BaseAppCompatActivity(), View.OnClickListener {
    private var compressedImage: File? = null
    var mPhotoFile: File? = null
    var mVideoFile: File? = null
    val LAUNCH_SECOND_ACTIVITY = 11
    var result = arrayListOf<String>()


    private var videoFile: File? = null
    private var path: String=""
    var feeds: FeedsData? = null
    private var photopath: String=""
    private val GALLERYVIDEO = 1
    private var CAMERAVIDEO: Int = 2
    val PICK_DOC_FILE = 4
    private lateinit var data: EventModel
    private lateinit var data1: EventModel
    private val REQUEST_TAKE_PHOTO = 101
    private val REQUEST_GALLERY_PHOTO = 201
    lateinit var mtype:String
    private lateinit var binding: ActivityAskDonationsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAskDonationsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        val incBinding:ActionBarLayout2Binding=binding.inc
        incBinding.tvScreenTitle.text = getString(R.string.ask_for_donations)
        mtype=intent.getStringExtra("type").toString()
        if (intent.getParcelableExtra<FeedsData>("feed")!=null) {
            feeds = intent.getParcelableExtra("feed")
            if (feeds!=null)binding.addDonation.setText("Edit")
            binding.etDonationTitle.setText(feeds?.title)
            binding.dontnDesc.setText(feeds?.description)
            if (feeds?.fileType=="photo") {
                Glide.with(this).load(feeds?.filePath).into(binding.uploadPhoto)
                //  photopath=feeds?.filePath.toString()
            }//else  if (feeds?.fileType=="video") path=feeds?.filePath.toString()
            //  dntnGoal.setText(feeds?.)

        }

        if (mtype=="photo"){

            binding.picLyt.visibility=View.VISIBLE
            binding.videoLyt.visibility=View.GONE
        }else  if (mtype=="video"){
            binding.videoLyt.visibility=View.VISIBLE
            binding.picLyt.visibility=View.GONE
        }

    }

    override fun onClick(v: View?) {
        when (v?.id) {

            R.id.btnUploadDoc -> {
                openFile()
            }R.id.tagDonor -> {
            val i =  Intent(this, DonorsActivity::class.java)
            // i.putExtra("debate","debate")
            startActivityForResult(i, LAUNCH_SECOND_ACTIVITY)
            }
            R.id.addDonation -> {
                if (binding.addDonation.text=="Post") {
                    if (path == "") askdonation("photo", compressedImage.toString())
                    else if (path != "") askdonation("video", path)
                }else  if (binding.addDonation.text=="Edit") {
                    if (path == "") editdonation(feeds!!, photopath)
                    else if (path != "") editdonation(feeds!!, path)
                }
            } R.id.pic_lyt -> {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(
                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    ),
                    0
                )
            } else { showPictureDialog() }
        }
            R.id.video_lyt -> {
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
                } else { showVideoDialog() }
            }
        }
    }
    fun openFile() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "file/*"
        startActivityForResult(intent,PICK_DOC_FILE)
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
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(
                Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE), 0)
        } else { val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI)

            startActivityForResult(galleryIntent, GALLERYVIDEO)
        }
    }

    private fun takeVideoFromCamera() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this, arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE),
                0)
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
        if(data!= null){
            when (requestCode) {
                GALLERYVIDEO -> {
                    if (data != null) {
                        val contentURI: Uri = data.data!!
                        path = getMediaPath(contentURI)


                        val mp: MediaPlayer = MediaPlayer.create(this, Uri.fromFile(File(path)))
                        val duration: Int = mp.duration
                        mp.release()
                        if (duration / 10000 > 10) {

                            showPopUp()
                        } else {
                            videoFile = saveVideoFile(path)
                            compressVideo(path, videoFile)


                            videoFile = File(getRealPathFromUri(contentURI)!!)
                        }

                    }
                }

                CAMERAVIDEO -> {
                    if (data != null) {
                        val contentURI: Uri = data.data!!
                        path = getMediaPath(contentURI)

                        videoFile = saveVideoFile(path)
                        compressVideo(path, videoFile)
                    }
                }
                REQUEST_GALLERY_PHOTO -> {
                    val selectedImage = data!!.data
                    try {

                        mPhotoFile = File(getRealPathFromUri(selectedImage)!!)

                        photopath=mPhotoFile.toString()
                        compressImage(mPhotoFile!!)
                        //Glide.with(this).load(mPhotoFile).into(addCoverPhoto)

                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }

//                REQUEST_TAKE_PHOTO -> {
//                    val selectedImage = data.data
//                    try {
//
//                        mPhotoFile = File(getRealPathFromUri(selectedImage)!!)
//
//                        photopath=mPhotoFile.toString()
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
//                }

                PICK_DOC_FILE->{
                    val selectedImage = data.data
                    mPhotoFile = File(getRealPathFromUri(selectedImage)!!)

                }
                LAUNCH_SECOND_ACTIVITY->{
                    if (resultCode == AppCompatActivity.RESULT_OK) {
                          result=
                              data?.getStringArrayListExtra("result") as ArrayList<String>//("result")!!
                        Toast.makeText(this, ""+result, Toast.LENGTH_SHORT).show()
                        //  dntnViewModel.addDonations(this,userPreferences.getUserREf(),result,damount)

                    }
                    if (resultCode == AppCompatActivity.RESULT_CANCELED) {
                        // Write your code if there's no result
                        Log.e("result","no result found")
                    }

                }
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


//        if (requestCode == LAUNCH_SECOND_ACTIVITY) { }
    }

    private fun showPopUp() {
        val builder =
            androidx.appcompat.app.AlertDialog.Builder(this)
        builder.setTitle("Alert!")
        builder.setMessage("Video limit is 10 seconds. Would you like to add again?")
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
                compressedImage = Compressor.compress(this@AskDonationsActivity, imageFile)

                showDialog()
                Glide.with(this@AskDonationsActivity).load(compressedImage).into(binding.uploadPhoto)
                dismissDialog()
                Log.e("komal", "new file ${compressedImage!!.length()}")

            }
        }

    }
    private fun compressVideo(path: String, mediaoFile: File?) {
        videoFile?.let {
            VideoCompressor.start(path, mediaoFile!!.path,
                object : CompressionListener {
                    override fun onProgress(percent: Float) {
                        //Update UI
                        if (percent <= 100 && percent.toInt() % 5 == 0)
                            this@AskDonationsActivity.runOnUiThread {

                            }
                    }

                    override fun onStart() {

                    }

                    override fun onSuccess() {


                        Looper.myLooper()?.let {

                            Looper.myLooper()?.let {
                                Handler(it).postDelayed({
                                }, 50)
                            }
                        }

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
                        while (inputStream.read(buf).also { len = it } > 0) outputStream.write(buf, 0, len)
                    }
                }
                return file.absolutePath
            }
        } finally { cursor?.close() }
    }

//    fun askdonation(type:String,mediaFile: String){
//        showDialog()
//        val charset = "UTF-8"
//        val uploadFile1 = File(mediaFile)
//        //  val uploadFile2 = File("e:/Test/PIC2.JPG")
//        val requestURL = "https://bdztl.com/onov/api/v1/requestDonation"
//
//        try {
//
//            Thread {
//                // Run whatever background code you want here.
//
//
//
//                /*  Handler(Looper.getMainLooper()).postDelayed({*/
//                val multipart = MultipartUtility(requestURL, charset)
//                multipart.addFormField("userRef", userPreferences.getUserREf())
//                multipart.addFormField("title", binding.etDonationTitle.text.toString())
//                multipart.addFormField("donationGoal", binding.dntnGoal.text.toString())
//                multipart.addFormField("description", binding.dontnDesc.text.toString())
//                multipart.addFormField("tagPeopleArr", result)
//                multipart.addFormField("fileType", type)
//                multipart.addFilePart("imageFile", uploadFile1)
//                // val response: List<String> = multipart.finish() as List<String>
//                val response: String = multipart.finish()
//                println("SERVER REPLIED:")
//                //   dismissDialog()
////            for (line in response) {
////                println(line)
////                val mdonations=response as DonationModel
////                if (mdonations.status!="success"){
////                    setError(mdonations.msg)
////                }
////            }
//
//                val gson = Gson() // Or use new GsonBuilder().create();
//
//                data = gson.fromJson(response, EventModel::class.java)
//                if (data != null) {
//                    dismissDialog()
//                    if (data.status == "success") {
//                        setError(data.status)
//                        runOnUiThread {
//                            startActivity(Intent(this, HomeTabActivity::class.java))
//                            dismissDialog()
//
//                        }
//
//                    } else {
//                        setError(data.msg)
//                    }
//
////                    Handler(Looper.getMainLooper()).postDelayed({
////
////                    }, 2000)
//                }
//
//
//
//
//
//
//            }.start()
//
//            /*    Handler(Looper.getMainLooper()).postDelayed({
//                val multipart = MultipartUtility(requestURL, charset)
//                multipart.addFormField("userRef", userPreferences.getUserREf())
//                multipart.addFormField("title", binding.etDonationTitle.text.toString())
//                multipart.addFormField("donationGoal", binding.dntnGoal.text.toString())
//                multipart.addFormField("description", binding.dontnDesc.text.toString())
//                multipart.addFormField("fileType", type)
//                multipart.addFilePart("imageFile", uploadFile1)
//               // val response: List<String> = multipart.finish() as List<String>
//                val response: String = multipart.finish()
//                println("SERVER REPLIED:")
//             //   dismissDialog()
//    //            for (line in response) {
//    //                println(line)
//    //                val mdonations=response as DonationModel
//    //                if (mdonations.status!="success"){
//    //                    setError(mdonations.msg)
//    //                }
//    //            }
//
//                    val gson = Gson() // Or use new GsonBuilder().create();
//
//                    data = gson.fromJson(response, EventModel::class.java)
//                    if (data!=null) {
//                        dismissDialog()
//                        if (data.status=="success"){
//                            setError(data.status)
//                            startActivity(Intent(this,HomeTabActivity::class.java))
//                        }else {
//                            setError(data.msg)
//                        }
//
//    //                    Handler(Looper.getMainLooper()).postDelayed({
//    //
//    //                    }, 2000)
//                    }
//                }, 2000)*/
//        } catch (ex: IOException) {
//            System.err.println(ex)
//        }
//
//
//    }

    fun askdonation(type:String,mediaFile: String){

        val charset = "UTF-8"
        //  val uploadFile1 = File(mediaFile)
        val uploadFile2 = File(mediaFile)
        val requestURL = "https://bdztl.com/onov/api/v1/requestDonation"
        showDialog()
        try {

            Thread {

                val multipart = MultipartUtility(requestURL, charset)

                multipart.addFormField("userRef", userPreferences.getUserREf())
                multipart.addFormField("title", binding.etDonationTitle.text.toString())
                multipart.addFormField("donationGoal", binding.dntnGoal.text.toString())
                multipart.addFormField("description", binding.dontnDesc.text.toString())
                multipart.addFormField("tagPeopleArr", result.toString())
                multipart.addFormField("fileType", type)


//                multipart.addFormField("recordId",data.id)
//                multipart.addFormField("recordType", data.recordType)
//                multipart.addFormField("title", binding.etDonationTitle.text.toString())
//                multipart.addFormField("description", binding.dontnDesc.text.toString())
//                multipart.addFormField("fileType", data.fileType)
//                multipart.addFormField("donationGoal", binding.dntnGoal.text.toString())
//                multipart.addFormField("tagPeopleArr", result)
                multipart.addFilePart("imageFile", uploadFile2)
                Log.e("mediaFilepath...=  ",mediaFile.toString())
                //   cretePostViewModel.addFilePart("fileUpload", uploadFile2)
                //  val response: List<String> = multipart.finish() as List<String>
                val response: String = multipart.finish()
                println("SERVER REPLIED:")
                // dismissDialog()
                val gson = Gson() // Or use new GsonBuilder().create();

                data = gson.fromJson(response, EventModel::class.java)
                if (data!=null) {
                    setError(data.msg)
                    dismissDialog()

                }
                runOnUiThread {

                    if (data.status=="success"){

                        startActivity(Intent(this,HomeTabActivity::class.java))
                    }
                    Handler(Looper.getMainLooper()).postDelayed({

                    }, 2000)
                }

            }.start()


/*
            Handler(Looper.getMainLooper()).postDelayed({

            }, 2000)*/

        } catch (ex: IOException) {
            System.err.println(ex)
        }


    }
    fun editdonation(data:FeedsData,mediaFile: String){

        val charset = "UTF-8"
        //  val uploadFile1 = File(mediaFile)
        val uploadFile2 = File(mediaFile)
        val requestURL = "https://bdztl.com/onov/api/v1/editFeed"
        showDialog()
        try {
            Handler(Looper.getMainLooper()).postDelayed({
                val multipart = MultipartUtility(requestURL, charset)

                multipart.addFormField("recordId",data.id)
                multipart.addFormField("recordType", data.recordType)
                multipart.addFormField("title", binding.etDonationTitle.text.toString())
                multipart.addFormField("description", binding.dontnDesc.text.toString())
                multipart.addFormField("fileType", data.fileType)
                multipart.addFormField("donationGoal", binding.dntnGoal.text.toString())
                multipart.addFilePart("mediaFile", uploadFile2)
                Log.e("mediaFilepath...=  ",mediaFile.toString())
                //   cretePostViewModel.addFilePart("fileUpload", uploadFile2)
                //  val response: List<String> = multipart.finish() as List<String>
                val response: String = multipart.finish()
                println("SERVER REPLIED:")
                // dismissDialog()
                val gson = Gson() // Or use new GsonBuilder().create();

                data1 = gson.fromJson(response, EventModel::class.java)
                if (data!=null) {
                    setError(data1.msg)
                    dismissDialog()
                    if (data1.status=="success"){
//                      finishAffinity()
//                        finish()
                        startActivity(Intent(this,HomeTabActivity::class.java))
                    }
                    Handler(Looper.getMainLooper()).postDelayed({
                        /// finish()
                    }, 2000)
                }
            }, 2000)

        } catch (ex: IOException) {
            System.err.println(ex)
        }


    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this,HomeTabActivity::class.java))
    }
}