package com.application.onovapplication.activities.common
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider

import com.application.onovapplication.R
import com.application.onovapplication.adapters.AllWinnersAdapter
import com.application.onovapplication.databinding.ActivityAboutUsBinding
import com.application.onovapplication.databinding.ActivityAllWinnersBinding
import com.application.onovapplication.model.AllWinnersList
import com.application.onovapplication.viewModels.WinningsViewModel


class AllWinnersActivity : BaseAppCompatActivity() {

    private var allWinnersAdapter: AllWinnersAdapter? = null
    private lateinit var binding: ActivityAllWinnersBinding

    private val allWinnersList: ArrayList<AllWinnersList> = ArrayList()

    private val winningsViewModel by lazy {
        ViewModelProvider(this).get(WinningsViewModel::class.java)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAllWinnersBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        winningsViewModel.getAllWinners(this)
        observeViewModel()


      showDialog()

        allWinnersAdapter = AllWinnersAdapter(this, allWinnersList)
      binding.rvAllWinners.adapter = allWinnersAdapter
    }

    private fun observeViewModel() {
        winningsViewModel.successfullyGotWinnersList.observe(this , Observer {
        dismissDialog()
        if (it)
        {
            if (winningsViewModel.status == "success") {

                allWinnersList.addAll(winningsViewModel.winnersResponse.allWinnersList!!)

                if (allWinnersList.isEmpty()) {
                    binding.noAllWinnersData.visibility = View.VISIBLE
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