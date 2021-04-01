package com.application.onovapplication.activities.common

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.application.onovapplication.R
import com.application.onovapplication.adapters.AllWinnersAdapter
import com.application.onovapplication.model.AllWinnersList
import com.application.onovapplication.viewModels.WinningsViewModel
import kotlinx.android.synthetic.main.activity_all_winners.*


class AllWinnersActivity : BaseAppCompatActivity() {

    private var allWinnersAdapter: AllWinnersAdapter? = null

    private val allWinnersList: ArrayList<AllWinnersList> = ArrayList()

    private val winningsViewModel by lazy {
        ViewModelProvider(this).get(WinningsViewModel::class.java)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_winners)

        observeViewModel()

        winningsViewModel.getAllWinners(this)
        showDialog()

        allWinnersAdapter = AllWinnersAdapter(this, allWinnersList)
        rv_all_winners.adapter = allWinnersAdapter
    }

    private fun observeViewModel() {
        winningsViewModel.successfullyGotWinnersList.observe(this , Observer {
        dismissDialog()
        if (it)
        {
            if (winningsViewModel.status == "success") {

                allWinnersList.addAll(winningsViewModel.winnersResponse.allWinnersList!!)

                if (allWinnersList.isEmpty()) {
                    noAllWinnersData.visibility = View.VISIBLE
                } else {
                    allWinnersAdapter?.notifyDataSetChanged()
                }
            }
            else{
                setError(winningsViewModel.message)
            }
        }
        else{
            setError(winningsViewModel.message)
        }
    })
    }
}