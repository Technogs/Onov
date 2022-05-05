package com.application.onovapplication.adapters

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.util.Log
import android.view.*
import android.webkit.WebView
import android.widget.*
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.RecyclerView
import com.application.onovapplication.R
import com.application.onovapplication.databinding.RvMediaBinding
import com.application.onovapplication.model.MediaData
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
import com.google.android.exoplayer2.ui.SimpleExoPlayerView
import com.google.android.exoplayer2.upstream.BandwidthMeter
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory
import java.io.File
import java.io.FileOutputStream

class MediaAdapter(
    val context: Context, val mediaData: List<MediaData>, val onClickMedia: OnClickMedia
) : RecyclerView.Adapter<MediaAdapter.RVHolder>() {
    var exoPlayer: SimpleExoPlayer? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RVHolder {
        val binding = RvMediaBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RVHolder(binding)
    }

    override fun getItemCount(): Int {
        return mediaData.size
    }

    override fun onBindViewHolder(holder: RVHolder, position: Int) {
        holder.bind(mediaData[position], position)

    }


    inner class RVHolder(val binding: RvMediaBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(dataItem: MediaData, position: Int) {
            binding.see.setOnClickListener { showDialog(dataItem, position) }
            when (dataItem.fileType) {
                "document" -> {
                    binding.wbFeed.visibility = View.VISIBLE
                    binding.vvFeed.visibility = View.GONE
                    binding.idExoPlayerVIew.visibility = View.GONE
                    binding.ivFeed.visibility = View.GONE
                    binding.wbFeed.settings.javaScriptEnabled = true
                    binding.wbFeed.settings.javaScriptCanOpenWindowsAutomatically = true
                    binding.wbFeed.scrollBarStyle = View.SCROLLBARS_INSIDE_OVERLAY
                    binding.wbFeed.loadUrl(
                        "https://docs.google.com/gview?embedded=true&url=".plus(
                            BaseUrl.photoUrl + dataItem.filePath
                        )
                    )
                    //     Log.e("PRACHI","https://docs.google.com/gview?embedded=true&url=".plus(BaseUrl.photoUrl + feedData.filePath))

                }

                "video" -> {

                    binding.wbFeed.visibility = View.GONE
                    //   binding.vvFeed.visibility = View.VISIBLE
                    binding.idExoPlayerVIew.visibility = View.VISIBLE
                    binding.ivFeed.visibility = View.GONE
                    exoplayer(BaseUrl.photoUrl + dataItem.filePath, binding.idExoPlayerVIew)
                }

                "photo" -> {
                    binding.wbFeed.visibility = View.GONE
                    binding.vvFeed.visibility = View.GONE
                    binding.idExoPlayerVIew.visibility = View.GONE
                    binding.ivFeed.visibility = View.VISIBLE
                    Glide.with(context).load(BaseUrl.photoUrl + dataItem.filePath)
                        .into(binding.ivFeed)

                }
            }

        }

        private fun shareImageandText(bitmap: Bitmap, link: String) {
            val uri = getImageToShare(bitmap)
            val intent = Intent(Intent.ACTION_SEND)

            // putting uri of image to be shared
            intent.putExtra(Intent.EXTRA_STREAM, uri)
//intent.setData(Uri.parse(BaseUrl.photoUrl+feeditem.filePath))
            // adding text to share
            intent.putExtra(Intent.EXTRA_TEXT, "" + "Media Link" + "\n" + BaseUrl.photoUrl + link)


            // Add subject Here
            intent.putExtra(Intent.EXTRA_SUBJECT, "Subject Here")

            // setting type to image
            intent.type = "*/*"

            // calling startactivity() to share
            context.startActivity(Intent.createChooser(intent, "Share Via"))
        }

        // Retrieving the url to share
        private fun getImageToShare(bitmap: Bitmap): Uri? {
            val imagefolder = File(context.cacheDir, "images")
            var uri: Uri? = null
            try {
                imagefolder.mkdirs()
                val file = File(imagefolder, "shared_image.png")
                val outputStream = FileOutputStream(file)
                bitmap.compress(Bitmap.CompressFormat.PNG, 90, outputStream)
                outputStream.flush()
                outputStream.close()
                uri = FileProvider.getUriForFile(
                    context,
                    "com.application.onovapplication.provider",
                    file
                )
            } catch (e: java.lang.Exception) {
            }
            return uri
        }

        private fun showDialog(mdData: MediaData, position: Int) {

            var dialog = Dialog(context)

            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setCancelable(false)
            dialog.setContentView(R.layout.custom_layout)
            dialog.window?.setLayout(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )

            val ivFeed = dialog.findViewById(R.id.ivFeed) as ImageView
            val cross = dialog.findViewById(R.id.close) as ImageView
            val share = dialog.findViewById(R.id.shareBtn) as TextView
            val delete = dialog.findViewById(R.id.deleteBtn) as TextView
            val vvFeed = dialog.findViewById(R.id.vvFeed) as VideoView
            val sdlyt = dialog.findViewById(R.id.shrDltLyt) as RelativeLayout
            sdlyt.visibility = View.VISIBLE
            val idExoPlayerVIew = dialog.findViewById(R.id.idExoPlayerVIew) as SimpleExoPlayerView
            val wbFeed = dialog.findViewById(R.id.wbFeed) as WebView
            cross.setOnClickListener { dialog.dismiss() }
            delete.setOnClickListener {
                onClickMedia.onMediaDelete(mdData, position)
                dialog.dismiss()
            }
            share.setOnClickListener {
                val bitmapDrawable =
                    context.getDrawable(R.drawable.onov_logo) as BitmapDrawable//imageView.drawable as BitmapDrawable
                val bitmap = bitmapDrawable.bitmap
                shareImageandText(bitmap, mdData.filePath)
            }
            when (mdData.fileType) {
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
                            BaseUrl.photoUrl + mdData.filePath
                        )
                    )

                }

                "video" -> {

                    wbFeed.visibility = View.GONE
                    //   binding.vvFeed.visibility = View.VISIBLE
                    idExoPlayerVIew.visibility = View.VISIBLE
                    ivFeed.visibility = View.GONE

                    exoplayer(BaseUrl.photoUrl + mdData.filePath, idExoPlayerVIew)
                }

                "photo" -> {
                    wbFeed.visibility = View.GONE
                    vvFeed.visibility = View.GONE
                    idExoPlayerVIew.visibility = View.GONE
                    ivFeed.visibility = View.VISIBLE
                    Glide.with(context).load(BaseUrl.photoUrl + mdData.filePath).into(ivFeed)

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

        fun exoplayer(videoURL: String, playerView: SimpleExoPlayerView) {
            try {

                // bandwisthmeter is used for
                // getting default bandwidth
                val bandwidthMeter: BandwidthMeter = DefaultBandwidthMeter()

                // track selector is used to navigate between
                // video using a default seekbar.
                val trackSelector: TrackSelector =
                    DefaultTrackSelector(AdaptiveTrackSelection.Factory(bandwidthMeter))

                // we are adding our track selector to exoplayer.
                exoPlayer = ExoPlayerFactory.newSimpleInstance(context, trackSelector)

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
                playerView.player = exoPlayer
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

    interface OnClickMedia {
        fun onMediaDelete(mdData: MediaData, position: Int)
    }
}