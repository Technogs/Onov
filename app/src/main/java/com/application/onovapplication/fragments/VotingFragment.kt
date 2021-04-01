package com.application.onovapplication.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.application.onovapplication.R
import com.application.onovapplication.adapters.VotingAdapter
import kotlinx.android.synthetic.main.fragment_voting.*

// TODO: Rename parameter arguments, choose names that match

class VotingFragment : Fragment() {

    var votingAdapter: VotingAdapter? = null



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_voting, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        votingAdapter = VotingAdapter(requireContext())

        rv_voting.adapter = votingAdapter
    }

}