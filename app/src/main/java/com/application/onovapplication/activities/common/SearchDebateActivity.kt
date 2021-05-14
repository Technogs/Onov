package com.application.onovapplication.activities.common

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.application.onovapplication.R
import com.application.onovapplication.adapters.WinningsAdapter
import com.application.onovapplication.model.StatsDataList
import com.application.onovapplication.viewModels.WinningsViewModel
import kotlinx.android.synthetic.main.activity_search_debate.*

class SearchDebateActivity : BaseAppCompatActivity(), View.OnClickListener {

    private var winningsAdapter: WinningsAdapter? = null

    private val winningsList: ArrayList<StatsDataList> = ArrayList()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_debate)



        winningsAdapter = WinningsAdapter(this, winningsList)
        rv_winnings.adapter = winningsAdapter
    }

    override fun onClick(v: View?) {

        when (v?.id) {
            R.id.upcoming -> {
                buttonActiveState(upcoming)
                buttonInActiveState(last)
                buttonInActiveState(live)
            }


            R.id.live -> {
                buttonActiveState(live)
                buttonInActiveState(last)
                buttonInActiveState(upcoming)
            }

            R.id.last -> {
                buttonActiveState(last)
                buttonInActiveState(live)
                buttonInActiveState(upcoming)
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