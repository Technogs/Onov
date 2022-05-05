package com.application.onovapplication.activities.common

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.application.onovapplication.R
import com.application.onovapplication.adapters.DebateJoineeAdapter
import com.application.onovapplication.databinding.ActivityDebateDetailsBinding
import com.application.onovapplication.model.DebateData
import com.application.onovapplication.model.DebateDetailResponse
import com.application.onovapplication.model.JoinerData
import com.application.onovapplication.repository.BaseUrl
import com.application.onovapplication.viewModels.DebatesViewModel
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions


class DebateDetailsActivity : BaseAppCompatActivity(), View.OnClickListener,
    DebateJoineeAdapter.onVoteClick {
    val debateViewModel by lazy { ViewModelProvider(this).get(DebatesViewModel::class.java) }
    lateinit var debate: DebateData
    lateinit var debateid: String
    lateinit var debateDetailResponse: DebateDetailResponse
    private lateinit var binding: ActivityDebateDetailsBinding
    private var debateJoineeAdapter: DebateJoineeAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDebateDetailsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        debate = intent.getParcelableExtra<DebateData>("debates") as DebateData

        debateViewModel.getDebateDetail(this, debate.dbatId)
        showDialog()
        init()
        observeViewModel()

    }

    fun init() {
        Log.d("users", debate.userRef + "  " + userPreferences.getuserDetails()?.userRef.toString())
        if (debate.requestStatus == "accepted") {
            binding.btnAccept.visibility = View.VISIBLE
            binding.btnReject.visibility = View.GONE
            binding.btnReshedule.visibility = View.GONE
            binding.btnAccept.text = "Accepted"
        } else if (debate.requestStatus == "rejected") {
            binding.btnAccept.visibility = View.GONE
            binding.btnReshedule.visibility = View.GONE
            binding.btnReject.visibility = View.VISIBLE
            binding.btnAccept.text = "Rejected"
        } else if (debate.requestStatus == "reshedule") {
            binding.btnAccept.visibility = View.VISIBLE
            binding.btnReshedule.visibility = View.GONE
            binding.btnReject.visibility = View.GONE
            binding.btnAccept.text = "Requested for Reschedule"
        }
        binding.debateDetailsTitleValue.text = debate.title
        binding.debateDetailsDateValue.text =
            convertDateFormat(debate.date, "yyyy-MM-dd", "MMM dd,yyyy")//
        binding.debateDetailsTimeValue.text = convertDateFormat(
            debate.time,
            "HH:mm",
            "hh:mm a"
        )

        binding.debateDetailsTimeCreatorValue.text = debate.userName
        //debateDetailsTimeCreator
        Glide.with(this).load(BaseUrl.photoUrl + debate.profilePic)
            .apply(RequestOptions().placeholder(R.drawable.ic_baseline_account_circle_24))
            .into(binding.debateDetailsTimeCreator)

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnAccept -> {
                if (binding.btnAccept.text == "Accept")
                    showDialog()
                debateViewModel.acceptRequest(this, debate.requestId, "1")

            }
            R.id.btnReject -> {
                if (binding.btnAccept.text == "Reject")
                    showDialog()
                debateViewModel.acceptRequest(this, debate.requestId, "0")

            }
            R.id.btnReshedule -> {
                if (binding.btnReshedule.text == "Reschedule")
                    showDialog()
                debateViewModel.acceptRequest(this, debate.requestId, "2")

            }
        }
    }


    private fun observeViewModel() {

        debateViewModel.successfulDebateAccept.observe(this, Observer {
            dismissDialog()
            if (it != null) {
                if (it) {
                    if (debateViewModel.status == "success") {
                        finish()

                    } else {
                        setError(debateViewModel.message)
                        finish()
                    }
                }
            } else {
                setError(debateViewModel.message)
            }

        })
        debateViewModel.successfulDebateDetail.observe(this, Observer {
            dismissDialog()
            if (it != null) {
                if (it) {
                    if (debateViewModel.status == "success") {

                        debateDetailResponse = debateViewModel.debateDetailResponse
                        if (debateDetailResponse.debateDetail.userRef == userPreferences.getuserDetails()?.userRef.toString()) {
                            binding.allBtn.visibility = View.GONE
                        }
                        debateJoineeAdapter = DebateJoineeAdapter(
                            this,
                            debateDetailResponse.debateDetail.debateJoinner,
                            this,
                            "dbdt"
                        )
                        binding.rvDebateRequests.adapter = debateJoineeAdapter
                        debateJoineeAdapter?.notifyDataSetChanged()

                    } else {
                        setError(debateViewModel.message)
                        finish()
                    }
                }
            } else {
                setError(debateViewModel.message)
            }

        })


    }

    override fun voteBtnClick(joinerData: JoinerData) {
        TODO("Not yet implemented")
    }
}