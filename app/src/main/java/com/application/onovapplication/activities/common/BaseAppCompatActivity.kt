package com.application.onovapplication.activities.common

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.application.onovapplication.R
import com.application.onovapplication.prefs.PreferenceManager

import com.google.android.material.snackbar.Snackbar
import android.view.MotionEvent as MotionEvent1


abstract class BaseAppCompatActivity : AppCompatActivity() {

    val userPreferences: PreferenceManager by lazy {
        PreferenceManager(this)
    }

    private val mDialog: Dialog by lazy {
        Dialog(this).apply {
            window?.requestFeature(Window.FEATURE_NO_TITLE)
            setContentView(R.layout.layout_progress_wheel)
            window?.setBackgroundDrawable(ColorDrawable(0))
        }
    }

    @SuppressLint("NewApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        adjustFontScale(resources.configuration)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            window.statusBarColor = ContextCompat.getColor(this, R.color.white)
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
        window.statusBarColor = ContextCompat.getColor(this, R.color.white)
    }


    open fun backBtnPressed(view: View?) {
        finish()
    }

    open fun notificationBtnPressed(view: View?) {
        val intent = Intent(this, NotificationsActivity::class.java)
        startActivity(intent)
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

    override fun dispatchTouchEvent(ev: MotionEvent1?): Boolean {
        val view = currentFocus
        val ret = super.dispatchTouchEvent(ev)

        if (view is EditText) {
            val w = currentFocus
            val scrcoords = IntArray(2)
            w!!.getLocationOnScreen(scrcoords)
            val x = ev!!.rawX + w.left - scrcoords[0]
            val y = ev.rawY + w.top - scrcoords[1]
            if (ev.action === MotionEvent1.ACTION_UP
                && (x < w.left || x >= w.right || y < w.top || y > w.bottom)
            ) {
                disappearKeyboard()
                view.clearFocus()
            }
        }
        return ret
    }

    fun disappearKeyboard() {
        val imm =
            getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        if (currentFocus != null) imm.hideSoftInputFromWindow(
            this.currentFocus!!.windowToken,
            InputMethodManager.HIDE_NOT_ALWAYS
        )
    }

    fun setError(string: String) {

        val snackBar: Snackbar =
            Snackbar.make(findViewById(android.R.id.content), string, Snackbar.LENGTH_SHORT)
        val snackBarView = snackBar.view
        snackBarView.setBackgroundColor(Color.parseColor("#C2272D"))
//        val textView =
//            snackBarView.findViewById<View>(com.google.android.material.R.id.snackbar_text) as TextView
//        textView.setTextColor(Color.parseColor("#FFFF"))
        snackBar.show()
        Snackbar.make(
            findViewById(android.R.id.content),
            string,
            Snackbar.LENGTH_SHORT
        ).show()
    }

    fun checkEmpty(editText: EditText): Boolean {
        return TextUtils.isEmpty(editText.text.toString().trim())
    }

    open fun adjustFontScale(configuration: Configuration) {
        if (configuration.fontScale > 1.30) {
//            LogUtil.log(
//                LogUtil.WARN,
// 01               FragmentActivity.TAG,
//                "fontScale=" + configuration.fontScale
//            ) //Custom Log class, you can use Log.w
//            LogUtil.log(
//                LogUtil.WARN,
//                FragmentActivity.TAG,
//                "font too big. scale down..."
//            ) //Custom Log class, you can use Log.w
            configuration.fontScale = 1.70f
            val metrics = resources.displayMetrics
            val wm =
                getSystemService(Context.WINDOW_SERVICE) as WindowManager
            wm.defaultDisplay.getMetrics(metrics)
            metrics.scaledDensity = configuration.fontScale * metrics.density
            baseContext.resources.updateConfiguration(configuration, metrics)
        }
    }
}
