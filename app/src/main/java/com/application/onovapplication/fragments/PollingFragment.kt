package com.application.onovapplication.fragments


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.application.onovapplication.R
import com.application.onovapplication.activities.PollActivity
import com.application.onovapplication.activities.common.BaseFragment
import com.application.onovapplication.activities.common.HomeTabActivity
import com.application.onovapplication.adapters.PollHistoryAdapter
import com.application.onovapplication.databinding.PollingLayoutBinding
import com.application.onovapplication.model.PollListsResponse
import com.application.onovapplication.model.PollingOptions
import com.application.onovapplication.viewModels.PollingViewModel
import java.util.*

class PollingFragment : BaseFragment() {
    lateinit var binding: PollingLayoutBinding
    val pollingViewModel by lazy { ViewModelProvider(requireActivity()).get(PollingViewModel::class.java) }
    val ids = ArrayList<String>()
    private val spinnerList = arrayOf("Select Radius", "Local", "State", "National")
    var radius = ""
    var size: Int = 2
    var pollHistoryAdapter: PollHistoryAdapter? = null

    var pollListsResponse: PollListsResponse? = null
    private val pollingOptions by lazy { PollingOptions(size) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = PollingLayoutBinding.inflate(inflater, container, false)

        pollingViewModel.getallpoll(
            requireActivity(),
            userPreferences.getuserDetails()?.userRef.toString()
        )
        showDialog()
        observeViewModel()

        binding.floatBtn.setOnClickListener {
            startActivity(Intent(requireActivity(), PollActivity::class.java))
//            requireActivity().finish()
        }
        initView()
        return binding.root
    }

    fun initView() {
        binding.active.setOnClickListener {
            binding.active.setBackgroundColor(resources.getColor(R.color.app_color))
            binding.active.setTextColor(resources.getColor(R.color.white))
            binding.expired.setTextColor(resources.getColor(R.color.app_color))
            binding.expired.setBackgroundColor(resources.getColor(R.color.white))
            if (pollListsResponse?.pollData?.activePoll != null) {
                binding.noData.visibility = View.GONE
                binding.rvWinnings.visibility = View.VISIBLE
                pollHistoryAdapter = PollHistoryAdapter(
                    requireActivity(),
                    pollListsResponse?.pollData?.activePoll!!,
                    "1"
                )
                binding.rvWinnings.adapter = pollHistoryAdapter
            } else {
                binding.noData.visibility = View.VISIBLE
                binding.rvWinnings.visibility = View.GONE
            }
        }
        binding.expired.setOnClickListener {
            binding.expired.setBackgroundColor(resources.getColor(R.color.app_color))
            binding.active.setBackgroundColor(resources.getColor(R.color.white))
            binding.active.setTextColor(resources.getColor(R.color.app_color))
            binding.expired.setTextColor(resources.getColor(R.color.white))
            if (pollListsResponse?.pollData?.expiredPoll != null) {
                binding.noData.visibility = View.GONE
                binding.rvWinnings.visibility = View.VISIBLE
                pollHistoryAdapter = PollHistoryAdapter(
                    requireActivity(),
                    pollListsResponse?.pollData?.expiredPoll!!,
                    "0"
                )
                binding.rvWinnings.adapter = pollHistoryAdapter
            } else if (pollListsResponse?.pollData?.expiredPoll.isNullOrEmpty()) {
                binding.noData.visibility = View.VISIBLE
                binding.rvWinnings.visibility = View.GONE
            }
        }


    }

    private fun observeViewModel() {
        val activity = activity

        pollingViewModel.successfulAllPoll.observe(
            activity as HomeTabActivity,
            androidx.lifecycle.Observer {
                dismissDialog()
                if (it != null) {
                    if (it) {

                        if (pollingViewModel.status == "success") {
                            pollListsResponse = pollingViewModel.pollListsResponse
                            if (isAdded && activity != null) {
                                pollHistoryAdapter = PollHistoryAdapter(
                                    requireContext(),
                                    pollListsResponse?.pollData?.activePoll!!,
                                    "1"
                                )
                                binding.rvWinnings.adapter = pollHistoryAdapter
                            }


                        } else {
                            setError(pollingViewModel.message)

                        }
                    }
                } else {
                    setError(pollingViewModel.message)
                }

            })


    }
}