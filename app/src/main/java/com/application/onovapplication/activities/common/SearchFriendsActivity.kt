package com.application.onovapplication.activities.common

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.application.onovapplication.R
import com.application.onovapplication.adapters.SearchFriendsAdapter
import com.application.onovapplication.databinding.ActivitySearchDebateBinding
import com.application.onovapplication.databinding.ActivitySearchFriendsBinding
import com.application.onovapplication.model.SearchData
import com.application.onovapplication.model.SearchModel
import com.application.onovapplication.viewModels.SearchViewModel


class SearchFriendsActivity : BaseAppCompatActivity(), SearchFriendsAdapter.OnViewClick {
    var type = 0
    var dataItem: SearchData? = null
    var searchFriendsAdapter: SearchFriendsAdapter? = null
    val searchViewModel by lazy { ViewModelProvider(this).get(SearchViewModel::class.java) }
    private lateinit var binding: ActivitySearchFriendsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchFriendsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)



        binding.btnSearch.setOnClickListener {
            if (binding.searchKey.text.toString() == "") Toast.makeText(
                this, "please enter a key",
                Toast.LENGTH_SHORT
            ).show()
            else searchViewModel.searchUser(
                this, userPreferences.getUserREf(),
                binding.searchKey.text.toString()
            )

        }

        observeViewModel()

    }

    private fun observeViewModel() {

        searchViewModel.successful.observe(this, Observer {
            dismissDialog()
            if (it != null) {
                if (it) {
                    if (searchViewModel.status == "success") {
                        setLayout(searchViewModel.searchList)

                    } else {
                        setError(searchViewModel.message)
                        finish()
                    }
                }
            } else {
                setError(searchViewModel.message)
            }

        })

        searchViewModel.successfullyUpdated.observe(this, Observer {
            dismissDialog()
            if (it != null) {
                if (it) {
                    if (searchViewModel.status == "success") {
                        Log.d("followuser", dataItem!!.follow)

                        if (dataItem!!.follow == "0") {
                            dataItem!!.follow = "4"
                            searchFriendsAdapter!!.notifyDataSetChanged()

                        } else {
                            dataItem!!.follow = "0"
                            searchFriendsAdapter!!.notifyDataSetChanged()
                        }

                        searchViewModel.status = ""
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


    fun setLayout(searchModel: SearchModel) {
        searchFriendsAdapter = SearchFriendsAdapter(this, searchModel.dataList, this)
        binding.rvSearchFriends.adapter = searchFriendsAdapter

    }


    override fun onClick(dataItem: SearchData) {
        this.dataItem = dataItem
        if (dataItem.follow == "0") {
            searchViewModel.followUser(this, userPreferences.getUserREf(), "1", dataItem.userRef)

        } else {
            searchViewModel.followUser(this, userPreferences.getUserREf(), "0", dataItem.userRef)

        }

    }

    //  }
}