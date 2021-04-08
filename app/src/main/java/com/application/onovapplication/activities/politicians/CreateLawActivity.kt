package com.application.onovapplication.activities.politicians

import android.Manifest
import android.app.Activity
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
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.application.onovapplication.R
import com.application.onovapplication.activities.common.BaseAppCompatActivity
import kotlinx.android.synthetic.main.action_bar_layout_2.*
import kotlinx.android.synthetic.main.activity_create_law.*


class CreateLawActivity : BaseAppCompatActivity(), View.OnClickListener {
    private val PICK_PDF_REQUEST = 1

    private val STORAGE_PERMISSION_CODE = 123


    private var myUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_law)
        tvScreenTitle.text = getString(R.string.new_law)

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

            R.id.ivCancel-> {
                pdfView.visibility = View.GONE
                ivCancel.visibility = View.GONE
            }
        }
    }


    private fun showFileChooser() {
        val intent = Intent()
        intent.type = "application/pdf"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Pdf"), PICK_PDF_REQUEST)
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_PDF_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            myUri = data.data

            val myFile =
                "${Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)}/${getFileName(
                    this@CreateLawActivity,
                    myUri
                )}"

            pdfView.visibility = View.VISIBLE
            ivCancel.visibility = View.VISIBLE

            pdfView.fromUri(myUri)
                .defaultPage(0)
                .load()

            Log.e("PRACHI", myFile)

            //val file : File = File(myFile)
        }
    }


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
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                showFileChooser()
            } else {
                //Displaying another toast if permission is not granted
                Toast.makeText(this, "Oops you just denied the permission", Toast.LENGTH_LONG)
                    .show()
            }
        }
    }


}