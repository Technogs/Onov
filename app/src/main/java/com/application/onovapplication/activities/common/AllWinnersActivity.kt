package com.application.onovapplication.activities.common

import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider

import com.application.onovapplication.R
import com.application.onovapplication.adapters.AllWinnersAdapter
import com.application.onovapplication.databinding.ActivityAllWinnersBinding
import com.application.onovapplication.model.AllWinnersList
import com.application.onovapplication.viewModels.WinningsViewModel


class AllWinnersActivity : BaseAppCompatActivity(), View.OnClickListener {

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
        winningsViewModel.getAllWinners(
            this,
            userPreferences.getuserDetails()?.userRef.toString(),
            ""
        )

        showDialog()
        observeViewModel()

        allWinnersAdapter = AllWinnersAdapter(this, allWinnersList)
        binding.rvAllWinners.adapter = allWinnersAdapter
    }

    private fun observeViewModel() {
        winningsViewModel.successfullyGotWinnersList.observe(this, Observer {
            dismissDialog()
            if (it) {
                if (winningsViewModel.status == "success") {
                    allWinnersList.clear()
                    allWinnersList.addAll(winningsViewModel.winnersResponse.allWinnersList!!)

                    if (winningsViewModel.winnersResponse.msg == "") {
                        binding.noAllWinnersData.visibility = View.VISIBLE
                        binding.rvAllWinners.visibility = View.GONE
                    } else if (allWinnersList.isEmpty()) {
                        binding.noAllWinnersData.visibility = View.VISIBLE
                        binding.rvAllWinners.visibility = View.GONE
                    } else {
                        allWinnersAdapter?.notifyDataSetChanged()
                    }
                } else {
                    binding.noAllWinnersData.visibility = View.VISIBLE
                }
            } else {
                setError(winningsViewModel.message)
            }
        })
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.nation -> {
                showDialog()
                buttonActiveState(binding.nation)
                buttonInActiveState(binding.local)
                buttonInActiveState(binding.overall)
                buttonInActiveState(binding.state)
                winningsViewModel.getAllWinners(
                    this,
                    userPreferences.getuserDetails()?.userRef.toString(),
                    "National"
                )

            }
            R.id.state -> {
                showDialog()
                buttonActiveState(binding.state)
                buttonInActiveState(binding.local)
                buttonInActiveState(binding.overall)
                buttonInActiveState(binding.nation)
                winningsViewModel.getAllWinners(
                    this,
                    userPreferences.getuserDetails()?.userRef.toString(),
                    "state"
                )
            }
            R.id.local -> {
                showDialog()
                buttonActiveState(binding.local)
                buttonInActiveState(binding.nation)
                buttonInActiveState(binding.overall)
                buttonInActiveState(binding.state)
                winningsViewModel.getAllWinners(
                    this,
                    userPreferences.getuserDetails()?.userRef.toString(),
                    "local"
                )
            }
            R.id.overall -> {
                showDialog()
                buttonActiveState(binding.overall)
                buttonInActiveState(binding.local)
                buttonInActiveState(binding.nation)
                buttonInActiveState(binding.state)
                winningsViewModel.getAllWinners(
                    this,
                    userPreferences.getuserDetails()?.userRef.toString(),
                    ""
                )
            }
        }
    }

    fun buttonActiveState(appCompatButton: AppCompatButton) {
        appCompatButton.setTextColor(resources.getColor(R.color.white))
        appCompatButton.setBackgroundDrawable(
            ContextCompat.getDrawable(
                this,
                R.drawable.blue_solid_curve
            )
        )

    }


    fun buttonInActiveState(appCompatButton: AppCompatButton) {
        appCompatButton.setTextColor(resources.getColor(R.color.navigation_color))
        appCompatButton.setBackgroundDrawable(
            ContextCompat.getDrawable(
                this,
                R.drawable.drawable_chat_background
            )
        )

    }
}