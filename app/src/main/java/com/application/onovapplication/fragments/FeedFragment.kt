package com.application.onovapplication.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.application.onovapplication.R
import com.application.onovapplication.activities.common.BaseAppCompatActivity
import com.application.onovapplication.activities.common.HomeTabActivity
import com.application.onovapplication.adapters.ViewDebatesAdapter
import com.application.onovapplication.model.FeedData
import com.application.onovapplication.viewModels.HomeViewModel
import kotlinx.android.synthetic.main.fragment_feeds.*


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class FeedFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    private val homeViewModel by lazy {
        ViewModelProvider(this).get(HomeViewModel::class.java)
    }
    var debatesAdapter: ViewDebatesAdapter? = null

    val feedData: ArrayList<FeedData> = ArrayList()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_feeds, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tvRole.text = "Role: ".plus((activity as BaseAppCompatActivity).userPreferences.getRole())
 //       (activity as HomeTabActivity).showDialog()
    //    homeViewModel.getFeed(requireContext(), "c3Y1b1L6i1KvM")
       // observeViewModel()
        // noDebateData.visibility = View.VISIBLE

        debatesAdapter = ViewDebatesAdapter(
            requireContext(),
            feedData
        )

        rv_view_debates.adapter = debatesAdapter


    }


    private fun observeViewModel() {
        homeViewModel.successful.observe(requireActivity(), Observer { successful ->
            (activity as HomeTabActivity).dismissDialog()
            if (successful != null) {
                if (successful) {
//
                    if (homeViewModel.status == "success") {

                        if (homeViewModel.getFeedResponse.feedData.isNullOrEmpty()) {
                            noDebateData.visibility = View.VISIBLE
                        } else {
                            debatesAdapter = ViewDebatesAdapter(
                                requireContext(),
                                homeViewModel.getFeedResponse.feedData!!
                            )
                            rv_view_debates.adapter = debatesAdapter
                        }

                    } else {
                        (activity as HomeTabActivity).setError(homeViewModel.message)
                    }

                } else {
                    (activity as HomeTabActivity).setError(homeViewModel.message)
                }
            }
        })
    }
}