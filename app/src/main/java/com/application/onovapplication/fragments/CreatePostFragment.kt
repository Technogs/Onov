package com.application.onovapplication.fragments



import android.Manifest
import android.app.AlertDialog
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.*
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.abedelazizshe.lightcompressorlibrary.CompressionListener
import com.abedelazizshe.lightcompressorlibrary.VideoCompressor
import com.abedelazizshe.lightcompressorlibrary.VideoQuality
import com.application.onovapplication.BuildConfig
import com.application.onovapplication.R
import com.application.onovapplication.UrlConnection.MultipartUtility
import com.application.onovapplication.activities.common.BaseFragment
import com.application.onovapplication.adapters.PredefinedTextsAdapter
import com.application.onovapplication.databinding.FragmentCreatePostBinding
import com.application.onovapplication.extensions.loadImage
import com.application.onovapplication.model.EventModel
import com.application.onovapplication.model.FeedsData
import com.application.onovapplication.model.PredefinedTextsModel
import com.application.onovapplication.repository.BaseUrl
import com.application.onovapplication.utils.CustomSpinnerAdapter
import com.application.onovapplication.viewModels.PostViewModel
import com.application.onovapplication.viewModels.StatsViewModel
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


class CreatePostFragment : BaseFragment(), PredefinedTextsAdapter.PredefinedTextClickInterface,
    View.OnClickListener {
    private val spinnerList = arrayOf("Select Radius", "Local", "State", "National")

    private var videoFile: File? = null
    private var path: String = ""
    private var photopath: String = ""
    private lateinit var data: EventModel
    private lateinit var data1: EventModel
    private var compressedImage: File? = null
    var mPhotoFile: File? = null
    var mVideoFile: File? = null
    val LAUNCH_INFO_ACTIVITY = 11
    var feedData: FeedsData? = null
    var radius = ""
    private val GALLERYVIDEO = 1
    private var CAMERAVIDEO: Int = 2
    private val REQUEST_TAKE_PHOTO = 101
    private val TAKE_PHOTO = 4
    private val REQUEST_GALLERY_PHOTO = 201
    private var adapter: PredefinedTextsAdapter? = null
    var type = ""
    lateinit var binding: FragmentCreatePostBinding
    val postViewModel by lazy { ViewModelProvider(this).get(PostViewModel::class.java) }
    private val statsViewModel by lazy { ViewModelProvider(this).get(StatsViewModel::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        type = arguments?.getString("type").toString()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentCreatePostBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setSpinner()
        val predefinedTextsList: ArrayList<PredefinedTextsModel> = ArrayList()
        observeViewModel()
        if (arguments?.getParcelable<FeedsData>("feedData") != null) {
            feedData = arguments?.getParcelable<FeedsData>("feedData")!!
//            if (feedData != null) {
                binding.createPost.text = "Edit"

//                if (feedData?.fileType == "photo") binding.uploadPhoto.loadImage(BaseUrl.photoUrl + feedData?.filePath)
//            }
            binding.etPostTitle.setText(feedData?.title)
            binding.etPostDscptn.setText(feedData?.description)
            if (feedData?.fileType == "photo") {
                Glide.with(requireActivity()).load(BaseUrl.photoUrl +feedData?.filePath).into(binding.uploadPhoto)

            }
            else if (feedData?.fileType=="video"){
                binding.uploadVideo.visibility=View.GONE
               binding.viewVideo.visibility=View.VISIBLE
//                path=feedData?.filePath.toString()
                binding.viewVideo.setVideoURI(Uri.parse(feedData?.filePath))
//                binding.viewVideo.start()
            }
            for (i in spinnerList.indices) {
                if (spinnerList[i] == feedData?.areaLimit) {
                    binding.spPetition.setSelection(i)
                }
            }
        }
        predefinedTextsList.add(PredefinedTextsModel("BREAKING NEWS!"))
        predefinedTextsList.add(PredefinedTextsModel("What's happening locally now!"))
        predefinedTextsList.add(PredefinedTextsModel("Call to action!"))
        if (type == "photo") {
            binding.addPhoto.visibility = View.VISIBLE
            binding.addVideo.visibility = View.GONE
        } else if (type == "video") {
            binding.addPhoto.visibility = View.GONE
            binding.addVideo.visibility = View.VISIBLE
        }
        adapter = PredefinedTextsAdapter(requireContext(), predefinedTextsList, this)

        binding.rvPredefinedTexts.adapter = adapter

        binding.addPhoto.setOnClickListener {
            if (
                ContextCompat.checkSelfPermission(
                    requireActivity(),
                    Manifest.permission.CAMERA
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    requireActivity(),
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

        binding.addVideo.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    requireActivity(),
                    Manifest.permission.CAMERA
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    requireActivity(),
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

        binding.createPost.setOnClickListener {
            if (binding.etPostTitle.text.toString() == "")
                setError("Enter a title")
            else if (binding.etPostDscptn.text.toString() == "")
                setError("Enter description")
            else if (type == "photo" && photopath == "" && binding.createPost.text == "Post")
                setError("add a picture to upload")
            else if (type == "video" && path == "" && binding.createPost.text == "Post")
                setError("add a video to upload")
            else if (radius == "Select Radius")
                setError("Please select a radius")
            else {

                if (binding.createPost.text == "Post") {
                    showDialog()
                    if (path != "") {

                        if (videoFile != null) {
                            postViewModel.createPost(
                                requireActivity(),
                                userPreferences.getuserDetails()?.userRef.toString(),
                                binding.etPostTitle.text.toString(),
                                binding.etPostDscptn.text.toString(),
                                "video",
                                radius,
                                videoFile
                            )
                        }
                    } else if (photopath != "") {
                        postViewModel.createPost(
                            requireActivity(),
                            userPreferences.getuserDetails()?.userRef.toString(),
                            binding.etPostTitle.text.toString(),
                            binding.etPostDscptn.text.toString(),
                            "photo",
                            radius,
                            compressedImage
                        )

                    }
                } else if (binding.createPost.text == "Edit") {
                    if (feedData?.fileType == "video") {
//                        if (videoFile != null) {
                            showDialog()
                            statsViewModel.editLaws(
                                requireActivity(),
                                feedData?.id.toString(),
                                feedData?.recordType.toString(),
                                binding.etPostTitle.text.toString(),
                                binding.etPostDscptn.text.toString(),
                                feedData?.fileType.toString(),
                                "",
                                radius,
                                videoFile
                            )
//                        }
                    } else if (feedData?.fileType == "photo")  {
                        showDialog()
                        statsViewModel.editLaws(
                            requireActivity(),
                            feedData?.id.toString(),
                            feedData?.recordType.toString(),
                            binding.etPostTitle.text.toString(),
                            binding.etPostDscptn.text.toString(),
                            feedData?.fileType.toString(),
                            "",
                            radius,
                            compressedImage
                        )
                    }
                }
            }
        }
    }

    private fun observeViewModel() {

        postViewModel.successful.observe(requireActivity(), androidx.lifecycle.Observer {

            if (it != null) {
                if (it) {
                    if (postViewModel.status == "success") {
                        dismissDialog()
                        setError(postViewModel.message)
                        val fragment = FeedFragment()
                        val transaction: FragmentTransaction =
                            requireActivity().supportFragmentManager.beginTransaction()
                        transaction.replace(
                            R.id.homeTabContainer,
                            fragment
                        ) // give your fragment container id in first parameter
                        transaction.addToBackStack(null).commit()

                    } else {
                        setError(postViewModel.message)

                    }
                }
            } else {
                setError(postViewModel.message)
            }

        })
        statsViewModel.successfulEditLaw.observe(requireActivity(), androidx.lifecycle.Observer {

            if (it != null) {
                if (it) {
                    if (statsViewModel.status == "success") {
                        dismissDialog()
                        setError(statsViewModel.message)
                        val fragment = FeedFragment()
                        val transaction: FragmentTransaction =
                            requireActivity().supportFragmentManager.beginTransaction()
                        transaction.replace(
                            R.id.homeTabContainer,
                            fragment
                        ) // give your fragment container id in first parameter
                        transaction.addToBackStack(null).commit()

                    } else {
                        setError(statsViewModel.message)

                    }
                }
            } else {
                setError(statsViewModel.message)
            }

        })
    }

    private fun setSpinner() {
        val spinnerAdapter = CustomSpinnerAdapter(
            requireActivity(),  // Use our custom adapter
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
                // selectedRole = parent?.getItemAtPosition(position).toString()
                radius = spinnerList[position]
            }
        }
    }

    override fun onClick(predefinedTextsModel: PredefinedTextsModel) {
        binding.etPostTitle.setText("")
        binding.etPostTitle.setText(predefinedTextsModel.text)

    }

    override fun onClick(v: View?) {
        when (v!!.id) {

        }
    }

    private fun showVideoDialog() {
        val pictureDialog: AlertDialog.Builder = AlertDialog.Builder(requireActivity())
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
                requireActivity(),
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(), arrayOf(
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
                requireActivity(),
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
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
        val pictureDialog: AlertDialog.Builder =
            AlertDialog.Builder(requireActivity(), R.style.TimePickerTheme)
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
        if (takePictureIntent.resolveActivity(requireActivity().packageManager) != null) {
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
                    requireActivity(), "${BuildConfig.APPLICATION_ID}.provider",
                    photoFile
                )
                mPhotoFile = photoFile
                Log.d(
                    "checkcamerapic",
                    "" + "photoFile is:" + photoFile + " photoURI" + photoURI + " mPhotoFile" + mPhotoFile
                )
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                startActivityForResult(
                    takePictureIntent, TAKE_PHOTO
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
            requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(mFileName, ".jpg", storageDir)
    }


    private fun getRealPathFromUri(contentUri: Uri?): String? {
        var cursor: Cursor? = null
        return try {
            val proj =
                arrayOf(MediaStore.Images.Media.DATA)
            cursor = requireActivity().contentResolver.query(contentUri!!, proj, null, null, null)
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
                    if (data != null) {
                        binding.uploadVideo.visibility = View.GONE
                        binding.viewVideo.visibility = View.VISIBLE

                        val contentURI: Uri = data.data!!
                        //val uri = Uri.parse("android.resource://" + getPackageName().toString() + "/" + R.raw.test)
                        binding.viewVideo.setVideoURI(contentURI)
                        binding.viewVideo.start()
                        path = getMediaPath(contentURI)

//                        Log.d("path...=  ",path)
//                        val mp: MediaPlayer = MediaPlayer.create(requireActivity(), Uri.fromFile(File(path)))
//                        val duration: Int = mp.duration
//                        mp.release()
//                        if (duration / 10000 > 10) {
//
//                            showPopUp()
//                        } else {
                        videoFile = saveVideoFile(path)
                        compressVideo(path, videoFile)

                    }
                }

                CAMERAVIDEO -> {
                    if (data != null) {
                        val contentURI: Uri = data.data!!
                        path = getMediaPath(contentURI)
                        Log.d("path...=  ", path)
                        binding.uploadVideo.visibility = View.GONE
                        binding.viewVideo.visibility = View.VISIBLE

                        binding.viewVideo.setVideoURI(contentURI)
                        binding.viewVideo.start()
                        videoFile = saveVideoFile(path)
                        compressVideo(path, videoFile)
                        //  path= videoFile.toString()
//                        compressVideo(path, videoFile)
                        Log.d("videoFilepath...=  ", videoFile.toString())
                    }
                }
                REQUEST_GALLERY_PHOTO -> {
                    val selectedImage = data.data
                    try {
                        mPhotoFile = File(getRealPathFromUri(selectedImage)!!)
                        photopath = mPhotoFile.toString()
                        compressImage(mPhotoFile!!)
                      Glide.with(this).load(mPhotoFile).into(binding.uploadPhoto)

                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }


            }
        }
        if (requestCode == TAKE_PHOTO && resultCode == AppCompatActivity.RESULT_OK) {
            binding.uploadPhoto.setImageURI(Uri.parse(mPhotoFile.toString()))


            try {

//                mPhotoFile = File(getRealPathFromUri(Uri.parse(mPhotoFile.toString()))!!)

                photopath = mPhotoFile.toString()
                compressImage(mPhotoFile!!)

            } catch (e: IOException) {
                e.printStackTrace()
            }
            compressImage(mPhotoFile!!)

            Glide.with(this).load(mPhotoFile).into(binding.uploadPhoto)
        }
    }

    private fun showPopUp() {
        val builder =
            androidx.appcompat.app.AlertDialog.Builder(requireActivity())
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
                compressedImage = Compressor.compress(requireActivity(), imageFile)
                Glide.with(requireActivity()).load(compressedImage).into(binding.uploadPhoto)

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
                            activity!!.runOnUiThread {

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

                val fileUri =
                    requireActivity().applicationContext.contentResolver.insert(collection, values)

                fileUri?.let {
                    requireActivity().application.contentResolver.openFileDescriptor(fileUri, "rw")
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
                    requireActivity().applicationContext.contentResolver.update(
                        fileUri,
                        values,
                        null,
                        null
                    )

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

        val resolver = requireActivity().contentResolver
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
                val filePath = (requireActivity().applicationInfo.dataDir + File.separator
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