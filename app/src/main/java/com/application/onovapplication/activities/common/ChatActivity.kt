package com.application.onovapplication.activities.common

import android.Manifest
import android.app.AlertDialog
import android.app.Dialog
import android.content.ContentValues
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.net.Uri
import android.os.*
import android.provider.MediaStore
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.Window
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.abedelazizshe.lightcompressorlibrary.CompressionListener
import com.abedelazizshe.lightcompressorlibrary.VideoCompressor
import com.abedelazizshe.lightcompressorlibrary.VideoQuality
import com.application.onovapplication.R
import com.application.onovapplication.UrlConnection.MultipartUtility
import com.application.onovapplication.chat.ChatMessageAdapter
import com.application.onovapplication.chat.Messages
import com.application.onovapplication.databinding.ActivityChatBinding
import com.application.onovapplication.extensions.loadImage
import com.application.onovapplication.model.ChatImageResponse
import com.application.onovapplication.model.EventData
import com.application.onovapplication.model.FeedsData
import com.application.onovapplication.repository.service.API
import com.application.onovapplication.utils.Constants
import com.application.onovapplication.viewModels.ChatViewModel
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.database.*
import com.application.onovapplication.repository.ApiManager
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
import kotlin.concurrent.thread

class ChatActivity : BaseAppCompatActivity(), View.OnClickListener,
    ChatMessageAdapter.OnChatMessageClickListener {
    private lateinit var binding: ActivityChatBinding
    val chatViewModel by lazy { ViewModelProvider(this).get(ChatViewModel::class.java) }
    private var compressedImage: File? = null

    private lateinit var firebaseDataBase: FirebaseDatabase  //for entry point of database
    private lateinit var databaseReference: DatabaseReference //for particular position in database
    private val TAG: String = ChatActivity::class.java.simpleName
    var senderId = 0
    var receiverId = 0
    var receiverUserName = ""
    var receiverUserRef = ""
    var senderName = ""
    var receiverPhoto = ""
    var connectionid = ""
    var msgType = ""
    var feeds:FeedsData? = null
    var events:EventData? = null
    var data:ChatImageResponse? = null

    var serverImageUrl = ""
    var serverAudioUrl = ""
    private lateinit var apiService: API
    private var messagesArrayList = ArrayList<Messages>()

    //   private lateinit var viewModel: NavigationViewModel
    //private lateinit var apiService: ApiService
    var audioStatus = 0

    companion object {
        var mPhotoFile: File? = null
        val REQUEST_TAKE_PHOTO = 102
        val REQUEST_GALLERY_PHOTO = 201
        val REQUEST_AUDIO_PERMISSION_CODE = 1
        private var mAudioPath: String? = null
        private var mRecorder: MediaRecorder? = null
        private var mPlayer: MediaPlayer? = null
        var mAudioFile: File? = null


        fun getStartIntent(context: Context): Intent {
            return Intent(context, ChatActivity::class.java)
        }

        fun getStartIntent(
            context: Context, receiverId: String, userName: String, photoUrl: String,
            msgType: String, feedsData: FeedsData?,eventData: EventData?, receiverUserRef: String): Intent {
            val startIntent = Intent(context, ChatActivity::class.java)
            startIntent.putExtra(Constants.USER_ID, receiverId)
            startIntent.putExtra(Constants.USER_NAME, userName)
            startIntent.putExtra(Constants.PHOTO, photoUrl)
            startIntent.putExtra(Constants.FEED, feedsData)
            startIntent.putExtra(Constants.EVENT, eventData)
            startIntent.putExtra(Constants.FEEDTYPE, msgType)
            startIntent.putExtra(Constants.USER_REF, receiverUserRef)

            return startIntent
        }
    }

    // var databaseReference: DatabaseReference? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        apiService = ApiManager.retrofitBuilderForChat().create(API::class.java)
        observeViewModel()
 if (Build.VERSION.SDK_INT > 9) {
            val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
            StrictMode.setThreadPolicy(policy) }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            window.statusBarColor = ContextCompat.getColor(this, R.color.grey)
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

        }

        /*RetrofitBuilder.BASE_URL + "" +*/
        window.statusBarColor = ContextCompat.getColor(this, R.color.app_color)
        senderId = userPreferences.getuserDetails()?.id?.trim()?.toInt()!!
        senderName = userPreferences.getUserREf().trim()
        receiverId = intent.getStringExtra(Constants.USER_ID).toString().trim().toInt()

        receiverUserName = intent.getStringExtra(Constants.USER_NAME).toString().trim()
        receiverPhoto = "${intent.getStringExtra(Constants.PHOTO).toString().trim()}"
        receiverUserRef = intent.getStringExtra(Constants.USER_REF).toString().trim()
        msgType = intent.getStringExtra(Constants.FEEDTYPE).toString().trim()
        feeds = intent.getParcelableExtra(Constants.FEED) as FeedsData?
        events = intent.getParcelableExtra(Constants.EVENT) as EventData?
        Log.e(TAG,"feedds ${feeds?.Name}")
        binding.userName.text = receiverUserName
        binding.userPhoto.loadImage(receiverPhoto)

       if (senderId < receiverId)
        connectionid = "$senderId-$receiverId"
       else
           connectionid = "$receiverId-$senderId"


        val chatMessageAdapter: ChatMessageAdapter by lazy {
            ChatMessageAdapter(this, messagesArrayList, receiverPhoto, this)
        }


        binding.rvChat.adapter = chatMessageAdapter
        firebaseDataBase = FirebaseDatabase.getInstance()

        if (feeds!=null){ uploadFeedOnFirebase(feeds,null)}
        else    if (events!=null){ uploadFeedOnFirebase(null,events)}
        val chatReference: DatabaseReference =
            firebaseDataBase.reference.child("Chats").child(connectionid)

        chatReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                messagesArrayList.clear()

                for (dataSnapshot in snapshot.children) {
                    val messages: Messages? = dataSnapshot.getValue(Messages::class.java)
                    messages?.let { messagesArrayList.add(it) }

                }

                Log.e(TAG, "chatReference")
                chatMessageAdapter.notifyDataSetChanged()
                binding.rvChat.scrollToPosition(messagesArrayList.size - 1)
            }

            override fun onCancelled(error: DatabaseError) {}
        })


    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.send_msg -> {
                val message: String = binding.etMessageBox.text.toString()
                if (message.isEmpty()) {
                        Toast.makeText(this@ChatActivity, "Please enter Valid Message", Toast.LENGTH_SHORT).show()
                    }else msgSend()
            }
            R.id.attachment -> {
                checkExternalPermission()
            }  R.id.userPhoto -> {
            previewChatImage(receiverPhoto, receiverUserName)
            }
        }
    }

    fun msgSend() {

        val message: String = binding.etMessageBox.text.toString()

        Log.e(TAG, "msgSend")
        binding.etMessageBox.setText("")
        val date = Date()
       // const val DEFAULTTIME = "EEE MMM dd hh:mm:ss z yyyy"

        val chatDate = convertDateFormat(
            date.toString().trim(),
            Constants.DEFAULTTIME,
            "yyyy-MM-dd HH:mm:ss"
        ).trim()
        Log.e( "msgSendtime",chatDate)
        val messages = Messages(
            "",null,null ,message, chatDate, "text",
            receiverId.toString(), receiverUserName,
            senderId.toString(), senderName, getEpochTime().toString()
        )

     //   var data= Data(      "Message Received",)



        firebaseDataBase.reference.child("Chats")
            .child(connectionid)
            .push().setValue(messages).addOnCompleteListener(OnCompleteListener<Void?> {
            })


        //     sendChatNotificationFromServer(message)




/*   val data: Data,
    val notification: Notification,
    var to: String*/





/*
        apiService.sendNotification(driverRef, latitude, longitude).enqueue(
            object : Callback<MessageResponse> {
                override fun onResponse(
                    call: Call<MessageResponse>,
                    response: Response<MessageResponse>
                ) {
                    Log.e(TAG, "OnResponse Success ${response.message()}")
                    //     jobFinished(params,false)
                }

                override fun onFailure(call: Call<MessageResponse>, t: Throwable) {
                    Log.e(TAG, "OnResponse Failure ${t.message}")
                }

            })*/







        generateNotification()

    }


    private fun generateNotification()
    {

    }



    private fun previewChatImage(url: String, userName: String) {
        val ImagePreviewDialog = Dialog(this,
            android.R.style.ThemeOverlay_Material_Dialog)
        ImagePreviewDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        ImagePreviewDialog.setCancelable(false)
        ImagePreviewDialog.setContentView(R.layout.big_image_preview)
        val btnClose = ImagePreviewDialog.findViewById(R.id.btnIvClose) as ImageView
        val ivPreview = ImagePreviewDialog.findViewById(R.id.iv_preview_image) as ImageView
        val tvUsername = ImagePreviewDialog.findViewById(R.id.tv_username) as TextView
        tvUsername.text = userName
        ivPreview.loadImage(url)
        btnClose.setOnClickListener {
            ImagePreviewDialog.dismiss()
        }

        ImagePreviewDialog.setOnKeyListener(DialogInterface.OnKeyListener { dialog, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                dialog?.cancel()
                return@OnKeyListener true
            }
            false
        })

        ImagePreviewDialog.show()
        ImagePreviewDialog.setCanceledOnTouchOutside(true)

    }

    private fun uploadImageOnFirebase(serverImageUrl: String) {

        val date = Date()
        var chatDate = convertDateFormat(
            date.toString().trim(),
            Constants.DEFAULTTIME,
            "yyyy-MM-dd HH:mm:ss"
        ).trim()
        val messages = Messages(
            serverImageUrl,
            null,null,"",
            chatDate,
            "image",
            receiverId.toString(),
            receiverUserName,
            senderId.toString(),
            senderName,
            getEpochTime().toString()
        )


        firebaseDataBase.reference.child("Chats")
            .child(connectionid)
            .push().setValue(messages).addOnCompleteListener(OnCompleteListener<Void?> {
            })

        //   sendChatNotificationFromServer("Image")
    }
    private fun uploadFeedOnFirebase(feeddata: FeedsData?,eventdata: EventData?) {

        val date = Date()
        var chatDate = convertDateFormat(
            date.toString().trim(),
            Constants.DEFAULTTIME,
            "yyyy-MM-dd HH:mm:ss"
        ).trim()
        var  messages = Messages()
        if (feeddata==null){
            messages = Messages(
                serverImageUrl,
                feeddata,eventdata,"",
                chatDate,
                "event",
                receiverId.toString(),
                receiverUserName,
                senderId.toString(),
                senderName,
                getEpochTime().toString() )
        }else  if (eventdata==null){
            messages = Messages(
                serverImageUrl,
                feeddata,eventdata,"",
                chatDate,
                "feed",
                receiverId.toString(),
                receiverUserName,
                senderId.toString(),
                senderName,
                getEpochTime().toString() )
        }




        firebaseDataBase.reference.child("Chats")
            .child(connectionid)
            .push().setValue(messages).addOnCompleteListener(OnCompleteListener<Void?> {
            })

        //   sendChatNotificationFromServer("Image")
    }


    private fun checkExternalPermission() {
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

    private fun showPictureDialog() {

        val pictureDialog: AlertDialog.Builder = AlertDialog.Builder(this)
        val title = pictureDialog.setTitle(getString(R.string.select_action))
        val pictureDialogItems = arrayOf(
            getString(R.string.select_image_from_gallery),
//            getString(R.string.select_image_from_camera)
        )
        pictureDialog.setItems(
            pictureDialogItems
        ) { _, which ->
            when (which) {
                0 -> dispatchGalleryIntent()
//                1 -> dispatchTakePictureIntent()
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

//          FileProvider.getUriForFile(Objects.requireNonNull(getApplicationContext()),
//                    BuildConfig.APPLICATION_ID + ".provider", file);


                val photoURI = FileProvider.getUriForFile(
                    Objects.requireNonNull(applicationContext),
                    "com.application.onovapplication.provider",
//                    "${BuildConfig.APPLICATION_ID}.provider",
                    photoFile
                )
                mPhotoFile = photoFile
//                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                startActivityForResult(
                    takePictureIntent,
                    REQUEST_TAKE_PHOTO
                )
            }
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode== RESULT_OK) {
            Log.e("dataimage",""+data?.data)
            val selectedImage = data?.data
            try {
                mPhotoFile = File(getRealPathFromUri(Uri.parse(mPhotoFile.toString()))!!)
                chatViewModel.uploadChatImage(this, mPhotoFile)
                //    uploadPhotoOnServer()

            } catch (e: IOException) {
                e.printStackTrace()
            }
            // uploadPhotoOnServer()
        }
        else if (requestCode == REQUEST_GALLERY_PHOTO) {
            val selectedImage = data!!.data
            try {
                mPhotoFile = File(getRealPathFromUri(selectedImage)!!)
                chatViewModel.uploadChatImage(this, mPhotoFile)
            //    uploadPhotoOnServer()

            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
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


    private fun createImageFile(): File? {
        // Create an image file name
        val timeStamp = SimpleDateFormat("yyyyMMddHHmmss").format(Date())
        val mFileName = "JPEG_" + timeStamp + "_"
        val storageDir =
            getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(mFileName, ".jpg", storageDir)
    }

    private fun observeViewModel() {

        chatViewModel.successfulChatImg.observe(this, androidx.lifecycle.Observer {
            dismissDialog()
            if (it != null) {
                if (it) {
                    if (chatViewModel.status == "success") {
                      uploadImageOnFirebase(chatViewModel.chatImgdata.chatImgUrl)

                    } else {
                        setError(chatViewModel.message)
                        finish()
                    }
                }
            } else {
                setError(chatViewModel.message)
            }

        })


    }
    override fun onImageViewClick(position: Int, status: Int) {
//        var username = messagesArrayList.get(position).receiver_name.trim()
//        if (status == 0)
//            username = "You"
//        //preview image open

    }
    private fun compressImage(photoFile: File) {

        Log.e("komal", "old file ${photoFile.length()}")

        photoFile.let { imageFile ->
            this.lifecycleScope.launch {
                // Default compression
                compressedImage = Compressor.compress(this@ChatActivity, imageFile)

//                alertDialog?.show()
                // showDialog()
//                Glide.with(this@ChatActivity).load(compressedImage).into(binding.uploadPhoto)
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
                            this@ChatActivity.runOnUiThread {

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

    fun addEvent(path:String){

        val charset = "UTF-8"
        showDialog()
        try {
            var uploadFile1=File("")
            var uploadFile2=File("")
            thread {
//                if (photopath.isNotEmpty()) {
//                    uploadFile1 = File(photopath)
//                }else if (path.isNotEmpty()) {
                    uploadFile1 = File(path)
//                }
                //  val uploadFile2 = File(path)
                val requestURL = "https://bdztl.com/onov/api/v1/uploadChatImage"
                /*     runOnUiThread {  }*/

                val multipart = MultipartUtility(requestURL, charset)
//                multipart.addFormField("userRef", userPreferences.getUserREf())
//                multipart.addFormField("title", title)
//                multipart.addFormField("price", binding.etEventPrice.text.toString())
//                multipart.addFormField("start_date", binding.edEventStartDate.text.toString())
//                multipart.addFormField("start_time", binding.edEventStartTime.text.toString())
//                multipart.addFormField("end_date", binding.edEventEndDate.text.toString())
//                multipart.addFormField("end_time", binding.edEventEndTime.text.toString())
//                multipart.addFormField("description", desc)
                multipart.addFilePart("chatImg", uploadFile1)
//                multipart.addFilePart("ent_video", uploadFile1)
                val response: String = multipart.finish()

                println("SERVER REPLIED:")

                val gson = Gson() // Or use new GsonBuilder().create();

                data = gson.fromJson(response, ChatImageResponse::class.java)

                if (data!=null) {
                    setError(data?.msg.toString())
                    dismissDialog()
                    Handler(Looper.getMainLooper()).postDelayed({
                        //finish()
//                        startActivity(Intent(this, EventsActivity::class.java))
                    }, 2000)
                }

            }


        } catch (ex: IOException) {
            System.err.println(ex)
        }
    }

}