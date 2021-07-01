package com.application.onovapplication.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.application.onovapplication.R

class ExecutiveFragment : Fragment() , View.OnClickListener{

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view = inflater.inflate(R.layout.fragment_executive, container, false)
        initViews(view)
        return view
    }

    fun initViews(view : View){


    }

    override fun onClick(p0: View?) {
        when (p0!!.id) {

        }
    }

}