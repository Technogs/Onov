package com.application.onovapplication.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.application.onovapplication.R
import com.application.onovapplication.activities.common.BaseFragment
import com.application.onovapplication.adapters.ImagesGovernmentAdapter
import com.application.onovapplication.adapters.StatesAdapter
import com.application.onovapplication.api.ApiClient
import com.application.onovapplication.api.ApiInterface
import com.application.onovapplication.databinding.FragmentCongressBinding
import com.application.onovapplication.model.GovtData
import com.application.onovapplication.model.SenateResponse
import com.application.onovapplication.model.statesData

import com.application.onovapplication.viewModels.GovernmentViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class CongressFragment(var title: String) : BaseFragment(), View.OnClickListener {
    lateinit var rvImages: RecyclerView


    var states = arrayListOf<String>()
    var stateslist = arrayListOf<statesData>()
    private var apiInterface: ApiInterface? = null
    lateinit var binding: FragmentCongressBinding
//    lateinit var adapter : ImagesGovernmentAdapter
     var govtData:List<GovtData> =ArrayList()
    val governmentViewModel by lazy { ViewModelProvider(this).get(GovernmentViewModel::class.java) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCongressBinding.inflate(inflater, container, false)
        initViews(binding.root)


        return binding.root
        //return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.allState.setOnClickListener(this)
        if (title == "Senate") {
            governmentViewModel.getgovtdata(requireActivity(), title, "")
        } else if (title == "Congress") {
            governmentViewModel.getgovtdata(requireActivity(), title, "")
        } else {
            binding.noDataText.visibility = View.VISIBLE
            binding.wholeView.visibility = View.GONE
        }
        governmentViewModel.getgovtstates(requireActivity())
        governmentViewModel.getgovtdata(requireActivity(), title, "")
        observeViewModel()
    }

    fun initViews(view: View) {
        rvImages = view.findViewById(R.id.rvImages)

        binding.spState.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    adapterView: AdapterView<*>?,
                    view: View?,
                    i: Int,
                    l: Long
                ) {
                    Log.e("datahghg", stateslist[i].state!!)
                    binding.allState.setBackgroundResource(R.color.white)

                    if (stateslist.isNotEmpty()) {
                        showDialog()
                        governmentViewModel.getgovtdata(
                            requireActivity(),
                            title,
                            states[i]
                        )
                    }
                }

                override fun onNothingSelected(adapterView: AdapterView<*>?) {}
            }

    }


    override fun onClick(v: View?) {
        when (v!!.id) {

            R.id.allState->{
                showDialog()
                binding.allState.setBackgroundResource(R.color.red)
                governmentViewModel.getgovtdata(requireActivity(), title, "")

            }
        }
    }

    private fun observeViewModel() {

        governmentViewModel.successfulGovtData.observe(requireActivity(), Observer {
dismissDialog()
            if (it != null) {
                if (it) {
                    if (governmentViewModel.status == "success") {
                     //  if (governmentViewModel.govtDataResponse.govtData.isNotEmpty()) {

                            govtData=governmentViewModel.govtDataResponse.govtData
                            rvImages.layoutManager = GridLayoutManager(requireActivity(), 2)
                          val adapter =ImagesGovernmentAdapter(requireActivity(),govtData)
                            rvImages.adapter = adapter
                            adapter.notifyDataSetChanged()
//                        } else{
//                           binding.rvImages.visibility=View.GONE
//                           binding.noDataText.visibility=View.VISIBLE
//
//                       }
                    }
                }
            }
        })
        governmentViewModel.successfulState.observe(requireActivity(), Observer {
dismissDialog()
            if (it != null) {
                if (it) {
                    if (governmentViewModel.status == "success") {
                        if (governmentViewModel.stateResponse.statesData!!.isNotEmpty()) {

                            if (stateslist.isNotEmpty())
                                stateslist.clear()

                            stateslist.addAll(governmentViewModel.stateResponse.statesData!!)
                            states.add("Alabama")

                            for (i in 0..stateslist.size-1)
                             {
                                 states.add(governmentViewModel.stateResponse.statesData!!.get(i).state.toString())
                            Log.e("datahghg", "dd "+states[i])
                            }
                            val spinnerAdapter = StatesAdapter(
                                requireActivity(),  // Use our custom adapter

                                R.layout.spinner_text, (states).toTypedArray()
                            )
                            spinnerAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown)
                            binding.spState.adapter = spinnerAdapter


                            // states.addAll(governmentViewModel.stateResponse.statesData)




                        }

                    }
                }
            }
        })
    }


    private fun getSenate() {
        showDialog()
        apiInterface = ApiClient.getRetrofit("https://api.propublica.org/congress/v1/116/senate/")
            ?.create(ApiInterface::class.java)
        val call = apiInterface?.classlist()
        call?.enqueue(object : Callback<SenateResponse?> {
            override fun onResponse(
                call: Call<SenateResponse?>,
                response: Response<SenateResponse?>
            ) {
                dismissDialog()
                val response1 = response.body()
                if (response1!!.status.equals("OK")) {
//
                    rvImages.layoutManager = GridLayoutManager(activity, 2)
                    val adapter = ImagesGovernmentAdapter(
                        requireActivity(),
                        governmentViewModel.govtDataResponse.govtData
                    )
                    rvImages.adapter = adapter
//                    if (r1 != null) {
//                        for (i in 1..r1.size) {
//                            arrayList.addAll(listOf(r1.get(i - 1).className))
//                        }
//                    }
                }
            }

            override fun onFailure(call: Call<SenateResponse?>, t: Throwable) {
                dismissDialog()
                if (t is IOException) {
                    Toast.makeText(requireActivity(), t.message, Toast.LENGTH_SHORT).show()
                    //  Toast.makeText(requireActivity(), "Something went wrong, Please try again", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireActivity(), "Something went wrong", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        })
    }
}