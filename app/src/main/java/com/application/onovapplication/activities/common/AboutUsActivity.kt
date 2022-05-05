package com.application.onovapplication.activities.common

import android.os.Build
import android.os.Bundle
import android.text.Html
import androidx.annotation.RequiresApi
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.application.onovapplication.R
import com.application.onovapplication.databinding.ActivityAboutUsBinding
import com.application.onovapplication.viewModels.PetitionViewModel
import java.lang.Integer.parseInt

class AboutUsActivity : BaseAppCompatActivity() {
    var type: String? = null
    val petitionViewModel by lazy { ViewModelProvider(this).get(PetitionViewModel::class.java) }
    private lateinit var binding: ActivityAboutUsBinding

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAboutUsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        observeViewModel()
        type = intent.getStringExtra("type")

        when (type) {
            "about" -> {
                binding.tvTitle.text = getString(R.string.about_us)
                binding.aboutContent.text=Html.fromHtml(getString( R.string.aboutus), Html.FROM_HTML_MODE_LEGACY)

            }

            "privacy" -> {
                binding.tvTitle.text = "Privacy Policy"
                binding.aboutContent.text=Html.fromHtml(getString( R.string.privacy_content), Html.FROM_HTML_MODE_LEGACY)
//                petitionViewModel.getPolicy(this,"privacy")
            }

            else -> {
                binding.tvTitle.text = getString(R.string.terms_and_conditions)
                binding.aboutContent.text=Html.fromHtml(getString( R.string.terms_of_use), Html.FROM_HTML_MODE_LEGACY)

            }
        }

    }

    private fun observeViewModel() {

        petitionViewModel.successfulAbout.observe(this, Observer {
            dismissDialog()
            if (it != null) {
                if (it) {
                    if (petitionViewModel.status == "success") {
                       /// Toast.makeText(this, petitionViewModel.message, Toast.LENGTH_SHORT).show()
                        binding.aboutContent.text=petitionViewModel.aboutdata.data
                    } else {
                        setError(petitionViewModel.message)
                        finish()
                    }
                }
            } else {
                setError(petitionViewModel.message)
            }

        })}
}