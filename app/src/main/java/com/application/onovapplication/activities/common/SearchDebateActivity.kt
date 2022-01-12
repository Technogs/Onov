package com.application.onovapplication.activities.common

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.application.onovapplication.R
import com.application.onovapplication.adapters.DebatesAdapter
import com.application.onovapplication.databinding.ActivitySearchDebateBinding
import com.application.onovapplication.debate.vlive.ui.main.MainActivity
import com.application.onovapplication.model.LiveDebate
import com.application.onovapplication.model.LiveDebateModel
import com.application.onovapplication.viewModels.DebatesViewModel
import com.application.onovapplication.viewModels.SearchViewModel

class SearchDebateActivity : BaseAppCompatActivity(), View.OnClickListener,DebatesAdapter.onClickDebate {
    private val debatesViewModel by lazy { ViewModelProvider(this).get(DebatesViewModel::class.java)}
    private val searchViewModel by lazy { ViewModelProvider(this).get(SearchViewModel::class.java)}



    private lateinit var binding: ActivitySearchDebateBinding

//    private val winningsList: ArrayList<StatsDataList> = ArrayList()
    lateinit var debates: LiveDebateModel
     var type: String="past"
  var debatesList: ArrayList<LiveDebate> = ArrayList()

    private val winningsAdapter by lazy { DebatesAdapter(this,type,debatesList,this)}



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchDebateBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        debatesViewModel.getlivedebate(this)

        showDialog()

        observeViewModel()

        binding.rvWinnings.adapter = winningsAdapter

    }
    private fun observeViewModel() {

        debatesViewModel.successfulLiveDebate.observe(this, androidx.lifecycle.Observer {
            dismissDialog()
            if (it != null) {
                if (debatesViewModel.status == "success") {
                   // setError(debatesViewModel.liveDebateModel.pastDebate.size.toString())
                    debates=debatesViewModel.liveDebateModel
                    if (debatesList.isNotEmpty())
                        debatesList.clear()
                    debatesList.addAll(debates.pastDebate)
                     type="past"
                   winningsAdapter.notifyDataSetChanged()
                    if (debatesList.isNullOrEmpty()){

                        binding.rvWinnings.visibility=View.GONE
                        binding.noWinningsData.visibility=View.VISIBLE
                    }
                    else {
                        binding.rvWinnings.visibility=View.VISIBLE
                        binding.noWinningsData.visibility=View.GONE
                    }

                } else {
                    setError(debatesViewModel.message)
                    finish()
                }
            }
            else {
                setError(debatesViewModel.message)
            }

        })

        debatesViewModel.successfulSearchDebate.observe(this, androidx.lifecycle.Observer {
            dismissDialog()
            if (it != null) {
                if (debatesViewModel.status == "success") {
                   // setError(debatesViewModel.liveDebateModel.pastDebate.size.toString())
                    debates=debatesViewModel.liveDebateModel
                    if (debatesList.isNotEmpty())
                        debatesList.clear()
                    debatesList.addAll(debates.pastDebate)
                     type="past"
                   winningsAdapter.notifyDataSetChanged()
                    if (debatesList.isNullOrEmpty()){

                        binding.rvWinnings.visibility=View.GONE
                        binding.noWinningsData.visibility=View.VISIBLE
                    }
                    else {
                        binding.rvWinnings.visibility=View.VISIBLE
                        binding.noWinningsData.visibility=View.GONE
                    }

                } else {
                    setError(debatesViewModel.message)
                    finish()
                }
            }
            else {
                setError(debatesViewModel.message)
            }

        })

        searchViewModel.successfullyDebateJoinee.observe(this, androidx.lifecycle.Observer {
            dismissDialog()
            if (it != null) {
                if (searchViewModel.status == "success") {
userPreferences.setDebateJoiner(searchViewModel.debateJoinerResponse)

                } else {
                    setError(searchViewModel.message)
                    finish()
                }
            }
            else {
                setError(searchViewModel.message)
            }

        })



    }
    override fun onClick(v: View?) {

        when (v?.id) {
            R.id.upcoming -> {
                debatesList.clear()
                type="upcoming"
                buttonActiveState(binding.upcoming)
                buttonInActiveState(binding.last)
                buttonInActiveState(binding.live)

                if (debates.upcomingDebate.isNullOrEmpty()){

                    binding.rvWinnings.visibility=View.GONE
                    binding.noWinningsData.visibility=View.VISIBLE
                }
                else {

                    debatesList.addAll(debates.upcomingDebate)
                    binding.rvWinnings.visibility=View.VISIBLE
                    binding.noWinningsData.visibility=View.GONE

//                    winningsAdapter =
//                        DebatesAdapter(this, debates.upcomingDebate)
                    binding.rvWinnings.adapter = winningsAdapter
                    winningsAdapter.notifyDataSetChanged()

                }

            }


            R.id.live -> {
                type="live"
                debatesList.clear()
                buttonActiveState(binding.live)
                buttonInActiveState(binding.last)
                buttonInActiveState(binding.upcoming)


                if (debates.liveDebate.isNullOrEmpty()){

                    binding.rvWinnings.visibility=View.GONE
                    binding.noWinningsData.visibility=View.VISIBLE
                }
                else {
                     val winningsAdapters by lazy { DebatesAdapter(this,type,debatesList,this)}
                    debatesList.addAll(debates.liveDebate)
                    binding.rvWinnings.visibility=View.VISIBLE
                    binding.noWinningsData.visibility=View.GONE

//                    winningsAdapter =
//                        DebatesAdapter(this, debates.liveDebate)
                    binding.rvWinnings.adapter = winningsAdapters
                }

            }

            R.id.last -> {
                debatesList.clear()
                type="past"
                buttonActiveState(binding.last)
                buttonInActiveState(binding.live)
                buttonInActiveState(binding.upcoming)


                if (debates.pastDebate.isNullOrEmpty()){

                    binding.rvWinnings.visibility=View.GONE
                    binding.noWinningsData.visibility=View.VISIBLE
                }
                else {

                    debatesList.addAll(debates.pastDebate)
                    binding.rvWinnings.visibility=View.VISIBLE
                    binding.noWinningsData.visibility=View.GONE

//                    winningsAdapter =
//                        DebatesAdapter(this, debates.pastDebate)
                    binding.rvWinnings.adapter = winningsAdapter
                }
            }
            R.id.searchBtn -> {

showDialog()
debatesViewModel.searchDebate(this,binding.searchDebate.text.toString())


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
            ContextCompat.getDrawable(     this,
                R.drawable.drawable_chat_background
            )
        )

    }

    override fun onDebateItemClick(liveDebate: LiveDebate) {
        userPreferences.setDebateDetails(liveDebate)
        searchViewModel.getdebaterjoiner(this,liveDebate.id.toString())
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}
         