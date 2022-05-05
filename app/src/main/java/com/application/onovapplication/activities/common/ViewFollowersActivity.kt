package com.application.onovapplication.activities.common

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.application.onovapplication.R
import com.application.onovapplication.adapters.ViewFollowersAdapter
import com.application.onovapplication.databinding.ActionBarLayout2Binding
import com.application.onovapplication.databinding.ActivityViewFollowersBinding
import com.application.onovapplication.model.Follow
import com.application.onovapplication.model.PeopleData
import com.application.onovapplication.model.SearchData
import com.application.onovapplication.viewModels.SearchViewModel


class ViewFollowersActivity : BaseAppCompatActivity(),ViewFollowersAdapter.OnPeopleClick {

    var type: String? = null
    var user: String? = null
    var people: PeopleData? = null
    val searchViewModel by lazy { ViewModelProvider(this).get(SearchViewModel::class.java) }

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
        user = intent.getStringExtra("user")
        people = intent.getParcelableExtra<PeopleData>("data") as PeopleData




        when (type) {
            "following" -> {
                incbinding.tvScreenTitle.text = getString(R.string.support)
                if (people!!.followingList.isNullOrEmpty()) {
                    binding.noDataLayout.visibility = View.VISIBLE
                    binding.rvFollowers.visibility = View.GONE
                } else {
                    binding.noDataLayout.visibility = View.GONE
                    binding.rvFollowers.visibility = View.VISIBLE
                }
                viewFollowersAdapter =
                    ViewFollowersAdapter(this, type.toString(),"",user.toString(),"",this, people!!.followingList)
                binding.rvFollowers.adapter = viewFollowersAdapter

            }
            "donors" -> {
                if (people!!.donnerList.isNullOrEmpty()) {
                    binding.noDataLayout.visibility = View.VISIBLE
                    binding.rvFollowers.visibility = View.GONE
                } else {
                    binding.noDataLayout.visibility = View.GONE
                    binding.rvFollowers.visibility = View.VISIBLE
                }
                incbinding.tvScreenTitle.text = getString(R.string.donors)
                viewFollowersAdapter =
                    ViewFollowersAdapter(this, type.toString(), "",user.toString(),"",this,people!!.donnerList)
                binding.rvFollowers.adapter = viewFollowersAdapter

            }"DonatedTo" -> {
                if (people!!.donetedList.isNullOrEmpty()) {
                    binding.noDataLayout.visibility = View.VISIBLE
                    binding.rvFollowers.visibility = View.GONE
                } else {
                    binding.noDataLayout.visibility = View.GONE
                    binding.rvFollowers.visibility = View.VISIBLE
                }
                incbinding.tvScreenTitle.text = getString(R.string.donatedTo)
                viewFollowersAdapter =
                    ViewFollowersAdapter(this, type.toString(), "",user.toString(),"",this,people!!.donetedList)
                binding.rvFollowers.adapter = viewFollowersAdapter

            }
            else -> {
                if (people!!.followList.isNullOrEmpty()) {
                    binding.noDataLayout.visibility = View.VISIBLE
                    binding.rvFollowers.visibility = View.GONE
                } else {
                    binding.noDataLayout.visibility = View.GONE
                    binding.rvFollowers.visibility = View.VISIBLE
                }
                incbinding.tvScreenTitle.text = "Citizens"
                viewFollowersAdapter =
                    ViewFollowersAdapter(this, type.toString(),"",user.toString(),"",this, people!!.followList)
                binding.rvFollowers.adapter = viewFollowersAdapter
            }
        }
observeViewModel()

    }

    private fun observeViewModel() {


        searchViewModel.successfullyUpdated.observe(this, Observer {
            dismissDialog()
            if (it != null) {
                if (it) {
                    if (searchViewModel.status == "success") {
                        setError(searchViewModel.message)
                        val intent = Intent(this, ProfileActivity2::class.java)
                        intent.putExtra("type", "user")
                        startActivity(intent)
                        finish()
//                        Log.d("followuser", dataItem!!.follow!!)
//
//                        if (dataItem!!.follow == "0") {
//                            dataItem!!.follow = "4"
//                            searchFriendsAdapter!!.notifyDataSetChanged()
//
//                        } else {
//                            dataItem!!.follow = "0"
//                            searchFriendsAdapter!!.notifyDataSetChanged()
//                        }
//
//                        searchViewModel.status = ""
                    } else {
                        setError(searchViewModel.message)
                        finish()
                    }
                }
            } else {
                setError(searchViewModel.message)
            }

        })

    }

    override fun onPClick(datatem: Follow) {
        TODO("Not yet implemented")
    }

    override fun onCheckboxClick(datatem: Follow) {
        TODO("Not yet implemented")
    }

    override fun onRemoveFollow(datatem: Follow) {
        showDialog()
        searchViewModel.followUser(this,datatem.userRef2 , "0", userPreferences.getUserREf())
    }

    override fun onRemoveSupport(datatem: Follow) {
        showDialog()
        searchViewModel.followUser(this, userPreferences.getUserREf(), "0", datatem.userRef2)
    }
}