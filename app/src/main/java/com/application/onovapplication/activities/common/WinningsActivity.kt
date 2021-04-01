package com.application.onovapplication.activities.common

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.application.onovapplication.R
import com.application.onovapplication.adapters.WinningsAdapter
import com.application.onovapplication.model.StatsDataList
import com.application.onovapplication.viewModels.StatsViewModel
import com.application.onovapplication.viewModels.WinningsViewModel
import kotlinx.android.synthetic.main.activity_winnings.*

class WinningsActivity : BaseAppCompatActivity() {

    private var winningsAdapter: WinningsAdapter? = null

    private val winningsList: ArrayList<StatsDataList> = ArrayList()

    private val winningsViewModel by lazy {
        ViewModelProvider(this).get(WinningsViewModel::class.java)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_winnings)

        observeViewModel()

        winningsViewModel.getWinnings(this, userPreferences.getUserREf())
        showDialog()

        winningsAdapter = WinningsAdapter(this, winningsList)
        rv_winnings.adapter = winningsAdapter
    }

    private fun observeViewModel() {
        
        winningsViewModel.successful.observe(this , Observer { 
            
            dismissDialog()
            if (it)
            {
                if (winningsViewModel.status == "success") {

                    winningsList.addAll(winningsViewModel.statsResponse.statsDataList!!)

                    if (winningsList.isEmpty()) {
                        noWinningsData.visibility = View.VISIBLE
                    } else {
                        winningsAdapter?.notifyDataSetChanged()
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