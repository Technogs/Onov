package com.application.onovapplication.fragments

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.AdapterView
import android.widget.LinearLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.application.onovapplication.R
import com.application.onovapplication.activities.common.BaseFragment
import com.application.onovapplication.adapters.ImagesGovernmentAdapter
import com.application.onovapplication.adapters.StatesAdapter
import com.application.onovapplication.api.ApiInterface
import com.application.onovapplication.databinding.FragmentCongressBinding
import com.application.onovapplication.databinding.GovtInfoDialogBinding
import com.application.onovapplication.model.GovtData
import com.application.onovapplication.model.statesData

import com.application.onovapplication.viewModels.GovernmentViewModel


class CongressFragment(var title: String) : BaseFragment(), View.OnClickListener,
    ImagesGovernmentAdapter.OnAdapterClick {
    lateinit var rvImages: RecyclerView


    var states = arrayListOf<String>()
    var stateslist = arrayListOf<statesData>()
    private var apiInterface: ApiInterface? = null
    lateinit var binding: FragmentCongressBinding

    var govtData: List<GovtData> = ArrayList()
    val governmentViewModel by lazy { ViewModelProvider(this).get(GovernmentViewModel::class.java) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCongressBinding.inflate(inflater, container, false)
        initViews(binding.root)

        return binding.root

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

            R.id.allState -> {
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
                        if (isAdded && activity != null) {
                            govtData = governmentViewModel.govtDataResponse.govtData
                            rvImages.layoutManager = GridLayoutManager(requireActivity(), 2)
                            val adapter = ImagesGovernmentAdapter(
                                requireActivity(), govtData, this
                            )
                            rvImages.adapter = adapter
                            adapter.notifyDataSetChanged()
                        }
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

                            for (i in 0..stateslist.size - 1) {
                                states.add(governmentViewModel.stateResponse.statesData!!.get(i).state.toString())
                                Log.e("datahghg", "dd " + states[i])
                            }
                            val spinnerAdapter = StatesAdapter(
                                requireActivity(),  // Use our custom adapter

                                R.layout.spinner_text, (states).toTypedArray()
                            )
                            spinnerAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown)
                            binding.spState.adapter = spinnerAdapter


                        }

                    }
                }
            }
        })
    }


    override fun doClick(dataItem: GovtData?) {
        openDonationsDialog(dataItem)
    }

    private fun openDonationsDialog(dataItem: GovtData?) {
        val dialog = Dialog(requireActivity())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        val binding = GovtInfoDialogBinding.inflate(dialog.layoutInflater)
        val view = binding.root
        dialog.setContentView(view)
        dialog.window?.setLayout(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )

        binding.titleTxt.text = dataItem?.name
        binding.officeLocation.text = "Office Location: " + dataItem?.officeLocation
        binding.titleVl.text = "Party: " + dataItem?.party
        binding.phoneTxt.text = "Contact Info: " + dataItem?.contactInfo
        dialog.setOnKeyListener(DialogInterface.OnKeyListener { dialog, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                dialog?.cancel()
                return@OnKeyListener true

            }
            false
        })

        binding.close.setOnClickListener { dialog.dismiss() }
        dialog.show()

    }


}