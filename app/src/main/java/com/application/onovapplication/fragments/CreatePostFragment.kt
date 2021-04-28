package com.application.onovapplication.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.application.onovapplication.R
import com.application.onovapplication.adapters.PredefinedTextsAdapter
import com.application.onovapplication.model.PredefinedTextsModel
import kotlinx.android.synthetic.main.fragment_create_post.*


class CreatePostFragment : Fragment(), PredefinedTextsAdapter.PredefinedTextClickInterface {


    private var adapter: PredefinedTextsAdapter? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create_post, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val predefinedTextsList: ArrayList<PredefinedTextsModel> = ArrayList()

        predefinedTextsList.add(PredefinedTextsModel("BREAKING NEWS!"))
        predefinedTextsList.add(PredefinedTextsModel("What's happening locally now!"))
        predefinedTextsList.add(PredefinedTextsModel("Call to action!"))

        adapter = PredefinedTextsAdapter(requireContext(), predefinedTextsList, this)

        rv_predefined_texts.adapter = adapter


    }

    override fun onClick(predefinedTextsModel: PredefinedTextsModel) {
        etPostTitle.setText("")
        etPostTitle.setText(predefinedTextsModel.text)

    }


}