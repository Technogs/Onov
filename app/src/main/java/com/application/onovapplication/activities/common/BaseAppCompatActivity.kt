package com.application.onovapplication.activities.common

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.webkit.WebView
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.application.onovapplication.R
import com.application.onovapplication.prefs.PreferenceManager
import com.application.onovapplication.repository.BaseUrl
import com.bumptech.glide.Glide
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory
import com.google.android.exoplayer2.extractor.ExtractorsFactory
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.trackselection.TrackSelector
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout
import com.google.android.exoplayer2.ui.SimpleExoPlayerView
import com.google.android.exoplayer2.upstream.BandwidthMeter
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory
import com.google.android.material.snackbar.Snackbar
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import android.view.MotionEvent as MotionEvent1


abstract class BaseAppCompatActivity : AppCompatActivity() {
    var exoPlayer: SimpleExoPlayer? = null

    val userPreferences: PreferenceManager by lazy {
        PreferenceManager(this)
    }

     val mDialog: Dialog by lazy {
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

    open fun searchBtnPressed(view:View?)
    {
        val intent = Intent(this, SearchFriendsActivity                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                ::class.java)
        startActivity(intent)
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
            if (ev.action == MotionEvent1.ACTION_UP
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

    fun getEpochDate(time: Long): String {
        val format = "yyyy-MM-dd hh:mm:ss";
        val sdf = SimpleDateFormat(format, Locale.getDefault());
        sdf.timeZone = TimeZone.getDefault();
        return sdf.format(Date(time * 1000));//format(Date(time * 1000));

    }

    fun getEpochTime(): Long {

//        val currentTime = Calendar.getInstance().time
        val millis = System.currentTimeMillis()
        val seconds = millis / 1000
        return seconds
    }

     fun showDialogMedia(data: String,type: String) {

        var dialog = Dialog(this)

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.custom_layout)
        dialog.getWindow()?.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        val ivFeed = dialog.findViewById(R.id.ivFeed) as ImageView
        val cross = dialog.findViewById(R.id.close) as ImageView
        val vvFeed = dialog.findViewById(R.id.vvFeed) as VideoView

        val idExoPlayerVIew = dialog.findViewById(R.id.idExoPlayerVIew) as SimpleExoPlayerView
        val wbFeed = dialog.findViewById(R.id.wbFeed) as WebView
        idExoPlayerVIew.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FILL)

        cross.setOnClickListener { dialog.dismiss() }

            when (type) {
                "document" -> {
                    wbFeed.visibility = View.VISIBLE
                    vvFeed.visibility = View.GONE
                    idExoPlayerVIew.visibility = View.GONE

                    ivFeed.visibility = View.GONE
                    wbFeed.settings.javaScriptEnabled = true

                    wbFeed.settings.javaScriptCanOpenWindowsAutomatically = true
                    wbFeed.scrollBarStyle = View.SCROLLBARS_INSIDE_OVERLAY
                    wbFeed.loadUrl(
                        "https://docs.google.com/gview?embedded=true&url=".plus(
                            BaseUrl.photoUrl + data
                        )
                    )

                }

                "video" -> {

                    wbFeed.visibility = View.GONE
                    vvFeed.visibility = View.GONE
                    idExoPlayerVIew.visibility = View.VISIBLE
                    ivFeed.visibility = View.GONE
                        exoplayer(BaseUrl.photoUrl + data,idExoPlayerVIew)
                }

                "photo" -> {
                    wbFeed.visibility = View.GONE
                    vvFeed.visibility = View.GONE
                    idExoPlayerVIew.visibility = View.GONE
                    ivFeed.visibility = View.VISIBLE
                        Glide.with(this).load(BaseUrl.photoUrl + data)
                            .into(ivFeed)

                }
            }
        dialog.setOnKeyListener(DialogInterface.OnKeyListener { dialog, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                dialog?.cancel()
                return@OnKeyListener true

            }
            false
        })
        dialog.show()

    }

    fun exoplayer(videoURL: String,explayer:SimpleExoPlayerView)
    {
        try {
            // bandwisthmeter is used for
            // getting default bandwidth
            val bandwidthMeter: BandwidthMeter = DefaultBandwidthMeter()

            // track selector is used to navigate between
            // video using a default seekbar.
            val trackSelector: TrackSelector =
                DefaultTrackSelector(AdaptiveTrackSelection.Factory(bandwidthMeter))

            // we are adding our track selector to exoplayer.
            exoPlayer = ExoPlayerFactory.newSimpleInstance(this, trackSelector)

            // we are parsing a video url
            // and parsing its video uri.
            val videouri: Uri = Uri.parse(videoURL)

            // we are creating a variable for datasource factory
            // and setting its user agent as 'exoplayer_view'
            val dataSourceFactory = DefaultHttpDataSourceFactory("exoplayer_video")

            // we are creating a variable for extractor factory
            // and setting it to default extractor factory.
            val extractorsFactory: ExtractorsFactory = DefaultExtractorsFactory()

            // we are creating a media source with above variables
            // and passing our event handler as null,
            val mediaSource: MediaSource =
                ExtractorMediaSource(videouri, dataSourceFactory, extractorsFactory, null, null)

            // inside our exoplayer view
            // we are setting our player
            explayer.player = exoPlayer
            // exoPlayerView.setPlayer(exoPlayer)

            // we are preparing our exoplayer
            // with media source.
            exoPlayer?.prepare(mediaSource)

            // we are setting our exoplayer
            // when it is ready.
            //   exoPlayer?.playWhenReady = true
            exoPlayer?.playWhenReady = false
        } catch (e: Exception) {
            // below line is used for
            // handling our errors.
            Log.e("TAG", "Error : $e")
        }
    }

}
