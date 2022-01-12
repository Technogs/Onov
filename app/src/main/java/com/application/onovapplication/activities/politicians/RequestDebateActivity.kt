package com.application.onovapplication.activities.politicians


import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.application.onovapplication.R
import com.application.onovapplication.activities.UsersActivity
import com.application.onovapplication.activities.common.BaseAppCompatActivity
import com.application.onovapplication.viewModels.DebatesViewModel

import java.text.SimpleDateFormat
import java.util.*
import android.util.Log
import android.widget.Toast
import com.application.onovapplication.activities.PeopleActivity
import com.application.onovapplication.databinding.ActionBarLayout2Binding
import com.application.onovapplication.databinding.ActivityRequestDebateBinding
import com.tencent.bugly.proguard.s
import kotlin.collections.ArrayList


class RequestDebateActivity : BaseAppCompatActivity() ,View.OnClickListener{

    private val debatesViewModel by lazy { ViewModelProvider(this).get(DebatesViewModel::class.java)}
    val LAUNCH_SECOND_ACTIVITY = 1
    val ids = ArrayList<String>()
    var result = arrayListOf<String>()

//    var result:String=""
    var date= ""
    var persons= ""
    var time = ""
    private lateinit var binding: ActivityRequestDebateBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRequestDebateBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        val incBinding: ActionBarLayout2Binding =binding.ab
        incBinding.tvScreenTitle.text = getString(R.string.request_debate)
        observeViewModel()
    }


    private fun observeViewModel() {

        debatesViewModel.successfulRequestDebate.observe(this, androidx.lifecycle.Observer {
            dismissDialog()
            if (it != null) {
                    if (debatesViewModel.status == "success") {
                        setError(debatesViewModel.message)
                        finish()

                    } else {
                        setError(debatesViewModel.message)
                        finish()
                    }
                }
            else {
                setError(debatesViewModel.message)
            }

        })



    }

    override fun onClick(v: View?) {
      when(v?.id){
          R.id.requestDebateBtn->{

              val i =  Intent(this, PeopleActivity::class.java)
            i.putExtra("debate","debate")
              startActivityForResult(i, LAUNCH_SECOND_ACTIVITY)
             // debatesViewModel.requestDebate(this,"","","","","","")
          }  R.id.rd_date->{
              setDatePicker(binding.rdDate)
          }R.id.rd_time->{
              setTimePicker(binding.rdTime)
          }
      }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == LAUNCH_SECOND_ACTIVITY) {
            if (resultCode == RESULT_OK) {
              result= data?.getStringArrayListExtra("result") as ArrayList<String>
                persons = TextUtils.join(", ", result)
//                Toast.makeText(this, ""+ s, Toast.LENGTH_SHORT).show()
if (binding.rdTitle.text.toString()==""){
    setError(resources.getString(R.string.title_error))
} else if (date==""){
             setError("Select date")
         } else if (time==""){
             setError("Select time")
         } else if (binding.etDebateMsg.text.toString()==""){
             setError("Add a message")
         } else {
            debatesViewModel.requestDebate(this,persons,userPreferences.getuserDetails()?.userRef.toString(),binding.rdTitle.text.toString(),binding.etDebateMsg.text.toString(),date,time)
}

            }
            if (resultCode == RESULT_CANCELED) {
                // Write your code if there's no result
                Log.e("result","no result found")
            }
        }

    }


    private fun setTimePicker(textView: TextView?) {
        val cal = Calendar.getInstance()
        val timeSetListener =
            TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
                cal.set(Calendar.HOUR_OF_DAY, hour)
                cal.set(Calendar.MINUTE, minute)
                textView?.setText(SimpleDateFormat("HH:mm").format(cal.time))
                time = SimpleDateFormat("HH:mm").format(cal.time)
            }
        TimePickerDialog(
            this,
            timeSetListener,
            cal.get(Calendar.HOUR_OF_DAY),
            cal.get(Calendar.MINUTE),
            true
        ).show()
    }


    private fun setDatePicker(textView: TextView?) {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)


        val dpd: DatePickerDialog = DatePickerDialog(
            this,
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                // Display Selected date in textBox

                val selectedMonth = String.format("%02d", monthOfYear + 1)
                val selectedDate = String.format("%02d", dayOfMonth)
                date = "$year-$selectedMonth-$selectedDate"

                textView?.setText("$year-$selectedMonth-$selectedDate")
            },
            year,
            month,
            day
        )
        dpd.show()
    }
}