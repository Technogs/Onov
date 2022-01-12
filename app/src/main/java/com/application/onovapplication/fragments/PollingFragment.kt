package com.application.onovapplication.fragments

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.application.onovapplication.R
import com.application.onovapplication.activities.common.BaseFragment
import com.application.onovapplication.activities.common.HomeTabActivity
import com.application.onovapplication.adapters.PollingAdapter
import com.application.onovapplication.databinding.PollingLayoutBinding
import com.application.onovapplication.model.PollingOptions
import com.application.onovapplication.viewModels.PollingViewModel
import java.text.SimpleDateFormat
import java.util.*

class PollingFragment : BaseFragment(), PollingAdapter.onclickCross, View.OnClickListener {
    lateinit var binding: PollingLayoutBinding
    val pollingViewModel by lazy { ViewModelProvider(requireActivity()).get(PollingViewModel::class.java) }
    val ids = ArrayList<String>()

    var size: Int = 2
    var votingAdapter: PollingAdapter? = null

    //    lateinit var pollingOptions: PollingOptions
    private val pollingOptions by lazy { PollingOptions(size) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ):
            View? {
        // Inflate the layout for this fragment
        binding = PollingLayoutBinding.inflate(inflater, container, false)

        votingAdapter = PollingAdapter(requireContext(), this, pollingOptions)

        observeViewModel()
        binding.rvVoting.adapter = votingAdapter
        binding.etAddOption.setOnClickListener {
            pollingOptions.count = pollingOptions.count + 1
            size=pollingOptions.count
            votingAdapter!!.notifyDataSetChanged()
        }
        binding.etPollingDate.setOnClickListener { setDatePicker(binding.etPollingDate) }
        binding.etPollingTime.setOnClickListener { setTimePicker(binding.etPollingTime) }
        binding.createPoll.setOnClickListener {

            showDialog()
//            if (ids.isNullOrEmpty()) {
                Log.d("arrayids", ids.toString())

                pollingViewModel.cretePolling(
                    requireActivity(), userPreferences.getuserDetails()?.userRef.toString(),
                    binding.etPollTitle.text.toString(), ids,
                    binding.etPollingDate.text.toString(), binding.etPollingTime.text.toString()
                )
//            }
        }
        return binding.root
    }


    override fun onclick() {
        pollingOptions.count = pollingOptions.count - 1
        size=pollingOptions.count
        votingAdapter!!.notifyDataSetChanged()
    }

    override fun onClickOption(option: String) {
        ids.add(option)
    }


    private fun setDatePicker(editText: TextView?) {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)


        val dpd: DatePickerDialog = DatePickerDialog(
            requireActivity(),
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                // Display Selected date in textbox

                val selectedMonth = String.format("%02d", monthOfYear + 1)
                val selectedDate = String.format("%02d", dayOfMonth)

                editText?.text = "$year-$selectedMonth-$selectedDate"
            },
            year,
            month,
            day
        )
        dpd.show()
    }

    private fun setTimePicker(editText: TextView?) {
        val cal = Calendar.getInstance()
        val timeSetListener =
            TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
                cal.set(Calendar.HOUR_OF_DAY, hour)
                cal.set(Calendar.MINUTE, minute)
                editText?.text = SimpleDateFormat("HH:mm").format(cal.time)
            }
        TimePickerDialog(
            requireActivity(),
            timeSetListener,
            cal.get(Calendar.HOUR_OF_DAY),
            cal.get(Calendar.MINUTE),
            true
        ).show()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
//           R.id.etPollingDate->{
//               setDatePicker(binding.etPollingDate)
//           }
//           R.id.etPollingTime->{
//               setTimePicker(binding.etPollingTime)
//           } R.id.createPoll->{
//               pollingViewModel.cretePolling(requireActivity(),userPreferences.getuserDetails()?.userRef.toString(),
//                   binding.etPollTitle.toString(),ids,
//                   binding.etPollingDate.toString(),binding.etPollingTime.toString())
//           }
        }
    }

    private fun observeViewModel() {

        pollingViewModel.successful.observe(requireActivity(), androidx.lifecycle.Observer {
            dismissDialog()
            if (it != null) {
                if (it) {
                    if (pollingViewModel.status == "success") {
                        startActivity(Intent(requireActivity(), HomeTabActivity::class.java))
//                        if (pollingViewModel.chatdata.chatList.isNullOrEmpty()){
//                            binding.noChatData.visibility=View.VISIBLE
//                        }
//                        chatsAdapter = ViewChatsAdapter(requireActivity(),chatViewModel.chatdata.chatList,this,this,0)
//                        binding.rvChatList.adapter = chatsAdapter
//                        //   chatsAdapter!!.notifyDataSetChanged()

                    } else {
                        setError(pollingViewModel.message)

                    }
                }
            } else {
                setError(pollingViewModel.message)
            }

        })
    }
}