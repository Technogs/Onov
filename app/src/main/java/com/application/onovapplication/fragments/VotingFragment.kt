package com.application.onovapplication.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.application.onovapplication.R
import com.application.onovapplication.adapters.VotingAdapter
import com.application.onovapplication.databinding.FragmentMoreBinding
import com.application.onovapplication.databinding.FragmentVotingBinding

// TODO: Rename parameter arguments, choose names that match

class VotingFragment : Fragment() {

    var votingAdapter: VotingAdapter? = null

    lateinit var binding: FragmentVotingBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentVotingBinding.inflate(inflater, container, false)
        return binding.getRoot()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        votingAdapter = VotingAdapter(requireContext())

        binding.rvVoting.adapter = votingAdapter
    }

}