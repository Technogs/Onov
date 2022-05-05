package com.application.onovapplication.extensions

import android.view.View
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

fun View.show() { visibility = View.VISIBLE
}

fun View.hide() { visibility = View.GONE
}

fun View.invisible() { visibility = View.INVISIBLE
}

fun convertDateFormat(time: String?, inputType: String, outputType: String): String {
    val inputPattern = inputType
    val outputPattern = outputType
    val inputFormat = SimpleDateFormat(inputPattern, Locale.ENGLISH)
    val outputFormat = SimpleDateFormat(outputPattern, Locale.ENGLISH)
    var date: Date? = null
    var str: String? = null
    try {
        date = inputFormat.parse(time)
        str = outputFormat.format(date)
    } catch (e: ParseException) {
        e.printStackTrace()
    }
    return str.toString()
}

fun convertTimeFormat(time: String?): String {
    // Heres your military time int like in your code sample
    // Heres your military time int like in your code sample
    val milTime = 56
// Convert the int to a string ensuring its 4 characters wide, parse as a date
// Convert the int to a string ensuring its 4 characters wide, parse as a date
    var date = SimpleDateFormat("hhmm").parse(String.format("%04d", milTime))
// Set format: print the hours and minutes of the date, with AM or PM at the end
// Set format: print the hours and minutes of the date, with AM or PM at the end
    val sdf = SimpleDateFormat("hh:mm a")
// Print the date!
// Print the date!
    println("time is: "+sdf.format(date))
    var str: String? = sdf.format(date)

    return str.toString()
}