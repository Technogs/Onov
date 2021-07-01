package com.application.onovapplication.activities.common

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.application.onovapplication.R
import com.application.onovapplication.adapters.SearchFriendsAdapter
import kotlinx.android.synthetic.main.activity_search_friends.*

class SearchFriendsActivity : BaseAppCompatActivity() {

    var searchFriendsAdapter: SearchFriendsAdapter?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_friends)

        searchFriendsAdapter = SearchFriendsAdapter(this)

        rv_search_friends.adapter = searchFriendsAdapter


        btnSearch.setOnClickListener {

        }



    }
}