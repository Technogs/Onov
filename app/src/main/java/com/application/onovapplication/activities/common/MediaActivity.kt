package com.application.onovapplication.activities.common

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.application.onovapplication.adapters.MediaAdapter
import com.application.onovapplication.databinding.ActivityMediaBinding
import com.application.onovapplication.model.MediaData
import com.live.kicktraders.viewModel.LoginViewModel

class MediaActivity : BaseAppCompatActivity(), MediaAdapter.OnClickMedia {
    private lateinit var binding: ActivityMediaBinding
    var mediaAdapter: MediaAdapter? = null

    private var pos: Int? = null
    var mediaData: MutableList<MediaData>? = null

    private val loginViewModel by lazy {
        ViewModelProvider(this).get(LoginViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMediaBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        loginViewModel.getAllMedia(this, userPreferences.getuserDetails()?.userRef.toString())
        showDialog()
        observeViewModel()

    }

    private fun observeViewModel() {

        loginViewModel.successfulMedia.observe(this, Observer {
            dismissDialog()
            if (it != null) {
                if (it) {
                    if (loginViewModel.status == "success") {

                        mediaData = loginViewModel.mediaResponse.mediaData as MutableList<MediaData>
                        mediaAdapter =
                            MediaAdapter(this, loginViewModel.mediaResponse.mediaData, this)
                        val layoutManager = GridLayoutManager(this, 3)

                        // at last set adapter to recycler view.
                        binding.rvMedia.layoutManager = layoutManager
                        binding.rvMedia.adapter = mediaAdapter
                    } else {
                        setError(loginViewModel.message)
                    }
                }
            } else {
                setError(loginViewModel.message)
            }

        })
        loginViewModel.successfulDeleteMedia.observe(this, Observer {
            dismissDialog()
            if (it != null) {
                if (it) {
                    if (loginViewModel.status == "success") {
                        setError(loginViewModel.message)
                        mediaData!!.remove(mediaData!![pos!!])
                        mediaAdapter?.notifyDataSetChanged()
                    } else {
                        setError(loginViewModel.message)
                    }
                }
            } else {
                setError(loginViewModel.message)
            }

        })
    }

    override fun onMediaDelete(mdData: MediaData, position: Int) {
        showDialog()
        pos = position
        loginViewModel.deleteMedia(this, mdData.id, mdData.recordType)
    }


}