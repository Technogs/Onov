package com.application.onovapplication.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.application.onovapplication.R
import com.application.onovapplication.databinding.ActivityAskToAddBinding


class AskForMediaFragment:Fragment(){

    lateinit var binding: ActivityAskToAddBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

       binding = ActivityAskToAddBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.postPhoto.setOnClickListener { addPost("photo") }
       binding.postVideo.setOnClickListener { addPost("video")}

        binding.ivBackIcon.setOnClickListener {
            val fFragment = FeedFragment()
        replaceFragment(fFragment)}


    }

    fun addPost(type:String){
        val bundle = Bundle()
        bundle.putString("type", type)

        val someFragment = CreatePostFragment()
        someFragment.setArguments(bundle)
         // if written, this transaction will be added to backstack
        replaceFragment(someFragment)


    }

    fun replaceFragment(fragment: Fragment){
        val transaction: FragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.homeTabContainer, fragment) // give your fragment container id in first parameter
        transaction.addToBackStack(null).commit()
    }


}