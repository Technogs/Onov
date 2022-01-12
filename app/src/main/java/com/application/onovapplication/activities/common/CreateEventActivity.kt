package com.application.onovapplication.activities.common

import android.Manifest
//https://stackoverflow.com/questions/34691175/how-to-send-httprequest-and-get-json-response-in-android

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
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
import android.widget.EditText
import android.widget.Toast
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
import com.application.onovapplication.activities.EventsActivity
import com.application.onovapplication.databinding.ActionBarLayout2Binding
import com.application.onovapplication.databinding.ActivityChangePasswordBinding
import com.application.onovapplication.databinding.ActivityCreateEventBinding
import com.application.onovapplication.model.EventModel
import com.application.onovapplication.viewModels.CreateEventViewModel
import com.bumptech.glide.Glide
import com.google.gson.Gson
import id.zelory.compressor.Compressor

import kotlinx.coroutines.launch
import java.io.*
import java.net.HttpURLConnection
import java.text.SimpleDateFormat
import java.util.*
import kotlin.concurrent.thread


class CreateEventActivity : BaseAppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityCreateEventBinding

    //   val creteEventViewModel by lazy { ViewModelProvider(this).get(CreateEventViewModel::class.java) }
    private var videoFile: File? = null
    private var path: String=""
    private var photopath: String=""
    private lateinit var videopath: String
    var data: EventModel? = null
    private var compressedImage: File? = null
    var mPhotoFile: File? = null
    var mVideoFile: File? = null
    val LAUNCH_INFO_ACTIVITY = 11
    var type = ""

    private val GALLERYVIDEO = 1
    private var CAMERAVIDEO: Int = 2
    private val REQUEST_TAKE_PHOTO = 101
    private val REQUEST_GALLERY_PHOTO = 201
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateEventBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        val incBinding: ActionBarLayout2Binding =binding.ab

        if (Build.VERSION.SDK_INT > 9) {
            val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
            StrictMode.setThreadPolicy(policy) }

        type = intent.getStringExtra("type").toString()

        incBinding.tvScreenTitle.text = getString(R.string.create_event)
        if (type == "photo") {
            binding.picLayout.visibility = View.VISIBLE
            binding.videoLyt.visibility = View.GONE
        } else if (type == "video") {
            binding.picLayout.visibility = View.GONE
            binding.videoLyt.visibility = View.VISIBLE
        }
        //tvScreenTitleRight.text = getString(R.string.post)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.edEventStartDate -> {
                setDatePicker(binding.edEventStartDate)
            }

            R.id.edEventEndDate -> {
                setDatePicker(binding.edEventEndDate)

            }
            R.id.edEventEndTime -> {
                setTimePicker(binding.edEventEndTime)
            }

            R.id.edEventStartTime -> {
                setTimePicker(binding.edEventStartTime)
            }
            R.id.create_event -> {
                if (binding.etEventTitle.text.toString() == "") {
                    setError(getString(R.string.title_error))

                } else if (binding.etEventPrice.text.toString() == "") {
                    setError(getString(R.string.event_price_error))

                } else if (binding.edEventStartDate.text.toString() == "") {
                    setError(getString(R.string.start_date_error))

                } else if (binding.edEventEndDate.text.toString() == "") {
                    setError(getString(R.string.end_date_error))

                } else if (binding.edEventStartTime.text.toString() == "") {
                    setError(getString(R.string.start_time_error))

                } else if (binding.edEventEndTime.text.toString() == "") {
                    setError(getString(R.string.end_time_error))

                } else if (binding.etPostDescription.text.toString() == "") {
                    setError(getString(R.string.description_error))

//                } else if ( photopath == "") {
//                    setError(getString(R.string.photo_error))
//
//                } else if (path == "") {
//                    setError(getString(R.string.video_error))
                } else {

                    addEvent(
                        binding.etEventTitle.text.toString(),
                        binding.etPostDescription.text.toString()
                    )}
            }
            R.id.pic_layout -> {
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
                } else {
                    showPictureDialog()
                }
            }
            R.id.video_lyt -> {
                if (ContextCompat.checkSelfPermission(
                        this,
                        Manifest.permission.CAMERA
                    ) != PackageManager.PERMISSION_GRANTED&&
                    ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED
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
                    showVideoDialog()
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


                            ////////////////
                            videoFile = File(getRealPathFromUri(contentURI)!!)
//                        // Get the Video from data

//                        // Get the Video from data
//                        val selectedVideo: Uri = data.data!!
//                        val filePathColumn = arrayOf(MediaStore.Video.Media.DATA)
//
//                        val cursor: Cursor =
//                            activity?.getContentResolver()?.query(
//                                selectedVideo,
//                                filePathColumn,
//                                null,
//                                null,
//                                null
//                            )!!
//                        cursor.moveToFirst()
//
//                        val columnIndex = cursor.getColumnIndex(filePathColumn[0])
//
//                      val  mediaPath1 = cursor.getString(columnIndex)
//                        videoFile= File(mediaPath1)
//                        str2.setText(mediaPath1)
//                        // Set the Video Thumb in ImageView Previewing the Media
//                        // Set the Video Thumb in ImageView Previewing the Media
//                        imgView.setImageBitmap(
//                            getThumbnailPathForLocalFile(
//                                this@MainActivity,
//                                selectedVideo
//                            )
//                        )
//                        cursor.close() /////////////////////////////////////

//                        saveVideoToInternalStorage(selectedVideoPath)
//                        addVideo.visibility = View.VISIBLE
//                        addVideoImage.visibility = View.GONE
//                        addVideo.setVideoPath(selectedVideoPath);
//                        addVideo.start();
                        }

                    }
                }

                CAMERAVIDEO -> {
                    if (data != null) {
                        val contentURI: Uri = data.data!!
                        path = getMediaPath(contentURI)
//                    addVideo.visibility = View.VISIBLE
//                    addVideoImage.visibility = View.GONE
//                    addVideo.setVideoPath(path);
//                    addVideo.start();
//
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

                REQUEST_TAKE_PHOTO -> {
                    val selectedImage = data!!.data
                    try {

                        mPhotoFile = File(getRealPathFromUri(selectedImage)!!)

                        photopath=mPhotoFile.toString()
                        compressImage(mPhotoFile!!)
                        //Glide.with(this).load(mPhotoFile).into(addCoverPhoto)

                    } catch (e: IOException) {
                        e.printStackTrace()

                    }


                    compressImage(mPhotoFile!!)

                    //Glide.with(this).load(mPhotoFile).into(addCoverPhoto)

                }
            }
        }
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
                compressedImage = Compressor.compress(this@CreateEventActivity, imageFile)

//                alertDialog?.show()
                // showDialog()
                Glide.with(this@CreateEventActivity).load(compressedImage).into(binding.uploadPhoto)
//                alertDialog?.dismiss()
                //dismissDialog()
                Log.e("komal", "new file ${compressedImage!!.length()}")

            }
        }

    }
    private fun compressVideo(path: String, videoFile: File?) {
        videoFile?.let {
            VideoCompressor.start(path, videoFile.path,
                object : CompressionListener {
                    override fun onProgress(percent: Float) {
                        //Update UI
                        if (percent <= 100 && percent.toInt() % 5 == 0)
                            this@CreateEventActivity.runOnUiThread {

//                                progressBar.progress = percent.toInt()
                            }
                    }

                    override fun onStart() {


//                        progressBar.visibility = View.VISIBLE
//                        progressBar.progress = 0
                    }

                    override fun onSuccess() {


                        Looper.myLooper()?.let {

                            Looper.myLooper()?.let {
                                Handler(it).postDelayed({
//                                    progressBar.visibility = View.GONE
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



    private fun setTimePicker(editText: EditText?) {
        val cal = Calendar.getInstance()
        val timeSetListener =
            TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
                cal.set(Calendar.HOUR_OF_DAY, hour)
                cal.set(Calendar.MINUTE, minute)
                editText?.setText(SimpleDateFormat("HH:mm").format(cal.time))
            }
        TimePickerDialog(
            this,
            timeSetListener,
            cal.get(Calendar.HOUR_OF_DAY),
            cal.get(Calendar.MINUTE),
            true
        ).show()
    }


    private fun setDatePicker(editText: EditText?) {
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

                editText?.setText("$year-$selectedMonth-$selectedDate")
            },
            year,
            month,
            day
        )
        dpd.show()
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

    fun addEvent(title:String,desc:String){

        val charset = "UTF-8"
        showDialog()
        try {
            var uploadFile1=File("")
            var uploadFile2=File("")
            thread {
                if (photopath.isNotEmpty()) {
                uploadFile1 = File(photopath)
            }else if (path.isNotEmpty()) {
                uploadFile1 = File(path)
            }
              //  val uploadFile2 = File(path)
                val requestURL = "https://bdztl.com/onov/api/v1/createEvent"
           /*     runOnUiThread {  }*/

                val multipart = MultipartUtility(requestURL, charset)
                multipart.addFormField("userRef", userPreferences.getUserREf())
                multipart.addFormField("title", title)
                multipart.addFormField("price", binding.etEventPrice.text.toString())
                multipart.addFormField("start_date", binding.edEventStartDate.text.toString())
                multipart.addFormField("start_time", binding.edEventStartTime.text.toString())
                multipart.addFormField("end_date", binding.edEventEndDate.text.toString())
                multipart.addFormField("end_time", binding.edEventEndTime.text.toString())
                multipart.addFormField("description", desc)
                multipart.addFilePart("cover_image", uploadFile1)
                multipart.addFilePart("ent_video", uploadFile1)
                val response: String = multipart.finish()

                println("SERVER REPLIED:")

                val gson = Gson() // Or use new GsonBuilder().create();

                data = gson.fromJson(response, EventModel::class.java)

                if (data!=null) {
                    setError(data?.msg.toString())
                    dismissDialog()
                    Handler(Looper.getMainLooper()).postDelayed({
                        //finish()
                      startActivity(Intent(this,EventsActivity::class.java))
                    }, 2000)
                }

            }


        } catch (ex: IOException) {
            System.err.println(ex)
        }
    }
}































//    /**
//     * This constructor initializes a new HTTP POST request with content type
//     * is set to multipart/form-data
//     *
//     * @param requestURL
//     * @param charset
//     * @param headers
//     * @throws IOException
//     */
//    @Throws(IOException::class)
//    fun HttpPostMultipart(requestURL: String?, charset: String, headers: Map<String?, String?>?) {
//
//     chrset = charset
//       val boundary = UUID.randomUUID().toString()
//        val url = URL(requestURL)
//        httpConn = url.openConnection() as HttpURLConnection
//        httpConn!!.setUseCaches(false)
//        httpConn!!.setDoOutput(true) // indicates POST method
//        httpConn!!.setDoInput(true)
//        httpConn!!.setRequestProperty("Content-Type", "multipart/form-data; boundary=$boundary")
//        if (headers != null && headers.size > 0) {
//            val it = headers.keys.iterator()
//            while (it.hasNext()) {
//                val key = it.next()
//                val value = headers[key]
//                httpConn!!.setRequestProperty(key, value)
//            }
//        }
//        outputStream = httpConn!!.getOutputStream()
//        writer = PrintWriter(OutputStreamWriter(outputStream, charset), true)
//    }
//
//    /**
//     * Adds a form field to the request
//     *
//     * @param name  field name
//     * @param value field value
//     */
//    fun addFormField(name: String, value: String?) {
//        writer!!.append("--$boundary").append(LINE)
//        writer!!.append("Content-Disposition: form-data; name=\"$name\"").append(LINE)
//        writer!!.append("Content-Type: text/plain; charset=$chrset").append(LINE)
//        writer!!.append(LINE)
//        writer!!.append(value).append(LINE)
//        writer!!.flush()
//    }
//
//    /**
//     * Adds a upload file section to the request
//     *
//     * @param fieldName
//     * @param uploadFile
//     * @throws IOException
//     */
//    @Throws(IOException::class)
//    fun addFilePart(fieldName: String, uploadFile: File) {
//        val fileName = uploadFile.name
//        writer!!.append("--$boundary").append(LINE)
//        writer!!.append("Content-Disposition: form-data; name=\"$fieldName\"; filename=\"$fileName\"")
//            .append(LINE)
//        writer!!.append("Content-Type: " + URLConnection.guessContentTypeFromName(fileName))
//            .append(LINE)
//        writer!!.append("Content-Transfer-Encoding: binary").append(LINE)
//        writer!!.append(LINE)
//        writer!!.flush()
//        val inputStream = FileInputStream(uploadFile)
//        val buffer = ByteArray(4096)
//        var bytesRead = -1
//        while (inputStream.read(buffer).also { bytesRead = it } != -1) {
//            outputStream!!.write(buffer, 0, bytesRead)
//        }
//        outputStream!!.flush()
//        inputStream.close()
//        writer!!.append(LINE)
//        writer!!.flush()
//    }
//
//    /**
//     * Completes the request and receives response from the server.
//     *
//     * @return String as response in case the server returned
//     * status OK, otherwise an exception is thrown.
//     * @throws IOException
//     */
//    @Throws(IOException::class)
//    override fun finish():String {
//        var response = ""
//        writer!!.flush()
//        writer!!.append("--" + boundary.toString() + "--").append(LINE)
//        writer!!.close()
//
//        // checks server's status code first
//        val status: Int = httpConn!!.getResponseCode()
//        if (status == HttpURLConnection.HTTP_OK) {
//            val result = ByteArrayOutputStream()
//            val buffer = ByteArray(1024)
//            var length: Int
//            while (httpConn!!.getInputStream().read(buffer).also({ length = it }) != -1) {
//                result.write(buffer, 0, length)
//            }
//            response = result.toString(this.chrset)
//            httpConn!!.disconnect()
//        } else {
//            throw IOException("Server returned non-OK status: $status")
//        }
//        return response
//    }