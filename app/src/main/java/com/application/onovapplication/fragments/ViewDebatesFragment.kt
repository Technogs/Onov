package com.application.onovapplication.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.application.onovapplication.R
import com.application.onovapplication.adapters.ViewDebatesAdapter
import kotlinx.android.synthetic.main.fragment_view_debates.*


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class ViewDebatesFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    var debatesAdapter: ViewDebatesAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_view_debates, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //    debatesAdapter = ViewDebatesAdapter(requireContext())
        noDebateData.visibility = View.VISIBLE
        rv_view_debates.adapter = debatesAdapter

    }

    companion object {

        fun newInstance(param1: String, param2: String) =
            ViewDebatesFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}