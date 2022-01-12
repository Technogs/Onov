package com.application.onovapplication.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.application.onovapplication.R
import com.application.onovapplication.activities.common.BaseFragment
import com.application.onovapplication.adapters.ImagesGovernmentAdapter
import com.application.onovapplication.api.ApiClient
import com.application.onovapplication.api.ApiInterface
import com.application.onovapplication.databinding.FragmentDonationsBinding
import com.application.onovapplication.databinding.FragmentExecutiveBinding
import com.application.onovapplication.model.PresidentResponse
import com.application.onovapplication.model.SenateResponse
import com.bumptech.glide.Glide
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class ExecutiveFragment : BaseFragment() , View.OnClickListener{
    private var apiInterface: ApiInterface? = null
    lateinit var binding: FragmentExecutiveBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        //var view = inflater.inflate(R.layout.fragment_executive, container, false)

        binding = FragmentExecutiveBinding.inflate(inflater, container, false)
        initViews(binding.root)
        return binding.getRoot()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getPresidentData()
    }

    fun initViews(view : View){


    }

    override fun onClick(p0: View?) {
        when (p0!!.id) {

        }
    }

    private fun getPresidentData() {
        showDialog()
        apiInterface = ApiClient.getRetrofit("https://en.wikipedia.org/w/rest.php/v1/search/")?.create(
            ApiInterface::class.java)
        val call = apiInterface?.presidentData()
        call?.enqueue(object : Callback<PresidentResponse?> {
            override fun onResponse(call: Call<PresidentResponse?>, response: Response<PresidentResponse?>) {
                dismissDialog()
                val response1 = response.body()
                if (response1!=null) {
//                    Toast.makeText(getActivity(), response1.msg, Toast.LENGTH_SHORT).show()
                    val r1 = response1.pages
                    for (i in 0..(r1.size-1)){
                        if (r1[i].key=="Joe_Biden"){
                            Glide.with(requireActivity()).load("https:"+r1[i].thumbnail.url).into(binding.presidentPic)
                            binding.presidentName.setText(r1[i].title)
                            binding.presidentBio.setText(r1[i].excerpt)
                        }
                    }

//                    if (r1 != null) {
//                        for (i in 1..r1.size) {
//                            arrayList.addAll(listOf(r1.get(i - 1).className))
//                        }
//                    }
                }
            }
            override fun onFailure(call: Call<PresidentResponse?>, t: Throwable) {
                dismissDialog()
                if (t is IOException) {
                    Toast.makeText(requireActivity(), t.message, Toast.LENGTH_SHORT).show()
                    //  Toast.makeText(requireActivity(), "Something went wrong, Please try again", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireActivity(), "Something went wrong", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

}