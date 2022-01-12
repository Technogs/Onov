package com.application.onovapplication.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.application.onovapplication.R
import com.application.onovapplication.adapters.DonationsAdapter
import com.application.onovapplication.adapters.JudicialAdapter
import com.application.onovapplication.viewModels.DonationViewModel
import com.application.onovapplication.viewModels.GovernmentViewModel

class JudicialFragment : Fragment() , View.OnClickListener{
    val governmentViewModel by lazy { ViewModelProvider(this).get(GovernmentViewModel::class.java) }

    lateinit var rvJudicial : RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_judicial, container, false)
        initViews(view)
        governmentViewModel.getjudicial(requireActivity())
        observeViewModel()
        return view
    }

    fun initViews(view : View){
        rvJudicial = view.findViewById(R.id.rvJudicial)

    }
    private fun observeViewModel() {

        governmentViewModel.successful.observe(requireActivity(), Observer {

            if (it != null) {
                if (it) {
                    if (governmentViewModel.status == "success") {
                        //   setLayout(eventViewModel.eventResponse)
                        //  donation=governmentViewModel.
                        if (governmentViewModel.judicialModel?.judicialData!!.isNotEmpty()){
                        rvJudicial.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
                        val adapter = this.context?.let { JudicialAdapter(it,governmentViewModel.judicialModel!!.judicialData) }
                        rvJudicial.adapter = adapter
                    }
            }
                    //            else {
//                setError(donationViewModel.message)
                }
            }
        })
    }

    override fun onClick(p0: View?) {
        when (p0!!.id) {

        }
    }

}