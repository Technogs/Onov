package com.application.onovapplication.activities


import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.application.onovapplication.R
import com.application.onovapplication.activities.common.BaseAppCompatActivity
import com.application.onovapplication.databinding.ActivitySignPetitionBinding
import com.application.onovapplication.viewModels.PetitionViewModel
import java.io.ByteArrayOutputStream


class SignPetitionActivity : BaseAppCompatActivity(), View.OnClickListener {
    val petitionViewModel by lazy { ViewModelProvider(this).get(PetitionViewModel::class.java) }

    lateinit var uri: Uri
    lateinit var ptId: String
    lateinit var signb: Bitmap
    private lateinit var binding: ActivitySignPetitionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignPetitionBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        ptId = intent.getStringExtra("pId").toString()

        observeViewModel()
    }


    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.ivClear -> {
                binding.signatureView.clearCanvas()
            }
            R.id.btnSignPetition -> {

                if (binding.signatureView.isBitmapEmpty) {
                    Toast.makeText(this, "please add a signature", Toast.LENGTH_SHORT).show()
                } else {
                    signb = binding.signatureView.signatureBitmap
                    uri = getImageUri(this, signb)
                    val bos = ByteArrayOutputStream()
                    val f = signb.compress(Bitmap.CompressFormat.PNG, 100, bos)
                    petitionViewModel.signPetition(this, userPreferences.getUserREf(), ptId, signb)

                }
            }
        }
    }

    fun getImageUri(inContext: Context, inImage: Bitmap): Uri {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = MediaStore.Images.Media.insertImage(
            inContext.contentResolver, inImage, "Title", null
        )
        return Uri.parse(path)
    }


    private fun observeViewModel() {

        petitionViewModel.successful.observe(this, Observer {
            dismissDialog()
            if (it != null) {
                if (it) {
                    if (petitionViewModel.status == "success") {
                        Toast.makeText(this, petitionViewModel.message, Toast.LENGTH_SHORT).show()
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
}
