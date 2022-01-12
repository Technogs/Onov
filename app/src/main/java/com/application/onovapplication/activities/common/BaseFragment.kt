package com.application.onovapplication.activities.common

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Window
import androidx.fragment.app.Fragment
import com.application.onovapplication.R
import com.application.onovapplication.prefs.PreferenceManager
import com.google.android.material.snackbar.Snackbar

open class BaseFragment:Fragment() {
    val userPreferences: PreferenceManager by lazy {
        PreferenceManager(requireActivity())
    }
    private val mDialog: Dialog by lazy {
        Dialog(requireActivity()).apply {
            window?.requestFeature(Window.FEATURE_NO_TITLE)
            setContentView(R.layout.layout_progress_wheel)
            window?.setBackgroundDrawable(ColorDrawable(0))
        }
    }


    fun setError(string: String) {

        val snackBar: Snackbar =
            Snackbar.make(requireActivity().findViewById(android.R.id.content), string, Snackbar.LENGTH_SHORT)
        val snackBarView = snackBar.view
        snackBarView.setBackgroundColor(Color.parseColor("#C2272D"))
//        val textView =
//            snackBarView.findViewById<View>(com.google.android.material.R.id.snackbar_text) as TextView
//        textView.setTextColor(Color.parseColor("#FFFF"))
        snackBar.show()
        Snackbar.make(
            activity!!.findViewById(android.R.id.content),
            string,
            Snackbar.LENGTH_SHORT
        ).show()
    }

    fun showDialog() {
        if (!mDialog.isShowing) {
            mDialog.show()
        }
    }

    fun dismissDialog() {
        if (mDialog.isShowing) {
            mDialog.dismiss()
        }
    }
}