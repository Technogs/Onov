package com.application.onovapplication.activities.common

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.EditText
import com.application.onovapplication.R
import kotlinx.android.synthetic.main.action_bar_layout_2.*
import kotlinx.android.synthetic.main.activity_create_event.*
import java.text.SimpleDateFormat
import java.util.*

class CreateEventActivity : BaseAppCompatActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_event)


        tvScreenTitle.text = getString(R.string.create_event)
        //tvScreenTitleRight.text = getString(R.string.post)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.edEventStartDate -> {
                setDatePicker(edEventStartDate)
            }

            R.id.edEventEndDate -> {
                setDatePicker(edEventEndDate)

            }
            R.id.edEventEndTime -> {
                setTimePicker(edEventEndTime)
            }

            R.id.edEventStartTime -> {
                setTimePicker(edEventStartTime)
            }
        }

    }


    private fun setTimePicker(editText: EditText?) {
        val cal = Calendar.getInstance()
        val timeSetListener =
            TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
                cal.set(Calendar.HOUR_OF_DAY, hour)
                cal.set(Calendar.MINUTE, minute)
                editText?.setText(SimpleDateFormat("HH:mm").format(cal.time))
            }
        TimePickerDialog(
            this,
            timeSetListener,
            cal.get(Calendar.HOUR_OF_DAY),
            cal.get(Calendar.MINUTE),
            true
        ).show()
    }


    private fun setDatePicker(editText: EditText?) {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)


        val dpd: DatePickerDialog = DatePickerDialog(
            this,
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                // Display Selected date in textbox

                val selectedMonth = String.format("%02d", monthOfYear + 1)
                val selectedDate = String.format("%02d", dayOfMonth)

                editText?.setText("$year-$selectedMonth-$selectedDate")
            },
            year,
            month,
            day
        )
        dpd.show()
    }
}