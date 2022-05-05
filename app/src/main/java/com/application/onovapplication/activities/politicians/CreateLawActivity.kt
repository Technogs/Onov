package com.application.onovapplication.activities.politicians

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.webkit.MimeTypeMap
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProvider
import com.application.onovapplication.BuildConfig
import com.application.onovapplication.R
import com.application.onovapplication.activities.common.BaseAppCompatActivity
import com.application.onovapplication.viewModels.StatsViewModel

import java.util.*
import android.provider.DocumentsContract
import android.content.ContentUris
import android.annotation.TargetApi
import android.os.*
import android.widget.AdapterView
import com.application.onovapplication.UrlConnection.MultipartUtility
import com.application.onovapplication.activities.common.ForgotPasswordOtpActivity
import com.application.onovapplication.activities.common.HomeTabActivity
import com.application.onovapplication.databinding.ActionBarLayout2Binding
import com.application.onovapplication.databinding.ActivityCreateAnnouncementBinding
import com.application.onovapplication.databinding.ActivityCreateLawBinding
import com.application.onovapplication.model.FeedsData
import com.application.onovapplication.model.RegisterResponse
import com.application.onovapplication.repository.BaseUrl
import com.application.onovapplication.utils.CustomSpinnerAdapter
import com.application.onovapplication.utils.FileUtils
import java.io.*
import com.application.onovapplication.utils.GetFilePath.isMediaDocument
import com.application.onovapplication.utils.GetFilePath.isDownloadsDocument
import com.application.onovapplication.utils.GetFilePath.isExternalStorageDocument
import com.google.gson.Gson
import kotlin.concurrent.thread

class CreateLawActivity : BaseAppCompatActivity(), View.OnClickListener {
    private val BUFFER_SIZE = 1024 * 2
    private val IMAGE_DIRECTORY = "/demonuts_upload_gallery"
    private val PICK_PDF_REQUEST = 1
    var feeds: FeedsData? = null
    var radius = ""
    private val STORAGE_PERMISSION_CODE = 123
    private val statsViewModel by lazy { ViewModelProvider(this).get(StatsViewModel::class.java) }
    private var myUri: Uri? = null
    private var file: File? = null
    var data: RegisterResponse? = null
    private val spinnerList =
        arrayOf("Select Radius", "Local", "State", "National")
    private var myFile: String? = null
    private lateinit var binding: ActivityCreateLawBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateLawBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        val incBinding: ActionBarLayout2Binding =binding.ab

        incBinding.tvScreenTitle.text = getString(R.string.new_law)
        if (intent.getParcelableExtra<FeedsData>("feed")!=null) {
            feeds = intent.getParcelableExtra("feed")
            if (feeds != null) binding.postLaw.setText("Edit")
            binding.etLawTitle.setText(feeds?.title)
            binding.lawDesc.setText(feeds?.description)
         //   file=File(BaseUrl.photoUrl+feeds?.filePath)
        }
        setSpinner()
        observeViewModel()
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
                // selectedRole = parent?.getItemAtPosition(position).toString()
                radius = spinnerList[position]
            }
        }
    }
    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.btnUploadPdf -> {
                if (ContextCompat.checkSelfPermission(
                        this,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf(
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE
                        ),
                        STORAGE_PERMISSION_CODE
                    )
                } else {
                    showFileChooser()
                }
            }
            R.id.ivCancel -> {
                binding.tvView.visibility = View.GONE
                binding.ivCancel.visibility = View.GONE
            }
            R.id.tvView -> {
                openDocument(myFile)
            }
            R.id.postLaw -> {
                if (binding.etLawTitle.text.toString()=="")
                setError(resources.getString(R.string.title_error))
                else if (binding.lawDesc.text.toString()=="")
                setError(resources.getString(R.string.description_error))
                else if (file==null)
                setError(resources.getString(R.string.document_error))
                else {
                    showDialog()
                    if (binding.postLaw.text=="Post") {
//                        addLaw()

                        statsViewModel.addLaws(
                            this,
                            binding.etLawTitle.text.toString(),
                            userPreferences.getuserDetails()?.userRef.toString(),
                            binding.lawDesc.text.toString(),radius,
                            "document", file
                        )
                    }else  if (binding.postLaw.text=="Edit") {
                        statsViewModel.editLaws(
                            this,feeds?.id.toString(),feeds?.recordType.toString(),
                            binding.etLawTitle.text.toString(),
                            binding.lawDesc.text.toString(),
                            "document","",radius, file
                        )}
                }
            }
        }
    }

    private fun showFileChooser() {
        val intent = Intent()
        //  intent.type = "application/*"
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "*/*"
        startActivityForResult(Intent.createChooser(intent, "Select File"), PICK_PDF_REQUEST)
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_PDF_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            myUri = data.data
            myFile =
                "${Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)}/${
                    getFileName(
                        this@CreateLawActivity,
                        myUri
                    )
                }"
            Log.e("CreateLaw", "uriii" + myUri)
            binding.tvView.visibility = View.VISIBLE
            binding.ivCancel.visibility = View.VISIBLE
            binding.tvView.text = getFileName(this, myUri)
            //changed here to path from to String
//if (data!=null)
            //  Log.e("CreateLaw", "getPath=="+getPath(myUri!!))
            // Log.e("CreateLaw", "getPathNew=="+FileUtils.getRealPath(this, myUri!!))

            //file = File(getPath(myUri!!))

            val isOreo = Build.VERSION.SDK_INT >= Build.VERSION_CODES.O
            if (isOreo)

                file = File(FileUtils.getRealPath(this, myUri!!))
            else

                file = File(getPath(myUri!!))


            //val file : File = File(myFile)
        }
    }

    private fun getPath(uri: Uri): String? {
        val isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT
        if (isKitKat) {
            // MediaStore (and general)
            return getForApi19(uri)
        } else if ("content".equals(uri.getScheme(), ignoreCase = true)) {
            // Return the remote address
            return if (isGooglePhotosUri(uri)) uri.getLastPathSegment() else getDataColumn(
                uri,
                null,
                null
            )
        } else if ("file".equals(uri.getScheme(), ignoreCase = true)) {
            return uri.getPath()
        }
        return null
    }

    @TargetApi(28)
    private fun getForApi19(uri: Uri): String? {
        Log.e("tag", "+++ API 19 URI :: $uri")
        if (DocumentsContract.isDocumentUri(this, uri)) {
            Log.e("tag", "+++ Document URI")
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                Log.e("tag", "+++ External Document URI")
                val docId = DocumentsContract.getDocumentId(uri)
                val split = docId.split(":").toTypedArray()
                val type = split[0]
                if ("primary".equals(type, ignoreCase = true)) {
                    Log.e("tag", "+++ Primary External Document URI")
                    return Environment.getExternalStorageDirectory().toString() + "/" + split[1]
                }
                // TODO handle non-primary volumes
            } else if (isDownloadsDocument(uri)) {
                Log.e("tag", "+++ Downloads External Document URI")
                val id = DocumentsContract.getDocumentId(uri)
                val contentUri: Uri = ContentUris.withAppendedId(
                    Uri.parse("content://downloads/public_downloads"), java.lang.Long.valueOf(id)
                )
                return getDataColumn(contentUri, null, null)
            } else if (isMediaDocument(uri)) {
                Log.e("tag", "+++ Media Document URI")
                val docId = DocumentsContract.getDocumentId(uri)
                val split = docId.split(":").toTypedArray()
                val type = split[0]
                var contentUri: Uri? = null
                if ("image" == type) {
                    Log.e("tag", "+++ Image Media Document URI")
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                } else if ("video" == type) {
                    Log.e("tag", "+++ Video Media Document URI")
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                } else if ("audio" == type) {
                    Log.e("tag", "+++ Audio Media Document URI")
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                }
                val selection = "_id=?"
                val selectionArgs = arrayOf(
                    split[1]
                )
                return getDataColumn(contentUri, selection, selectionArgs)
            }
        } else if ("content".equals(uri.getScheme(), ignoreCase = true)) {
            Log.e("tag", "+++ No DOCUMENT URI :: CONTENT ")
            // Return the remote address
            return if (isGooglePhotosUri(uri)) {
                uri.getLastPathSegment()
            } else getDataColumn(
                uri,
                null,
                null
            )
        } else if ("file".equals(uri.getScheme(), ignoreCase = true)) {
            Log.e("tag", "+++ No DOCUMENT URI :: FILE ")
            return uri.getPath()
        }
        return null
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param uri The Uri to query.
     * @param selection (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    fun getDataColumn(
        uri: Uri?, selection: String?,
        selectionArgs: Array<String>?
    ): String? {
        var cursor: Cursor? = null
        val column = "_data"
        val projection = arrayOf(
            column
        )
        try {
            cursor = contentResolver.query(
                uri!!, projection, selection, selectionArgs,
                null
            )
            if (cursor != null && cursor.moveToFirst()) {
                val index: Int = cursor.getColumnIndexOrThrow(column)
                return cursor.getString(index)
            }
        } finally {
            if (cursor != null) cursor.close()
        }
        return null
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    fun isExternalStorageDocument(uri: Uri): Boolean {
        return "com.android.externalstorage.documents" == uri.getAuthority()
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    fun isDownloadsDocument(uri: Uri): Boolean {
        return "com.android.providers.downloads.documents" == uri.getAuthority()
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    fun isMediaDocument(uri: Uri): Boolean {
        return "com.android.providers.media.documents" == uri.getAuthority()
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    fun isGooglePhotosUri(uri: Uri): Boolean {
        return "com.google.android.apps.photos.content" == uri.getAuthority()
    }

    //    //method to show file chooser
//    private fun showFileChooser() {
//        val intent = Intent()
//        intent.type = "application/pdf"
//        intent.action = Intent.ACTION_GET_CONTENT
//        startActivityForResult(Intent.createChooser(intent, "Select Pdf"), PICK_PDF_REQUEST)
//    }
//
//    //handling the image chooser activity result
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (requestCode == PICK_PDF_REQUEST && resultCode == RESULT_OK && data != null && data.data != null) {
//            filePath = data.data
//        }
//    }
    fun getFileName(context: Context, uri: Uri?): String? {
        var cursor: Cursor? = null
        val projection = arrayOf(
            MediaStore.MediaColumns.DISPLAY_NAME
        )
        try {
            cursor = context.contentResolver.query(
                uri!!, projection, null, null,
                null
            )
            if (cursor != null && cursor.moveToFirst()) {
                val index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DISPLAY_NAME)
                return cursor.getString(index)
            }
        } finally {
            cursor?.close()
        }
        return null
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        //Checking the request code of our request
        if (requestCode == STORAGE_PERMISSION_CODE) {
            //If permission is granted
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                showFileChooser()
            }
        }
    }

    private fun openDocument(name: String?) {
        val intent = Intent(Intent.ACTION_VIEW)
        val file = File(name)
        val extension = MimeTypeMap.getFileExtensionFromUrl(
            FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".provider", file)
                .toString()
        )
        val mimetype =
            MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)
        if (extension.equals("", ignoreCase = true) || mimetype == null) {
            // if there is no extension or there is no definite mimetype, still try to open the file
            intent.setDataAndType(
                FileProvider.getUriForFile(
                    this,
                    BuildConfig.APPLICATION_ID + ".provider",
                    file
                ), "text/*"
            )
        } else {
            intent.setDataAndType(
                FileProvider.getUriForFile(
                    this,
                    BuildConfig.APPLICATION_ID + ".provider",
                    file
                ), mimetype
            )
        }
        // custom message for the intent
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        startActivity(Intent.createChooser(intent, "Choose an Application:"))
    }

    private fun observeViewModel() {
        statsViewModel.successfulAddLaw.observe(this, androidx.lifecycle.Observer {
            dismissDialog()
            if (it != null) {
                if (it) {
                    if (statsViewModel.status == "success") {
                        setError(statsViewModel.message)
                       startActivity(Intent(this,HomeTabActivity::class.java))
                    } else {
                        setError(statsViewModel.message)
                        finish()
                    }
                }
            } else {
                setError(statsViewModel.message)
            }
        })
        statsViewModel.successfulEditLaw.observe(this, androidx.lifecycle.Observer {
            dismissDialog()
            if (it != null) {
                if (it) {
                    if (statsViewModel.status == "success") {
                        setError(statsViewModel.message)
                        finish()
                    } else {
                        setError(statsViewModel.message)
                        finish()
                    }
                }
            } else {
                setError(statsViewModel.message)
            }
        })
    }


    fun addLaw(){

        val charset = "UTF-8"
//        showDialog()
        try {
            var uploadFile1=File(myFile.toString())
            var uploadFile2=File("")
            thread {
                val requestURL = "https://bdztl.com/onov/api/v1/addLaws"
                /*     runOnUiThread {  }*/

                val multipart = MultipartUtility(requestURL, charset)
                multipart.addFormField("userRef", userPreferences.getuserDetails()?.userRef.toString())
                multipart.addFormField("lawTitle",  binding.etLawTitle.text.toString())
                multipart.addFormField("description",   binding.lawDesc.text.toString())
                multipart.addFormField("fileType",  "document")
                multipart.addFormField("areaLimit", radius)

                multipart.addFilePart("documentFile", uploadFile1)
//                multipart.addFilePart("coverPhoto", compressedImage)
                val response: String = multipart.finish()

                println("SERVER REPLIED:")

                val gson = Gson() // Or use new GsonBuilder().create();

                data = gson.fromJson(response, RegisterResponse::class.java)

                if (data!=null) {
                    setError(data?.msg.toString())
                    dismissDialog()
                    Handler(Looper.getMainLooper()).postDelayed({
                        //finish()
                        val intent = Intent(this, HomeTabActivity::class.java)
                        startActivity(intent)
                        finish()
                        //finishAffinity()
                    }, 2000)
                }

            }


        } catch (ex: IOException) {
            System.err.println(ex)
        }
    }
}
