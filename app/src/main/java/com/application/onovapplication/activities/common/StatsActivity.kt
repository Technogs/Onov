package com.application.onovapplication.activities.common

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.application.onovapplication.R
import com.application.onovapplication.adapters.StatsAdapter
import com.application.onovapplication.model.StatsDataList
import com.application.onovapplication.viewModels.StatsViewModel
import kotlinx.android.synthetic.main.activity_stats.*

class StatsActivity : BaseAppCompatActivity() {

    private var statsAdapter: StatsAdapter? = null

    private val statsList: ArrayList<StatsDataList> = ArrayList()

//    private val statsViewModel by lazy {
//        ViewModelProvider(this).get(StatsViewModel::class.java)
//    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stats)


//        statsViewModel.getStats(this, userPreferences.getUserREf())
//        showDialog()
//

        statsAdapter = StatsAdapter(this, statsList)
        rv_stats.adapter = statsAdapter

       // observeViewModel()
    }


//    private fun observeViewModel() {
//
//        statsViewModel.successful.observe(this, Observer {
//            dismissDialog()
//            if (it) {
//                if (statsViewModel.status == "success") {
//
//                    statsList.addAll(statsViewModel.statsResponse.statsDataList!!)
//
//                    if (statsList.isEmpty()) {
//                        noStatsData.visibility = View.VISIBLE
//                    } else {
//                        statsAdapter?.notifyDataSetChanged()
//                    }
//                }
//                else{
//                    setError(statsViewModel.message)
//                }
//            }
//            else{
//                setError(statsViewModel.message)
//
//            }
//        })

   // }
}