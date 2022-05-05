package com.application.onovapplication.extensions

import android.app.Dialog
import android.content.DialogInterface
import android.net.Uri
import android.view.KeyEvent
import android.view.View
import android.view.Window
import android.webkit.WebView
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.VideoView
import com.application.onovapplication.R
import com.application.onovapplication.model.FeedsData
import com.application.onovapplication.repository.BaseUrl
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout
import com.google.android.exoplayer2.ui.SimpleExoPlayerView
import java.io.File

fun ImageView.loadImage(url: String) {
    Glide.with(context).load(url).placeholder(R.drawable.ic_baseline_account_circle_24).diskCacheStrategy(
        DiskCacheStrategy.AUTOMATIC).into(this)
}


fun ImageView.loadImage(file: File?) {
    Glide.with(this).load(file).into(this)
}

fun ImageView.loadThumbnail(url: String) {
    Glide.with(context).load(url).diskCacheStrategy(DiskCacheStrategy.AUTOMATIC).thumbnail(0.5f).into(this)
}

fun ImageView.loadImage(uri: Uri) {
    Glide.with(context).load(uri).diskCacheStrategy(DiskCacheStrategy.AUTOMATIC).into(this)
}

fun ImageView.loadImage(resource: Int) {
    Glide.with(context).load(resource).diskCacheStrategy(DiskCacheStrategy.AUTOMATIC).into(this)
}

