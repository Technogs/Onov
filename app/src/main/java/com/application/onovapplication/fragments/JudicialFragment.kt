package com.application.onovapplication.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.application.onovapplication.R
import com.application.onovapplication.adapters.JudicialAdapter

class JudicialFragment : Fragment() , View.OnClickListener{
    lateinit var rvJudicial : RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view = inflater.inflate(R.layout.fragment_judicial, container, false)
        initViews(view)
        return view
    }

    fun initViews(view : View){
        rvJudicial = view.findViewById(R.id.rvJudicial)
        rvJudicial.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
        val adapter = this.context?.let { JudicialAdapter(it) }
        rvJudicial.adapter = adapter
    }

    override fun onClick(p0: View?) {
        when (p0!!.id) {

        }
    }

}