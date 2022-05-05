package com.application.onovapplication.activities.common

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.application.onovapplication.R
import com.application.onovapplication.adapters.StatsAdapter
import com.application.onovapplication.databinding.ActivityStartPetitionBinding
import com.application.onovapplication.databinding.ActivityStatsBinding
import com.application.onovapplication.model.StatsDataList
import com.application.onovapplication.viewModels.StatsViewModel

class StatsActivity : BaseAppCompatActivity() {

    private var statsAdapter: StatsAdapter? = null

    private val statsList: ArrayList<StatsDataList> = ArrayList()

    private val statsViewModel by lazy {
        ViewModelProvider(this).get(StatsViewModel::class.java)
    }

    private lateinit var binding: ActivityStatsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStatsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        statsViewModel.getStats(this, userPreferences.getUserREf())
        showDialog()


      observeViewModel()
    }


    private fun observeViewModel() {

        statsViewModel.successful.observe(this, Observer {
            dismissDialog()
            if (it) {
                if (statsViewModel.status == "success") {

                    statsList.addAll(statsViewModel.statsResponse.statsDataList!!)
                    binding.totalLose.text=statsViewModel.statsResponse.totallose
                    binding.totalWin.text=statsViewModel.statsResponse.totalWin
                    statsAdapter = StatsAdapter(this,
                        statsViewModel.statsResponse.statsDataList as ArrayList<StatsDataList>
                    )
                 binding.rvStats.adapter = statsAdapter
                    if (statsList.isEmpty()) {
                        binding.noStatsData.visibility = View.VISIBLE
                        binding.rvStats.visibility=View.GONE
                    } else {
                        statsAdapter?.notifyDataSetChanged()
                    }
                }
                else{
                    setError(statsViewModel.message)
                }
            }
            else{
                setError(statsViewModel.message)

            }
        })

 }
}