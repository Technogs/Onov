package com.application.onovapplication.activities.common

import android.os.Bundle
import android.view.View
import com.application.onovapplication.R
import com.application.onovapplication.adapters.ViewFollowersAdapter
import com.application.onovapplication.databinding.ActionBarLayout2Binding
import com.application.onovapplication.databinding.ActivityStatsBinding
import com.application.onovapplication.databinding.ActivityViewFollowersBinding
import com.application.onovapplication.model.Follow
import com.application.onovapplication.model.PeopleData


class ViewFollowersActivity : BaseAppCompatActivity(),ViewFollowersAdapter.OnPeopleClick {

    var type: String? = null
    var people: PeopleData? = null

    var viewFollowersAdapter: ViewFollowersAdapter? = null
    private lateinit var binding: ActivityViewFollowersBinding
    private lateinit var incbinding: ActionBarLayout2Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityViewFollowersBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        incbinding=binding.ab
        type = intent.getStringExtra("type")

        people = intent.getParcelableExtra<PeopleData>("data") as PeopleData




        when (type) {
            "following" -> {
                incbinding.tvScreenTitle.text = getString(R.string.support)
                if (people!!.followList.isNullOrEmpty()) {
                    binding.noDataLayout.visibility = View.VISIBLE
                } else {
                    binding.noDataLayout.visibility = View.GONE
                }
                viewFollowersAdapter =
                    ViewFollowersAdapter(this, type.toString(),"",this, people!!.followingList)
                binding.rvFollowers.adapter = viewFollowersAdapter

            }
            "donors" -> {
                if (people!!.donnerList.isNullOrEmpty()) {
                    binding.noDataLayout.visibility = View.VISIBLE
                } else {
                    binding.noDataLayout.visibility = View.GONE
                }
                incbinding.tvScreenTitle.text = getString(R.string.donors)
                viewFollowersAdapter =
                    ViewFollowersAdapter(this, type.toString(), "",this,people!!.donnerList)
                binding.rvFollowers.adapter = viewFollowersAdapter

            }
            else -> {
                if (people!!.followList.isNullOrEmpty()) {
                    binding.noDataLayout.visibility = View.VISIBLE
                } else {
                    binding.noDataLayout.visibility = View.GONE
                }
                incbinding.tvScreenTitle.text = getString(R.string.followers)
                viewFollowersAdapter =
                    ViewFollowersAdapter(this, type.toString(),"",this, people!!.followList)
                binding.rvFollowers.adapter = viewFollowersAdapter
            }
        }


    }

    override fun onPClick(datatem: Follow) {
        TODO("Not yet implemented")
    }

    override fun onCheckboxClick(datatem: Follow) {
        TODO("Not yet implemented")
    }
}