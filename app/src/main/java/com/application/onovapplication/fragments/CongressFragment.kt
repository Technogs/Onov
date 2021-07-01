package com.application.onovapplication.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.application.onovapplication.R
import com.application.onovapplication.adapters.ImagesGovernmentAdapter

class CongressFragment : Fragment() , View.OnClickListener{
    lateinit var rvImages : RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view = inflater.inflate(R.layout.fragment_congress, container, false)
        initViews(view)
        return view
    }

    fun initViews(view : View){
        rvImages = view.findViewById(R.id.rvImages)
        rvImages.layoutManager = GridLayoutManager(activity, 2)
        val adapter = this.context?.let { ImagesGovernmentAdapter(it) }
        rvImages.adapter = adapter
    }

    override fun onClick(p0: View?) {
        when (p0!!.id) {

        }
    }

}