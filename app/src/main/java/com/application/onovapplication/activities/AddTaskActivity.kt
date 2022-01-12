package com.application.onovapplication.activities//package com.application.onovapplication.activities
//
//import androidx.appcompat.app.AppCompatActivity
//import android.os.Bundle
//import android.provider.MediaStore
//
//import android.provider.DocumentsContract
//
//import android.content.ContentUris
//
//import android.os.Environment
//
//import android.annotation.TargetApi
//
//import android.os.Build
//
//import android.app.DatePickerDialog
//
//import android.widget.DatePicker
//
//import android.R
//
//import android.content.Intent
//
//import android.widget.Toast
//
//import okhttp3.MultipartBody
//
//import okhttp3.RequestBody
//
//import android.app.Activity
//import android.app.AlertDialog
//import android.app.DatePickerDialog.OnDateSetListener
//
//import android.widget.AdapterView
//
//import android.content.SharedPreferences
//import android.view.View
//import android.widget.AdapterView.OnItemSelectedListener
//
//import android.widget.TextView
//
//import android.widget.LinearLayout
//
//import android.widget.CheckBox
//
//import android.widget.Spinner
//
//import android.widget.EditText
//
//
//
//
//class AddTaskActivity : AppCompatActivity() {
//    var alertDialog: AlertDialog? = null
//    var sharedPreferences: SharedPreferences? = null
//    var teamAdapter: TeamAdapter? = null
//    private var apiInterface: ApiInterface? = null
//
//    //   List<String> userDts=Arrays.asList("Hello", "World!", "How", "Are", "You");
//    //    ArrayList<String> assignee;//= (ArrayList<String>) Arrays.asList("98", "28");//
//    var assignee = arrayOf("98", "28")
//    var ids = ArrayList<RequestBody>()
//    private val PICKFILE_RESULT_CODE = 1
//    val SELECTED_USER_RESULT = 8981
//    val myCalendar: Calendar = Calendar.getInstance()
//    var tskName: EditText? = null
//    var fileUri: String? = null, var taskStatus:kotlin.String? = null
//    var spinner: Spinner? = null
//    var bck: ImageView? = null
//    var tag: MutableList<String> = ArrayList()
//    var elyt: LinearLayout? = null  var call:Call<TaskResponse?>? = null
//    var tagGroup: TagContainerLayout? = null
//    var uri: Uri? = null   var file:File? = null
//    var selectedList: List<UserDt>? = null
//    var REQUEST_CODE = 111, var RESULT_CODE:Int = 101
//    var teamnm: TextView? =
//        null, var startDate:TextView? = null, var endDate:TextView? = null, var filename:TextView? = null, var doneButton:TextView? = null, var tskDesc:TextView? = null
//    var checkBox: CheckBox? =
//        null   var requestImageFile:RequestBody? = null  var filePart:Part? = null
//    var chipsInput: ChipsInput? = null
//    var linearLayout: LinearLayout? = null
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_add_task)
//        //Toast.makeText(AddTaskActivity.this,"teamid  "+ teamAdapter.teamid,Toast.LENGTH_SHORT).show();
//        elyt = findViewById(R.id.end_date_lyt)
//        tagGroup = findViewById(R.id.tagcontainerLayout)
//        spinner = findViewById(R.id.dynamic_spinner)
//        chipsInput = findViewById(R.id.chips_input)
//        bck = findViewById(R.id.bck_add_task)
//        linearLayout = findViewById(R.id.chip_lyt)
//        doneButton = findViewById<TextView>(R.id.task_create_done)
//        filename = findViewById<View>(R.id.document_attach) as TextView
//        teamnm = findViewById(R.id.ad_tm_name)
//        //        if(teamAdapter.teamname.equalsIgnoreCase("")){
////        teamnm.setText(teamAdapter.teamname);}else {teamnm.setText("");}
//        teamnm.setText(TeamAdapter.teamname)
//        startDate = findViewById<TextView>(R.id.add_start_date)
//        endDate = findViewById<TextView>(R.id.add_end_date)
//        checkBox = findViewById(R.id.deadline_btn)
//        tskName = findViewById(R.id.add_tsk_name)
//        tskDesc = findViewById<TextView>(R.id.add_tsk_description)
//        linearLayout.setOnClickListener(object : OnClickListener() {
//            fun onClick(view: View?) {
//                val intent = Intent(this@AddTaskActivity, NewGroupActivity::class.java)
//                intent.putExtra("header", "Select Assignee")
//                //                startActivity(intent);
//                startActivityForResult(intent, REQUEST_CODE)
//                //                Intent intent = new Intent(getActivity(), SelectUserActivity.class);
////                getActivity().startActivityForResult(intent, SELECTED_USER_RESULT);
//            }
//        })
//        init()
//        initView()
//        initView1()
//    }
//
//
//    private fun init() {
////        Bundle b=new Bundle();
////        b=getIntent().getExtras();
////        if (b!=null){
////        b.getParcelable("users");
////        Toast.makeText(AddTaskActivity.this,""+b.getParcelable("users"),Toast.LENGTH_SHORT).show();}
//        alertDialog = SpotsDialog(this)
//        sharedPreferences = SharedPreferences.getsharedprefInstance(this@AddTaskActivity)
//        checkBox!!.setOnClickListener(object : OnClickListener() {
//            fun onClick(view: View?) {
//                if (!checkBox!!.isChecked) {
//                    elyt!!.visibility = View.VISIBLE
//                } else if (checkBox!!.isChecked) {
//                    elyt!!.visibility = View.GONE
//                    endDate.setText("")
//                }
//            }
//        })
//        filename.setOnClickListener(object : OnClickListener() {
//            fun onClick(view: View?) {
//                val intent = Intent(Intent.ACTION_GET_CONTENT)
//                intent.type = "/"
//                startActivityForResult(intent, PICKFILE_RESULT_CODE)
//            }
//        })
//        val status: List<String> = Arrays.asList("Assigned", "Progress", "Completed", "Overdue")
//        val spinnerTimeAdapter1 = TaskStatusSpinnerAdapter(this@AddTaskActivity, status)
//        spinner!!.adapter = spinnerTimeAdapter1
//        spinner!!.onItemSelectedListener = object : OnItemSelectedListener {
//            override fun onItemSelected(
//                adapterView: AdapterView<*>?,
//                view: View?,
//                i: Int,
//                l: Long
//            ) {
//                //                slot =   mslot.get(i).getId();
//                taskStatus = status[i]
//            }
//
//            override fun onNothingSelected(adapterView: AdapterView<*>?) {}
//        }
//        doneButton.setOnClickListener(object : OnClickListener() {
//            fun onClick(view: View?) {
//                if (tskName!!.text.toString().equals("", ignoreCase = true)) {
//                    Toast.makeText(this@AddTaskActivity, "add task name", Toast.LENGTH_SHORT).show()
//                } else if (startDate.getText().toString().equals("", ignoreCase = true)) {
//                    Toast.makeText(this@AddTaskActivity, "add start date", Toast.LENGTH_SHORT)
//                        .show()
//                    //                }else if (endDate.getText().toString().equalsIgnoreCase("")){
////                    Toast.makeText(AddTaskActivity.this,"add end date",Toast.LENGTH_SHORT).show();
////                }else   if (fileUri==null){
////                    Toast.makeText(AddTaskActivity.this,"attach file",Toast.LENGTH_SHORT).show();
//                } else if (taskStatus.equals("", ignoreCase = true)) {
//                    Toast.makeText(this@AddTaskActivity, "Select task status", Toast.LENGTH_SHORT)
//                        .show()
//                } else {
//                    addTask()
//                }
//            }
//        })
//    }
//
//    private fun getRealPathFromURIPath(contentURI: Uri, activity: Activity): String? {
//        val cursor: Cursor? = activity.contentResolver.query(contentURI, null, null, null, null)
//        return if (cursor == null) {
//            contentURI.getPath()
//        } else {
//            cursor.moveToFirst()
//            val idx: Int = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
//            cursor.getString(idx)
//        }
//    }
//
//    private fun addTask() {
//        alertDialog!!.show()
//        apiInterface = ApiClient.getRetrofit().create(ApiInterface::class.java)
//        val tkn: RequestBody =
//            create(MediaType.parse("text/plain"), sharedPreferences.getFCMToken())
//        val tname: RequestBody = create(MediaType.parse("text/plain"), tskName!!.text.toString())
//        val tmid: RequestBody = create(MediaType.parse("text/plain"), teamAdapter.teamid)
//        val tdesc: RequestBody = create(MediaType.parse("text/plain"), tskDesc.getText().toString())
//        val tstatus: RequestBody = create(MediaType.parse("text/plain"), taskStatus)
//        val sdate: RequestBody =
//            create(MediaType.parse("text/plain"), startDate.getText().toString())
//        val edate: RequestBody = create(MediaType.parse("text/plain"), endDate.getText().toString())
//        if (selectedList != null) {
//            for (a in selectedList!!.indices) {
//                val id: RequestBody =
//                    create(MediaType.parse("text/plain"), selectedList!![a].getUid())
//                ids.add(id)
//            }
//        }
//
//
////        File file=new File(getRealPathFromURIPath(uri,getActivity()));
//        if (uri != null) {
//            file = File(getPath(uri))
//            requestImageFile = create(MediaType.parse("/"), file)
//            filePart = createFormData.createFormData("file_link", file.getName(), requestImageFile)
//        } else {
//            filePart = null
//        }
//        //Toast.makeText(AddTaskActivity.this,""+TeamAdapter.teamid+filePart+endDate.getText().toString(),Toast.LENGTH_SHORT).show();
////        if (filePart==null){
////        call=apiInterface.addTask(tkn,tname,tmid,tdesc,tstatus,sdate,edate,null,ids);}else {
////           call=apiInterface.addTask(tkn,tname,tmid,tdesc,tstatus,sdate,edate,filePart,ids); }
//        call = apiInterface.addTask(tkn, tname, tmid, tdesc, tstatus, sdate, edate, filePart, ids)
//        //        Toast.makeText(AddTaskActivity.this,call.toString(),Toast.LENGTH_SHORT).show();
//        call.enqueue(object : Callback<TaskResponse?>() {
//            fun onResponse(call: Call<TaskResponse?>?, response: Response<TaskResponse?>) {
//                alertDialog!!.dismiss()
//                val response1: TaskResponse = response.body()
//                if (response1.getResponseStatus().equalsIgnoreCase("200")) {
////                    Toast.makeText(AddTaskActivity.this,response1.getResponseMessage(),Toast.LENGTH_SHORT).show();
////                    startActivity(new Intent(getActivity(), ChatActivity.class));
//                    val detailResponse: TaskDetailResponse = response1.getData()
//                    val userDts: List<UserDt> = detailResponse.getUser()
//                    //                    Intent data = getIntent();
////                    data.putExtra("users", detailResponse );
////                    setResult(Activity.RESULT_OK, data);
////                    finish();
////                    Toast.makeText(AddTaskActivity.this,"task name is:"+detailResponse.getTaskStatus(),Toast.LENGTH_SHORT).show();
//                    val hashMap = HashMap<String, String>()
//                    hashMap["tname"] = detailResponse.getTaskName()
//                    hashMap["tsdt"] = detailResponse.getCreatedDate()
//                    hashMap["tedt"] = detailResponse.getEndDate()
//                    hashMap["tid"] = detailResponse.getTaskId()
//                    hashMap["firebaseid"] = detailResponse.getFirebaseTaskid()
//                    hashMap["tstatus"] = detailResponse.getTaskStatus()
//                    hashMap["tdesc"] = detailResponse.getTaskDescription()
//                    hashMap["user"] = userDts[0].getUserName().toString() + " +" + userDts.size
//                    val intent = Intent() //getIntent();
//                    intent.putExtra("key", hashMap) //(Serializable) detailResponse);
//                    setResult(RESULT_OK, intent)
//                    finish()
//                }
//            }
//
//            fun onFailure(call: Call<TaskResponse?>, t: Throwable) {
//                alertDialog!!.dismiss()
//                println("" + call)
//                if (t is IOException) {
//                    Toast.makeText(
//                        this@AddTaskActivity,
//                        "Something went wrong.Please try after some time",
//                        Toast.LENGTH_SHORT
//                    ).show()
//                    // logging probably not necessar
//                    t.printStackTrace()
//                } else {
//                    Toast.makeText(this@AddTaskActivity, "Something went wrong", Toast.LENGTH_SHORT)
//                        .show()
//                    // todo log to some central bug tracking service
//                }
//            }
//        })
//    }
//
//    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
//        super.onActivityResult(requestCode, resultCode, data)
//        //        Toast.makeText(this,""+requestCode,Toast.LENGTH_LONG).show();
//        if (requestCode == PICKFILE_RESULT_CODE) {
//            if (resultCode == RESULT_OK) {
//                fileUri = data.data!!.path
//                val f = File(fileUri)
//                uri = data.data
//                filename.setText(f.getName())
//            }
//        } else if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
//            selectedList = data.getSerializableExtra("users") as List<UserDt>?
//            //                Toast.makeText(AddTaskActivity.this,""+userDts,Toast.LENGTH_SHORT).show();
//            if (selectedList != null) {
//                for (i in selectedList!!.indices) {
//                    tag.add(selectedList!![i].getUserName())
//                }
//                tagGroup.setBackgroundColor(resources.getColor(R.color.white))
//                tagGroup.setTags(tag)
//            }
//        }
//    }
//
//    fun initView() {
//        val date =
//            OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
//                // TODO Auto-generated method stub
//                myCalendar.set(Calendar.YEAR, year)
//                myCalendar.set(Calendar.MONTH, monthOfYear)
//                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
//                updateLabel()
//                //                 updateLabel1();
//            }
//        startDate.setOnClickListener(object : OnClickListener() {
//            fun onClick(v: View?) {
//                val datePickerDialog = DatePickerDialog(
//                    this@AddTaskActivity, date, myCalendar
//                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar
//                        .get(Calendar.DAY_OF_MONTH)
//                )
//                datePickerDialog.datePicker.minDate = System.currentTimeMillis() - 1000
//                datePickerDialog.show()
//            }
//        })
//        endDate.setOnClickListener(object : OnClickListener() {
//            fun onClick(v: View?) {
//                val datePickerDialog = DatePickerDialog(
//                    this@AddTaskActivity, date, myCalendar
//                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar
//                        .get(Calendar.DAY_OF_MONTH)
//                )
//                datePickerDialog.datePicker.minDate = System.currentTimeMillis() - 1000
//                datePickerDialog.show()
//            }
//        })
//    }
//
//    fun initView1() {
//        val date =
//            OnDateSetListener { view, year, monthOfYear, dayOfMonth -> // TODO Auto-generated method stub
//                myCalendar.set(Calendar.YEAR, year)
//                myCalendar.set(Calendar.MONTH, monthOfYear)
//                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
//                updateLabel1()
//            }
//        endDate.setOnClickListener(object : OnClickListener() {
//            fun onClick(v: View?) {
//                val datePickerDialog = DatePickerDialog(
//                    this@AddTaskActivity, date, myCalendar
//                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar
//                        .get(Calendar.DAY_OF_MONTH)
//                )
//                datePickerDialog.datePicker.minDate = System.currentTimeMillis() - 1000
//                datePickerDialog.show()
//            }
//        })
//    }
//
//    private fun updateLabel() {
//        val myFormat = "yyyy-MM-dd"
//        val sdf = SimpleDateFormat(myFormat, Locale.US)
//        startDate.setText(sdf.format(myCalendar.getTime()))
////        myCalendar.add(Calendar.DATE, 30);
////        Date end = myCalendar.getTime();
////        endDate.setText(sdf.format(end));
//    }
//
//    private fun updateLabel1() {
//        val myFormat = "yyyy-MM-dd"
//        val sdf = SimpleDateFormat(myFormat, Locale.US)
//        endDate.setText(sdf.format(myCalendar.getTime()))
//    }
//
//    private fun getPath(uri: Uri): String? {
//        val isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT
//        if (isKitKat) {
//            // MediaStore (and general)
//            return getForApi19(uri)
//        } else if ("content".equals(uri.getScheme(), ignoreCase = true)) {
//
//            // Return the remote address
//            return if (isGooglePhotosUri(uri)) uri.getLastPathSegment() else getDataColumn(
//                uri,
//                null,
//                null
//            )
//        } else if ("file".equals(uri.getScheme(), ignoreCase = true)) {
//            return uri.getPath()
//        }
//        return null
//    }
//
//    @TargetApi(19)
//    private fun getForApi19(uri: Uri): String? {
//        Log.e("tag", "+++ API 19 URI :: $uri")
//        if (DocumentsContract.isDocumentUri(this, uri)) {
//            Log.e("tag", "+++ Document URI")
//            // ExternalStorageProvider
//            if (isExternalStorageDocument(uri)) {
//                Log.e("tag", "+++ External Document URI")
//                val docId = DocumentsContract.getDocumentId(uri)
//                val split = docId.split(":").toTypedArray()
//                val type = split[0]
//                if ("primary".equals(type, ignoreCase = true)) {
//                    Log.e("tag", "+++ Primary External Document URI")
//                    return Environment.getExternalStorageDirectory().toString() + "/" + split[1]
//                }
//
//                // TODO handle non-primary volumes
//            } else if (isDownloadsDocument(uri)) {
//                Log.e("tag", "+++ Downloads External Document URI")
//                val id = DocumentsContract.getDocumentId(uri)
//                val contentUri: Uri = ContentUris.withAppendedId(
//                    Uri.parse("content://downloads/public_downloads"), java.lang.Long.valueOf(id)
//                )
//                return getDataColumn(contentUri, null, null)
//            } else if (isMediaDocument(uri)) {
//                Log.e("tag", "+++ Media Document URI")
//                val docId = DocumentsContract.getDocumentId(uri)
//                val split = docId.split(":").toTypedArray()
//                val type = split[0]
//                var contentUri: Uri? = null
//                if ("image" == type) {
//                    Log.e("tag", "+++ Image Media Document URI")
//                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
//                } else if ("video" == type) {
//                    Log.e("tag", "+++ Video Media Document URI")
//                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
//                } else if ("audio" == type) {
//                    Log.e("tag", "+++ Audio Media Document URI")
//                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
//                }
//                val selection = "_id=?"
//                val selectionArgs = arrayOf(
//                    split[1]
//                )
//                return getDataColumn(contentUri, selection, selectionArgs)
//            }
//        } else if ("content".equals(uri.getScheme(), ignoreCase = true)) {
//            Log.e("tag", "+++ No DOCUMENT URI :: CONTENT ")
//
//            // Return the remote address
//            return if (isGooglePhotosUri(uri)) uri.getLastPathSegment() else getDataColumn(
//                uri,
//                null,
//                null
//            )
//        } else if ("file".equals(uri.getScheme(), ignoreCase = true)) {
//            Log.e("tag", "+++ No DOCUMENT URI :: FILE ")
//            return uri.getPath()
//        }
//        return null
//    }
//
//    /**
//     * Get the value of the data column for this Uri. This is useful for
//     * MediaStore Uris, and other file-based ContentProviders.
//     *
//     * @param uri The Uri to query.
//     * @param selection (Optional) Filter used in the query.
//     * @param selectionArgs (Optional) Selection arguments used in the query.
//     * @return The value of the _data column, which is typically a file path.
//     */
//    fun getDataColumn(
//        uri: Uri?, selection: String?,
//        selectionArgs: Array<String>?
//    ): String? {
//        var cursor: Cursor? = null
//        val column = "_data"
//        val projection = arrayOf(
//            column
//        )
//        try {
//            cursor = contentResolver.query(
//                uri, projection, selection, selectionArgs,
//                null
//            )
//            if (cursor != null && cursor.moveToFirst()) {
//                val index: Int = cursor.getColumnIndexOrThrow(column)
//                return cursor.getString(index)
//            }
//        } finally {
//            if (cursor != null) cursor.close()
//        }
//        return null
//    }
//
//
//    /**
//     * @param uri The Uri to check.
//     * @return Whether the Uri authority is ExternalStorageProvider.
//     */
//    fun isExternalStorageDocument(uri: Uri): Boolean {
//        return "com.android.externalstorage.documents" == uri.getAuthority()
//    }
//
//    /**
//     * @param uri The Uri to check.
//     * @return Whether the Uri authority is DownloadsProvider.
//     */
//    fun isDownloadsDocument(uri: Uri): Boolean {
//        return "com.android.providers.downloads.documents" == uri.getAuthority()
//    }
//
//    /**
//     * @param uri The Uri to check.
//     * @return Whether the Uri authority is MediaProvider.
//     */
//    fun isMediaDocument(uri: Uri): Boolean {
//        return "com.android.providers.media.documents" == uri.getAuthority()
//    }
//
//    /**
//     * @param uri The Uri to check.
//     * @return Whether the Uri authority is Google Photos.
//     */
//    fun isGooglePhotosUri(uri: Uri): Boolean {
//        return "com.google.android.apps.photos.content" == uri.getAuthority()
//    }
//}


